package kr.ac.hs.beet;

public class ToDo {
    public static final String TABLE_NAME = "todo";
    public static final String TODO_ID = "todo_id";
    public static final String QUSET = "quset";
    public static final String DOIT_COUNT = "doit_count";
    public static final String DATE = "date";

    //TODO_db
    private static final String SQL_CREATE_TODO =
            "CREATE TABLE " + ToDo.TABLE_NAME + " (" +
                    ToDo.TODO_ID + " INTEGER PRIMARY KEY," +
                    ToDo.QUSET + " TEXT," +
                    ToDo.DOIT_COUNT + " INTEGER," +
                    ToDo.DATE + " TEXT)";

    private static final String SQL_DELETE_TODO =
            "DROP TABLE IF EXISTS " + ToDo.TABLE_NAME;
}
