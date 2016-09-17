package com.bwf.xmduobao.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.bwf.xmduobao.Constants;
import com.bwf.xmduobao.R;
import com.bwf.xmduobao.adapter.RevealedPeriodAdapter;
import com.bwf.xmduobao.entity.ResponseRevealedPeriodInfo;
import com.bwf.xmduobao.entity.ResponseRevealedPeriodInfo.RevealedPeriodInfo;
import com.bwf.xmduobao.ui.activity.GoodsPeriodActivity;
import com.bwf.xmduobao.ui.view.LoadingView;
import com.bwf.xmduobao.ui.view.LoadingView.OnLoadingViewListener;
import com.bwf.xmduobao.ui.view.LoadmoreGridView;
import com.bwf.xmduobao.ui.view.LoadmoreGridView.OnLoadingMoreListener;
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

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class WinFragment extends LazyLoadFragment{
	@ViewInject(R.id.gridView)
	private LoadmoreGridView mGridView;

	private HttpUtils mHttpUtils;
	private RevealedPeriodAdapter mGridViewAdapter;
	@ViewInject(R.id.img_refresh)
	private View mRefreshImg;
	@ViewInject(R.id.loadingView)
	private LoadingView mLoadingView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_win, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mHttpUtils = MHttpHolder.getHttpUtils();
		initViews();
		mLoadingView.startLoading();
	}
	private boolean isLoading;
	protected void initData() {
		loadDatas();
	}
	private int nextPage = 1;
	private void loadDatas() {
		if(isLoading){
			return;
		}
		isLoading = true;
		String url =UrlHandler.handlUrl(Constants.URL_REVEALED_LASTEST, nextPage,20);
		mHttpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				mGridView.loadFailed();
				if(nextPage == 1){
					mLoadingView.loadFailed();
				}
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String content = arg0.result;
				System.out.println("content:"+content);
				ResponseRevealedPeriodInfo response = new Gson().fromJson(content, ResponseRevealedPeriodInfo.class);
				List<RevealedPeriodInfo> list = response.result.list;
				System.out.println("content:"+list.size());
				mGridViewAdapter.addDatas(list);
				if(nextPage == 1){
					mGridView.showFooterView();
					mLoadingView.loadSuccess();
					mRefreshImg.setVisibility(View.VISIBLE);
				}
				nextPage++;
				mGridView.loadSuccess();
				if(response.result.totalCnt <= mGridViewAdapter.getCount()){
					mGridView.setNoMore();
				}
				isLoading = false;
			}
		});
	}

	protected void initViews() {
		ViewUtils.inject(this, getView());
		mLoadingView.setOnLoadingViewListener(onLoadingViewListener);
		mRefreshImg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nextPage = 1;
				mGridViewAdapter.clearDatas();
				loadDatas();
			}
		});
		List<RevealedPeriodInfo> list = new ArrayList<RevealedPeriodInfo>();
		mGridViewAdapter = new RevealedPeriodAdapter(list , getActivity(),mGridView);
		mGridView.setAdapter(mGridViewAdapter);
		mGridView.setOnLoadingMoreListener(onLoadingMoreListener);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(), GoodsPeriodActivity.class);
				intent.putExtra("period", mGridViewAdapter.getItem(position).period);
				startActivity(intent);
				
			}
		});
	}
	OnLoadingViewListener onLoadingViewListener = new OnLoadingViewListener() {
		
		@Override
		public void onTryAgainClick() {
			loadDatas();
		}
	};
	OnLoadingMoreListener onLoadingMoreListener = new OnLoadingMoreListener() {

		@Override
		public void retryMore() {
			loadDatas();
		}
		@Override
		public void loadMore() {
			loadDatas();
		}
	};
	@Override
	protected void lazyLoad() {
		initData();
	}
}
