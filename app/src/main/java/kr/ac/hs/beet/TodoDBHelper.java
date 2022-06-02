package kr.ac.hs.beet;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TodoDBHelper extends SQLiteOpenHelper
{
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "todolist.db";

    private static final String TABLE_NAME = "table_name";
    private static final String TODO_ID = "table_name";
    private static final String QUSET = "table_name";
    private static final String DOIT_COUNT = "table_name";
    private static final String DATE= "table_name";


    public TodoDBHelper(@Nullable Context context)
    {
        super(context,DB_NAME, null,  DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //데이터 베이스가 생성이 될 때 호출
        //데이터베이스 -> 테이블 -> 컬럼 -> 값
        db.execSQL("CREATE TABLE IF NOT EXISTS TodoList (id INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT NOT NULL, writeDate TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onCreate(db);
    }

    //SELECT 문 (할 일 목록을 조회)
    public ArrayList<TodoItem> getTodoList() {
        ArrayList<TodoItem> todoItems = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TodoList ORDER BY writeDate DESC", null);
        if(cursor.getCount() != 0){ //조회한 데이터가 있을 때 수행
            while(cursor.moveToNext()){
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") String writeDate = cursor.getString(cursor.getColumnIndex("writeDate"));

                TodoItem todoItem = new TodoItem();
                todoItem.setId(id);
                todoItem.setContent(content);
                todoItem.setWriteDate(writeDate);
                todoItems.add(todoItem);
            }
        }
        cursor.close();
        return todoItems;
    }

    //INSERT 문 (할 일 목록을 db에 넣기)
    public void InsertTodo(String _content,  String _writeDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO TodoList (content, writeDate) VALUES('" + _content + "','" + _writeDate + "');");
    }

    //UPDATE 문 (할 일 목록을 수정)
    public void UpdateTodo(String _content, String _writeDate, String _beforeDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE TodoList SET content= '" + _content + "', writeDate= '" + _writeDate + "' WHERE writeDate='" + _beforeDate + "'");
    }

    //DELETE 문 (할 일 목록을 제거)
    public void DeleteTodo(String _beforeDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM TodoList WHERE writeDate = '" + _beforeDate + "'");
    }
}
