package com.rhtop.dongdongtool.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rhtop.dongdongtool.R;
import com.rhtop.dongdongtool.adapter.GetGoodListAdapter;
import com.rhtop.dongdongtool.db.GetGoodDataBaseHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by cx on 2017/2/10.
 * email :chenxin@rhtop.cn
 */

public class GetGoodFragment extends Fragment {
    GetGoodDataBaseHelper helper;
    private TextView textView;
    private ListView listView;
    private GridView gridView;
    private Button button;
    private HorizontalScrollView layout;
    private ArrayList<String> list1;
    private ArrayList<ArrayList<String>> lists = new ArrayList<ArrayList<String>>();

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.getgood_fragment, container, false);//关联布局文件
        listView = (ListView) rootView.findViewById(R.id.listview);
        button = (Button) rootView.findViewById(R.id.button);
        layout = (HorizontalScrollView) rootView.findViewById(R.id.layout);
        File file = getActivity().getFilesDir();
        String path = file.getAbsolutePath()+"/trade.db";
        ArrayList<String> list = new ArrayList<String>();
        list.add("序号");
        list.add("品类名称");
        list.add("厂号");
        list.add("包装");
        list.add("单价(元/吨)");
        list.add("数量(吨)");
        list.add("总价");
        list.add("首付");
        list.add("到港日");
        lists.add(list);
        helper = new GetGoodDataBaseHelper(getContext(), path, 1);
        final SQLiteDatabase db = helper.getReadableDatabase();
        db.execSQL("insert into trade values(null,?,?,?,?,?,?,?,?)",
                new String[]{"猪副/猪手","202","20kg/件","255000.22","30","15893.232","2500.2","2017.01.15"} );
        db.execSQL("insert into trade values(null,?,?,?,?,?,?,?,?)",
                new String[]{"猪头","203","30kg/件","25556.22","50","24222.232","3600.2","2017.02.11"} );
        db.execSQL("insert into trade values(null,?,?,?,?,?,?,?,?)",
                new String[]{"猪脚","204","40kg/件","345322.22","80","23248.32","3500.2","2017.02.28"} );
        int i = 0;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Cursor cursor = db.rawQuery("select * from trade", null);
                while (cursor.moveToNext()) {
                    //"create table trade(_id integer primary key autoincrement,serialNo,money,cardNum,date,status)";
                    list1 = new ArrayList<String>();
                    list1.add(cursor.getString(cursor.getColumnIndex("_id")));
                    list1.add(cursor.getString(cursor.getColumnIndex("serialName")));
                    list1.add(cursor.getString(cursor.getColumnIndex("factoryNo")));
                    list1.add(cursor.getString(cursor.getColumnIndex("pack")));
                    list1.add(cursor.getString(cursor.getColumnIndex("price")));
                    list1.add(cursor.getString(cursor.getColumnIndex("number")));
                    list1.add(cursor.getString(cursor.getColumnIndex("totalPay")));
                    list1.add(cursor.getString(cursor.getColumnIndex("firstPay")));
                    list1.add(cursor.getString(cursor.getColumnIndex("onPort")));

                    lists.add(list1);
                }
                cursor.close();
                GetGoodListAdapter adapter = new GetGoodListAdapter(getActivity(), lists);
                listView.setAdapter(adapter);
                layout.setVisibility(View.VISIBLE);

            }
        });
        return rootView;
    }


}
