package ru.avb.iremember.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ru.avb.iremember.G;
import ru.avb.iremember.HomeActivity;
import ru.avb.iremember.LanguageActivity;
import ru.avb.iremember.Options;
import ru.avb.iremember.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentSettings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentSettings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSettings extends Fragment {
    HomeActivity thisActivity;
    LinearLayout layout_optionLanguages, layout_optionNotificationEnable;
    CheckBox checkbox_notificationEnable;
    ImageView image_languageIcon;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentSettings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSettings.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSettings newInstance(String param1, String param2) {
        FragmentSettings fragment = new FragmentSettings();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        initializeViews(v);
        thisActivity.updateUI();
        //updateUI();
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

/*
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
    */

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    void initializeViews(View parent) {
        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId()==R.id.optionLayout3) {chooseLanguges();}
                switch (view.getId()) {
                    case R.id.optionLayout4: switchNotificationEnable(); return;
                }
            }
        };
        layout_optionNotificationEnable = (LinearLayout)parent.findViewById(R.id.optionLayout4);
        checkbox_notificationEnable = (CheckBox)parent.findViewById(R.id.checkBox4);
        layout_optionLanguages = (LinearLayout)parent.findViewById(R.id.optionLayout3);
        image_languageIcon = (ImageView)parent.findViewById(R.id.imageView3);

        layout_optionNotificationEnable.setOnClickListener(onClick);
        layout_optionLanguages.setOnClickListener(onClick);

    }

    void chooseLanguges() {
        Intent intent = new Intent(this.getActivity(), LanguageActivity.class);
        startActivityForResult(intent, G.REQUEST_CHANGE_LANGUAGE);
        G.Log("FrSettings.chooseLanguages..");
    }

    void switchNotificationEnable() {
        G.Log("switchNotifiactionEnable..");
        checkbox_notificationEnable.setChecked(!checkbox_notificationEnable.isChecked());
        Options.showNotification = checkbox_notificationEnable.isChecked();
        G.Log("Notification enabled: "+Options.showNotification);
        Options.saveOptions();

    }

    void updateUI() {
        //----------SHOW NOTIFICATION----------------------------------
        checkbox_notificationEnable.setChecked(Options.showNotification);
        //----------LANGUAGE-------------------------------------------
        int imageId = R.drawable.locale_default;
        switch (Options.locale) {
            case "default": imageId=R.drawable.locale_default; break;
            case "ru": imageId=R.mipmap.locale_ru; break;
            case "en": imageId=R.mipmap.locale_en; break;
        }
        image_languageIcon.setImageResource(imageId);
        //---------- -------------------------------------------
    }
}
