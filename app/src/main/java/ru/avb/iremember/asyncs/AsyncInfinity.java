package ru.avb.iremember.asyncs;

import android.os.AsyncTask;

import ru.avb.iremember.G;

/**
 * Created by Alex on 30.10.2016.
 */

public class AsyncInfinity extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        try {
            G.Log("AsynInfinity!!");
            int t=0;
            boolean b = true;
            while (b == true) {
                t+=1000;
                wait(1000);
                G.LogInteres("t:"+t);
            }
        }
        catch (Exception e) {G.LogException(e);}
        return null;
    }
}
