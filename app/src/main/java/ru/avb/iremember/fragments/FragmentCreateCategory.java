package ru.avb.iremember.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import ru.avb.iremember.G;
import ru.avb.iremember.HomeActivity;
import ru.avb.iremember.R;

public class FragmentCreateCategory extends Fragment {
    HomeActivity thisActivity;
    Spinner  spnrType, spnrValueEverageEventcount, spnrPredictionPeriod;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentCreateCategory() {
        // Required empty public constructor
    }

    public static FragmentCreateCategory newInstance(String param1, String param2) {
        FragmentCreateCategory fragment = new FragmentCreateCategory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisActivity = (HomeActivity)inflater.getContext();
        thisActivity.faButton.setVisibility(View.INVISIBLE);
        View v = inflater.inflate(R.layout.fragment_create_category, container, false);
        spnrType = (Spinner)v.findViewById(R.id.spinner_type);
        spnrType.setEnabled(true);
        setTypeSpinnerAdapter();
        return v;
    }

    private void setTypeSpinnerAdapter() {
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(thisActivity,
                R.array.items_cat_condition, android.R.layout.simple_spinner_dropdown_item);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //SpinnerAdapter adapter2 = new SpinnerAdapter(thisActivity, R.array.items_cat_condition);

        // Применяем адаптер к элементу pinner
        //adapter.setDropDownViewResource(R.layout.item_dropdown_spinner);
        spnrType.setAdapter(adapter);
        spnrType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                G.Log("===pos: "+position+",  id:"+id);

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
}
