package com.bwf.xmduobao.utils;

import com.bwf.xmduobao.entity.ResponseUserinfo.Userinfo;
import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class UserInfoHandler {
	/**
	 * 保存用户信息
	 * @param context
	 * @param userinfo
	 */
	public static void saveUserinfo(Context context,Userinfo userinfo){
		SharedPreferences preferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
		String json = new Gson().toJson(userinfo);
		preferences.edit().putString("user_info", json)
		.putBoolean("is_login", true)
		.commit();
	}
	/**
	 * 获取之前保存的用户信息
	 * @param context
	 * @return
	 */
	public static Userinfo getUserInfo(Context context){
		Userinfo userinfo = null;
		SharedPreferences preferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
		String json = preferences.getString("user_info", null);
		if(!TextUtils.isEmpty(json)){
			userinfo = new Gson().fromJson(json, Userinfo.class);
		}
		return userinfo;
	}
	/**
	 * 清空之前保存的用户信息
	 * @param context
	 */
	public static void clearUserInfo(Context context){
		SharedPreferences preferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
		preferences.edit().putString("user_info", "")
		.putBoolean("is_login", false)
		.commit();
	}
	/**
	 * 获取用户是否登入
	 * @param context
	 * @return
	 */
	public static boolean isLogin(Context context){
		SharedPreferences preferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
		return preferences.getBoolean("is_login", false);
	}
}
