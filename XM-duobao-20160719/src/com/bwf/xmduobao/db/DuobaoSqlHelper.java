package com.bwf.xmduobao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DuobaoSqlHelper extends SQLiteOpenHelper{
	private static final int VERSION = 1;
	private static final String DB_NAME = "duobao.db";
	public DuobaoSqlHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + Columns.DuobaoListColumns.TABLE_NAME+"("
				+ Columns.DuobaoListColumns.ID +" integer primary key autoincrement,"
				+ Columns.DuobaoListColumns.EACH_TIME_MONEY +" integer,"
				+ Columns.DuobaoListColumns.IMG + " text,"
				+ Columns.DuobaoListColumns.TIMES_IN_CAR + " integer,"
				+ Columns.DuobaoListColumns.PERIOD + " long,"
				+ Columns.DuobaoListColumns.REMAIN_TIMES + " integer,"
				+ Columns.DuobaoListColumns.TITLE + " text,"
				+ Columns.DuobaoListColumns.TOTAL_TIMES +" integer,"
				+ Columns.DuobaoListColumns.MONEY +" integer,"
				+ Columns.DuobaoListColumns.TYPE + " integer)";
		db.execSQL(sql);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	public static class Columns{
		public interface DuobaoListColumns{
			String TABLE_NAME = "duobao_list";
			String ID = "_id";
			String PERIOD = "period";
			String TITLE = "title";
			String IMG = "img";
			String TOTAL_TIMES = "title_times";
			String REMAIN_TIMES = "remain_times";
			String TIMES_IN_CAR = "times_in_car";
			String TYPE = "type";
			String EACH_TIME_MONEY = "each_time_money";
			String MONEY = "money";
		}
	}
}
