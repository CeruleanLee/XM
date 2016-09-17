package com.bwf.xmduobao.ui.activity;

import java.util.List;

import com.bwf.xmduobao.Constants;
import com.bwf.xmduobao.R;
import com.bwf.xmduobao.adapter.CategoryAdapter;
import com.bwf.xmduobao.entity.Category;
import com.bwf.xmduobao.entity.ResponseCategory;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CategoryActivity extends Activity implements OnClickListener, OnItemClickListener{
	@ViewInject(R.id.backBtn)
	private View mBackBtn;
	@ViewInject(R.id.listView)
	private ListView mListView;

	private CategoryAdapter mAdapter;
	private HttpUtils mHttpUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		initViews();

		mHttpUtils = new HttpUtils();
		mHttpUtils.configCurrentHttpCacheExpiry(0);
		mHttpUtils.configSoTimeout(4000);
		mHttpUtils.configTimeout(4000);
		initDatas();
	}
	private void initDatas() {
		mHttpUtils.send(HttpMethod.GET, UrlHandler.handlUrl(Constants.URL_CATEGORY), new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String json = arg0.result;
				ResponseCategory responseCategory = new Gson().fromJson(json, ResponseCategory.class);
				List<Category> list = responseCategory.getResult().getList();
				mAdapter = new CategoryAdapter(list, CategoryActivity.this);
				mListView.setAdapter(mAdapter);
			}

		});
	}
	private void initViews() {
		ViewUtils.inject(this);
		mBackBtn.setOnClickListener(this);
		mListView.addHeaderView(getHeaderView());
		mListView.setOnItemClickListener(this);
	}
	@ViewInject(R.id.allGoodsLayout)
	private View mAllGoodsLayout;
	/**
	 * 加载头部视图，并初始化头部里面的控件
	 * @return
	 */
	private View getHeaderView() {
		View header = LayoutInflater.from(this).inflate(R.layout.v_category_header, null);
		ViewUtils.inject(this, header);
		mAllGoodsLayout.setOnClickListener(this);
		return header;
	}
	@Override
	public void onClick(View v) {
		if(v == mBackBtn){
			onBackPressed();
		}else if(v == mAllGoodsLayout){
			Intent intent = new Intent(this, CategoryGoodsListActivity.class);
			intent.putExtra("categoryId", 0);
			intent.putExtra("title", "全部商品");
			startActivity(intent);
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(id >= 0){
			Category category = mAdapter.getItem((int) id);
			Intent intent = new Intent(this, CategoryGoodsListActivity.class);
			intent.putExtra("categoryId", category.getCategoryId());
			intent.putExtra("title", category.getCategoryName());
			startActivity(intent);
		}
	}

}
