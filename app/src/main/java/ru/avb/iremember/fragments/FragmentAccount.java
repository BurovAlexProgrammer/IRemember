package ru.avb.iremember.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentAccount.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentAccount#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAccount extends Fragment{
    FragmentActivity thisActivity;
    TextView textview_name, textview_email, textview_lastSync, textView_sign;
    ImageView imageview_avatar;
    LinearLayout buttonSing;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentAccount() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAccount.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAccount newInstance(String param1, String param2) {
        FragmentAccount fragment = new FragmentAccount();
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
        thisActivity = (FragmentActivity)inflater.getContext();
        G.Log("FragmentAccount thisActivity = "+thisActivity.toString());
        View v = inflater.inflate(R.layout.fragment_account, null);
        initializeViews(v);
        updateUI();
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    private void initializeViews(View parent) {

        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.imageview_avatar) {
                    G.Log("Clicked on avatar");
                    if (G.user.isAuthorized) signOut();
                    else signIn();
                    updateUI();
                };
                if (v.getId() == R.id.button_sign) {
                    if (G.user.isAuthorized) signOut();
                    else signIn();
                    updateUI();
                };
                if (v.getId() == R.id.button)  Google.Drive.uploadFile2(getDbFile(), DB.DB_NAME);
                if (v.getId() == R.id.button2) readFile();
                if (v.getId() == R.id.button3) DB.logTable(getActivity());
                if (v.getId() == R.id.button4) DB.createRow(getActivity());
                if (v.getId() == R.id.button5) deleteFile();
                if (v.getId() == R.id.button6) downloadDbFromDrive();
                //if (v.getId() == R.id.button7)
                //if (v.getId() == R.id.button4) disconnect();


            }
        };

        textview_name = (TextView)parent.findViewById(R.id.textview_name);
        textview_email = (TextView)parent.findViewById(R.id.textview_email);
        textview_lastSync = (TextView)parent.findViewById(R.id.textview_lastSync);
        textView_sign = (TextView)parent.findViewById(R.id.textview_sign);
        buttonSing = (LinearLayout)parent.findViewById(R.id.button_sign);
        buttonSing.setOnClickListener(onClick);
        //temp
        ((Button)parent.findViewById(R.id.button)).setOnClickListener(onClick);
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
        Google.Drive.downloadFileFromDrive(getActivity(), DB.DB_NAME, DB.db.getPath());
    }

    private File getDbFile() {
        G.Log("get DB-file..");
        G.LogInteres("databasePath: "+getActivity().getDatabasePath(DB.DB_NAME));
        File f = getActivity().getDatabasePath(DB.DB_NAME);
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
        Google.Drive.appFolder = Drive.DriveApi.getAppFolder(Google.apiClient);
        Google.Drive.getDriveId(DB.DB_NAME);
        Google.Drive.deleteFile();
    }

    private void connect() {
    }


    public void readFile() {
        G.Log("frAccount. Read drive file..");
        Google.Drive.appFolder = Drive.DriveApi.getAppFolder(Google.apiClient);
        Google.Drive.getDriveId(DB.DB_NAME);
    }

    public void createFile() {
        G.Log("Try to create Drive file");
//        Drive.DriveApi.newDriveContents(Google.apiClient)
//                .setResultCallback(contentsCallback2);
        Drive.DriveApi.newDriveContents(Google.apiClient)
                .setResultCallback(driveContentsCallback);
    }



    public void updateUI() {
        G.Log("FrAccount.updateUI..");
        if (G.user.isAuthorized) {
            G.Log("User authorized");
            //AVATAR
            if (G.user.getPhotoUrl() != G.NONE_STRING)
                UrlImageViewHelper.setUrlDrawable(imageview_avatar, G.user.getPhotoUrl());
            else imageview_avatar.setImageResource(R.mipmap.placeholder_account);
            //NAME
            if (G.user.getNameDefined()) textview_name.setText(G.user.getDisplayName());
            else textview_name.setText("No name");
            //EMAIL
            if (G.user.getEmailDefined()) textview_email.setText(G.user.getEmail());
                //else textview_email.setText(R.string.spaceholder_accountEmail);
            else textview_email.setText("No email");
            //LAST SYNC
            if (G.user.getLastSyncDefined()) textview_lastSync.setText(getString(R.string.lastSync)+": "+G.user.getLastSync().toString());
            else textview_lastSync.setText(getString(R.string.lastSync)+": "+getString(R.string.never));
            //BUTTON SIGN-OUT
            textView_sign.setText(getString(R.string.sign_out));
        }
        else {
            G.Log("User not authorized");
            G.user.logData();
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

    //CREATE EMPTY FILE
    final ResultCallback<DriveFolder.DriveFileResult> createFileCallback = new ResultCallback<DriveFolder.DriveFileResult>() {
        @Override
        public void onResult(@NonNull DriveFolder.DriveFileResult result) {  //onResult(@NonNull DriveFolder.DriveFileResult result) //must be
            G.Log("DriveFileResult");
            if (!result.getStatus().isSuccess()) {
                G.Log("ERROR!! :Error while trying to create the file");
                G.Toast(getActivity(),"ERROR!! :Error while trying to create the file");
                return;
            }
            G.LogToast(getActivity(),"+++ Created a file in App Folder: " + result.getDriveFile().getDriveId());
        }
    };
    final ResultCallback<DriveApi.DriveContentsResult> contentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    G.Log("Drive Contents Callback..");
                    if (!result.getStatus().isSuccess()) {
                        G.Toast(getActivity(),"Error while trying to create new file contents");
                        G.Log("ERROR!! :Error while trying to create new file contents");
                        return;
                    }

                    MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                            .setTitle("appconfig.txt")
                            .setMimeType("text/plain")
                            .build();
                    Drive.DriveApi.getAppFolder(Google.apiClient)
                            .createFile(Google.apiClient, metadataChangeSet, result.getDriveContents())
                            .setResultCallback(createFileCallback);
                    Drive.DriveApi.getAppFolder(Google.apiClient);
                    G.LogToast(getActivity(),"Callback: Success creating file in AppFolder.");
                }
            };
    //DELETE FIRST FILE
    final ResultCallback<DriveApi.DriveContentsResult> deleteFileCallback = new ResultCallback<DriveApi.DriveContentsResult>() {
        @Override
        public void onResult(@NonNull DriveApi.DriveContentsResult result) {
            G.Log("Delete File Callback..");
            if (!result.getStatus().isSuccess()) {
                G.Toast(getActivity(),"Error while trying to delete file contents");
                G.Log("ERROR!! :Error while trying to delete file contents");
                return;
            }

            MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                    .setTitle("appconfig.txt")
                    .setMimeType("text/plain")
                    .build();
            Drive.DriveApi.getAppFolder(Google.apiClient)
                    .createFile(Google.apiClient, metadataChangeSet, result.getDriveContents())
                    .setResultCallback(createFileCallback);
            Drive.DriveApi.getAppFolder(Google.apiClient);
            G.LogToast(getActivity(),"Callback: Success creating file in AppFolder.");
        }
    };
    //CREATE FILE 2
    final private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        G.Log("ERROR driveContentsCallback: Error while trying to create new file contents");
                        G.Log("Status message: "+result.getStatus().getStatusMessage());
                        return;
                    }
                    try {
                        byte[] bytes = "Hellooooo world!! I miss youuu...".getBytes();
                        DriveContents driveContents = result.getDriveContents();
                        driveContents.getOutputStream().write(bytes);
                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle("appconfig.txt")
                                .setMimeType("text/plain")
                                .build();
                        Drive.DriveApi.getAppFolder(Google.apiClient)
                                .createFile(Google.apiClient, changeSet, driveContents)
                                .setResultCallback(fileCallback);
                    } catch (IOException ioException) {G.Log("EXCEPTION: "+ioException.getMessage());}
                }
            };
    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        G.Log("ERROR fileCallback: Error while trying to create the file");
                        return;
                    }
                    G.LogToast(getActivity(), "Created a file in App Folder: " + result.getDriveFile().getDriveId());
                }
            };

    //UPLOAD TEST
    void upload() {
        final ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback3 =
                new ResultCallback<DriveApi.DriveContentsResult>() {
                    @Override
                    public void onResult(DriveApi.DriveContentsResult result) {
                        if (!result.getStatus().isSuccess()) {
                            G.Log("ERROR driveContentsCallback: Error while trying to create new file contents");
                            G.Log("Status message: " + result.getStatus().getStatusMessage());
                            return;
                        }
                        try {
                            byte[] bytes = "Hellooooo world!! I miss youuu...".getBytes();
                            DriveContents driveContents = result.getDriveContents();
                            driveContents.getOutputStream().write(bytes);
                            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                    .setTitle("appconfig.txt")
                                    .setMimeType("text/plain")
                                    .build();
                            Drive.DriveApi.getAppFolder(Google.apiClient)
                                    .createFile(Google.apiClient, changeSet, driveContents)
                                    .setResultCallback(fileCallback);
                        } catch (IOException ioException) {
                            G.Log("EXCEPTION: " + ioException.getMessage());
                        }
                    }
                };
        final ResultCallback<DriveFolder.DriveFileResult> fileCallback3 = new
                ResultCallback<DriveFolder.DriveFileResult>() {
                    @Override
                    public void onResult(DriveFolder.DriveFileResult result) {
                        if (!result.getStatus().isSuccess()) {
                            G.Log("ERROR fileCallback: Error while trying to create the file");
                            return;
                        }
                        G.LogToast(getActivity(), "Created a file in App Folder: " + result.getDriveFile().getDriveId());
                    }
                };
    }

    private void saveDbToDrive() {
//        Drive.DriveApi.newDriveContents(Google.apiClient)
//                .setResultCallback(contentsCallback);
//        Google.Drive.uploadFile();
    }

}
