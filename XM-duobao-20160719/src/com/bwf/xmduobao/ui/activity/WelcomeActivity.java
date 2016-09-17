package com.bwf.xmduobao.ui.activity;

import com.bwf.xmduobao.R;
import com.bwf.xmduobao.utils.AppSharedPreference;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

public class WelcomeActivity extends Activity{
	private boolean isFinish;
	private BitmapUtils mBitmapUtils;
	@ViewInject(R.id.imgWelcome)
	private ImageView mImg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		ViewUtils.inject(this);
		
		
		String diskCachePath = Environment.getExternalStorageDirectory().getPath()+
				"/"+getPackageName()+"/cache/imgs";
		mBitmapUtils = new BitmapUtils(this, diskCachePath );
		loadBigImg();
		
		
		new Thread(){
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(isFinish){
					return;
				}
				if(isFirstRun()){
					startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
				}else{
					startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
				}
				finish();
			};
		}.start();
	}
	/**
	 * 加载大图
	 * 步骤：先去获取网络图片地址，然后下载这个图片显示
	 */
	private void loadBigImg() {
		String urlStr = "http://123.56.145.151:8080/Duobao-XM/splash";
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configSoTimeout(1500);
		httpUtils.configTimeout(1500);
		httpUtils.send(HttpMethod.GET, urlStr, new RequestCallBack<String>() {
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				mImg.setImageResource(R.drawable.splash);
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String imgUrl = arg0.result;
				mBitmapUtils.display(mImg, imgUrl);
			}
		});
	}
	/**
	 * 判断程序是否是第一次运行
	 * @return
	 */
	private boolean isFirstRun(){
		boolean isFirstRun = AppSharedPreference.getBooleanValue(getApplicationContext(), "app", "is_first_run", true);
		if(isFirstRun){
			AppSharedPreference.putValue(getApplicationContext(), "app", "is_first_run", false);
		}
		return isFirstRun;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		isFinish = true;
	}
}
