package com.bwf.xmduobao.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public final class AppSharedPreference {
	
	public static final void putBoolean(Context context,String sharedName,boolean value){
		
	}
	public static final void putValue(Context context,String sharedName,String key,Object value){
		SharedPreferences preferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		if(value instanceof String){
			editor.putString(key, value.toString());
		}else if(value instanceof Boolean){
			editor.putBoolean(key, Boolean.parseBoolean(value+""));
		}
		editor.commit();
	}
	public static final boolean getBooleanValue(Context context,String sharedName,String key,boolean defaultValue){
		SharedPreferences preferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
		return preferences.getBoolean(key, defaultValue);
	}
	
}
