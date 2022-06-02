package kr.ac.hs.beet;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ToDoFragment extends Fragment implements ToDoAdapter.BeetCheckBoxClickListener{
    private static final String TAG = "ToDoFragment";
    private RecyclerView mRv_todo;
    private FloatingActionButton mBtn_write;
    private ArrayList<TodoItem> mTodoItems;
    private MyDbHelper mDBHelper;
    private ToDoAdapter mAdapter;

    //MyDbHelper myDbHelper;
    Button button_beet;
    int count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        mRv_todo = view.findViewById(R.id.tasksRecyclerView);
        mBtn_write = view.findViewById(R.id.fab);
        mTodoItems = new ArrayList<>();

        mDBHelper = new MyDbHelper(getActivity().getApplicationContext());

        button_beet = view.findViewById(R.id.button_beet);

        button_beet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int beetsize = mTodoItems.size();
                Log.i(TAG,"beetsize: " + beetsize);

                for(int i=0; i<beetsize; i++){
                    count++;
                }
                if(count == beetsize){
                    ContentValues values3 = new ContentValues();
                    values3.put(Beet.BEET_COUNT,count);
                    SQLiteDatabase db = mDBHelper.getWritableDatabase();
                    long newRowId = db.insert(Beet.TABLE_NAME, null, values3);
                }else
                    Log.i(TAG,"비트없음");

                /*ContentValues values = new ContentValues();

                //values.put(ToDo.DOIT_COUNT, beetsize); -> 오류남 todohelper랑 겹쳐서 그런가봄

                values.put(Storage.DOIT_COUNT, beetsize);
                values.put(Customer.DOIT_COUNT, beetsize);

                SQLiteDatabase db = mDBHelper.getWritableDatabase();
                long newRowId = db.insert(Storage.TABLE_NAME, null, values);
                long newRowId2 = db.insert(Customer.TABLE_NAME, null, values);
                Log.i(TAG, "new row ID: " + newRowId);*/

                SQLiteDatabase db2 = mDBHelper.getReadableDatabase();
                Cursor c = db2.rawQuery("SELECT * FROM " + Beet.TABLE_NAME, null);
                if(c.moveToFirst()) {
                    int beetcount = c.getInt(1);
                    Log.i(TAG, "ToDoFragment beetcount: " + beetcount);

                    ContentValues values = new ContentValues();
                    values.put(Storage.DOIT_COUNT, beetcount);
                    values.put(Customer.DOIT_COUNT, beetcount);

                    SQLiteDatabase db = mDBHelper.getWritableDatabase();
                    long newRowId = db.insert(Storage.TABLE_NAME, null, values);
                    long newRowId2 = db.insert(Customer.TABLE_NAME, null, values);
                    Log.i(TAG, "new row ID: " + newRowId);

                }while(c.moveToNext());
                c.close();
                db2.close();
            }
        });


        loadRecentDB(); // load recent DB

        mBtn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.activity_todo_dialog_edit);
                EditText et_content = dialog.findViewById(R.id.newTaskText);
                Button btn_ok = dialog.findViewById(R.id.newTaskButton);

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Insert Databasse
                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()); // 현재 시간 (연월일시분초) 받아오기
                        mDBHelper.InsertTodo(et_content.getText().toString(), currentTime);
                        // Insert UI
                        TodoItem item = new TodoItem();
                        item.setContent(et_content.getText().toString());
                        item.setWriteDate(currentTime);

                        mAdapter.addItem(item);
                        mRv_todo.smoothScrollToPosition(0);
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "할일 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });
        return view;
    }

    private void loadRecentDB() { // 저장되어 있던 DB를 가져온다.
        mTodoItems = mDBHelper.getTodoList();
        mAdapter = new ToDoAdapter(mTodoItems, getActivity(),this);
        mRv_todo.setHasFixedSize(true);
        mRv_todo.setAdapter(mAdapter);

    }

    @Override
    public void BeetCheckBoxClick(int count){
        count++;
        boolean checkDB = true;
        SQLiteDatabase db1 = mDBHelper.getReadableDatabase();
        Cursor c = db1.rawQuery("SELECT * FROM " + Beet.TABLE_NAME,null);
        while(c.moveToFirst()){
            if(c.getString(0).equals("beet_id")){
                checkDB = false;
                break;
            }
        }c.close();

        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        if(checkDB){ //튜플 생성
            ContentValues values = new ContentValues();
            values.put(Beet.BEET_ID, 1);
            values.put(Beet.BEET_COUNT, 0);
            long newRowId2 = db.insert(Beet.TABLE_NAME,null, values);
            Log.i(TAG,"new row Id : " + newRowId2);
        }

        SQLiteDatabase db3 = mDBHelper.getWritableDatabase();

        ContentValues values3 = new ContentValues();
        values3.put(Beet.BEET_COUNT,count); //개수를 세고
        db3.update(Beet.TABLE_NAME, values3, Beet.BEET_ID + " = " + 1 ,null );
        //업데이트가 필요하면 보드아이디로 확인해 count를 새로 집어넣음
    }

}
