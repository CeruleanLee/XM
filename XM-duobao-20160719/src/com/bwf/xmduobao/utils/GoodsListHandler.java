package com.bwf.xmduobao.utils;

import java.util.ArrayList;
import java.util.List;

import com.bwf.xmduobao.entity.GoodsItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class GoodsListHandler {

	/**
	 * 将商品添加进清单，保存在SharedPreferences中
	 * @param context
	 * @param goods
	 */
	public static void addGoods(Context context,GoodsItem goods){
		SharedPreferences preferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
		String goodsListJson = preferences.getString("goods_list", null);
		List<GoodsItem> list = null;
		Gson gson = new Gson();
		if(TextUtils.isEmpty(goodsListJson)){
			list = new ArrayList<GoodsItem>();
		}else{
			list = gson.fromJson(goodsListJson, new TypeToken<List<GoodsItem>>(){}.getType());
		}
		list.add(0,goods);
		preferences.edit().putString("goods_list", gson.toJson(list)).commit();
	}
	/**
	 * 将一系列商品添加进清单，保存在SharedPreferences中
	 * @param context
	 * @param goods
	 */
	public static void addGoodsList(Context context,List<GoodsItem> list){
		SharedPreferences preferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
		String goodsListJson = preferences.getString("goods_list", null);
		List<GoodsItem> list2 = null;
		Gson gson = new Gson();
		if(TextUtils.isEmpty(goodsListJson)){
			list2 = list;
		}else{
			list2 = gson.fromJson(goodsListJson, new TypeToken<List<GoodsItem>>(){}.getType());
			list2.addAll(0,list);
		}
		preferences.edit().putString("goods_list", gson.toJson(list)).commit();
	}
	/**
	 * 从SharedPreferences中获取清单列表
	 * @param context
	 * @return
	 */
	public static List<GoodsItem> getGoodsList(Context context){
		SharedPreferences preferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
		String goodsListJson = preferences.getString("goods_list", null);
		if(TextUtils.isEmpty(goodsListJson)){
			return null;
		}
		Gson gson = new Gson();
		return gson.fromJson(goodsListJson, new TypeToken<List<GoodsItem>>(){}.getType());
	}
	/**
	 * 清空保存的清单列表
	 * @param context
	 */
	public static void clearGoodsList(Context context){
		SharedPreferences preferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
		preferences.edit().putString("goods_list","").commit();
	}
	
}
