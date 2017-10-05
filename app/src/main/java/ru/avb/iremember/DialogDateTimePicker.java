package ru.avb.iremember;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.math.MathUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TabHost;
import android.widget.TimePicker;

import org.joda.time.DateTime;

/**
 * Created by Alex on 04.09.2017.
 */
//TODO исправить - при выборе дня, не должно быть дней выше maxDayInMonth
public class DialogDateTimePicker extends DialogFragment implements View.OnClickListener {
    int mode = 0;
    int DATE = 0, DATETIME = 1;
    NumberPicker pickerDay, pickerMonth, pickerYear;
    TimePicker timePicker;
    Button buttonOk;
    TabHost tabHost;
    LinearLayout layoutDatePicker, layoutCalendar;
    int minYear;
    int selectedDay, selectedMonth, selectedYear, selectedHour, selectedMinute;

    public interface OnCompleteListener {
        public abstract void onCompleteDialog(Bundle bundle);
    }
    private OnCompleteListener onCompleteListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_date_time_picker, null);
        if ((getTag()==G.Tag.SET_DATE_TO_FINAL_VALUE)||(getTag()==G.Tag.SET_DATE_TO_INIT_VALUE)) mode = DATE;
        if ((getTag()==G.Tag.SET_DATETIME_TO_FINAL_VALUE)||(getTag()==G.Tag.SET_DATETIME_TO_INIT_VALUE)) mode = DATETIME;

        initViews(v);
        return v;
    }


    @Override
    public void onClick(View view) {
        
    }

    private void initViews(View v) {
        layoutDatePicker = v.findViewById(R.id.layout_datePicker);
        layoutCalendar = v.findViewById(R.id.layout_calendar);
        tabHost = v.findViewById(android.R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tabSpec;
        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator(getString(R.string.date));
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);
        if (mode==DATETIME) {
            tabSpec = tabHost.newTabSpec("tag2");
            tabSpec.setIndicator(getString(R.string.time));
            tabSpec.setContent(R.id.tab2);
            tabHost.addTab(tabSpec);
        }
        if (mode==DATE) {
            v.findViewById(R.id.tab2).setVisibility(View.GONE);
            ((Button)v.findViewById(R.id.button_next)).setText(getString(R.string.ok));
        }

//        tabSpec = tabHost.newTabSpec("tag3");
//        tabSpec.setIndicator("Nhbfasf");
//        tabSpec.setContent(R.id.tab3);
//        tabHost.addTab(tabSpec);

        tabHost.setCurrentTabByTag("tag1");

        pickerDay = v.findViewById(R.id.picker_day);
        int[] days = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
        String[] daysStr = new String[31];
        for (int i=0; i<days.length; i++) {daysStr[i] = ""+days[i];}
        pickerDay.setMinValue(1);
        pickerDay.setMaxValue(daysStr.length);
        pickerDay.setDisplayedValues(daysStr);
        pickerDay.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        pickerDay.setWrapSelectorWheel(true);

        pickerMonth = v.findViewById(R.id.picker_month);
        int[] months = {1,2,3,4,5,6,7,8,9,10,11,12};
        String[] monthsStr = new String[months.length];
        for (int i=0; i<monthsStr.length; i++) {monthsStr[i] = ""+months[i];}
        pickerMonth.setMinValue(1);
        pickerMonth.setMaxValue(monthsStr.length);
        pickerMonth.setDisplayedValues(monthsStr);
//        pickerMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        pickerMonth.setWrapSelectorWheel(true);

        pickerYear = v.findViewById(R.id.picker_year);
        minYear = 2000;
        final int[] years = new int[50];
        for (int i=0; i<years.length; i++) {years[i]=minYear+i;}
        String[] yearsStr = new String[years.length];
        for (int i=0; i<yearsStr.length; i++) {yearsStr[i] = ""+years[i];}
        pickerYear.setMinValue(0);
        pickerYear.setMaxValue(yearsStr.length-1);
        pickerYear.setDisplayedValues(yearsStr);
//        pickerYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        //set current date
        int currentDay = DateTime.now().getDayOfMonth();
        int currentMonth = DateTime.now().getMonthOfYear();
        int currentYear = DateTime.now().getYear();
        int currentHour = DateTime.now().getHourOfDay();
        int currentMinute = DateTime.now().getMinuteOfHour();

        selectedDay = currentDay;
        selectedMonth = currentMonth;
        selectedYear = currentYear;
        selectedHour = currentHour;
        selectedMinute = currentMinute;

        G.Log("Current date: "+currentDay+"-"+currentMonth+"-"+currentYear+"  "+selectedHour+":"+selectedMinute);
        pickerDay.setValue(currentDay);
        pickerMonth.setValue(currentMonth);
        pickerYear.setValue(currentYear-minYear);

        pickerDay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                selectedDay = i1;
            }
        });

        pickerMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                selectedMonth = i1;
                //Set max day by month
                if (pickerDay.getValue()>getMaxDayInMonth(selectedMonth,selectedYear)) pickerDay.setValue(getMaxDayInMonth(selectedMonth,selectedYear));
            }
        });

        pickerYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                selectedYear = minYear+i1;
            }
        });

        G.Log(G.LOGLINE);
        //TODO при повторном вызове не сбрасывает время
        timePicker = v.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(selectedHour);
        timePicker.setCurrentMinute(selectedMinute);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                selectedHour = timePicker.getCurrentHour();
                selectedMinute = timePicker.getCurrentMinute();
            }
        });

        Button buttonNext = v.findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mode==DATETIME) tabHost.setCurrentTabByTag("tag2");
                if (mode==DATE) sendResult();
            }
        });
        Button buttonOk = v.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResult();
            }
        });

        ImageView iconCalendar = v.findViewById(R.id.icon_calendar);
        iconCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layoutCalendar.getVisibility()==View.GONE) {
                    layoutDatePicker.setVisibility(View.GONE);
                    layoutCalendar.setVisibility(View.VISIBLE);
                }
                else {
                    layoutDatePicker.setVisibility(View.VISIBLE);
                    layoutCalendar.setVisibility(View.GONE);
                }
            }
        });
    }

    // make sure the Activity implemented it
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.onCompleteListener = (OnCompleteListener) activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }


    public int getMaxDayInMonth (int month, int year) {
        int maxDayInMonth = 30;
        switch (month) {
            case 1: maxDayInMonth = 31; break;
            case 2: maxDayInMonth = 28; break;
            case 3: maxDayInMonth = 31; break;
            case 4: maxDayInMonth = 30; break;
            case 5: maxDayInMonth = 31; break;
            case 6: maxDayInMonth = 30; break;
            case 7: maxDayInMonth = 31; break;
            case 8: maxDayInMonth = 31; break;
            case 9: maxDayInMonth = 30; break;
            case 10: maxDayInMonth = 31; break;
            case 11: maxDayInMonth = 30; break;
            case 12: maxDayInMonth = 31; break;
        }
        //Если високосный год
        if (((year%4)==0)&&(month==2)) maxDayInMonth=29;
        return maxDayInMonth;
    }

    public void sendResult() {
        G.Log("Send result.. With tag: "+getTag());
        Bundle args = new Bundle();
        args.putInt(G.KEY_REQUEST, G.Request.SET_DATETIME);
        args.putInt(G.KEY_RESULT, G.Result.OK);
        args.putString(G.KEY_TAG, getTag());
        args.putInt(G.KEY_DLG_DAY, selectedDay);
        args.putInt(G.KEY_DLG_MONTH, selectedMonth);
        args.putInt(G.KEY_DLG_YEAR, selectedYear);
        args.putInt(G.KEY_DLG_HOUR, selectedHour);
        args.putInt(G.KEY_DLG_MINUTE, selectedMinute);
        onCompleteListener.onCompleteDialog(args);
        dismiss();
    }
}
