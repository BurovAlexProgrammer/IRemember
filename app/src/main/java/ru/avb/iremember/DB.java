package ru.avb.iremember;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DB {
    public static final String LOCAL_NAME = "localUser.db";
    public static final int dbVersion = 4;

    public static String
            dbName = LOCAL_NAME,
            TABLE_CATEGORIES = "categories",
            TABLE_EVENTS = "events",
            TABLE_NOTIFICS = "notifications";

    //Column name payment
    final static String CNP_ID = "id";
    final static String CNP_ID_CREDIT = "idCredit";

    public static SQLiteDatabase db;


    //Column name categories
    final static String
            CNC_ID = "id",
            CNC_LABEL = "label",
            CNC_TYPE = "type",
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
            CNC_EVERAGE_VALUE_CALCULATE_EVENTCOUNT = "everage_value_calculate_eventcount";

    //Column name events
    final static String
            CNE_ID = "id",
            CNE_CATEGORY_ID = "category_id",
            CNE_VALUE = "value",
            CNE_DATE_CREATED = "date_created",
            CNE_DATE_MODIFIED = "date_modified";

    //Column name notifications
    final static String
            CNN_ID = "id",
            CNN_CATEGORY_ID = "category_id",
            CNN_LABEL = "label",
            CNN_VALUE = "value",
            CNN_REPEAT_ENABLED = "repeat_enabled",
            CNN_TIME_BEGIN = "time_begin",
            CNN_TIME_END = "time_end",
            CNN_FREQUENCY = "frequency",
            CNN_LAST_REMINDER = "last_reminder";

    //Table type of variables
    final static String
            TYPE_ID = "integer primary key autoincrement",
            TYPE_INTEGER = "integer",
            TYPE_TEXT = "text",
            TYPE_DATE = "date",
            TYPE_BOOLEAN = "boolean";
            static String TYPE_NUMERIC(int a, int b) {return "numeric ("+a+","+b+")";}


    public static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, dbName, null, dbVersion);
        }

        public void onCreate(SQLiteDatabase dbLocal) {
            try {
                G.Log("Create database");
                db = dbLocal;
                createTable(TABLE_CATEGORIES);
                createTable(TABLE_EVENTS);
                createTable(TABLE_NOTIFICS);
                G.Log("Successfully");
            }
            catch (SQLException e) {G.Log("EXCEPTION: "+e.getMessage());}
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            G.Log("onUpgrage Database from version "+oldVersion+" to version "+newVersion+"..");
            //read database rows
            DB.db = db;
            dropTable(TABLE_CATEGORIES);
            dropTable(TABLE_EVENTS);
            dropTable(TABLE_NOTIFICS);
            createTable(TABLE_CATEGORIES);
            createTable(TABLE_EVENTS);
            createTable(TABLE_NOTIFICS);
            closeDB();
            //write database rows
        }
    }

    public static SQLiteDatabase getReadableDB(Context context) {
        G.Log("get readable DB");
        try
        {
            DBHelper dbHelper = new DBHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            G.Log("Successfully");
        }
        catch(SQLException e)
        {
            G.Log("EXCEPTION: "+e.getMessage());
        }
        return db;
    }
    public static SQLiteDatabase getWritableDB(Context context) {
        G.Log("get writable DB");
        try
        {
            DBHelper dbHelper = new DBHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            G.Log("Successfully");
        }
        catch(SQLException e)
        {
            G.Log("EXCEPTION: "+e.getMessage());
        }
        return db;
    }
    public static void openReadableDB(Context context) {
        G.Log("open readable DB");
        try
        {
            DBHelper dbHelper = new DBHelper(context);
            db = dbHelper.getReadableDatabase();
            G.Log("Successfully");
        }
        catch(SQLException e)
        {
            G.Log("EXCEPTION: "+e.getMessage());
        }
    }
    public static void openWritableDB(Context context) {
        G.Log("open writable DB");
        try
        {
            DBHelper dbHelper = new DBHelper(context);
            db = dbHelper.getReadableDatabase();
            G.Log("Successfully");
        }
        catch(SQLException e)
        {
            G.Log("EXCEPTION: "+e.getMessage());
        }
    }

    public static void closeDB() {
        G.Log("close DB. off");
        try
        {
            //db.close();
            //G.Log("Database "+db.getPath().toString()+"  closed.");
            //G.Log("Successfully");
        }
        catch (SQLException e)
        {G.Log("EXCEPTION: "+e.getMessage());}
    }

    public static void createTable(String tableName) {
        G.Log("create table "+tableName);

            try
            {
                if (tableName==TABLE_CATEGORIES) {
                    db.execSQL("create table " + TABLE_CATEGORIES + " (" +
                            CNC_ID +" "+ TYPE_ID + "," +
                            CNC_LABEL +" "+ TYPE_TEXT + ");"
                    );
                }

                if (tableName==TABLE_EVENTS) {
                    db.execSQL("create table " + TABLE_EVENTS + " (" +
                            CNE_ID +" "+ TYPE_ID +","+
                            CNE_CATEGORY_ID +" "+ TYPE_INTEGER + ");"
                    );
                }

                if (tableName==TABLE_NOTIFICS) {
                    db.execSQL("create table " + TABLE_NOTIFICS + " (" +
                            CNN_ID +" "+ TYPE_ID +","+
                            CNN_LABEL +" "+ TYPE_TEXT +");"
                    );
                }
                G.Log("Table " + tableName + " created successfully.");
            }
            catch (SQLException e)		{G.Log("EXCEPTION: "+e.getMessage());}
            catch (Exception e) {G.Log("EXC: "+e.getMessage());}
    }

    private static void dropTable(String tableName) {
        G.Log("drop table "+tableName);
        try {db.execSQL("drop table " + tableName); G.Log("Successfully");}
        catch (SQLException e) {G.Log("EXCEPTION: "+e.getMessage());}
    }

    public static void logTable(Context context) {
        try {
            G.Log("Log table in INTERES");
            G.LogInteres("-------------rows in "+TABLE_CATEGORIES+"--------------");
            //подключаемся к DB
            openReadableDB(context);
            //делаем запрос всех данных из таблицы, получаем Cursor
            Cursor c = db.query(TABLE_CATEGORIES, null, null, null, null, null, null);
            //ставим позицию курсора на первую строку выборки
            //если в выборке нет строк, вернетс¤ false
            G.Log("Row count: "+c.getCount());
            int row = 0;
            if (c.moveToFirst()) {
                int idColIndex = c.getColumnIndex(CNC_ID);
                int nameColIndex = c.getColumnIndex(CNC_LABEL);
                do {
                    G.LogInteres("Row "+row+
                            "  id: "+c.getInt(idColIndex)+
                            "  name: "+c.getString(nameColIndex));
                    row++;
                } while (c.moveToNext());
            }
            else {G.LogInteres("No rows in this table.");}
            closeDB();
            G.LogInteres("------------------------------------------------------");
        }
        catch (SQLException e) {G.Log("EXCEPTION: "+e.getMessage());}
    }

    public static boolean createRow(Context context) {
        G.Log(G.LOGLINE);
        G.Log("Insert new record in table Categories");
        ContentValues values = new ContentValues();
        values.put(CNC_LABEL, "MyFirstRow");
        try
        {
            openWritableDB(context);
            for (int i=0;i<100;i++) {
                db.insert(TABLE_CATEGORIES, null, values);
            }
            G.Log("Successfully");
            closeDB();
            G.Log(G.LOGLINE);
            return true;
        }
        catch (SQLException e)
        {
            G.Log("EXCEPTION: "+e.getMessage());
            G.Log(G.LOGLINE);
            return false;
        }
    }

    public static void setDbName() {
        G.LogInteres("DB.setDbName..");
        if (G.user == null) {dbName = LOCAL_NAME; G.LogInteres("User is null. ");}
        if (G.user.isAuthorized()) {dbName = G.user.id+".db";}
        G.LogInteres("DB.dbName="+dbName);
    }
}
