package kr.ac.hs.beet;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DiaryAdapter extends BaseAdapter {
    private static final String TAG = "DiaryAdapter";
    Context context;
    LayoutInflater layoutInflater;
    RelativeLayout rListView;

    //어댑터에 추가된 데이터를 저장하기 위한 ArrayList
    ArrayList<DiaryList> diaryLists = new ArrayList<DiaryList>();

    TextView day_text;
    TextView context_text;
    ImageView emoji;
    ImageView edit_img;
    ImageView delete_img;
    String reg_date;

    public DiaryAdapter(Context context, ArrayList<DiaryList> diaryLists){
        this.context = context;
        this.diaryLists = diaryLists;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        Log.i(TAG,"diaryLists.size(): "+ diaryLists.size());
        return diaryLists.size();
    }

    @Override
    public Object getItem(int position) {
        Log.i(TAG,"diaryLists.get(position): "+ diaryLists.get(position));
        return diaryLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.i(TAG,"diaryLists.getId(position): "+ position);
        return position;
    }

    //position에 위치한 데이터를 화면에 뿌려줄 view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.activity_list_diary, null);
        //DiaryList diaryList = diaryLists.get(position);

        //글 내용, 이모지, 삭제버튼, 편집버튼
        context_text =view.findViewById(R.id.context_text);
        emoji = view.findViewById(R.id.emoji);
        day_text = view.findViewById(R.id.day_text);
        edit_img = view.findViewById(R.id.edit_img);
        delete_img = view.findViewById(R.id.delete_img);

        //각 위젯에 데이터를 반영
        context_text.setText(diaryLists.get(position).getSentence());
        day_text.setText(diaryLists.get(position).getDate());


        edit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "edit", Toast.LENGTH_LONG).show();
            }
        });

        delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "delete", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

}
