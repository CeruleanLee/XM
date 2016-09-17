package com.bwf.xmduobao.ui.activity;

import java.util.List;

import com.bwf.xmduobao.Constants;
import com.bwf.xmduobao.R;
import com.bwf.xmduobao.adapter.WinHistoryListAdapter;
import com.bwf.xmduobao.entity.ResponseWinHistory;
import com.bwf.xmduobao.entity.ResponseWinHistory.WinHistoryItem;
import com.bwf.xmduobao.utils.MHttpHolder;
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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class WinHistoryActivity extends Activity{
	@ViewInject(R.id.listView)
	private ListView mListView;
	private WinHistoryListAdapter mAdapter;
	private HttpUtils mHttpUtils;
	private long goodsId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_win_history);
		mHttpUtils = MHttpHolder.getHttpUtils();
		goodsId = getIntent().getIntExtra("goodsId", 0);
		initViews();
		initDatas();
	}
	private void initDatas() {
		String url = UrlHandler.handlUrl(Constants.URL_WIN_HISTORY,goodsId);
		System.out.println(url);
		mHttpUtils.send(HttpMethod.GET,url , new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String json = arg0.result;
				ResponseWinHistory responseQuestion = new Gson().fromJson(json, ResponseWinHistory.class);
				List<WinHistoryItem> list = responseQuestion.result.list;
				mAdapter = new WinHistoryListAdapter(WinHistoryActivity.this,list);
				mListView.setAdapter(mAdapter);
			}

		});
	}
	private void initViews() {
		ViewUtils.inject(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(WinHistoryActivity.this, GoodsPeriodActivity.class);
				intent.putExtra("period", Long.parseLong(mAdapter.getItem(position).period));
				startActivity(intent);
			}
		});
	}
}
