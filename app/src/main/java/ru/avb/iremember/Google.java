package ru.avb.iremember;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.drive.query.SortOrder;
import com.google.android.gms.drive.query.SortableField;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static ru.avb.iremember.G.user;


/**
 * Created by Алекс on 26.05.2016.
 */
public class Google {
    public static GoogleSignInOptions signInOptions;
    public static GoogleApiClient apiClient, driveApi;
    public static GoogleSignInAccount signInAccount;
    public static GoogleSignInResult lastSignInResult;

    public static  class SingIn {
        static boolean success;
        private static String s;
        public static void setSuccess(boolean bool) {success = bool;}
        public static boolean getSuccess() {return success;}
    }

    public static boolean isResolvingError;

    //=============DRIVE===================
    //TODO драйв загружает первый файл, а удаляет последний
    public static class Drive {
        public static DriveFolder appFolder;
        public static MetadataBuffer metadataBuffer;
        public static Metadata currentMetadata;
        public static String currentDriveId;
        public static DriveFile currentDriveFile;

        void createFile(final GoogleApiClient gac, final DriveFolder fldr,
                        final String name, final String mime, final byte[] buff) {
            G.Log("[Google.Drive.createFile]");


        }


        /**
         * Return to Google.Drive.currentDriveId last-modified-drive-file from AppFolder/'fileName'
         */
        @Nullable
        public static String getDriveId(String fileName) {
            G.Log("[Google.Drive.getDriveId]");
            //final String result;
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
                public void onResult(@NonNull DriveApi.MetadataBufferResult callbackResult) {
                    try {
                        G.Log("[Google.Drive.getDriveId.onResult] Callback");
                        if (callbackResult.getStatus().isSuccess() == false) {
                            G.Log("EXEPTION: " + callbackResult.getStatus().getStatusMessage());
                        } else {
                            metadataBuffer = callbackResult.getMetadataBuffer();
                            G.Log(G.LOGLINE);
                            G.Log("MetadataBuffer count: " + metadataBuffer.getCount());
                            G.Log(G.LOGLINE);
                            if (metadataBuffer.getCount() != 0) {
                                Drive.currentMetadata = metadataBuffer.get(0);
                                Drive.currentDriveId = metadataBuffer.get(0).getDriveId().encodeToString();
                                G.Log("current DriveID: " + Drive.currentDriveId +"||"+
                                "file size: " + metadataBuffer.get(0).getFileSize() +"||"+
                                "last modif: " + metadataBuffer.get(0).getModifiedDate());
                            } else {
                                G.Log("METADATA: NULL!");
                                Drive.currentDriveId = G.NONE_STRING;
                            }
                        }
                    } catch (Exception e) {Crashlytics.logException(e);}
                }
            });
            return Drive.currentDriveId;
        }

        public static void openFile() {
            G.Log("[Google.Drive.openFile]");
            try {
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
                                G.Convert.InputStreamToString(inputStream); //temp
                                //Must be open file code
                            }
                        });
            } catch (Exception e) {Crashlytics.logException(e);}
        }


        @Nullable
        public static String downloadFileFromDrive(final Context context, final String fileName, final String pathToSave) {
            try {
                G.Log("[Google.Drive.downloadFileFromDrive]");
                G.Log("File name: " + fileName + ", path to save: " + pathToSave);
                // Find the named file with the specific Mime type.
                Query query = new Query.Builder()
                        .addFilter(Filters.and(
                                Filters.eq(SearchableField.TITLE, fileName)))
                        //Filters.eq(SearchableField.MIME_TYPE, mimeType)))
                        .setSortOrder(new SortOrder.Builder()
                                .addSortDescending(SortableField.MODIFIED_DATE)
                                .build())
                        .build();

                com.google.android.gms.drive.Drive.DriveApi.query(apiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                    @Override
                    public void onResult(@NonNull DriveApi.MetadataBufferResult result) {
                        G.Log("[Google.Drive.getDriveId.onResult] Callback");
                        if (result.getStatus().isSuccess() == false) {
                            G.Log("EXEPTION: " + result.getStatus().getStatusMessage());
                        } else {
                            metadataBuffer = result.getMetadataBuffer();
                            G.Log("Count of files in Drive with name(" + fileName + "): " + metadataBuffer.getCount());
                            if (metadataBuffer.getCount() != 0) {
                                Drive.currentDriveId = metadataBuffer.get(0).getDriveId().encodeToString();
                                G.Log("Current file DriveID: " + Drive.currentDriveId);
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
                                                    G.Log("EXCEPTION(Google.Drive.downloadFileFromDrive.openFirstFile): " + result.getStatus().getStatusMessage());
                                                    return;
                                                }
                                                try {
                                                    InputStream is = result.getDriveContents().getInputStream();
                                                    OutputStream os = new FileOutputStream(pathToSave);

                                                    byte[] buffer = new byte[1024];
                                                    int length;
                                                    int size = 0;
                                                    while ((length = is.read(buffer)) > 0) {
                                                        //G.LogInteres("Stream lenght: "+length);
                                                        size = size + length;
                                                        os.write(buffer, 0, length);
                                                    }
                                                    G.Log("Input stream size: " + size);
                                                    os.flush();
                                                } catch (IOException e) {Crashlytics.logException(e);}
                                            }
                                        });
                                G.Log(G.LOGLINE);
                            } else {
                                G.Log("METADATA: NULL!");
                                Drive.currentDriveId = G.NONE_STRING;
                            }
                        }
                    }
                });
            }catch (Exception e) {Crashlytics.logException(e);}
            return null;
        }

        //Delete first file (the oldest)
        public static void deleteFile(String driveId) {
            try {
                G.Log("[Google.Drive.deleteFile]");
                appFolder = com.google.android.gms.drive.Drive.DriveApi.getAppFolder(Google.apiClient);
                getDriveId(DB.dbName);
                //DriveFile driveFile = com.google.android.gms.drive.Drive.DriveApi.getFile()
                DriveFile driveFile = DriveId.decodeFromString(driveId).asDriveFile();
                driveFile.delete(apiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        G.Log("[Google.Drive.deleteFile] Ballback");
                        if (status.getStatus().isSuccess() == false) {
                            G.Log("EXCEPTION: " + status.getStatus().getStatusMessage());
                        } else {
                            G.Log("DriveFile has deleted successfuly. ID: " + currentDriveId);
                            currentDriveId = G.NONE_STRING;
                        }
                    }
                });
            } catch (Exception e) {Crashlytics.logException(e);}
        }

        public static void uploadFile(final Context context, final File file, final String driveFileName) {
            try {
                G.Log("[Google.Drive.uploadFile]");
                G.Log("Upload from: " + file.getAbsolutePath() + ",  to Drive/appFolder/" + driveFileName);
                final ResultCallback<DriveFolder.DriveFileResult> uploadFileResult = new
                        ResultCallback<DriveFolder.DriveFileResult>() {
                            @Override
                            public void onResult(DriveFolder.DriveFileResult result) {
                                if (!result.getStatus().isSuccess()) {
                                    G.Log("EXCEPTION: fileCallback: Error while trying to create the file");
                                    return;
                                }
                                G.Log("Created a file in App Folder with DriveID: " + result.getDriveFile().getDriveId());
                                //Get metadata after uploading file to drive
                                result.getDriveFile().getMetadata(Google.apiClient).setResultCallback(new ResultCallback<DriveResource.MetadataResult>() {
                                    @Override
                                    public void onResult(@NonNull DriveResource.MetadataResult metadataResult) {
                                        G.Log("Get metadata after uploading file");
                                        if (metadataResult.getStatus().isSuccess() == false) {
                                            G.Log("EXCEPTION: " + metadataResult.getStatus().getStatusMessage());
                                            //TODO если метаданные не получены - то считать что файл не загружен (Указать на это пользователю)
                                            return;
                                        }
                                        Drive.currentMetadata = metadataResult.getMetadata();
                                        DateTime lastModif = new DateTime(Drive.currentMetadata.getModifiedDate());
                                        user.setLastSync(lastModif);
                                        user.setLastSyncText(user.getLastSyncTextFromDatetime(context, user.getLastSync()));
                                        //Options.initPreferences(context);
                                        Options.writeOption(Options.KEY_LAST_SYNC, lastModif);
                                        G.Log("Options.lastSyncText: " + user.getLastSyncTextFromDatetime(context, user.getLastSync()));
                                        //TODO удалить ненужные updateUI
                                        ((HomeActivity) context).updateUI();
                                    }
                                });
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
                                    byte[] bytes = G.Convert.fileToBytes(file);
                                    DriveContents driveContents = result.getDriveContents();
                                    driveContents.getOutputStream().write(bytes);
                                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                            .setTitle(driveFileName)
                                            //.setMimeType("text/plain")
                                            .build();
                                    com.google.android.gms.drive.Drive.DriveApi.getAppFolder(Google.apiClient)
                                            .createFile(Google.apiClient, changeSet, driveContents)
                                            .setResultCallback(uploadFileResult);
                                } catch (IOException ioException) {
                                    G.Log("EXCEPTION: " + ioException.getMessage());
                                }
                            }
                        };

                com.google.android.gms.drive.Drive.DriveApi.newDriveContents(Google.apiClient)
                        .setResultCallback(driveContentsCallback);
            } catch (Exception e) {Crashlytics.logException(e);}
        }
    }
    //===========END DRIVE=================

    public static void signInInit(FragmentActivity context, GoogleApiClient.ConnectionCallbacks callbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailed) {
        try {
            G.Log("[Google.signInInit]");
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

            //Drive.appFolder = com.google.android.gms.drive.Drive.DriveApi.getAppFolder(apiClient);
            //G.Log("google drive appFolder: "+Drive.appFolder);

            G.Log("Successfully.");
        } catch (IllegalStateException e) {G.Log("FAILED!"); G.Log("EXCEPTION: "+e.getMessage());}
          catch (Exception e) {G.Log("EXC: "+e.getMessage());}
    }

    public static void setAccount(GoogleSignInResult result) {
        try {
            G.Log("[Google.setAccount]");
            lastSignInResult = result;
            if (Google.lastSignInResult == null) {
                G.Log("ERROR!! User has not authorized in Google. No data to get SignInAccount.");
                return;
            } else {
                signInAccount = result.getSignInAccount();
            }
        } catch (Exception e) {Crashlytics.logException(e);}
    }

    public static boolean handleSignInResult(Context context, GoogleSignInResult result) {
        G.Log("[Google.handleSignInResult]");
        if (result.isSuccess()) {
            Google.SingIn.setSuccess(true);
            Google.setSignInResult(result);
            user.getDataFromGoogle(context);
            user.logData();
            G.Log("Successfully");
            return true;
        }
        else {
            Google.SingIn.setSuccess(false);
            G.Log("Google.handleSignInResult - Failed!");
            G.Log("Google.handleSignInResult: "+result.toString());
            G.Log("Google.handleSignInResult status: "+result.getStatus());
            G.Log("Google.handleSignInResult account: "+result.getSignInAccount());
            return false;
        }
    }

    public static void setSignInResult(GoogleSignInResult result) {
        lastSignInResult = result;
    }


    //public static void silentSignIn()
}
