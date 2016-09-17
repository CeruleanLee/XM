package com.bwf.xmduobao.utils;

import java.util.ArrayList;
import java.util.List;

import com.bwf.xmduobao.Constants;
import com.bwf.xmduobao.db.DuobaoListHandler;
import com.bwf.xmduobao.entity.GoodsItem;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

import android.content.Context;

public class AddToCarHandler {
	private Context context;
	private HttpUtils mHttpUtils;
	private DuobaoListHandler mDbHandler;
	public AddToCarHandler(Context context) {
		super();
		this.context = context;
		mHttpUtils = MHttpHolder.getHttpUtils();
		mDbHandler = new DuobaoListHandler(context);
	}
	/**
	 * 执行加入清单操作
	 * @param period
	 * @param times
	 */
	public void addToList(List<GoodsItem> goodsList){

		if(UserInfoHandler.isLogin(context)){//已经登入
			String token = UserInfoHandler.getUserInfo(context).token;
			RequestParams params = new RequestParams();
			List<Long> periodsList = new ArrayList<Long>();
			List<Integer> timesList = new ArrayList<Integer>();
			for (GoodsItem goods : goodsList) {
				periodsList.add(goods.getPeriod());
				timesList.add(goods.getDefaultJoinTimes());
			}
			params.addBodyParameter("token", token);
			Gson gson = new Gson();
			params.addBodyParameter("periods", gson.toJson(periodsList));
			params.addBodyParameter("times", gson.toJson(timesList));
			mHttpUtils.send(HttpMethod.POST, Constants.URL_ADD_TO_LIST, params , new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {

				}
				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					
				}
			});
		}else{ // 没有登入
			LogUtils.d("没有登入");
			for (GoodsItem item : goodsList) {
				mDbHandler.add(item,0);
			}
		}
	}
}
