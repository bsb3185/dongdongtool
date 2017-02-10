package com.rhtop.dongdongtool.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GetGoodDataBaseHelper extends SQLiteOpenHelper {

	public GetGoodDataBaseHelper(Context context, String name, int version) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}
	String sql = "create table trade(_id integer primary key autoincrement,serialName,factoryNo,pack,price,number,totalPay,firstPay,onPort)";
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
