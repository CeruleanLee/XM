package com.bwf.xmduobao.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bwf.xmduobao.Constants;
import com.bwf.xmduobao.R;
import com.bwf.xmduobao.db.DuobaoListHandler;
import com.bwf.xmduobao.entity.GoodsItem;
import com.bwf.xmduobao.entity.ResponseIpAddress;
import com.bwf.xmduobao.entity.ResponseUserinfo;
import com.bwf.xmduobao.entity.ResponseUserinfo.Userinfo;
import com.bwf.xmduobao.ui.activity.MainActivity;
import com.bwf.xmduobao.utils.MBitmapHolder;
import com.bwf.xmduobao.utils.MHttpHolder;
import com.bwf.xmduobao.utils.UserInfoHandler;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MineFragment extends Fragment implements OnClickListener{
	@ViewInject(R.id.layout_unlogin)
	private View mUnloginLayout;
	@ViewInject(R.id.layout_logined)
	private View mLoginedLayout;
	@ViewInject(R.id.tv_login_weixin)
	private View mWeixin;
	@ViewInject(R.id.tv_login_qq)
	private View mQQ;
	@ViewInject(R.id.tv_login_sina)
	private View mSina;

	@ViewInject(R.id.img_avatar)
	private ImageView mAvatarImg;
	@ViewInject(R.id.tv_nickname)
	private TextView mNicknameTv;
	@ViewInject(R.id.tv_uid)
	private TextView mUidTv;
	@ViewInject(R.id.tv_loginCancel)
	private TextView mLoginCancelTv;

	private UMShareAPI mShareAPI ;
	private HttpUtils mHttpUtils;
	private BitmapUtils mBitmapUtils;
	private DuobaoListHandler mDuobaoListHandler;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_mine, null);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		MainActivity aty = (MainActivity) getActivity();
		mDuobaoListHandler = new DuobaoListHandler(aty);
		mShareAPI = aty.mShareAPI;
		mHttpUtils = MHttpHolder.getHttpUtils();
		mBitmapUtils = MBitmapHolder.getBitmapUtils(getActivity());
		initViews();
	}
	private void initViews() {
		ViewUtils.inject(this, getView());
		if(isLogin()){
			mLoginedLayout.setVisibility(View.VISIBLE);
			mUnloginLayout.setVisibility(View.GONE);
			setLoginedViewData(userinfo);
		}else{
			mLoginedLayout.setVisibility(View.GONE);
			mUnloginLayout.setVisibility(View.VISIBLE);
		}
		mWeixin.setOnClickListener(this);
		mQQ.setOnClickListener(this);
		mSina.setOnClickListener(this);
		mLoginCancelTv.setOnClickListener(this);
	}
	/**
	 * 判断是否已经登入
	 * @return
	 */
	private boolean isLogin() {
		userinfo = UserInfoHandler.getUserInfo(getActivity());
		return userinfo != null;
	}
	@Override
	public void onClick(View v) {
		if(v == mWeixin){
			platformLogin(SHARE_MEDIA.WEIXIN);
		}else if(v == mQQ){
			platformLogin(SHARE_MEDIA.QQ);
		}else if(v == mSina){
			platformLogin(SHARE_MEDIA.SINA);
		}else if(v == mLoginCancelTv){
			//注销
			UserInfoHandler.clearUserInfo(getActivity());
			mLoginedLayout.setVisibility(View.GONE);
			mUnloginLayout.setVisibility(View.VISIBLE);
		}
	}
	private void platformLogin(SHARE_MEDIA platform){
		mShareAPI.doOauthVerify(getActivity(), platform, umAuthListener);
	}
	UMAuthListener umAuthListener = new UMAuthListener() {

		@Override
		public void onError(SHARE_MEDIA arg0, int arg1, Throwable arg2) {
			LogUtils.d("授权onError");
		}
		@Override
		public void onComplete(SHARE_MEDIA arg0, int arg1, Map<String, String> data) {
			LogUtils.d("授权成功");
			//去获取用户信息
			mShareAPI.getPlatformInfo(getActivity(), arg0, getInfoListener);
		}
		@Override
		public void onCancel(SHARE_MEDIA arg0, int arg1) {
			LogUtils.d("授权onCancel");
		}
	};
	UMAuthListener getInfoListener = new UMAuthListener() {

		@Override
		public void onError(SHARE_MEDIA arg0, int arg1, Throwable arg2) {

		}

		@Override
		public void onComplete(SHARE_MEDIA arg0, int arg1, Map<String, String> data) {
			/**
			 {is_yellow_year_vip=0, vip=0, level=0, province=四川, yellow_vip_level=0,
			  is_yellow_vip=0, gender=男, openid=BE49BD466A406751CAA80BA42FB167C2,
			   screen_name=Mr.Li, msg=, 
			   profile_image_url=http://q.qlogo.cn/qqapp/1105524284/BE49BD466A406751CAA80BA42FB167C2/100, city=成都} 
			 */
			LogUtils.d(data.toString());
			//访问自己的服务器接口，完成登入
			userinfo = new Userinfo();
			userinfo.openid = data.get("openid");
			userinfo.province = data.get("province");
			userinfo.sex = data.get("gender");
			userinfo.nickname = data.get("screen_name");
			userinfo.avatar = data.get("profile_image_url");
			userinfo.city = data.get("city");
			String url = Constants.URL_LOGIN_OTHER_PLATFORM;
			RequestParams params = new RequestParams("UTF-8");
			params.addBodyParameter("openid", userinfo.openid);
			params.addBodyParameter("nickname", userinfo.nickname);
			params.addBodyParameter("sex", userinfo.sex);
			params.addBodyParameter("province", userinfo.province);
			params.addBodyParameter("city", userinfo.city);
			params.addBodyParameter("headImg", userinfo.avatar);
			int userType = 0;
			if(arg0 == SHARE_MEDIA.WEIXIN){
				userType = Constants.TYPE_WEIXIN;
			}else if(arg0 == SHARE_MEDIA.QQ){
				userType = Constants.TYPE_QQ;
			}else if(arg0 == SHARE_MEDIA.SINA){
				userType = Constants.TYPE_SINA;
			}
			params.addBodyParameter("userType", userType+"");
			mHttpUtils.send(HttpMethod.POST, url, params, requestCallBack);
		}

		@Override
		public void onCancel(SHARE_MEDIA arg0, int arg1) {

		}
	};
	private Userinfo userinfo;
	private RequestCallBack<String> requestCallBack = new RequestCallBack<String>() {

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			String json = arg0.result.toString();
			Gson gson = new Gson();
			ResponseUserinfo obj = gson.fromJson(json, ResponseUserinfo.class);
			userinfo = obj.result;
			platformLoginSuccess(userinfo);
			Toast.makeText(getActivity(), userinfo.toString(), 1).show();
			LogUtils.d(userinfo.toString());
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {

		}
	};
	/**
	 * 第三方平台登入成功
	 * @param userinfo
	 */
	private void platformLoginSuccess(final Userinfo userinfo){
		mUnloginLayout.setVisibility(View.GONE);
		mLoginedLayout.setVisibility(View.VISIBLE);
		setLoginedViewData(userinfo);
		UserInfoHandler.saveUserinfo(getActivity(), userinfo);

		mHttpUtils.send(HttpMethod.GET, Constants.URL_GET_IP_ADDRESS, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String json = arg0.result;
				ResponseIpAddress responseIpAddress = new Gson().fromJson(json, ResponseIpAddress.class);
				if("ok".equals(responseIpAddress.ret) && responseIpAddress.data != null & responseIpAddress.data.size() != 0){
					userinfo.IP = responseIpAddress.ip;
					userinfo.IPAddress = responseIpAddress.data.get(1);
					userinfo.IPAddress += responseIpAddress.data.get(2);
					UserInfoHandler.saveUserinfo(getActivity(), userinfo);
				}
			}
		});
		
		//上传本地夺宝清单
		List<GoodsItem> list = mDuobaoListHandler.getList();
		if(list != null && list.size() != 0){
			RequestParams params = new RequestParams();
			List<Long> periods = new ArrayList<Long>();
			List<Integer> times = new ArrayList<Integer>();
			for (GoodsItem goodsItem : list) {
				periods.add(goodsItem.getPeriod());
				times.add(goodsItem.getTimesInCar());
			}
			params.addBodyParameter("token", userinfo.token);
			Gson gson = new Gson();
			params.addBodyParameter("periods", gson.toJson(periods));
			params.addBodyParameter("times", gson.toJson(times));
			mHttpUtils.send(HttpMethod.POST, Constants.URL_ADD_TO_LIST, params , new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					
				}
				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					mDuobaoListHandler.clearList();
				}
			});
		}
		
	}
	/**
	 * 获取到用户信息之后，设置控件
	 * @param userinfo
	 */
	private void setLoginedViewData(Userinfo userinfo){
		mBitmapUtils.display(mAvatarImg, userinfo.avatar);
		mNicknameTv.setText(userinfo.nickname);
		mUidTv.setText(userinfo.uid);
	}
}
