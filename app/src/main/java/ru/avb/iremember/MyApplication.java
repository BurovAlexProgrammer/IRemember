package ru.avb.iremember;

import android.app.Application;
import android.content.res.Configuration;

import java.util.Locale;

/**
 * Created by Алекс on 25.05.2016.
 */


public class MyApplication extends Application{
    private Locale locale;
    //private String lang;

    @Override
    public void onCreate() {
        super.onCreate();
        G.Log("MyApplication.onCreate()..");
        Options.initPreferences(this);
        String localeStr = Options.sharedPref.getString(Options.KEY_LOCALE, "none");
        if (localeStr.equals("none"))
            {Options.locale = G.locale.default_;}
        else {
            Options.locale = localeStr;
            G.Log("App language : '"+Options.locale+"'");
            if (Options.locale.equals("default")) {Options.locale=getResources().getConfiguration().locale.getCountry();}
            locale = new Locale(Options.locale);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.setLocale(locale);
            getBaseContext().getResources().updateConfiguration(config, null);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        G.Log("MyApplication.onConfigurationChanged()..");

        locale = new Locale(Options.locale);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, null);
    }
}