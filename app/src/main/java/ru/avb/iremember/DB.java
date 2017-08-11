package ru.avb.iremember;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DB {
    public static final String LOCAL_NAME = "localUser.db";

    public static String
            dbName = LOCAL_NAME,
            TABLE_CATEGORIES = "categories",
            TABLE_EVENTS = "events",
            TABLE_NOTIFICS = "notifications";

    //Column name categories
    final static String
            CNC_ID = "id",
            CNC_LABEL = "label",
            CNC_TYPE = "type",
            CNC_ICON_ID = "icon_id",
            CNC_PARENT_CATEGORY = "parent_category",
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
    CNE_VALUE = "value";

    public static SQLiteDatabase db;

    public static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, dbName, null, 3);
        }

        public void onCreate(SQLiteDatabase dbLocal) {
            try {
                G.Log("Create database");
                db = dbLocal;
                createTable(TABLE_CATEGORIES);
                G.Log("Successfully");
            }
            catch (SQLException e) {G.Log("EXCEPTION: "+e.getMessage());}
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //read database rows
            DB.db = db;
            dropTable(TABLE_CATEGORIES);
            createTable(TABLE_CATEGORIES);
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
        if (tableName==TABLE_CATEGORIES) {
            try
            {
                db.execSQL("create table "+TABLE_CATEGORIES+" (" +
                        "id     integer primary key autoincrement," +
                        "name   text" +
                        ");"
                );
                G.Log("Successfully");
            }
            catch (SQLException e)		{G.Log("EXCEPTION: "+e.getMessage());}
            catch (Exception e) {G.Log("EXC: "+e.getMessage());}
        }
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
            //подключаемс¤ к Ѕƒ
            openReadableDB(context);
            //делаем запрос всех данных из таблицы mytable, получаем Cursor
            Cursor c = db.query(TABLE_CATEGORIES, null, null, null, null, null, null);
            //ставим позицию курсора на первую строку выборки
            //если в выборке нет строк, вернетс¤ false
            G.Log("Row count: "+c.getCount());
            int row = 0;
            if (c.moveToFirst()) {
                int idColIndex = c.getColumnIndex("id");
                int nameColIndex = c.getColumnIndex("name");
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
        values.put("name", "MyFirstRow");
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
