package ru.avb.iremember.asyncs;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.drive.query.SortOrder;
import com.google.android.gms.drive.query.SortableField;

import org.joda.time.DateTime;

import java.io.File;

import ru.avb.iremember.DB;
import ru.avb.iremember.G;
import ru.avb.iremember.Google;
import ru.avb.iremember.HomeActivity;

import static ru.avb.iremember.G.user;
import static ru.avb.iremember.Google.Drive.metadataBuffer;
import static ru.avb.iremember.Google.apiClient;

/**
 * Created by Admin on 24.10.2016.
 */

public class syncData extends AsyncTask {
    public String   fromServer = "from server",
                    toServer = "to server",
                    notSelected = "not selected";
    private Context context;
    private String type = notSelected;

    public syncData(Context c, String syncType) {
        context = c;
        type = syncType;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        G.Log("My checkLastSync..");
        if (type==notSelected) return null;
        if (type==toServer) {
            File f = context.getDatabasePath(DB.DB_NAME);
            if (f==null) {G.Log("DB-file in null"); return null;}
            Google.Drive.uploadFile(context, f, DB.DB_NAME);
        }
        if (type==fromServer) {
            Google.Drive.appFolder = Drive.DriveApi.getAppFolder(Google.apiClient);
            DB.getWritableDB(context);
            Google.Drive.downloadFileFromDrive(context, DB.DB_NAME, DB.db.getPath());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
