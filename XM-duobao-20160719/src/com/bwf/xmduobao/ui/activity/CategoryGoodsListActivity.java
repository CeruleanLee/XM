package com.bwf.xmduobao.ui.activity;

import java.util.List;

import com.bwf.xmduobao.Constants;
import com.bwf.xmduobao.R;
import com.bwf.xmduobao.adapter.CategoryGoodsListAdapter;
import com.bwf.xmduobao.entity.GoodsItem;
import com.bwf.xmduobao.entity.ResponseGoodsItem;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryGoodsListActivity extends Activity implements OnClickListener{
	@ViewInject(R.id.backBtn)
	private View mBackBtn;
	@ViewInject(R.id.titleTv)
	private TextView mTitleTv;
	@ViewInject(R.id.goodsTotalTv)
	private TextView mGoodsTotalTv;
	@ViewInject(R.id.allToShoppingCartBtn)
	private View mAllToShoppingCartBtn;
	@ViewInject(R.id.listView)
	private ListView mListView;
	private CategoryGoodsListAdapter mAdapter;
	private HttpUtils mHttpUtils;
	@ViewInject(R.id.allToShoppingCartBtn)
	private View mAddAllToList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_goods_list);
		initViews();
		mHttpUtils = new HttpUtils();
		mHttpUtils.configCurrentHttpCacheExpiry(0);
		mHttpUtils.configSoTimeout(4000);
		mHttpUtils.configTimeout(4000);
		initDatas();
	}
	private int categoryId;
	private void initViews() {
		ViewUtils.inject(this);
		mBackBtn.setOnClickListener(this);
		Intent intent  = getIntent();
		String title = intent.getStringExtra("title");
		mTitleTv.setText(title);
		categoryId = intent.getIntExtra("categoryId", 0);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(CategoryGoodsListActivity.this, GoodsPeriodActivity.class);
				intent.putExtra("period", mAdapter.getItem((int)id).getPeriod());
				startActivity(intent);
				
			}
		});
		mAddAllToList.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v == mBackBtn){
			onBackPressed();
		}else if(v == mAddAllToList){
			
		}
	}
	private void initDatas() {
		String url = UrlHandler.handlUrl(Constants.URL_CATEGORY_GOODS_LIST,categoryId);
		
		mHttpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String json = arg0.result;
				ResponseGoodsItem responseGoodsItem = new Gson().fromJson(json, ResponseGoodsItem.class);
				mGoodsTotalTv.setVisibility(View.VISIBLE);
				int total = responseGoodsItem.getResult().totalCnt;
				mGoodsTotalTv.setText(String.format(getString(R.string.goos_total_text), total));
				List<GoodsItem> list = responseGoodsItem.getResult().list;
				mAdapter = new CategoryGoodsListAdapter(CategoryGoodsListActivity.this, list);
				mListView.setAdapter(mAdapter);
			}
		});
	}
}
