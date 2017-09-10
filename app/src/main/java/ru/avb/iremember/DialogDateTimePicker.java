package ru.avb.iremember;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class DialogDateTimePicker extends DialogFragment implements View.OnClickListener{
    NumberPicker pickerDay, pickerMonth, pickerYear;
    Button buttonOk;
    TabHost tabHost;
    LinearLayout layoutDatePicker, layoutCalendar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_date_time_picker, null);
        //v.findViewById(R.id.button_ok).setOnClickListener(this);
        //v.findViewById(R.id.button_cancel).setOnClickListener(this);
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
        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator(getString(R.string.time));
        tabSpec.setContent(R.id.tab2);
        tabHost.addTab(tabSpec);
//        tabSpec = tabHost.newTabSpec("tag3");
//        tabSpec.setIndicator("Nhbfasf");
//        tabSpec.setContent(R.id.tab3);
//        tabHost.addTab(tabSpec);

        tabHost.setCurrentTabByTag("tag1");

        pickerDay = v.findViewById(R.id.picker_day);
        int[] days = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
        String[] daysStr = new String[31];
        for (int i=0; i<days.length; i++) {daysStr[i] = ""+days[i];}
        pickerDay.setMinValue(0);
        pickerDay.setMaxValue(daysStr.length-1);
        pickerDay.setDisplayedValues(daysStr);
        pickerDay.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        pickerDay.setWrapSelectorWheel(true);

        pickerMonth = v.findViewById(R.id.picker_month);
        int[] months = {1,2,3,4,5,6,7,8,9,10,11,12};
        String[] monthsStr = new String[months.length];
        for (int i=0; i<monthsStr.length; i++) {monthsStr[i] = ""+months[i];}
        pickerMonth.setMinValue(0);
        pickerMonth.setMaxValue(monthsStr.length-1);
        pickerMonth.setDisplayedValues(monthsStr);
//        pickerMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        pickerMonth.setWrapSelectorWheel(true);

        pickerYear = v.findViewById(R.id.picker_year);
        int minYear = 2000;
        int[] years = new int[50];
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
        G.Log("Current date: "+currentDay+"-"+currentMonth+"-"+currentYear);
        pickerDay.setValue(currentDay-1);
        pickerMonth.setValue(currentMonth-1);
        pickerYear.setValue(currentYear-minYear);

        Button buttonNext = v.findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabHost.setCurrentTabByTag("tag2");
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


}
