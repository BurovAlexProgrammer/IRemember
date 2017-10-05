package ru.avb.iremember;

//Нужно локально хранить lastSync
//Нужно обновить FragmentAccount from Google

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class G {

    public final class Request {
        public static final int
                ADD_CATEGORY = 1,
                SELECT_CATEGORY_ICON = 2,
                WELCOME_FROM_MAIN = 3,
                CHANGE_LANGUAGE = 4,
                SIGN_IN_GOOGLE = 5,
                RESOLVE_DRIVE_CONNECTION = 6,
                RESOLVE_ERROR = 7,
                FIRST = 8,
                LAST = 9,
                INTRO = 10,
                NEED_RESTART = 11,
                SET_DATETIME = 12;
    }
    public final class Result {
        public static final int
                OK = 101,
                CANCEL = 102,
                LATER = 103;
    }
    public final class Tag {
        public final static String
                NEED_RESTART = "dlgNeedrestart",
                SET_DATETIME_TO_INIT_VALUE = "dlgSetDateTimeToInitialValue",
                SET_DATETIME_TO_FINAL_VALUE = "dlgSetDateTimeToFinalValue",
                SET_DATE_TO_INIT_VALUE = "dlgSetDateToInitialValue",
                SET_DATE_TO_FINAL_VALUE = "dlgSetDateToFinalValue",
                SET_ICON = "dlgSetIcon";

    }

    public static final int
            ZERO = -999;


    public static final String
            PREF_FILENAME = "prfs",
            PREF_NEED_WELCOME = "nwk",
            PREF_TYPE = "type_",
            PREF_STYLE = "style_",

            DATETIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    public static final String
            EXTRA_MESSAGE = "mess",
            EXTRA_CATEGORY_NAME = "category_name",
            EXTRA_CATEGORY_ICON_ID = "category_iconId",
            EXTRA_CATEGORY_ICON_RESCODE = "categoty_iconResCode",
            EXTRA_SELECTED_LOCALE = "selected_locale",
            NULL = "$NULL",
            NONE_STRING = "$NONE_STR";
    public static final Date NONE_DATE = new Date(0,0,0);
    public static final DateTime NONE_DATETIME = new DateTime(0);
    public static final Calendar NONE_CALENDAR = new GregorianCalendar(2000,1,1);

    public static final String
            KEY_REQUEST = "kRequest",
            KEY_RESULT = "kResult",
            KEY_TAG = "kTag",
            KEY_LOCALE = "kLocale",
            KEY_DLG_DAY = "kDlgDay",
            KEY_DLG_MONTH = "kDlgMonth",
            KEY_DLG_YEAR = "kDlgYear",
            KEY_DLG_HOUR = "kDlgHour",
            KEY_DLG_MINUTE = "kDlgMinute";


    public final static class SyncType {
        public final static String FROM_SERVER = "from server",
            TO_SERVER = "to server",
            NOT_SELECTED = "not selected"; };
    //GLOBAL
    public static Fragment homeFragment;
    public static String homeFragmentTitle;
    public static FragmentManager homeFragmentManager;
    public static User user;
    public static final DateTimeFormatter datetimeFormatter = DateTimeFormat.forPattern(DATETIME_FORMATTER);

    public static final String LOGTAG = "logApp", LOGDB = "logDB", LOGINTERES = "logInteres", LOGLINE = "---------------------------------------------";

    public static void LogDB(String s) {
        Log.i(LOGDB, s);
    }
    public static void Log(String s) {
        Crashlytics.log(Log.INFO, LOGTAG, s);
        }
    public static void LogE(String s) {Log.e(LOGTAG, s); }
    public static void LogW(String s) {Log.w(LOGTAG, s); }
    public static void LogException(Exception e) {
        Log.i(LOGTAG, "EXCEPTION: "+e.getMessage());
    }
    public static void LogInteres(String s) {
        Log.i(LOGINTERES, s);}

    public static void Toast(Context context,String s) {Toast.makeText(context, s, Toast.LENGTH_SHORT).show();}
    public static void LogToast(Context context, String s) {
        Log(s);
        Toast(context, s);
    }

    public static Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            G.Log("Try open URL: "+url);
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log("Error getting bitmap" + e.getLocalizedMessage());
        }
        return bm;
    }

    public static class locale {
        public static String default_ = "default",
        ru = "ru",
        en = "en";
    }

/*
    public static Bitmap getImageBitmap2(String url) {
        Bitmap bm = null;
        try {
            File f = new File();
            G.Log("Try open URL: "+url);
            URL aURL = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection)aURL.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.Copy
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return bm;
    }
*/

    public static Bitmap getImageBitmap3(String surl) {
        Bitmap bm = null;
        try {
            G.Log("Try open URL: "+surl);
            URL url = new URL(surl);
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setInstanceFollowRedirects(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return bm;
    }

    public static Bitmap getImageBitmap4(String surl) {
        Bitmap bm = null;
        try {
            G.Log("Try open URL: "+surl);
            URL url = new URL(surl);
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setInstanceFollowRedirects(true);
            conn.setDoInput(true);
            conn.connect();
            //G.Log("Response message: "+conn.getResponseMessage());
            //G.Log("Response code: "+conn.getResponseCode());
            InputStream is = new BufferedInputStream(conn.getInputStream());
            bm = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return bm;
    }

    public static Bitmap getContactPhoto(String url) {
        G.Log("getContactPhoto");
        Bitmap pic = null;
        try {
            pic = BitmapFactory
                    .decodeStream((InputStream) new URL(url).getContent());
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return pic;

    }

    public static InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            throw new IOException("Error connecting");
        }
        return in;
    }

    public static Bitmap DownloadImage(String URL) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return bitmap;
    }

    public static String InputStreamToString(InputStream inputStream) {
        try {
            ByteArrayOutputStream inputStreamResult = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int lenght;
            while ((lenght = inputStream.read(buffer)) != -1) {
                inputStreamResult.write(buffer, 0, lenght);
            }
            G.Log(G.LOGLINE);
            G.LogInteres("Convert InputStream to String..");
            G.Log("Result: " + inputStreamResult.toString("UTF-8"));
            G.Log(G.LOGLINE);
            return inputStreamResult.toString("UTF-8");
        } catch (IOException ioException) {G.Log("EXCEPTION(get input stream): "+ioException.getMessage()); return G.NONE_STRING;}
    }

    public static byte[] fileToBytes(File f) {
        G.Log("file to bytes..");
        byte[] bytes = new byte[(int)f.length()];
        try {
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(f));
            dataInputStream.readFully(bytes);
            dataInputStream.close();
            G.Log("bytes: " + bytes.toString());
        }
        catch (IOException e) {G.Log("EXCEPTION: "+e.getMessage());}
        return bytes;
    }

    public static String timeAgo(Context context, DateTime datePrev, DateTime dateNow) {
        Period period = new Period(datePrev,dateNow);

        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendMonths().appendSuffix(context.getString(R.string.months_ago)).printZeroNever()
                .appendDays().appendSuffix(context.getString(R.string.days_ago))
                .appendHours().appendSuffix(context.getString(R.string.hours_ago))
                .appendMinutes().appendSuffix(context.getString(R.string.minutes_ago))
                //.appendSeconds().appendSuffix(context.getString(R.string.seconds_ago))
                //.appendWeeks().appendSuffix(context.getString(R.string.weeks_ago))
                //.appendYears().appendSuffix(context.getString(R.string.years_ago))
                .printZeroNever()
                .toFormatter();

        return formatter.print(period);
    }



    public static class Convert {
        public static String dateToString(Date date) {
            DateTime dateTime = new DateTime(date);
            DateTimeFormatter formatter = DateTimeFormat.forPattern(DATETIME_FORMATTER);
            String dateStr = formatter.print(dateTime);
            return dateStr;
        }


        public static Date stringToDate(String str) {

            DateTimeFormatter formatter = DateTimeFormat.forPattern(DATETIME_FORMATTER);
            DateTime dateTime = formatter.parseDateTime(str);
            Date date = dateTime.toDate();
            return date;
        }

        public static DateTime dateToDatetime(Date date) {
            return new DateTime(date);
        }
    }
}

