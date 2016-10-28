package ru.avb.iremember.asyncs;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.drive.Drive;

import java.io.File;

import ru.avb.iremember.DB;
import ru.avb.iremember.G;
import ru.avb.iremember.Google;

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
            File f = context.getDatabasePath(DB.dbName);
            if (f==null) {G.Log("DB-file in null"); return null;}
            Google.Drive.uploadFile(context, f, DB.dbName);
        }
        if (type==fromServer) {
            Google.Drive.appFolder = Drive.DriveApi.getAppFolder(Google.apiClient);
            DB.getWritableDB(context);
            Google.Drive.downloadFileFromDrive(context, DB.dbName, DB.db.getPath());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
