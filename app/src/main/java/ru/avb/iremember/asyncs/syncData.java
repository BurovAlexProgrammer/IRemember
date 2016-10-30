package ru.avb.iremember.asyncs;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.drive.Drive;

import java.io.File;

import ru.avb.iremember.DB;
import ru.avb.iremember.G;
import ru.avb.iremember.Google;

public class syncData extends AsyncTask {
    private Context context;
    private String type = G.SyncType.NOT_SELECTED;

    public syncData(Context c, String syncType) {
        context = c;
        type = syncType;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        G.Log("Sync Data..");
        if (type==G.SyncType.NOT_SELECTED) {G.Log("Type is not selected."); return null;}
        if (type==G.SyncType.TO_SERVER) {
            G.Log("Type is to-server");
            File f = context.getDatabasePath(DB.dbName);
            if (f==null) {G.Log("DB-file in null"); return null;}
            Google.Drive.uploadFile(context, f, DB.dbName);
        }
        if (type==G.SyncType.FROM_SERVER) {
            G.Log("Type is from-server");
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
