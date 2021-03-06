package kr.ac.hs.beet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> implements Checkable{
    private static final String TAG = "ToDoAdapter";
    private ArrayList<TodoItem> mTodoItems;
    private Context mContext;
    private MyDbHelper mTodoDBHelper;
    int count;
    private boolean mIsChecked;
    private BeetCheckBoxClickListener beetCheckBoxClickListener;


    public interface BeetCheckBoxClickListener{
        void BeetCheckBoxClick(int count);
    }

    public ToDoAdapter(ArrayList<TodoItem> todoItems, Context mContext, BeetCheckBoxClickListener beetCheckBoxClickListener) {
        this.mTodoItems = todoItems;
        this.mContext = mContext;
        this.beetCheckBoxClickListener = beetCheckBoxClickListener;
        mTodoDBHelper = new MyDbHelper(mContext);
    }

    @NonNull
    @Override
    public ToDoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_todo, parent, false);
        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoAdapter.ViewHolder holder, int position) {
        holder.checkBox.setText(mTodoItems.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mTodoItems.size();
    }

    @Override
    public void setChecked(boolean checked) {
        setChecked(mIsChecked ? false : true) ;
    }

    @Override
    public boolean isChecked() {
        return mIsChecked ;
    }

    @Override
    public void toggle() {
        setChecked(mIsChecked ? false : true) ;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private EditText text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            EditText text = itemView.findViewById(R.id.newTaskText);
            int beetposition = getAdapterPosition();
            checkBox = itemView.findViewById(R.id.todoCheckBox);

            //beetadd();
            //?????? ????????? ????????????

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int curPos = getAdapterPosition(); // ?????? ????????? ????????? ????????? ??????
                    TodoItem todoItem = mTodoItems.get(curPos);

                    String[] strChoiceItems = {"????????????", "????????????"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("????????? ????????? ?????? ????????????");
                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if(position == 0){ // ????????????
                                //?????? ?????????
                                Dialog dialog = new Dialog(mContext);
                                dialog.setContentView(R.layout.activity_todo_dialog_edit);
                                EditText et_content = dialog.findViewById(R.id.newTaskText);
                                Button btn_ok = dialog.findViewById(R.id.newTaskButton);
                                et_content.setText(todoItem.getContent());
                                //????????? ?????? ??????????????? ??????
                                et_content.setSelection(et_content.getText().length() - 1);

                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // Update table
                                        String content = et_content.getText().toString();
                                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()); // ?????? ?????? (??????????????????) ????????????
                                        String beforeTime = todoItem.getWriteDate();

                                        mTodoDBHelper.UpdateTodo(content, currentTime, beforeTime);

                                        // Update UI
                                        todoItem.setContent(content);
                                        todoItem.setWriteDate(currentTime);
                                        notifyItemChanged(curPos, todoItem);
                                        dialog.dismiss();
                                        Toast.makeText(mContext, "?????? ????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.show();
                            }else if(position == 1){ // ????????????
                                // Delete table
                                String beforeTime = todoItem.getWriteDate();
                                mTodoDBHelper.DeleteTodo(beforeTime);

                                // Delete UI
                                mTodoItems.remove(curPos);
                                notifyItemRemoved(curPos);
                                Toast.makeText(mContext, "????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.show();
                }
            });
        }
    }
    //?????????????????? ???????????? ??????, ?????? ???????????? ????????? ????????? ???????????? ???????????? ??????
    public void addItem(TodoItem _item){
        mTodoItems.add(0, _item);
        notifyItemInserted(0);
    }


}
