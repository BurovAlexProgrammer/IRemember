package ru.avb.iremember.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.joda.time.DateTime;

import ru.avb.iremember.Category;
import ru.avb.iremember.DialogDateTimePicker;
import ru.avb.iremember.G;
import ru.avb.iremember.HomeActivity;
import ru.avb.iremember.R;

public class FragmentCreateCategory extends Fragment implements DialogDateTimePicker.OnCompleteListener{
    HomeActivity thisActivity;
    Spinner spinnerType, spinnerEverageValueEventcount, spinnerPredictionPeriod;
    EditText editTextName, editTextUnit, editTextInitialValue, editTextFinalValue, editTextEverageValue;
    CheckBox checkBoxEverageCalculate, checkBoxPrediction;
    LinearLayout layoutUnit, layoutEverageCalculate, layoutEverageManual, layoutDevider, layoutPrediction;
    DialogFragment dialogDateTimePicker;
    public static String iconPath = "";
    public static Color iconColor;
    public static int iconTintMode;
    public static int selectedCondition = 0;


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
        initViews(v);
        spinnerType.setEnabled(true);


        return v;
    }

    public void initViews(View v) {
        spinnerType = (Spinner)v.findViewById(R.id.spinner_type);
        spinnerEverageValueEventcount = (Spinner)v.findViewById(R.id.spinner_ValueEverageEventcount);
        spinnerPredictionPeriod = (Spinner)v.findViewById(R.id.spinner_redictionPeriod);
        editTextName = (EditText)v.findViewById(R.id.editText_name);
        editTextUnit = (EditText)v.findViewById(R.id.editText_unit);
        editTextInitialValue = (EditText)v.findViewById(R.id.editText_initialValue);
        editTextFinalValue = (EditText)v.findViewById(R.id.editText_finalValue);
        editTextEverageValue = (EditText)v.findViewById(R.id.editText_everageValue);
        checkBoxEverageCalculate = (CheckBox)v.findViewById(R.id.checkBox_everageValueCalculateEnabled);
        checkBoxPrediction = (CheckBox)v.findViewById(R.id.checkBox_predictionEnabled);
        layoutUnit = (LinearLayout)v.findViewById(R.id.layout_unitSelected);
        layoutEverageCalculate = (LinearLayout)v.findViewById(R.id.layout_everageValueCalculate);
        layoutEverageManual = (LinearLayout)v.findViewById(R.id.layout_everageValueManual);
        layoutDevider = (LinearLayout)v.findViewById(R.id.layout_deviderAfterEverage);
        layoutPrediction = (LinearLayout)v.findViewById(R.id.layout_predictionEnabled);

        dialogDateTimePicker = new DialogDateTimePicker();

        setTypeSpinnerAdapter();
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==editTextInitialValue.getId()) {showDateTimePicker(G.TAG_SET_DATETIME_TO_INIT_VALUE);}
                if (v.getId()==editTextFinalValue.getId()) {showDateTimePicker(G.TAG_SET_DATETIME_TO_FINAL_VALUE);}
                update();
            }
        };
        checkBoxEverageCalculate.setOnClickListener(onClickListener);
        checkBoxPrediction.setOnClickListener(onClickListener);
        editTextInitialValue.setOnClickListener(onClickListener);
        editTextFinalValue.setOnClickListener(onClickListener);
        update();
    }

    public void showDateTimePicker(String tag) {
        dialogDateTimePicker.show(getFragmentManager(), tag);
    }



    private void setTypeSpinnerAdapter() {
        String[] ITEMS = getResources().getStringArray(R.array.items_cat_condition);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(thisActivity, android.R.layout.simple_dropdown_item_1line, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(thisActivity, R.array.items_cat_condition);
        spinnerType.setAdapter(adapter);
        spinnerType.setEnabled(true);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //G.Log("===pos: "+position+",  id:"+id);
                selectedCondition = position;
                update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                G.Log("===nothing: ");
            }
        });
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
        if (selectedCondition == 1) {  //Selexted Unit type
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
        setDefaultInitialValue();
    }

    void setDefaultInitialValue() {
        if (selectedCondition == Category.Condition.UNIT) editTextInitialValue.setText("0");
        if (selectedCondition == Category.Condition.TIME) {
            String currentDay = DateTime.now().getDayOfMonth() + "";
            currentDay = currentDay.length() == 1 ? "0" + currentDay : currentDay;
            String currentMonth = DateTime.now().getMonthOfYear() + "";
            currentMonth = currentMonth.length() == 1 ? "0" + currentMonth : currentMonth;
            editTextInitialValue.setText(currentDay + "." + currentMonth + "." + DateTime.now().getYear());
        }
    }

    @Override
    public void onCompleteDialog(Bundle bundle) {

        int request = bundle.getInt(G.KEY_REQUEST);
        String tag = bundle.getString(G.KEY_TAG);
        if (request == G.Request.SET_DATETIME) {
            int result = bundle.getInt(G.KEY_RESULT);
            G.Log("From Dialog tag: '"+tag+"'. Result="+result);
            if (result == G.Result.OK) {
                if (tag==G.TAG_SET_DATETIME_TO_INIT_VALUE) {
                    int day = bundle.getInt("keyDlgDay");
                    G.Log("INITIAL VALUEEEE");
                    G.Log(day+"");
                }
                if (tag==G.TAG_SET_DATETIME_TO_FINAL_VALUE) {
                    G.Log("FINAL VALUEEEE");
                }
            }
            if (result == G.Result.CANCEL) {}
        }
    }
}
