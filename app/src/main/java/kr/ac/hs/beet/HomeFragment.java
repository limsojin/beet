package kr.ac.hs.beet;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private final String TAG = "HomeFragment";

    private FragmentManager fragmentManager;
    private Toolbar toolbar; //툴바

    //하단 버튼 없애기
    private View decorView;
    private int	uiOption;
    MyDbHelper myDbHelper;
    ViewPager2 viewPager2;

    //ArrayList<HomeQuestList> list = new ArrayList<>();
    EditText text2;
    TextView doit_count;

    int count;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager2 = view.findViewById(R.id.viewPager2);

        myDbHelper = new MyDbHelper(getActivity().getApplicationContext());

        text2 = view.findViewById(R.id.d_day);
        doit_count = view.findViewById(R.id.doit_count);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);

        String getTime = dateFormat.format(date);
        String today = "2022-05-31";

        if(today.compareTo(getTime)<0){
                count++;
                text2.setText(String.valueOf(count));
        }

        doit_count_lookup();


        /* 예시로 집어넣음!
        list.add(new HomeQuestList("7PM go to the gym"));
        viewPager2.setAdapter(new HomeAdapter(list));*/

        QusetInit();

        fragmentManager = getFragmentManager();

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

        return view;
    }
    //ToolBar에 toolbar.xml 을 인플레이트

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    //---------------

    //ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.action_store :
                //Log.i(TAG, "store");
                Toast.makeText(getActivity().getApplicationContext(), "Store Click", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_custom :
                //Log.i(TAG, "custom");
                Toast.makeText(getActivity().getApplicationContext(), "Custom Click", Toast.LENGTH_LONG).show();
                return true;
            case android.R.id.home :
                //Log.i(TAG, "home");
                Toast.makeText(getActivity().getApplicationContext(), "Home Click", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //--------------
    public void QusetInit(){
        ArrayList<HomeQuestList> list = new ArrayList<>();

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + ToDo.TABLE_NAME, null);

        if(c.moveToFirst()){
            do{
                int col1 = c.getInt(0);
                String col2 = c.getString(1);
                String co13 = c.getString(2);
                Log.i(TAG,"col1: "+ col1 + " col2: " + col2 + " col3: " + co13);

                list.add(new HomeQuestList(col2));
                viewPager2.setAdapter(new HomeAdapter(list));
            }while (c.moveToNext());
        }
        c.close();
        db.close();
    }

    public void doit_count_lookup(){
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Storage.TABLE_NAME, null);
        if(c.moveToFirst()){
            do{
                int count = c.getInt(2);
                Log.i(TAG,"READ COUNT: " + count);

                doit_count.setText(String.valueOf(count));

                if(count == 0){
                    doit_count.setText(String.valueOf(0));
                }
            }while(c.moveToNext());
        }
        c.close();
        db.close();
        //todo디비와 연동할것.
        //1.todo에서 비트 받기 버튼 -> 퀘스트 개수만큼 비트 카운트 -> 비트 개수 디비에 저장
        //2.비트 개수를 sql문으로 불러와서 int or string으로 받아옴 -> 메인홈 textview:doit_count에 집어넣음
    }

}
