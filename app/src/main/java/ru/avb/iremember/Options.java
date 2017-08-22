package ru.avb.iremember;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;

import org.joda.time.DateTime;

import static ru.avb.iremember.G.user;

/**
 * Created by Alex on 27.04.2016.
 */
public class Options extends AppCompatActivity {
    public static SharedPreferences sharedPref;
    public static SharedPreferences.Editor prefEditor;
    public static boolean   isOptionsExist,
                            isNeedWelcome,
                            showNotification;
    public static String    locale;
    public static DateTime lastSync;


    //OPTIONS PREFERENCES
    public static final String  KEY_NEED_WELCOME = "isNeedWelcome",
                                KEY_OPTIONS_EXIST = "isOptionsExist",
                                KEY_LOCALE = "prefLocale",
                                KEY_SHOW_NOTIFICATION = "prefShowNotif",
                                KEY_LAST_SYNC = "prefLastSync",
                                KEY_USER_ID = "lastUserId";
    public static final String VALUE_OPTIONS_EXIST = "OptionsExist";
    public static final String VALUE_OPTIONS_FAIL = "OptionsFail",
                                VALUE_TRUE = "true",
                                VALUE_FALSE = "false";

    public static int currentTheme = G.ZERO;
    public static final int
            THEME_DEFAULT = R.style.DefaultTheme,
            THEME_GRAY = R.style.GrayTheme,
            THEME_BLACK = R.style.BlackTheme;
    public static String KEY_THEME = "spCurrentTheme";

    public static void applyTheme(Activity activity, int theme) {
        G.Log("[Options.applyTheme]: ThemeId = "+theme);
        currentTheme = theme;
        spWriteTheme(activity);
        restartActivity(activity);
    }

    public static void restartActivity(Activity activity) {
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void setTheme(Activity activity) {
        G.Log("setThemeOnCreate(): currentThemeId = "+currentTheme);
        if (currentTheme!=G.ZERO) activity.setTheme(currentTheme);
        //if (currentTheme!=G.ZERO) activity.getApplication().setTheme(currentTheme);
    }

    public static int spReadTheme(Activity activity) {
        initPreferences(activity);
        G.Log("spReadTheme(): theme Id = "+sharedPref.getInt(KEY_THEME, G.ZERO));
        return sharedPref.getInt(KEY_THEME, G.ZERO);
    }

    public static void spWriteTheme(Activity activity) {
        initPreferences(activity);
        prefEditor.putInt(KEY_THEME, currentTheme);
        prefEditor.commit();
    }

    public static void initOptions(Context context) {
        G.Log("[Options.initOptions]");
        initPreferences(context);
        //Check options exist
        isOptionsExist = sharedPref.getBoolean(KEY_OPTIONS_EXIST, false);
        if (!isOptionsExist) {
            G.Log("Options: not found.");
            loadDefaultOptions();
            saveOptions();
            initOptions(context);
        } else G.Log("Options: exist");
        //Load all options
        isNeedWelcome = sharedPref.getBoolean(KEY_NEED_WELCOME, true);
        locale = sharedPref.getString(KEY_LOCALE, "none");
        showNotification = sharedPref.getBoolean(KEY_SHOW_NOTIFICATION, true);
        if (locale.equals("none")) {
            G.Log("EXCEPTION: Options.locale didn't load. locale = default");
            locale = "default";
        }
        lastSync = readOption(KEY_LAST_SYNC, G.NONE_DATETIME);
        user.lastSync = lastSync;
        logOptions();
    }

    public static void writeOption(String key, String value) {
        G.Log("Write string option: '"+key.toString()+ "', value = "+value);
        prefEditor.putString(key, value);
        prefEditor.commit();
    }
    public static void writeOption(String key, boolean value) {
        G.Log("Write boolean option: '"+key.toString()+ "', value = "+value);
        prefEditor.putBoolean(key, value);
        prefEditor.commit();
    }
    public static void writeOption(String key, int value) {
        G.Log("Write int option: '"+key.toString()+ "', value = "+value);
        prefEditor.putInt(key, value);
        prefEditor.commit();
    }
    public static void writeOption(String key, DateTime value) {
        G.Log("Write datetime option: value(dt): "+value.toString());
        String s = String.valueOf(value.getMillis());
        G.Log("Write datetime option: '"+key.toString()+ "', value = "+s);
        prefEditor.putString(key, s);
        prefEditor.commit();
    }

    public static String readOption(String key, String defaultValue) {
        G.Log("Read string option: '"+key.toString()+ "', value = "+sharedPref.getString(key, defaultValue));
        return sharedPref.getString(key, defaultValue);
    }
    public static boolean readOption(String key, boolean defaultValue) {
        G.Log("Read boolean option: '"+key.toString()+ "', value = "+sharedPref.getBoolean(key, defaultValue));
        return sharedPref.getBoolean(key, defaultValue);
    }
    public static int readOption(String key, int defaultValue) {
        G.Log("Read int option: '"+key.toString()+ "', value = "+sharedPref.getInt(key, defaultValue));
        return sharedPref.getInt(key, defaultValue);
    }
    public static DateTime readOption(String key, DateTime defaultValue) {
        String s = sharedPref.getString(key, G.NONE_STRING);
        if (s.equals(G.NONE_STRING)) {
            G.Log("Read datetime option: not found!");
            return G.NONE_DATETIME;
        } else {
            long l = Long.parseLong(s);
            DateTime value = new DateTime(l);
            G.Log("Read datetime option: '" + key.toString() + "', value(String) = " + s + ", value(DateTime) = " + value.toString());
            return value;
        }
    }

    public static void loadDefaultOptions() {
        G.Log("Loading default options..");

        isOptionsExist = true;
        isNeedWelcome = true;
        locale = "default";
        showNotification = true;
        lastSync = G.NONE_DATETIME;
    }
    public static void saveOptions() {
        G.Log("[Options.saveOptions]");
        try {
            writeOption(KEY_OPTIONS_EXIST, isOptionsExist);
            writeOption(KEY_NEED_WELCOME, isNeedWelcome);
            writeOption(KEY_LOCALE, locale);
            writeOption(KEY_SHOW_NOTIFICATION, showNotification);
            writeOption(KEY_LAST_SYNC, lastSync);
        } catch (Exception e) {Crashlytics.logException(e);}
    }

    public static void initPreferences(Context context) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        prefEditor = sharedPref.edit();
    }

    private static void logOptions() {
        G.Log("[Options.logOptions]");
        G.Log("isOptionsExist: "+isOptionsExist+"||"+
        "isNeedWelcome: "+isNeedWelcome+"||"+
        "locale: "+locale+"||"+
        "showNotification: "+showNotification+"||"+
        "lastSync: "+lastSync.toString());
    }
}
