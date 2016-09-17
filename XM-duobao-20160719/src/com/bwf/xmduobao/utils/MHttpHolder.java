package com.bwf.xmduobao.utils;

import com.lidroid.xutils.HttpUtils;

public class MHttpHolder {
	public static HttpUtils getHttpUtils(){
		HttpUtils mHttpUtils= new HttpUtils();
		mHttpUtils.configCurrentHttpCacheExpiry(0);
		mHttpUtils.configSoTimeout(4000);
		mHttpUtils.configTimeout(4000);
		return mHttpUtils;
	}
}
