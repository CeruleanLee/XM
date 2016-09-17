package com.bwf.xmduobao.db;

import java.util.ArrayList;
import java.util.List;

import com.bwf.xmduobao.db.DuobaoSqlHelper.Columns.DuobaoListColumns;
import com.bwf.xmduobao.entity.GoodsItem;
import com.lidroid.xutils.util.LogUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DuobaoListHandler extends DuobaoSqlHelper{
	private SQLiteDatabase db;
	public DuobaoListHandler(Context context) {
		super(context);
		db = getReadableDatabase();
	}
	public void add(GoodsItem goods,int times){
		if(times <= 0){
			times = goods.getDefaultJoinTimes();
		}
		if(exist(goods.getPeriod())){
			String sql = "update duobao_list set times_in_car = times_in_car+{0},money = money+{1} where period = "+goods.getPeriod();
			sql = sql.replace("{0}", times+"")
					.replace("{1}", goods.getEachTimeMoney()*times+"");
			db.execSQL(sql);
			sql = "update duobao_list set times_in_car = remain_times,money = remain_times*each_time_money where times_in_car >remain_times and period = "+goods.getPeriod();
			db.execSQL(sql);
		}else{
			ContentValues values = new ContentValues();
			values.put(Columns.DuobaoListColumns.EACH_TIME_MONEY, goods.getEachTimeMoney());
			values.put(Columns.DuobaoListColumns.IMG, goods.getImg());
			values.put(Columns.DuobaoListColumns.TIMES_IN_CAR, times);
			values.put(Columns.DuobaoListColumns.PERIOD, goods.getPeriod());
			values.put(Columns.DuobaoListColumns.REMAIN_TIMES, goods.getRemainTimes());
			values.put(Columns.DuobaoListColumns.TITLE, goods.getTitle());
			values.put(Columns.DuobaoListColumns.TOTAL_TIMES, goods.getTotalTimes());
			values.put(Columns.DuobaoListColumns.TYPE, goods.getCategory());
			values.put(Columns.DuobaoListColumns.MONEY, goods.getEachTimeMoney()*times);
			db.insert(Columns.DuobaoListColumns.TABLE_NAME, null, values );
		}
	}
	private boolean exist(long period){			
		String selection = DuobaoListColumns.PERIOD + " = ?";
		String[] selectionArgs = new String[]{period+""};
		Cursor cursor = db.query(DuobaoListColumns.TABLE_NAME, new String[]{"_id"}, selection, selectionArgs, null, null, null, null);
		if(cursor.moveToNext()){
			return true;
		}
		return false;
	}

	public void queryAll(){
		Cursor cursor = db.query(DuobaoListColumns.TABLE_NAME, null, null, null, null, null, null, null);
		while(cursor.moveToNext()){
			LogUtils.d(cursor.getInt(cursor.getColumnIndex(Columns.DuobaoListColumns.ID))+"");
			LogUtils.d(cursor.getString(cursor.getColumnIndex(Columns.DuobaoListColumns.TITLE))+"");
			LogUtils.d(cursor.getInt(cursor.getColumnIndex(Columns.DuobaoListColumns.TIMES_IN_CAR))+"");
			LogUtils.d(cursor.getInt(cursor.getColumnIndex(Columns.DuobaoListColumns.MONEY))+"");
			LogUtils.d("-------------");
		}
	}
	public void clearList(){
		db.delete(Columns.DuobaoListColumns.TABLE_NAME, null, null);
	}
	public void close(){
		if(db!= null){
			db.close();
		}
	}
	public List<GoodsItem> getList() {
		Cursor cursor = db.query(Columns.DuobaoListColumns.TABLE_NAME, null, null, null, null, null, "_id desc");
		List<GoodsItem> list = new ArrayList<GoodsItem>();
		LogUtils.d("数据库中数据条数："+cursor.getCount());
		while(cursor.moveToNext()){
			GoodsItem item = new GoodsItem();
			item.setCategory(cursor.getInt(cursor.getColumnIndex(DuobaoListColumns.TYPE)));
			item.setImg(cursor.getString(cursor.getColumnIndex(DuobaoListColumns.IMG)));
			item.setTitle(cursor.getString(cursor.getColumnIndex(DuobaoListColumns.TITLE)));
			item.setTotalTimes(cursor.getInt(cursor.getColumnIndex(DuobaoListColumns.TOTAL_TIMES)));
			item.setRemainTimes(cursor.getInt(cursor.getColumnIndex(DuobaoListColumns.REMAIN_TIMES)));
			item.setTimesInCar(cursor.getInt(cursor.getColumnIndex(DuobaoListColumns.TIMES_IN_CAR)));
			item.setPeriod(cursor.getLong(cursor.getColumnIndex(DuobaoListColumns.PERIOD)));
			item.setEachTimeMoney(cursor.getInt(cursor.getColumnIndex(DuobaoListColumns.EACH_TIME_MONEY)));
			list.add(item);
		}
		cursor.close();
		return list;
	}
	public void update(GoodsItem goodsItem){
		ContentValues values = new ContentValues();
		values.put(DuobaoListColumns.TIMES_IN_CAR, goodsItem.getTimesInCar());
		String whereClause = DuobaoListColumns.PERIOD +" = ?";
		String[] whereArgs = new String[]{goodsItem.getPeriod()+""};
		db.update(DuobaoListColumns.TABLE_NAME, values, whereClause, whereArgs);
	}
	public void updateList(List<GoodsItem> list) {
		for (GoodsItem goodsItem : list) {
			ContentValues values = new ContentValues();
			values.put(DuobaoListColumns.TIMES_IN_CAR, goodsItem.getTimesInCar());
			String whereClause = DuobaoListColumns.PERIOD +" = ?";
			String[] whereArgs = new String[]{goodsItem.getPeriod()+""};
			db.update(DuobaoListColumns.TABLE_NAME, values, whereClause, whereArgs);
		}
	}
}
