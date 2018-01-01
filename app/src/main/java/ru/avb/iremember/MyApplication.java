package ru.avb.iremember;

import android.app.Application;
import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.Locale;

/**
 * Created by Алекс on 25.05.2016.
 */


public class MyApplication extends MultiDexApplication {
    private Locale locale;
    //private String lang;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        G.Log("[MyApplication.onCreate]");
        Options.initPreferences(this);
        String localeStr = Options.sharedPref.getString(Options.KEY_LOCALE, "none");
        G.Log("Options.locale: '"+localeStr+"'");
        if (localeStr.equals("none"))
            {Options.locale = G.locale.default_;
                locale = new Locale(Options.locale);
                G.Log("Installed locale: "+locale.toString());}
        else {
            Options.locale = localeStr;
            if (Options.locale.equals("default")) {Options.locale=getResources().getConfiguration().locale.getCountry();}
            locale = new Locale(Options.locale);
            G.Log("Installed locale: "+locale.toString());
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.setLocale(locale);
            getBaseContext().getResources().updateConfiguration(config, null);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        G.Log("[MyApplication.onConfigurationChanged]");
        Options.initPreferences(this);
        Options.locale = Options.sharedPref.getString(Options.KEY_LOCALE, G.locale.default_);
        locale = new Locale(G.locale.ru);
        Locale.setDefault(locale);
        G.Log("Installed locale: "+locale.toString());
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, null);
    }
}