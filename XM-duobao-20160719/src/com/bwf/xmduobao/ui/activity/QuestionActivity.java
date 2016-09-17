package com.bwf.xmduobao.ui.activity;

import java.util.List;

import com.bwf.xmduobao.Constants;
import com.bwf.xmduobao.R;
import com.bwf.xmduobao.adapter.QuestionAdapter;
import com.bwf.xmduobao.entity.Question;
import com.bwf.xmduobao.entity.ResponseQuestion;
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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class QuestionActivity extends Activity implements OnClickListener, OnItemClickListener{
	@ViewInject(R.id.backBtn)
	private View mBackBtn;
	@ViewInject(R.id.listView)
	private ListView mListView;
	
	private QuestionAdapter mAdapter;
	private HttpUtils mHttpUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		initViews();

		mHttpUtils = new HttpUtils();
		mHttpUtils.configCurrentHttpCacheExpiry(0);
		mHttpUtils.configSoTimeout(4000);
		mHttpUtils.configTimeout(4000);
		initDatas();
	}
	private void initDatas() {
		mHttpUtils.send(HttpMethod.GET, UrlHandler.handlUrl(Constants.URL_QUESTION), new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String json = arg0.result;
				ResponseQuestion responseQuestion = new Gson().fromJson(json, ResponseQuestion.class);
				List<Question> list = responseQuestion.result.list;
				mAdapter = new QuestionAdapter(list, QuestionActivity.this);
				mListView.setAdapter(mAdapter);
			}

		});
	}
	private void initViews() {
		ViewUtils.inject(this);
		mBackBtn.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v == mBackBtn){
			onBackPressed();
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mAdapter.pressItem(position);
	}
}
