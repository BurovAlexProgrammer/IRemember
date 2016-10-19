package ru.avb.iremember;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.cast.CastRemoteDisplayLocalService;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Contents;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.drive.query.SortOrder;
import com.google.android.gms.drive.query.SortableField;
import com.google.android.gms.location.ActivityRecognition;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;


/**
 * Created by Алекс on 26.05.2016.
 */
public class Google {
    public static GoogleSignInOptions signInOptions;
    public static GoogleApiClient apiClient, driveApi;
    public static GoogleSignInAccount signInAccount;
    public static GoogleSignInResult lastSignInResult;


    public static boolean isResolvingError;

    //=============DRIVE===================
    public static class Drive {
        public static DriveFolder appFolder;
        public static MetadataBuffer metadataBuffer;
        public static Metadata metadata;
        public static String currentDriveId;

        void createFile(final GoogleApiClient gac, final DriveFolder fldr,
                        final String name, final String mime, final byte[] buff) {
            G.Log("Google.Drive.createFile..");


        }

        @Nullable
        public static String getDriveId(String fileName) {
            G.Log("Google.Drive.getDriveId..");
            // Find the named file with the specific Mime type.
            Query query = new Query.Builder()
                    .addFilter(Filters.and(
                            Filters.eq(SearchableField.TITLE, fileName)))
                            //Filters.eq(SearchableField.MIME_TYPE, mimeType)))
                    .setSortOrder(new SortOrder.Builder()
                            .addSortDescending(SortableField.MODIFIED_DATE)
                            .build())
                    .build();

            com.google.android.gms.drive.Drive.DriveApi.query(apiClient,query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(@NonNull DriveApi.MetadataBufferResult result) {
                    G.Log("Callback Google.Drive.getDriveId.onResult..");
                    if (result.getStatus().isSuccess()==false) {G.Log("EXEPTION: "+result.getStatus().getStatusMessage());}
                    else {
                        metadataBuffer = result.getMetadataBuffer();
                        G.Log(G.LOGLINE);
                        G.Log("MetadataBuffer count: " + metadataBuffer.getCount());
                        G.Log(G.LOGLINE);
                        if (metadataBuffer.getCount()!=0) {
                            Drive.currentDriveId = metadataBuffer.get(0).getDriveId().encodeToString();
                            G.Log("current DriveID: "+Drive.currentDriveId);
                            G.Log("file size: "+metadataBuffer.get(0).getFileSize());
                            G.Log("last modif: "+metadataBuffer.get(0).getModifiedDate());
                            G.Log(G.LOGLINE);
                        } else {
                            G.Log("FIRST METADATA: NULL!"); Drive.currentDriveId = G.NONE_STRING;
                        }
                    }
                }
            });
            return null;
        }

        public static void openFile() {
            DriveId driveId = DriveId.decodeFromString(Drive.currentDriveId);
            final DriveFile driveFile = driveId.asDriveFile();
            driveFile.open(Google.apiClient, DriveFile.MODE_READ_ONLY, new DriveFile.DownloadProgressListener() {
                @Override
                public void onProgress(long l, long l1) {
                    G.LogInteres("Progress done");
                }
            })
                    .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                        @Override
                        public void onResult(@NonNull DriveApi.DriveContentsResult result) {
                            if (!result.getStatus().isSuccess()) {
                                G.Log("EXCEPTION(open drive file): " + result.getStatus().getStatusMessage());
                                return;
                            }
                            InputStream inputStream = result.getDriveContents().getInputStream();
                            G.InputStreamToString(inputStream);
                        }
                    });
        }


        @Nullable
        public static String downloadFileFromDrive(final Context context, final String fileName, final String pathToSave) {
            G.Log("Google.Drive.downloadFileFromDrive..");
            G.Log("file name: "+fileName+", path to save: "+pathToSave);
            // Find the named file with the specific Mime type.
            Query query = new Query.Builder()
                    .addFilter(Filters.and(
                            Filters.eq(SearchableField.TITLE, fileName)))
                            //Filters.eq(SearchableField.MIME_TYPE, mimeType)))
                    .setSortOrder(new SortOrder.Builder()
                            .addSortDescending(SortableField.MODIFIED_DATE)
                            .build())
                    .build();

            com.google.android.gms.drive.Drive.DriveApi.query(apiClient,query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(@NonNull DriveApi.MetadataBufferResult result) {
                    G.Log("Callback Google.Drive.getDriveId.onResult..");
                    if (result.getStatus().isSuccess()==false) {G.Log("EXEPTION(Google.Drive.downloadFileFromDrive): "+result.getStatus().getStatusMessage());}
                    else {
                        metadataBuffer = result.getMetadataBuffer();
                        G.Log("Count of files in Drive with name("+fileName+"): " + metadataBuffer.getCount());
                        if (metadataBuffer.getCount()!=0) {
                            Drive.currentDriveId = metadataBuffer.get(0).getDriveId().encodeToString();
                            G.Log("current file DriveID: "+Drive.currentDriveId);
                            DriveId driveId = DriveId.decodeFromString(Drive.currentDriveId);
                            final DriveFile driveFile = driveId.asDriveFile();
                            driveFile.open(Google.apiClient, DriveFile.MODE_READ_ONLY, new DriveFile.DownloadProgressListener() {
                                @Override
                                public void onProgress(long l, long l1) {G.LogInteres("Progress done");}
                            })
                                    .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                                        @Override
                                        public void onResult(@NonNull DriveApi.DriveContentsResult result) {
                                            if (!result.getStatus().isSuccess()) {
                                                G.Log("EXCEPTION(Google.Drive.downloadFileFromDrive.openFirstFile): "+result.getStatus().getStatusMessage());
                                                return;
                                            }

                                            try {
                                                G.Log("is: "+result.getDriveContents().getDriveId());
                                                InputStream is = result.getDriveContents().getInputStream();
                                                OutputStream os = new FileOutputStream(pathToSave);

                                                byte[] buffer = new byte[1024];
                                                int length;
                                                int size=0;
                                                while ((length = is.read(buffer)) > 0) {
                                                    //G.LogInteres("Stream lenght: "+length);
                                                    size=size+length;
                                                    os.write(buffer, 0, length);
                                                }
                                                G.Log("Input stream size: "+size);
                                                os.flush();
                                            } catch (Exception e) {
                                                G.Log("EXCEPTION(Google.Drive.downloadFileFromDrive.saveLocalFile): "+e.getMessage());}
                                        }
                                    });
                            G.Log(G.LOGLINE);
                        } else {
                            G.Log("FIRST METADATA: NULL!"); Drive.currentDriveId = G.NONE_STRING;
                        }
                    }
                }
            });


//            MetadataBuffer buffer = null;
//            try {
//                buffer = appFolder.queryChildren(apiClient, query).await().getMetadataBuffer();
//
//                if (buffer != null && buffer.getCount() > 0) {
//                    G.Log("Found: " + buffer.getCount() + " file(s)");
//                    return buffer.get(0).getDriveId();
//                }
//                return null;
//            } finally {
//                if (buffer != null) {
//                    buffer.close();
//                }
//            }
            return null;
        }

        public static void deleteFile(String driveId) {
            G.Log("Google.Drive.deleteFile..");
            appFolder = com.google.android.gms.drive.Drive.DriveApi.getAppFolder(Google.apiClient);
            G.Log("1");
            getDriveId(DB.DB_NAME);
            G.Log("2");
            //DriveFile driveFile = com.google.android.gms.drive.Drive.DriveApi.getFile()
            DriveFile driveFile = DriveId.decodeFromString(driveId).asDriveFile();
            driveFile.delete(apiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    G.Log("Delete DriveFile Ballback..");
                    if (status.getStatus().isSuccess()==false) {G.Log("EXEPTION: "+status.getStatus().getStatusMessage());}
                    else {
                        G.Log("++DriveFile has deleted successfuly. ID: "+currentDriveId);
                        currentDriveId = G.NONE_STRING;
                    }
                }
            });
        }

        public static void uploadFile(final File file, final String driveFileName) {
            G.Log("+++Upload File");
            G.Log("From: "+file.getAbsolutePath()+",  to Drive/appFolder/"+driveFileName);
            final ResultCallback<DriveFolder.DriveFileResult> fileCallback3 = new
                    ResultCallback<DriveFolder.DriveFileResult>() {
                        @Override
                        public void onResult(DriveFolder.DriveFileResult result) {
                            if (!result.getStatus().isSuccess()) {
                                G.Log("ERROR fileCallback: Error while trying to create the file");
                                return;
                            }
                            G.Log("Created a file in App Folder: " + result.getDriveFile().getDriveId());
                        }
                    };

            final ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
                    new ResultCallback<DriveApi.DriveContentsResult>() {
                        @Override
                        public void onResult(DriveApi.DriveContentsResult result) {
                            if (!result.getStatus().isSuccess()) {
                                G.Log("ERROR driveContentsCallback: Error while trying to create new file contents");
                                G.Log("Status message: " + result.getStatus().getStatusMessage());
                                return;
                            }
                            try {
                                byte[] bytes = G.fileToBytes(file);
                                DriveContents driveContents = result.getDriveContents();
                                driveContents.getOutputStream().write(bytes);
                                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                        .setTitle(driveFileName)
                                        //.setMimeType("text/plain")
                                        .build();
                                com.google.android.gms.drive.Drive.DriveApi.getAppFolder(Google.apiClient)
                                        .createFile(Google.apiClient, changeSet, driveContents)
                                        .setResultCallback(fileCallback3);
                            } catch (IOException ioException) {
                                G.Log("EXCEPTION: " + ioException.getMessage());
                            }
                        }
                    };

            com.google.android.gms.drive.Drive.DriveApi.newDriveContents(Google.apiClient)
                    .setResultCallback(driveContentsCallback);
        }
    }
    //===========END DRIVE=================

    public static void signInInitialize(FragmentActivity context, GoogleApiClient.ConnectionCallbacks callbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailed) {
        try {
            G.Log("Google.sign-in initialization..");
            signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestScopes(com.google.android.gms.drive.Drive.SCOPE_FILE)
                    .requestScopes(com.google.android.gms.drive.Drive.SCOPE_APPFOLDER)
                    .build();
            apiClient = new GoogleApiClient.Builder(context)
                    .enableAutoManage(context, onConnectionFailed)
                    .addConnectionCallbacks(callbacks)
                    .addOnConnectionFailedListener(onConnectionFailed)
                    .addApi(com.google.android.gms.drive.Drive.API)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                    .build();
            G.Log("google API: "+apiClient.toString());

            Drive.appFolder = com.google.android.gms.drive.Drive.DriveApi.getAppFolder(apiClient);
            G.Log("google drive appFolder: "+Drive.appFolder);

            G.Log("Successfully.");
        } catch (IllegalStateException e) {G.Log("FAILED!"); G.Log("EXCEPTION: "+e.getMessage());}
    }

    public static void setAccount(GoogleSignInResult result) {
        G.Log("Google.setAccount..");
        lastSignInResult = result;
        if (Google.lastSignInResult == null) {
            G.Log("ERROR!! User has not authorized in Google. No data to get SignInAccount.");
            return;}
        else {
            signInAccount = result.getSignInAccount();
        }
    }

    public static boolean handleSignInResult(GoogleSignInResult result) {
        G.Log("Google.handleSignInResult..");
        if (result.isSuccess()) {
            Google.setSignInResult(result);
            G.user.getDataFromGoogle();
            G.user.logData();
            G.Log("Successfully");
            return true;
        }
        else {
            G.Log("Failed!");
            return false;
        }
    }

    public static void setSignInResult(GoogleSignInResult result) {
        lastSignInResult = result;
    }

    //public static void silentSignIn()
}
