package kr.ac.hs.beet;

import java.lang.reflect.Member;


public class ToDo {
    public static final String TABLE_NAME = "todo";
    public static final String TODO_ID = "todo_id";
    public static final String QUSET = "quset";
    public static final String DATE= "date";


    private static final String SQL_CREATE_TODO =
            "CREATE TABLE " + ToDo.TABLE_NAME + " (" +
                    ToDo.TODO_ID + " INTEGER PRIMARY KEY," +
                    ToDo.QUSET + " TEXT," +
                    ToDo.DATE + " TEXT)";

    private static final String SQL_DELETE_TODO =
            "DROP TABLE IF EXISTS " + ToDo.TABLE_NAME;

}
