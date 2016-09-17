package com.bwf.xmduobao.ui.activity;

import java.text.SimpleDateFormat;

import com.bwf.xmduobao.Constants;
import com.bwf.xmduobao.R;
import com.bwf.xmduobao.entity.ResponseUserDuobaoTime;
import com.bwf.xmduobao.entity.UserDuobaoTime;
import com.bwf.xmduobao.entity.UserDuobaoTimes;
import com.bwf.xmduobao.utils.MHttpHolder;
import com.bwf.xmduobao.utils.SpannableStringBuild;
import com.bwf.xmduobao.utils.UrlHandler;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class ComputeDetailActivity extends Activity implements OnClickListener{
	@ViewInject(R.id.backBtn)
	private View mBackBtn;
	@ViewInject(R.id.tv_timeCount)
	private TextView mTimeCountTv;
	@ViewInject(R.id.tv_show_and_hide)
	private CheckedTextView mShowAndHide;
	@ViewInject(R.id.tv_luckCode)
	private TextView mLuckCodeTv;
	@ViewInject(R.id.layout_all_time)
	private ViewGroup mAllTimeLayout;

	private HttpUtils mHttpUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compute_detail);
		initViews();
		mHttpUtils = MHttpHolder.getHttpUtils();
		period = getIntent().getLongExtra("period", -1);
		loadDatas();
	}
	private long period;
	private void loadDatas() {
		String url = UrlHandler.handlUrl(Constants.URL_COMPUTE_DETAIL, period);
		mHttpUtils.send(HttpMethod.GET, url , new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String content = arg0.result;
				setDatas(content);
			}
		});
	}
	private void setDatas(String content) {
		ResponseUserDuobaoTime response = new Gson().fromJson(content, ResponseUserDuobaoTime.class);
		UserDuobaoTimes result = response.getResult();
		mTimeCountTv.setText(result.getTimeCountForCompute());
		String luckCode = String.format(getString(R.string.luck_code), result.getLuckCode());
		SpannableString spannableString = SpannableStringBuild.build(luckCode, 0xffff2233, result.getLuckCode());
		mLuckCodeTv.setText(SpannableStringBuild.buildStyle(spannableString, Typeface.BOLD, result.getLuckCode()));
		for (UserDuobaoTime item : result.getTimesList()) {
			mAllTimeLayout.addView(getItemView(item));
		}
	}
	private void initViews() {
		ViewUtils.inject(this);
		mBackBtn.setOnClickListener(this);
		mShowAndHide.setOnClickListener(this);
	}
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private View getItemView(UserDuobaoTime item){
		View view = getLayoutInflater().inflate(R.layout.item_compute_detail, null);
		TextView tv1 = (TextView) view.findViewById(R.id.tv_duobao_time);
		TextView tv2 = (TextView) view.findViewById(R.id.tv_user);
		String text = sdf.format(Long.parseLong(item.getDateTime()))+" → "+item.getTimeForCompute();
		SpannableString spannableString = SpannableStringBuild.build(text, 0xffff0000, " → "+item.getTimeForCompute());
		tv1.setText(SpannableStringBuild.buildStyle(spannableString, Typeface.BOLD, " → "+item.getTimeForCompute()));
		tv2.setText(item.getUserNickName());
		return view;
	}
	@Override
	public void onClick(View v) {
		if(v == mShowAndHide){
			if(!mShowAndHide.isChecked()){
				mAllTimeLayout.setVisibility(View.VISIBLE);
				mShowAndHide.setText(getString(R.string.hide));
			}else{
				mAllTimeLayout.setVisibility(View.GONE);
				mShowAndHide.setText(getString(R.string.show));
			}
			mShowAndHide.toggle();
		}else if(v == mBackBtn){
			onBackPressed();
		}
	}

}
