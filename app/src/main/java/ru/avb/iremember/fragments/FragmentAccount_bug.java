package ru.avb.iremember.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.drive.Drive;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import ru.avb.iremember.DB;
import ru.avb.iremember.G;
import ru.avb.iremember.Google;
import ru.avb.iremember.HomeActivity;
import ru.avb.iremember.R;

import static ru.avb.iremember.G.user;

public class FragmentAccount_bug extends Fragment{
    FragmentActivity thisActivity;
    TextView textview_name, textview_email, textview_lastSync, textView_sign;
    ImageView imageview_avatar;
    LinearLayout buttonSing;

    private OnFragmentInteractionListener mListener;

    public FragmentAccount_bug() {

    }

    public static FragmentAccount_bug newInstance(String param1, String param2) {
        FragmentAccount_bug fragment = new FragmentAccount_bug();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisActivity = (FragmentActivity)inflater.getContext();
        G.Log("FragmentAccount thisActivity = "+thisActivity.toString());
        View v = inflater.inflate(R.layout.fragment_account, null);
        initializeViews(v);
        updateUI();
        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public OnFragmentInteractionListener getmListener() {
        return mListener;
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

    private void initializeViews(View parent) {

        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.imageview_avatar) {
                    G.Log("Clicked on avatar");
                    if (user.isAuthorized()) signOut();
                    else signIn();
                    updateUI();
                };
                if (v.getId() == R.id.button_sign) {
                    if (user.isAuthorized()) signOut();
                    else signIn();
                    updateUI();
                };
                //temp
                if (v.getId() == R.id.button) {
                    Google.Drive.uploadFile(getActivity() ,getDbFile(), DB.dbName);

                }
                if (v.getId() == R.id.button2) readFile();
                if (v.getId() == R.id.button3) DB.logTable(getActivity());
                if (v.getId() == R.id.button4) DB.createRow(getActivity());
                if (v.getId() == R.id.button5) deleteFile();
                if (v.getId() == R.id.button6) downloadDbFromDrive();
                //if (v.getId() == R.id.button7)
                //end temp


            }
        };

        textview_name = (TextView)parent.findViewById(R.id.textview_name);
        textview_email = (TextView)parent.findViewById(R.id.textview_email);
        textview_lastSync = (TextView)parent.findViewById(R.id.textview_lastSync);
        textView_sign = (TextView)parent.findViewById(R.id.textview_sign);
        buttonSing = (LinearLayout)parent.findViewById(R.id.button_sign);
        buttonSing.setOnClickListener(onClick);
        //temp
        ((Button)parent.findViewById(R.id.button )).setOnClickListener(onClick);
        ((Button)parent.findViewById(R.id.button2)).setOnClickListener(onClick);
        ((Button)parent.findViewById(R.id.button3)).setOnClickListener(onClick);
        ((Button)parent.findViewById(R.id.button4)).setOnClickListener(onClick);
        ((Button)parent.findViewById(R.id.button5)).setOnClickListener(onClick);
        ((Button)parent.findViewById(R.id.button6)).setOnClickListener(onClick);
        ((Button)parent.findViewById(R.id.button7)).setOnClickListener(onClick);
        //end temp

        imageview_avatar = (ImageView)parent.findViewById(R.id.imageview_avatar);
        imageview_avatar.setOnClickListener(onClick);

    }

    private void downloadDbFromDrive() {
        Google.Drive.appFolder = Drive.DriveApi.getAppFolder(Google.apiClient);
        DB.getWritableDB(getActivity());
        Google.Drive.downloadFileFromDrive(getActivity(), DB.dbName, DB.db.getPath());
    }

    private File getDbFile() {
        G.Log("get DB-file..");
        G.LogInteres("databasePath: "+getActivity().getDatabasePath(DB.dbName));
        File f = getActivity().getDatabasePath(DB.dbName);
        if (f==null) {G.Log("DB-file in null");return null;}
        return f;
    }

    private void getSharedPrefs(){
        G.Log("++++Get Shared Prefs");
        try {
            File f = getActivity().getDatabasePath(G.PREF_FILENAME);

            if (f == null) {
                G.LogToast(getActivity(), "Nothing");
                return;
            }
            //G.LogToast(getActivity(), f.getAbsolutePath());
            G.LogToast(getActivity(), f.getAbsoluteFile().toString());
            G.LogToast(getActivity(), f.getName().toString());

            byte[] fileData = new byte[(int) f.length()];

            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(f));
            dataInputStream.readFully(fileData);
            dataInputStream.close();

            G.LogToast(getActivity(), "" + dataInputStream.toString());
        }
        catch (IOException exception) {G.Log("EXCEPTION: "+exception.getMessage()); exception.printStackTrace();}
    }

    private void deleteFile() {
        G.Log("frAccount. Delete first drive file..");
        Google.Drive.deleteFile(Google.Drive.currentDriveId);
    }


    public void readFile() {
        G.Log("frAccount. Read drive file..");
        Google.Drive.appFolder = Drive.DriveApi.getAppFolder(Google.apiClient);
        Google.Drive.getDriveId(DB.dbName);
    }

    public void updateUI() {
        G.Log("FrAccount.updateUI..");
        if (user.isAuthorized()) {
            G.Log("User authorized");
            //AVATAR
            if (user.getPhotoUrl() != G.NONE_STRING)
                UrlImageViewHelper.setUrlDrawable(imageview_avatar, user.getPhotoUrl());
            else imageview_avatar.setImageResource(R.mipmap.placeholder_account);
            //NAME
            if (user.getNameDefined()) textview_name.setText(user.getDisplayName());
            else textview_name.setText("No name");
            //EMAIL
            if (user.getEmailDefined()) textview_email.setText(user.getEmail());
                //else textview_email.setText(R.string.spaceholder_accountEmail);
            else textview_email.setText("No email");
            //LAST SYNC
            textview_lastSync.setText(user.getLastSyncText());
            //if (user.getLastSyncDefined()) textview_lastSync.setText(getString(R.string.lastSync)+": "+ user.getLastSync().toString());
            //else textview_lastSync.setText(getString(R.string.lastSync)+": "+getString(R.string.never));
            //BUTTON SIGN-OUT
            textView_sign.setText(getString(R.string.sign_out));
        }
        else {
            G.Log("User not authorized");
            user.logData();
            //AVATAR
            imageview_avatar.setImageResource(R.mipmap.placeholder_account);
            //NAME
            textview_name.setText(getString(R.string.spaceholder_accountName));
            //EMAIL
            textview_email.setText(getString(R.string.spaceholder_accountEmail));
            //LAST SYNC
            textview_lastSync.setText("");
            //BUTTON SIGN-IN
            textView_sign.setText(getString(R.string.sign_in_google));
        }
    }

    void signIn() {
        ((HomeActivity)thisActivity).signIn();
    }

    void signOut() {
        ((HomeActivity)thisActivity).signOut();
    }
}
