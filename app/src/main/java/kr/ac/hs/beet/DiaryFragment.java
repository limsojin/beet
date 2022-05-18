package kr.ac.hs.beet;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DiaryFragment extends Fragment {
    private static final String TAG = "DiaryFragment";

    MyDbHelper myDbHelper;

    private FragmentManager fragmentManager;
    private Toolbar toolbar; //툴바

    private DiaryAdapter diaryAdapter;
    private ListView listView;

    private ArrayList<DiaryList> diaryListArrayList;

    //하단 버튼 없애기
    private View decorView;
    private int	uiOption;

    TextView texttext;
    TextView diary_date;
    EditText diarycontent;
    Button save_button;
    String editText_diary_content;
    String reg_date;
    TextInputLayout inputLayout;
    TextInputEditText inputEditText;

    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime = dateFormat.format(date);
        return getTime;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        fragmentManager = getFragmentManager();
        myDbHelper = new MyDbHelper(getActivity().getApplicationContext());


        inputLayout = view.findViewById(R.id.input_layout);
        inputEditText = view.findViewById(R.id.diarycontent);

        //diarycontent = view.findViewById(R.id.diarycontent);
        save_button = view.findViewById(R.id.save_button);
        diary_date = view.findViewById(R.id.diary_date);

        inputEditText.getText().toString();


        //일기 내용물 가져옴 --?? 출력이 안됨
        //editText_diary_content= diarycontent.getText().toString();
        //String editText_diary_content= String.valueOf(diary_content); -> 안됨
        //editText_diary_content= String.valueOf(diary_content); -> 안됨
        diary_date.setText(getTime());

        editText_diary_content=inputEditText.getText().toString();

        Log.i(TAG,"inputEditText: " + inputEditText);
        Log.i(TAG,"inputEditText: " + inputEditText.getText());
        Log.i(TAG,"inputEditText: " + inputEditText.getText().toString());
        Log.i(TAG,"editText_diary_content: " + inputEditText.getText().toString());

        //툴바만들기
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        actionBar.setDisplayShowCustomEnabled(true); //뒤로가기 버튼 생성
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_card_giftcard_24);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        //----------

        //하단 버튼을 없애는 기능
        decorView = getActivity().getWindow().getDecorView();
        uiOption = getActivity().getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility( uiOption );
        //---------------------


        //버튼 누르면 글 내용, 날짜 저장
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //현재 시간 가져오기
                long now = System.currentTimeMillis();
                java.sql.Date date;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                now = System.currentTimeMillis();
                date = new java.sql.Date(now);
                reg_date = sdf.format(date);

                ContentValues values = new ContentValues();
                values.put(Diary.SENTENCE, editText_diary_content);
                values.put(Diary.DATE, reg_date);

                SQLiteDatabase db = myDbHelper.getWritableDatabase();
                long newRowId = db.insert(Diary.TABLE_NAME, null, values);

                Log.i(TAG, "new row ID: " + newRowId);
            }
        });
        // 리스트 아이템
        firstInit(view);

        DiaryInit();

        //어댑터 생성
        diaryAdapter = new DiaryAdapter(getActivity().getApplicationContext(), diaryListArrayList);
        listView.setAdapter(diaryAdapter);
        diaryAdapter.notifyDataSetChanged();
        //diaryAdapter.addItem(editText_diary_content,reg_date);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.diary_toolbar, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.action_calendar :
                Toast.makeText(getActivity().getApplicationContext(), "Calender action", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void firstInit(View v){
        listView = v.findViewById(R.id.listView);
        diaryListArrayList = new ArrayList<>();
    }


    public void DiaryInit(){

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Diary.TABLE_NAME, null);
        if(c.moveToFirst()){
            do{
                int diaryid = c.getInt(0);
                String sentence = c.getString(1);
                String date = c.getString(2);
                Log.i(TAG, "DIARYID: " + diaryid +" sentence: " + sentence + " date: " + date);

                diaryListArrayList.add(new DiaryList(sentence, date));

            }while(c.moveToNext());
        }c.close();
        db.close();
    }
}
