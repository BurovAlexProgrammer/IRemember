package ru.avb.iremember.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.satsuware.usefulviews.LabelledSpinner;

import org.joda.time.DateTime;

import ru.avb.iremember.Category;
import ru.avb.iremember.DialogDateTimePicker;
import ru.avb.iremember.G;
import ru.avb.iremember.HomeActivity;
import ru.avb.iremember.R;
import ru.avb.iremember.dialogs.DialogIconPicker;

public class FragmentCreateCategory extends Fragment {
    HomeActivity thisActivity;
    LabelledSpinner spinnerType;
    Spinner spinnerEverageValueEventcount, spinnerPredictionPeriod;
    EditText editTextName, editTextInitialValue, editTextFinalValue, editTextUnit, editTextEverageValue;
    CheckBox checkBoxEverageCalculate, checkBoxPrediction;
    ImageView imageViewCategoryIcon;
    LinearLayout buttonOk;
    LinearLayout layoutUnit, layoutEverageCalculate, layoutEverageManual, layoutDevider, layoutPrediction;
    DialogFragment dialogDateTimePicker;
    DialogIconPicker dialogIconPicker;
    public static View root;
    public static String iconPath = "";
    public static Color iconColor;
    public static int iconTintMode;
    public static int selectedCondition = 0;
    public static date initDatetime, finalDatetime;
    public static class date{
        int day, month, year, hour, minute;
        public date(int day, int month, int year) {this.day=day; this.month=month; this.year=year; this.hour=0; this.minute=0;}
        public date(int day, int month, int year, int hour, int minute) {this.day = day; this.month = month; this.year = year; this.hour = hour; this.minute = minute;}

        @Override
        public String toString() {
            String s="";
            if (day<10) s+="0"; s+=day;
            s+=".";
            if (month<10) s+="0"; s+=month;
            s+=".";
            s+=year;
            if (hour==0 && minute==0) return s;
            else {
                s+="  ";
                if (hour<10) s+="0"; s+=hour;
                s+=":";
                if (minute<10) s+="0"; s+=minute;
            }
            return s;
        }
    }

    private OnFragmentInteractionListener mListener;

    public FragmentCreateCategory() {
        // Required empty public constructor
    }

    public static FragmentCreateCategory newInstance() {
        FragmentCreateCategory fragment = new FragmentCreateCategory();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisActivity = (HomeActivity)inflater.getContext();
        thisActivity.faButton.setVisibility(View.INVISIBLE);
        View v = inflater.inflate(R.layout.fragment_create_category, container, false);
        root = v;
        initViews(v);
        spinnerType.setEnabled(true);


        return v;
    }

    public void initViews(View v) {
        spinnerType = (LabelledSpinner)v.findViewById(R.id.spinner_type);
        spinnerEverageValueEventcount = (Spinner)v.findViewById(R.id.spinner_ValueEverageEventcount);
        spinnerPredictionPeriod = (Spinner)v.findViewById(R.id.spinner_redictionPeriod);
        editTextName = (EditText)v.findViewById(R.id.editText_name);
        editTextUnit = (EditText)v.findViewById(R.id.editText_unit);
        editTextInitialValue = (EditText)v.findViewById(R.id.editText_initialValue);
        editTextFinalValue = (EditText)v.findViewById(R.id.editText_finalValue);
        editTextEverageValue = (EditText)v.findViewById(R.id.editText_everageValue);
        checkBoxEverageCalculate = (CheckBox)v.findViewById(R.id.checkBox_everageValueCalculateEnabled);
        checkBoxPrediction = (CheckBox)v.findViewById(R.id.checkBox_predictionEnabled);
        imageViewCategoryIcon = (ImageView)v.findViewById(R.id.icon_category);
        layoutUnit = (LinearLayout)v.findViewById(R.id.layout_unitSelected);
        layoutEverageCalculate = (LinearLayout)v.findViewById(R.id.layout_everageValueCalculate);
        layoutEverageManual = (LinearLayout)v.findViewById(R.id.layout_everageValueManual);
        layoutDevider = (LinearLayout)v.findViewById(R.id.layout_deviderAfterEverage);
        layoutPrediction = (LinearLayout)v.findViewById(R.id.layout_predictionEnabled);
        buttonOk = (LinearLayout) v.findViewById(R.id.button_ok);

        dialogDateTimePicker = new DialogDateTimePicker();
        dialogIconPicker = new DialogIconPicker();

        setTypeSpinnerAdapter();

        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean b) {
                if (selectedCondition == Category.Condition.DATETIME) {
                    if (v.getId() == editTextInitialValue.getId() && b) {
                        showDateTimePicker(G.Tag.SET_DATETIME_TO_INIT_VALUE);
                    }
                    if (v.getId() == editTextFinalValue.getId() && b) {
                        showDateTimePicker(G.Tag.SET_DATETIME_TO_FINAL_VALUE);
                    }
                }
                if (selectedCondition == Category.Condition.DATE) {
                    if (v.getId() == editTextInitialValue.getId() && b) {
                        showDateTimePicker(G.Tag.SET_DATE_TO_INIT_VALUE);
                    }
                    if (v.getId() == editTextFinalValue.getId() && b) {
                        showDateTimePicker(G.Tag.SET_DATE_TO_FINAL_VALUE);
                    }
                }
            }
        };
        checkBoxEverageCalculate.setOnClickListener(onClickListener);
        checkBoxPrediction.setOnClickListener(onClickListener);
        editTextInitialValue.setOnClickListener(onClickListener);
        editTextFinalValue.setOnClickListener(onClickListener);
        editTextInitialValue.setOnFocusChangeListener(onFocusChangeListener);
        editTextFinalValue.setOnFocusChangeListener(onFocusChangeListener);
        imageViewCategoryIcon.setOnClickListener(onClickListener);
        buttonOk.setOnClickListener(onClickListener);
        update();
    }

    public void showDateTimePicker(String tag) {
        dialogDateTimePicker.show(getFragmentManager(), tag);
    }

    public void showIconPicker(String tag) {
        dialogIconPicker.show(getFragmentManager(), tag);
    }

    private void setTypeSpinnerAdapter() {
        String[] ITEMS = getResources().getStringArray(R.array.items_cat_condition);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(thisActivity, android.R.layout.simple_dropdown_item_1line, ITEMS);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(thisActivity, R.array.items_cat_condition);
//        spinnerType.setAdapter(adapter);
//
//        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //G.Log("===pos: "+position+",  id:"+id);
//                if (selectedCondition!=position) {
//                    selectedCondition = position;
//                    setDefaultInitialValue();
//                }
//                selectedCondition = position;
//                update();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent){}
//        });
        spinnerType.setItemsArray(ITEMS);
        spinnerType.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                if (position!=Category.Condition.NOTSELECTED) {
                    spinnerType.setLabelText(getString(R.string.condition));
                    selectedCondition = position;
                    setDefaultInitialValue();
                }
                else {spinnerType.setLabelText("");}
                selectedCondition = position;
                update();
                G.Log("Condition: "+selectedCondition);
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });
        //spinnerType.setColor(R.color.wallet_holo_blue_light);
        //spinnerType.setLabelText(getString(R.string.condition));
        //spinnerType.setDefaultErrorText(getString(R.string.error_need_select_condition));
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    class SpinnerAdapter extends ArrayAdapter<String> {
        public SpinnerAdapter(Context context, int resource) {
            super(context, android.R.layout.simple_list_item_1, resource);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return initView(position,convertView);
        }

        private View initView(int position, View convertView) {
            if(convertView == null)
                convertView = View.inflate(getContext(),
                        android.R.layout.simple_list_item_1,
                        null);
            TextView tvText1 = (TextView)convertView.findViewById(android.R.id.text1);
            TextView tvText2 = (TextView)convertView.findViewById(android.R.id.text2);
            tvText1.setText("pos: "+position);
            tvText2.setText("t2");
            return convertView;
        }


    }

    public void update() {
        G.Log("[FragmentCreateCategory.update]");
        if (selectedCondition == Category.Condition.NOTSELECTED) {
            editTextInitialValue.setVisibility(View.GONE);
            editTextFinalValue.setVisibility(View.GONE);
        } else {
            editTextInitialValue.setVisibility(View.VISIBLE);
            editTextFinalValue.setVisibility(View.VISIBLE);
        }

        if (selectedCondition == Category.Condition.UNIT) {
            editTextUnit.setVisibility(View.VISIBLE);
        }
        else {
            editTextUnit.setVisibility(View.GONE);
        }
        //Everage value calculate
        if (checkBoxEverageCalculate.isChecked()) {
            layoutEverageCalculate.setVisibility(View.VISIBLE);
            layoutEverageManual.setVisibility(View.GONE);
            layoutDevider.setVisibility(View.VISIBLE);
        }
        else {
            layoutEverageCalculate.setVisibility(View.GONE);
            layoutEverageManual.setVisibility(View.VISIBLE);
            layoutDevider.setVisibility(View.GONE);
        }
        //Prediction
        if (checkBoxPrediction.isChecked()) {
            layoutPrediction.setVisibility(View.VISIBLE);
        }
        else {
            layoutPrediction.setVisibility(View.GONE);
        }
        //TODO перенести - только при изменении типа категории
        //setDefaultInitialValue();

       // if (spinnerType.get)
    }

    void setDefaultInitialValue() {
        G.Log("DEFAULT "+selectedCondition);
        if (selectedCondition == Category.Condition.UNIT) editTextInitialValue.setText("0");
        if (selectedCondition == Category.Condition.DATE) {
            String currentDay = DateTime.now().getDayOfMonth() + "";
            currentDay = currentDay.length() == 1 ? "0" + currentDay : currentDay;
            String currentMonth = DateTime.now().getMonthOfYear() + "";
            currentMonth = currentMonth.length() == 1 ? "0" + currentMonth : currentMonth;
            editTextInitialValue.setText(currentDay + "." + currentMonth + "." + DateTime.now().getYear());
        }
        if (selectedCondition == Category.Condition.DATETIME) {
            G.Log("DATETIME!!");
            String currentDay = DateTime.now().getDayOfMonth() + "";
            currentDay = currentDay.length() == 1 ? "0" + currentDay : currentDay;
            String currentMonth = DateTime.now().getMonthOfYear() + "";
            currentMonth = currentMonth.length() == 1 ? "0" + currentMonth : currentMonth;
            String currentHour = DateTime.now().getHourOfDay()+"";
            currentHour = currentHour.length() == 1 ? "0" + currentHour : currentHour;
            String currentMinute = DateTime.now().getMinuteOfHour()+"";
            currentMinute = currentMinute.length() == 1 ? "0" + currentMinute : currentMinute;
            editTextInitialValue.setText(currentDay +"."+ currentMonth +"."+ DateTime.now().getYear() +"  "+ currentHour +":"+ currentMinute);
        }
    }

    void createCategory() {
        if (checkData()) {
            Category newCategory = new Category();
            newCategory.setLabel(editTextName.getText().toString());
            //newCategory.setCondition(spinnerType.get);
            //TODO add creatCategory to SQL

        }
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (selectedCondition == Category.Condition.DATETIME) {
                if (v.getId() == editTextInitialValue.getId()) {
                    showDateTimePicker(G.Tag.SET_DATETIME_TO_INIT_VALUE);
                }
                if (v.getId() == editTextFinalValue.getId()) {
                    showDateTimePicker(G.Tag.SET_DATETIME_TO_FINAL_VALUE);
                }
            }
            if (selectedCondition == Category.Condition.DATE) {
                if (v.getId() == editTextInitialValue.getId()) {
                    showDateTimePicker(G.Tag.SET_DATE_TO_INIT_VALUE);
                }
                if (v.getId() == editTextFinalValue.getId()) {
                    showDateTimePicker(G.Tag.SET_DATE_TO_FINAL_VALUE);
                }
            }
            if (v.getId() == imageViewCategoryIcon.getId()) {
                showIconPicker(G.Tag.SET_ICON);
            }
            if (v.getId() == buttonOk.getId()) {createCategory();}
            update();
        }
    };

    boolean checkData() {
        boolean error = false;
        TextInputLayout tilName = (TextInputLayout)editTextName.getParentForAccessibility();
        if (editTextName.getText().toString().equals("")) {tilName.setError(getString(R.string.error_need_cat_name)); error=true;} else {tilName.setError("");}
        if (selectedCondition==Category.Condition.NOTSELECTED) {
//            spinnerType.setDefaultErrorEnabled(true);

            spinnerType.setColor(R.color.colorAccent);
            G.Log("condition error");
            spinnerType.setLabelText("NEED select condition");
//            spinnerType.setDefaultErrorEnabled(true);
//            spinnerType.setDefaultErrorText("WTF");
            error=true;
        } else {
            G.Log("condition norm");
            spinnerType.setDefaultErrorEnabled(false);
        }
        G.Log("Chk: "+!error);
        return !error;
    }


}
