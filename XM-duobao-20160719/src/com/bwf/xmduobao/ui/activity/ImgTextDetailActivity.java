package com.bwf.xmduobao.ui.activity;

import java.util.List;

import com.bwf.xmduobao.Constants;
import com.bwf.xmduobao.R;
import com.bwf.xmduobao.adapter.GoodsDetailListAdapter;
import com.bwf.xmduobao.entity.ResponseGoodsDetail;
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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ImgTextDetailActivity extends Activity{
	@ViewInject(R.id.backBtn)
	private View mBackImg;
	@ViewInject(R.id.listView)
	private ListView mListView;
	private HttpUtils mHttpUtils;
	private GoodsDetailListAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_img_text_detail);
		initViews();
		initDatas();
	}
	private void initViews() {
		ViewUtils.inject(this);
		mListView.addFooterView(getFooterView());
		mBackImg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	private void initDatas() {
		int goodsId  = getIntent().getIntExtra("goodsId", 0);
		mHttpUtils=MHttpHolder.getHttpUtils();

		String url =UrlHandler.handlUrl(Constants.URL_GOODS_DETAIL, goodsId);
		mHttpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String content = arg0.result;
				ResponseGoodsDetail response = new Gson().fromJson(content, ResponseGoodsDetail.class);
				List<String> list = response.getResult().getList();
				mAdapter = new GoodsDetailListAdapter(ImgTextDetailActivity.this, list);
				mListView.setAdapter(mAdapter);
				mTv.setText(response.getResult().getDeclaration());
			}
		});

	
	}
	private TextView mTv;
	private View getFooterView() {
		View headerView = LayoutInflater.from(this).inflate(R.layout.v_footer_img_text_detail, null);
		mTv = (TextView) headerView;
		return headerView;
	}
}
