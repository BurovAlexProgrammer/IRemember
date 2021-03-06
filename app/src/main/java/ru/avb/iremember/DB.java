package ru.avb.iremember;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.crashlytics.android.Crashlytics;


public class DB {

    public static final String LOCAL_NAME = "localUser.db";
    public static final int dbVersion = 9;

    public static String
            dbName = LOCAL_NAME,
            TABLE_CATEGORIES = "categories",
            TABLE_EVENTS = "events",
            TABLE_NOTIFICS = "notifications";

    public static SQLiteDatabase db;

    //<editor-fold desc="Column name categories">
    public final static String
            CNC_ID = "id",
            CNC_LABEL = "label",
            CNC_UNIT_LABEL = "unit_label",
            CNC_CONDITION = "condition",
            CNC_ICON_ID = "icon_id",
            CNC_PARENT_CATEGORY = "parent_category",
            CNC_ORDER_NUMBER = "order_number",
            CNC_DATE_CREATED = "date_created",
            CNC_DATE_MODIFIED = "date_modified",
            CNC_INITIAL_VALUE = "initial_value",
            CNC_FINAL_VALUE = "final_value",
            CNC_FINAL_VALUE_ENABLED = "final_value_enabled",
            CNC_NEGATIVE_VALUE_ENABLED = "negative_value_enabled",
            CNC_PREDICTION_ENABLED = "prediction_enabled",
            CNC_PREDICTION_PERIOD = "prediction_period",
            CNC_EVERAGE_VALUE = "everage_value",
            CNC_EVERAGE_VALUE_CALCULATE_ENABLED = "everage_value_calculate_enabled",
            CNC_EVERAGE_VALUE_CALCULATE_EVENTCOUNT = "everage_value_calculate_eventcount",
            CNC_FAVORITE_ENABLED = "favorite_enabled",
            CNC_FAVORITE_ORDER_NUMBER = "favorite_order_number";
    //</editor-fold>

    //<editor-fold desc="Column name events">
    public final static String
            CNE_ID = "id",
            CNE_CATEGORY_ID = "category_id",
            CNE_VALUE = "value",
            CNE_DATE_CREATED = "date_created",
            CNE_DATE_MODIFIED = "date_modified";
    //</editor-fold>

    //<editor-fold desc="Column name notifications">
    public final static String
            CNN_ID = "id",
            CNN_CATEGORY_ID = "category_id",
            CNN_LABEL = "label",
            CNN_VALUE = "value",
            CNN_REPEAT_ENABLED = "repeat_enabled",
            CNN_DAYTIME_BEGIN = "daytime_begin",
            CNN_DAYTIME_END = "daytime_end",
            CNN_DATE_LAST_REMINDER = "date_last_reminder",
            CNN_DATE_NEXT_REMINDER = "date_next_reminder";
    //</editor-fold>

    //Table type of variables
    public final static String
            TYPE_ID = "integer primary key autoincrement",
            TYPE_INTEGER = "integer",
            TYPE_TEXT = "text",
            TYPE_DATE = "date",
            TYPE_BOOLEAN = "boolean";
            static String TYPE_NUMERIC(int a, int b) {return "numeric ("+a+","+b+")";}


    public static class DBHelper extends SQLiteOpenHelper {
        private static DBHelper sInstance;
        public static synchronized DBHelper getInstance(Context context) {
            // Use the application context, which will ensure that you
            // don't accidentally leak an Activity's context.
            // See this article for more information: http://bit.ly/6LRzfx
            if (sInstance == null) {
                sInstance = new DBHelper(context.getApplicationContext());
            }
            return sInstance;
        }

        private DBHelper(Context context) {
            super(context, dbName, null, dbVersion);
        }

        public void onCreate(SQLiteDatabase dbLocal) {
            try {
                G.Log("[DBHelper.onCreate]");
                db = dbLocal;
                createTable(TABLE_CATEGORIES);
                createTable(TABLE_EVENTS);
                createTable(TABLE_NOTIFICS);
            }
            catch (SQLException e) {Crashlytics.logException(e);}
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                G.Log("[DBHelper.onUpgrade] onUpgrage DB from version " + oldVersion + " to version " + newVersion + "..");
                //read database rows
                DB.db = db;
                dropTable(TABLE_CATEGORIES);
                dropTable(TABLE_EVENTS);
                dropTable(TABLE_NOTIFICS);
                createTable(TABLE_CATEGORIES);
                createTable(TABLE_EVENTS);
                createTable(TABLE_NOTIFICS);
                DB.db.setVersion(newVersion);
                db.setVersion(newVersion);
                //closeDB();
                //write database rows
            } catch (SQLException e) {Crashlytics.logException(e);}
        }
    }

    public static SQLiteDatabase getReadableDB(Context context) {
        G.Log("[DB.getReadableDB]");
        SQLiteDatabase db = null;
        try
        {
            DBHelper dbHelper = new DBHelper(context);
            db = dbHelper.getReadableDatabase();
        }
        catch(SQLException e) {Crashlytics.logException(e);}
        return db;
    }

    public static SQLiteDatabase getWritableDB(Context context) {
        G.Log("[DB.getWritableDB]");
        SQLiteDatabase db = null;
        try
        {
            DBHelper dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();
        }
        catch(SQLException e) {Crashlytics.logException(e);}
        return db;
    }

    public static void openReadableDB(Context context) {
        try
        {
            G.Log("[DB.openReadableDB]");
            DBHelper dbHelper = new DBHelper(context);
            db = dbHelper.getReadableDatabase();
        }
        catch(SQLException e) {Crashlytics.logException(e);}
    }

    public static void openWritableDB(Context context) {
        try
        {
            G.Log("[DB.openWritableDB]");
            DBHelper dbHelper = new DBHelper(context);
            db = dbHelper.getReadableDatabase();

        }
        catch(SQLException e) {Crashlytics.logException(e);}
    }

    public static void closeDB() {
        try
        {
            G.Log("[DB.closeDB]");
            final String path = db.getPath().toString();
            db.close();
            G.Log("Database "+path+"  closed successfully.");
            if (db == null) G.Log("is Null!!!!");
        }
        catch (SQLException e) {Crashlytics.logException(e);}
    }

    public static void createTable(String tableName) {
        G.Log("[DB.createTable ("+tableName+")]");

            try
            {
                if (tableName==TABLE_CATEGORIES) {
                    db.execSQL("create table " + TABLE_CATEGORIES + " (" +
                            CNC_ID +" "+ TYPE_ID +","+
                            CNC_LABEL +" "+ TYPE_TEXT +","+
                            CNC_UNIT_LABEL +" "+ TYPE_TEXT +","+
                            CNC_CONDITION +" "+ TYPE_TEXT +","+
                            CNC_ICON_ID +" "+ TYPE_INTEGER +","+
                            CNC_PARENT_CATEGORY +" "+ TYPE_INTEGER +","+
                            CNC_ORDER_NUMBER +" "+ TYPE_INTEGER +","+
                            CNC_DATE_CREATED +" "+ TYPE_DATE +","+
                            CNC_DATE_MODIFIED +" "+ TYPE_DATE +","+
                            CNC_INITIAL_VALUE  +" "+ TYPE_INTEGER +","+
                            CNC_FINAL_VALUE  +" "+ TYPE_INTEGER +","+
                            CNC_FINAL_VALUE_ENABLED  +" "+ TYPE_BOOLEAN +","+
                            CNC_NEGATIVE_VALUE_ENABLED  +" "+ TYPE_BOOLEAN +","+
                            CNC_PREDICTION_ENABLED  +" "+ TYPE_BOOLEAN +","+
                            CNC_PREDICTION_PERIOD  +" "+ TYPE_INTEGER +","+
                            CNC_EVERAGE_VALUE  +" "+ TYPE_INTEGER +","+
                            CNC_EVERAGE_VALUE_CALCULATE_ENABLED  +" "+ TYPE_BOOLEAN +","+
                            CNC_EVERAGE_VALUE_CALCULATE_EVENTCOUNT  +" "+ TYPE_INTEGER +","+
                            CNC_FAVORITE_ENABLED  +" "+ TYPE_BOOLEAN +","+
                            CNC_FAVORITE_ORDER_NUMBER  +" "+ TYPE_INTEGER +
                            ");"
                    );
                }

                if (tableName==TABLE_EVENTS) {
                    db.execSQL("create table " + TABLE_EVENTS + " (" +
                            CNE_ID +" "+ TYPE_ID +","+
                            CNE_CATEGORY_ID +" "+ TYPE_INTEGER +","+
                            CNE_VALUE +" "+ TYPE_INTEGER +","+
                            CNE_DATE_CREATED +" "+TYPE_DATE +","+
                            CNE_DATE_MODIFIED +" "+TYPE_DATE +
                            ");"
                    );
                }

                if (tableName==TABLE_NOTIFICS) {
                    db.execSQL("create table " + TABLE_NOTIFICS + " (" +
                            CNN_ID +" "+ TYPE_ID +","+
                            CNN_CATEGORY_ID +" "+ TYPE_INTEGER +","+
                            CNN_LABEL +" "+ TYPE_TEXT +","+
                            CNN_VALUE +" "+ TYPE_INTEGER +","+
                            CNN_REPEAT_ENABLED +" "+ TYPE_BOOLEAN +","+
                            CNN_DAYTIME_BEGIN +" "+ TYPE_INTEGER +","+
                            CNN_DAYTIME_END +" "+ TYPE_INTEGER +","+
                            CNN_DATE_LAST_REMINDER +" "+ TYPE_DATE +","+
                            CNN_DATE_NEXT_REMINDER +" "+ TYPE_DATE +
                            ");"
                    );
                }
            }
            catch (SQLException e)		{Crashlytics.logException(e);}
    }

    private static void dropTable(String tableName) {
        G.Log("[DB.dropTable ("+tableName+")]");
        try {db.execSQL("drop table " + tableName);
            //G.Log("Successfully");
        }
        catch (SQLException e) {Crashlytics.logException(e);}
    }

    public static void logTable(Context context) {
        try {
            G.Log("[DB.logTable] log table to tag 'logDB'");
            //подключаемся к DB
            openReadableDB(context);
            //делаем запрос всех данных из таблицы, получаем Cursor
            Cursor c = db.query(TABLE_CATEGORIES, null, null, null, null, null, null);
            G.LogDB("Rows count in table "+TABLE_CATEGORIES+": "+c.getCount());
            //ставим позицию курсора на первую строку выборки
            //если в выборке нет строк, вернетс¤ false
//            G.Log("Row count: "+c.getCount());
            int row = 0;
            if (c.moveToFirst()) {
                int idColIndex = c.getColumnIndex(CNC_ID);
                int nameColIndex = c.getColumnIndex(CNC_LABEL);
                do {
                    G.LogDB("Row "+row+
                            "  id: "+c.getInt(idColIndex)+
                            "  label: "+c.getString(nameColIndex));
                    row++;
                } while (c.moveToNext());
            }
            else {G.LogInteres("No rows in this table.");}
            closeDB();
            G.LogInteres("------------------------------------------------------");
        }
        catch (SQLException e) {Crashlytics.logException(e);}
    }

    public static boolean createRow(Context context) {
        G.Log("[DB.createRow]");
        G.Log("Insert new record in table Categories");
        try
        {
            openWritableDB(context);
            ContentValues values = new ContentValues();
            values.put(CNC_LABEL, "MyFirstRow");
            for (int i=0;i<100;i++) {
                db.insert(TABLE_CATEGORIES, null, values);
            }
            G.Log("Successfully");
            closeDB();
            return true;
        }
        catch (SQLException e)
        {
            Crashlytics.logException(e);
            return false;
        }
    }

    public static void setDbName() {
        G.LogDB("[DB.setDbName]");
        if (G.user == null) {dbName = LOCAL_NAME; G.LogInteres("User is null. ");}
        if (G.user.isAuthorized())
            dbName = G.user.id+".db";
        else
            dbName = DB.LOCAL_NAME;
        G.LogDB("DB.dbName="+dbName);
    }

    public static void insertCategory(String name, String type, String unitLabel, int iconId, int initialValue) {

    }
}
