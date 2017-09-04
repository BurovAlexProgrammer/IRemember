package ru.avb.iremember.fragments;

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

import ru.avb.iremember.G;
import ru.avb.iremember.HomeActivity;
import ru.avb.iremember.R;

public class FragmentCreateCategory extends Fragment {
    HomeActivity thisActivity;
    Spinner spinnerType, spinnerEverageValueEventcount, spinnerPredictionPeriod;
    EditText editTextName, editTextUnit, editTextInitialValue, editTextFinalValue, editTextEverageValue;
    CheckBox checkBoxEverageCalculate, checkBoxPrediction;
    LinearLayout layoutUnit, layoutEverageCalculate, layoutEverageManual, layoutDevider, layoutPrediction;
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

        setTypeSpinnerAdapter();
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        };
        checkBoxEverageCalculate.setOnClickListener(onClickListener);
        checkBoxPrediction.setOnClickListener(onClickListener);
        update();
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
        if (selectedCondition == 1) {editTextUnit.setVisibility(View.VISIBLE); }
            else {editTextUnit.setVisibility(View.GONE);}
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
    }
}
