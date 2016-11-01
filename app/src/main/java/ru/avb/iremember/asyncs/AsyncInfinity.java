package ru.avb.iremember.asyncs;

import android.content.Context;
import android.os.AsyncTask;

import ru.avb.iremember.G;
import ru.avb.iremember.Options;

import static java.lang.Thread.sleep;

/**
 * Created by Alex on 30.10.2016.
 */

public class AsyncInfinity extends AsyncTask {
    private Context context;

        public AsyncInfinity(Context c) {
        context = c;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        G.Log("AsynInfinity1!!");
        boolean finish= false;
        try {
            G.Log("AsynInfinity2!!");
            int t=0;
            //boolean b = true;
            Options.initializeOptions(context);
            Options.readOption(Options.KEY_USER_ID, G.NONE_STRING);
            while (!finish) {
                if (isCancelled()) return null;
                t+=1000;
                sleep(1000);
                G.LogInteres("t:"+t);
            }
        }
        catch (Exception e) {G.LogException(e);}
        return null;
    }
}