package ru.avb.iremember.asyncs;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.drive.query.SortOrder;
import com.google.android.gms.drive.query.SortableField;

import org.joda.time.DateTime;

import ru.avb.iremember.DB;
import ru.avb.iremember.G;
import ru.avb.iremember.Google;

import static ru.avb.iremember.G.user;
import static ru.avb.iremember.Google.Drive.metadataBuffer;
import static ru.avb.iremember.Google.apiClient;

/**
 * Created by Alex on 22.10.2016.
 */

public class CheckLastSync extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        G.Log("My CheckLastSync..");
        Query query = new Query.Builder()
                .addFilter(Filters.and(
                        Filters.eq(SearchableField.TITLE, DB.DB_NAME)))
                .setSortOrder(new SortOrder.Builder()
                        .addSortDescending(SortableField.MODIFIED_DATE)
                        .build())
                .build();


        com.google.android.gms.drive.Drive.DriveApi.query(apiClient,query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(@NonNull DriveApi.MetadataBufferResult callbackResult) {
                G.Log("Callback CheckLastSync.CheckLastSync.onResult..");
                user.lastSync = G.NONE_DATETIME;
                if (callbackResult.getStatus().isSuccess()==false) {
                    G.Log("EXCEPTION: "+callbackResult.getStatus().getStatusMessage());
                    return;}
                if (metadataBuffer.getCount()==0) {
                    G.Log("File does not exist in appFolder");
                    return;
                }
                //end check errors
                Google.Drive.currentMetadata = metadataBuffer.get(0);
                Google.Drive.currentDriveId = metadataBuffer.get(0).getDriveId().encodeToString();
                G.Log("current DriveID: "+ Google.Drive.currentDriveId);
                G.Log("last modif: "+metadataBuffer.get(0).getModifiedDate());

                metadataBuffer = callbackResult.getMetadataBuffer();
                user.lastSync = new DateTime(metadataBuffer.get(0).getModifiedDate());
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
