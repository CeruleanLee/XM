package com.bwf.xmduobao.utils;

import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.os.Environment;

public class MBitmapHolder {
	
	public static BitmapUtils getBitmapUtils(Context context){
		String diskCachePath = Environment.getExternalStorageDirectory().getPath()+
				"/"+context.getPackageName()+"/cache/imgs";
		
		int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory()/4);
		BitmapUtils bitmapUtils = new BitmapUtils(context, diskCachePath, memoryCacheSize ); 
		return bitmapUtils;
	}
}
