package com.bwf.xmduobao;

import com.umeng.socialize.PlatformConfig;

import android.app.Application;

public class DuobaoApp extends Application{

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		PlatformConfig.setWeixin("wx5065a2ddf73a2e76", "69a332f7761044c02a809d5fa36d8957");
		//微信 appid appsecret
		PlatformConfig.setSinaWeibo("3921700954","04b48b094faeb16683c32669824ebdad");
		//新浪微博 appkey appsecret
		PlatformConfig.setQQZone("1105524284", "tQbGVJpyB6GXJWxu"); 
		// QQ和Qzone appid appkey     
		PlatformConfig.setAlipay("2015111700822536");
		//支付宝 appid
		PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
		//易信 appkey      
		PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
		//Twitter appid appkey
		PlatformConfig.setPinterest("1439206");
		//Pinterest appid 
		PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
		//来往 appid appkey
	}
}
