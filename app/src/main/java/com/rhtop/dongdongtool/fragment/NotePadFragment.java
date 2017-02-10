package com.rhtop.dongdongtool.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.rhtop.dongdongtool.R;
import com.rhtop.dongdongtool.db.NotesDB;
import com.rhtop.dongdongtool.view.AlertDialog;

import junit.framework.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cx on 2017/2/6.
 * email :chenxin@rhtop.cn
 */

public class NotePadFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AbsListView.OnScrollListener {
    private static final String TAG = "chenxin";
    private TextView tv_date;
    private EditText et_content;
    private ImageButton btn_ok;
    private ImageButton btn_cancel;
    private ImageButton btn_new;
    public static int ENTER_STATE = 0;
    public static int id;

    private ListView saveListview;
    private SimpleAdapter simp_adapter;
    private List<Map<String, Object>> dataList;
    private NotesDB DB;
    private SQLiteDatabase dbread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "NotePadFragment-----onCreateView");
        View view = inflater.inflate(R.layout.note_fragment, container, false);
        tv_date = (TextView) view.findViewById(R.id.tv_date);
        saveListview = (ListView) view.findViewById(R.id.save_listview);

        DB = new NotesDB(getContext());
        dbread = DB.getReadableDatabase();
        // 清空数据库中表的内容
        //dbread.execSQL("delete from note");
//        RefreshNotesList();

        saveListview.setOnItemClickListener(this);
        saveListview.setOnItemLongClickListener(this);
        saveListview.setOnScrollListener(this);

        dataList = new ArrayList<Map<String, Object>>();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = sdf.format(date);
        tv_date.setText(dateString);

        et_content = (EditText) view.findViewById(R.id.et_content);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        RefreshNotesList();
//        Bundle myBundle = getActivity().getIntent().getExtras();
//        last_content = myBundle.getString("info");
//        Log.d("LAST_CONTENT", last_content);
//        et_content.setText(last_content);
        btn_ok = (ImageButton) view.findViewById(R.id.button_save);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // 获取日志内容
                String content = et_content.getText().toString();
                Log.i("LOG1", content);
                // 获取写日志时间
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateNum = sdf.format(date);
                String sql;
                String sql_count = "SELECT COUNT(*) FROM note";
                SQLiteStatement statement = dbread.compileStatement(sql_count);
                long count = statement.simpleQueryForLong();
                Log.i("COUNT", count + "");
                Log.i("ENTER_STATE", ENTER_STATE + "");
                // 添加一个新的日志
                if (ENTER_STATE == 0) {
                    if (!content.equals("")) {
                        sql = "insert into " + NotesDB.TABLE_NAME_NOTES
                                + " values(" + count + "," + "'" + content
                                + "'" + "," + "'" + dateNum + "')";
                        Log.d("LOG", sql);
                        dbread.execSQL(sql);
                    }
                }
                // 查看并修改一个已有的日志
                else {
                    Log.d("执行命令", "执行了该函数");
                    String updatesql = "update note set content='"
                            + content + "' where _id=" + id;
                    dbread.execSQL(updatesql);
                    // et_content.setText(last_content);
                }
//                Intent data = new Intent();
//                setResult(2, data);
                RefreshNotesList();
            }
        });
        btn_cancel = (ImageButton) view.findViewById(R.id.button_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                et_content.setText("");
//                NotePadFragment.ENTER_STATE = 0;
            }
        });
        btn_new = (ImageButton) view.findViewById(R.id.button_new);
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_content.setText("");
                NotePadFragment.ENTER_STATE = 0;
            }
        });

        return view;

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NotePadFragment.ENTER_STATE = 1;
        // Log.d("arg2", arg2 + "");
        // TextView
        // content=(TextView)listview.getChildAt(arg2).findViewById(R.id.tv_content);
        // String content1=content.toString();
        String content = saveListview.getItemAtPosition(position) + "";
        String content1 = content.substring(content.indexOf("=") + 1,
                content.indexOf(","));
        Log.i("CONTENT", content1);
        Cursor c = dbread.query("note", null,
                "content=" + "'" + content1 + "'", null, null, null, null);
        while (c.moveToNext()) {
            String No = c.getString(c.getColumnIndex("_id"));
            Log.i("TEXT", No);
            // Intent intent = new Intent(mContext, noteEdit.class);
            // intent.putExtra("data", text);
            // setResult(4, intent);
            // // intent.putExtra("data",text);
            // startActivityForResult(intent, 3);
//            Intent myIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putString("info", content1);
            NotePadFragment.id = Integer.parseInt(No);
//            myIntent.putExtras(bundle);
//            myIntent.setClass(MainActivity.this, noteEdit.class);
//            startActivityForResult(myIntent, 1);
            et_content.setText(content1);
        }
    }

    /**
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final int n=position;
        final AlertDialog ad=new AlertDialog(getContext());
        ad.setTitle("删除记事");
        ad.setMessage("确定删除此备忘信息？");
        ad.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ad.dismiss();
                String content = saveListview.getItemAtPosition(n) + "";
                String content1 = content.substring(content.indexOf("=") + 1,
                        content.indexOf(","));
                Cursor c = dbread.query("note", null, "content=" + "'"
                        + content1 + "'", null, null, null, null);
                while (c.moveToNext()) {
                    String id = c.getString(c.getColumnIndex("_id"));
                    String sql_del = "update note set content='' where _id="
                            + id;
                    dbread.execSQL(sql_del);
                    RefreshNotesList();
                }
            }
        });
        ad.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                ad.dismiss();
            }
        });
        Log.i("chenxin", "changan");
        return true;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_FLING:
                Log.i("chenxin", "用户在手指离开屏幕之前，由于用力的滑了一下，视图能依靠惯性继续滑动");
            case SCROLL_STATE_IDLE:
                Log.i("chenxin", "视图已经停止滑动");
            case SCROLL_STATE_TOUCH_SCROLL:
                Log.i("chenxin", "手指没有离开屏幕，试图正在滑动");
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public void RefreshNotesList() {

        int size = dataList.size();
        if (size > 0) {
            dataList.removeAll(dataList);
            simp_adapter.notifyDataSetChanged();
            saveListview.setAdapter(simp_adapter);
        }
        simp_adapter = new SimpleAdapter(getContext(), getData(), R.layout.note_save_item,
                new String[] { "tv_content", "tv_date" }, new int[] {
                R.id.tv_content, R.id.tv_date });
        saveListview.setAdapter(simp_adapter);
    }

    private List<Map<String, Object>> getData() {

        Cursor cursor = dbread.query("note", null, "content!=\"\"", null, null,
                null, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("content"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tv_content", name);
            map.put("tv_date", date);
            dataList.add(map);
        }
        cursor.close();
        return dataList;

    }

}
