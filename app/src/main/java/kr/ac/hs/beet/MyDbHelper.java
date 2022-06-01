package kr.ac.hs.beet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Pocketpet.db";

    //TODO_db
    private static final String SQL_CREATE_TODO =
            "CREATE TABLE " + ToDo.TABLE_NAME + " (" +
                    ToDo.TODO_ID + " INTEGER PRIMARY KEY," +
                    ToDo.QUSET + " TEXT," +
                    ToDo.DOIT_COUNT + " INTEGER," +
                    ToDo.DATE + " TEXT)";

    private static final String SQL_DELETE_TODO =
            "DROP TABLE IF EXISTS " + ToDo.TABLE_NAME;

    //DIARY_db
    private static final String SQL_CREATE_DIARY =
            "CREATE TABLE " + Diary.TABLE_NAME + " (" +
                    Diary.DIARY_ID + " INTEGER PRIMARY KEY," +
                    Diary.SENTENCE + " TEXT," +
                    Diary.DATE + " TEXT," +
                    Diary.IMAGE + " TEXT)";

    private static final String SQL_DELETE_DIARY =
            "DROP TABLE IF EXISTS " + Diary.TABLE_NAME;


    //Storage_db
    private static final String SQL_CREATE_STORAGE =
            "CREATE TABLE " + Storage.TABLE_NAME + " (" +
                    Storage.IMAGE + " INTEGER PRIMARY KEY," +
                    Storage.ITEM_NAME + " TEXT," +
                    Storage.DOIT_COUNT + " INTEGER)";

    private static final String SQL_DELETE_STORAGE =
            "DROP TABLE IF EXISTS " + Storage.TABLE_NAME;


    //Customer_db
    private static final String SQL_CREATE_CUSTOMER =
            "CREATE TABLE " + Customer.TABLE_NAME + " (" +
                    Customer.CUSTOMER_ID + " INTEGER PRIMARY KEY," +
                    Customer.JOIN_DATE + " TEXT," +
                    Customer.DOIT_COUNT + " INTEGER)";

    private static final String SQL_DELETE_CUSTOMER =
            "DROP TABLE IF EXISTS " + Customer.TABLE_NAME;


    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TODO);
        sqLiteDatabase.execSQL(SQL_CREATE_DIARY);
        sqLiteDatabase.execSQL(SQL_CREATE_STORAGE);
        sqLiteDatabase.execSQL(SQL_CREATE_CUSTOMER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_TODO);
        sqLiteDatabase.execSQL(SQL_DELETE_DIARY);
        sqLiteDatabase.execSQL(SQL_DELETE_STORAGE);
        sqLiteDatabase.execSQL(SQL_DELETE_CUSTOMER);
        onCreate(sqLiteDatabase);
    }
}
