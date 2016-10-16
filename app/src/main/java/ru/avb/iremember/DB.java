package ru.avb.iremember;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Алекс on 16.02.2016.
 */

//JUST TRY GIT!!!
    //NEW COMMIT FROM NOTEBOOK
public class DB {
    public final static String
            DB_NAME = "irememberDB",
            TABLE_CATEGORIES = "categories";

    //Column name payment
    final static String CNP_ID = "id";
    final static String CNP_ID_CREDIT = "idCredit";

    public static SQLiteDatabase db;

    public static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, DB_NAME, null, 3);
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
        G.Log("close DB");
        try
        {
            db.close();
            G.Log("Database "+db.getPath().toString()+"  closed.");
            G.Log("Successfully");
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
            int row = 0;
            if (c.moveToFirst())
            {
                int idColIndex = c.getColumnIndex("id");
                int nameColIndex = c.getColumnIndex("name");
                do
                {
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
            db.insert(TABLE_CATEGORIES, null, values);
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
}
