package com.bwf.xmduobao.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.bwf.xmduobao.Constants;
import com.bwf.xmduobao.R;
import com.bwf.xmduobao.adapter.HomeGridViewAdapter;
import com.bwf.xmduobao.adapter.HomeViewPagerAdapter;
import com.bwf.xmduobao.entity.Channel;
import com.bwf.xmduobao.entity.GoodsItem;
import com.bwf.xmduobao.entity.ResponseChannel;
import com.bwf.xmduobao.entity.ResponseGoodsItem;
import com.bwf.xmduobao.ui.activity.CategoryActivity;
import com.bwf.xmduobao.ui.activity.CategoryGoodsListActivity;
import com.bwf.xmduobao.ui.activity.GoodsPeriodActivity;
import com.bwf.xmduobao.ui.activity.QuestionActivity;
import com.bwf.xmduobao.ui.activity.SearchActivity;
import com.bwf.xmduobao.ui.view.AutoScrollViewPager;
import com.bwf.xmduobao.ui.view.LoadingView;
import com.bwf.xmduobao.ui.view.LoadingView.OnLoadingViewListener;
import com.bwf.xmduobao.ui.view.LoadmoreGridView;
import com.bwf.xmduobao.ui.view.LoadmoreGridView.OnLoadingMoreListener;
import com.bwf.xmduobao.utils.UrlHandler;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class HomeFragment extends Fragment{
	@ViewInject(R.id.gridView)
	private LoadmoreGridView mGridView;
	@ViewInject(R.id.viewPager)
	private AutoScrollViewPager mHeadViewPager;
	@ViewInject(R.id.indicatorContainer)
	private LinearLayout mHeadIndicatorContainer;
	/**作为指示器的那些小圆点**/
	private ImageView[] mIndicators;
	private HttpUtils mHttpUtils;

	@ViewInject(R.id.img_search)
	private View mSearchImg;

	@ViewInject(R.id.loadingView)
	private LoadingView mLoadingView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
		//初始化HttpUtils
		mHttpUtils = new HttpUtils();
		mHttpUtils.configCurrentHttpCacheExpiry(0);
		mHttpUtils.configSoTimeout(4000);
		mHttpUtils.configTimeout(4000);
		initDatas();

	}
	private void initViews() {
		ViewUtils.inject(this, getView());
		mGridView.addHeaderView(getHeader1());
		mGridView.addHeaderView(getHeader2());
		mGridView.addHeaderView(getHeader3());
		mGoodslist = new ArrayList<GoodsItem>();
		mGridViewAdapter = new HomeGridViewAdapter(mGoodslist , getActivity());


		mGridView.setAdapter(mGridViewAdapter);
		mGridView.setOnLoadingMoreListener(onLoadingMoreListener);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(), GoodsPeriodActivity.class);
				intent.putExtra("period", mGridViewAdapter.getItem((int)id).getPeriod());
				startActivity(intent);
			}
		});
		mSearchImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), SearchActivity.class));
			}
		});

		mLoadingView.setOnLoadingViewListener(onLoadingViewListener);
	}
	/**
	 * 监听 LoadingView 按钮的点击
	 */
	OnLoadingViewListener onLoadingViewListener = new OnLoadingViewListener() {

		@Override
		public void onTryAgainClick() {
			if(!isNoNetwork)
				loadHomeDatas();
		}
	};
	OnLoadingMoreListener onLoadingMoreListener = new OnLoadingMoreListener() {

		@Override
		public void retryMore() {
			loadHomeDatas();
		}
		@Override
		public void loadMore() {
			loadHomeDatas();
		}
	};
	@ViewInject(R.id.hotCheckedTv)
	private CheckedTextView mHotCheckedTv;
	@ViewInject(R.id.totalTimesCheckedTv)
	private CheckedTextView mTotalTimesCheckedTv;
	private View getHeader3() {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.v_home_header3, null);
		ViewUtils.inject(this, view);//初始化头部视图里的控件
		mHotCheckedTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mTotalTimesCheckedTv.setChecked(false);
				mHotCheckedTv.setChecked(true);
				currentTag = TAG_HOT;
				mTotalTimesCheckedTv.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_home_total_disable,0);
				nextPage = 1;
				mGridViewAdapter.clearDatas();
				loadHomeDatas();
			}
		});
		mTotalTimesCheckedTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mTotalTimesCheckedTv.setChecked(true);
				mHotCheckedTv.setChecked(false);
				switch (currentTag) {
				case TAG_HOT:
				case TAG_TOTAL_TIMES_ASC:
					currentTag = TAG_TOTAL_TIMES_DESC;
					mTotalTimesCheckedTv.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_home_total_down,0);
					break;
				case TAG_TOTAL_TIMES_DESC:
					currentTag = TAG_TOTAL_TIMES_ASC;
					mTotalTimesCheckedTv.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_home_total_up,0);
					break;
				}
				nextPage = 1;
				mGridViewAdapter.clearDatas();
				loadHomeDatas();
			}
		});
		return view;
	}
	@ViewInject(R.id.categoryLayout)
	private View mCategoryLayout;
	@ViewInject(R.id.tenLayout)
	private View mTenLayout;
	@ViewInject(R.id.questionLayout)
	private View mQuestionLayout;
	private View getHeader2() {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.v_home_header2, null);
		ViewUtils.inject(this, view);
		return view;
	}
	@OnClick({R.id.categoryLayout,R.id.tenLayout,R.id.baiLayout,R.id.questionLayout})
	public void onClick(View view){
		if(view == mCategoryLayout){
			startActivity(new Intent(getActivity(), CategoryActivity.class));
		}else if(view == mTenLayout){
			Intent intent = new Intent(getActivity(), CategoryGoodsListActivity.class);
			intent.putExtra("categoryId", 1);
			intent.putExtra("title", "10元专区");
			startActivity(intent);
		}else if(view == mQuestionLayout){
			startActivity(new Intent(getActivity(), QuestionActivity.class));
		}else if(view.getId() == R.id.baiLayout){
			Intent intent = new Intent(getActivity(), CategoryGoodsListActivity.class);
			intent.putExtra("categoryId", 100);
			intent.putExtra("title", "100元区");
			startActivity(intent);

		}
	}

	private int nextPage = 1;
	private static final int TAG_HOT = 1;//热门
	private static final int TAG_TOTAL_TIMES_ASC = 2;//升序
	private static final int TAG_TOTAL_TIMES_DESC = 3; //降序
	private int currentTag = TAG_HOT;
	/**
	 * 根据当前标签获取对应的接口地址(人气、总需次数)
	 * @return
	 */
	private String getUrl(){
		switch (currentTag) {
		case TAG_HOT:
			return UrlHandler.handlUrl(Constants.URL_HOME_GOODS_BY_HOT, nextPage,20);
		case TAG_TOTAL_TIMES_ASC:
			return UrlHandler.handlUrl(Constants.URL_HOME_GOODS_BY_TIMES_ASC, nextPage,20);
		case TAG_TOTAL_TIMES_DESC:
			return UrlHandler.handlUrl(Constants.URL_HOME_GOODS_BY_TIMES_DESC, nextPage,20);
		}
		return UrlHandler.handlUrl(Constants.URL_HOME_GOODS_BY_HOT, nextPage,20);
	}
	/**
	 * 初始化数据
	 */
	private void initDatas() {
		if(isLoading){
			return;
		}
		initHeadViewPagerDatas();
		loadHomeDatas();
	}
	private boolean isLoading;
	private void loadHomeDatas(){

		if(isLoading){
			return;
		}
		if(nextPage == 1){
			//显示 正在加载 的视图
			mLoadingView.startLoading();
		}
		isLoading = true;
		String url =UrlHandler.handlUrl(getUrl(), nextPage,20);
		LogUtils.d("开始加载第"+nextPage+"页");
		mHttpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				mGridView.loadFailed();
				LogUtils.d("第"+nextPage+"页加载失败");
				if(nextPage == 1){//如果是获取第一页数据失败，就隐藏 正在加载 视图
					if(!isNoNetwork){
						mLoadingView.loadFailed();
					}
				}
				isLoading = false;
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				LogUtils.d("第"+nextPage+"页加载完成");
				String content = arg0.result;
				ResponseGoodsItem responseGoodsItem = new Gson().fromJson(content, ResponseGoodsItem.class);
				List<GoodsItem> list = responseGoodsItem.getResult().list;
				mGridViewAdapter.addDatas(list);

				//如果是获取第一页数据成功，就显示脚部视图，隐藏 正在加载 视图
				if(nextPage == 1){
					mGridView.showFooterView();
					mLoadingView.loadSuccess();
				}
				nextPage++;
				mGridView.loadSuccess();
				if(responseGoodsItem.getResult().totalCnt <= mGridViewAdapter.getCount()){
					mGridView.setNoMore();
				}
				isLoading = false;

			}
		});
	}

	private void initHeadViewPagerDatas() {
		String url =Constants.URL_HOME_CHANNEL;
		mHttpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(getActivity(), "failed", 0).show();
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String content = arg0.result;
				setHeaderViewPagerDatas(content);
			}
		});

	}
	/**
	 * 用数据来填充ViewPager
	 * @param content
	 */
	protected void setHeaderViewPagerDatas(String content) {
		ResponseChannel responseChannel = new Gson().fromJson(content, ResponseChannel.class);
		List<Channel> list = responseChannel.result.list;
		List<ImageView> imgviewList = new ArrayList<ImageView>();
		for (int i = 0; i < list.size(); i++) {
			ImageView imgView = new ImageView(getActivity());
			imgView.setScaleType(ScaleType.CENTER_CROP);
			imgviewList.add(imgView);
		}
		HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getActivity(), imgviewList, list);
		mHeadViewPager.setAdapter(adapter);
		mIndicators = new ImageView[list.size()];
		for (int i = 0; i < mIndicators.length; i++) {
			mIndicators[i] = new ImageView(getActivity());
			mIndicators[i].setImageResource(R.drawable.guide_indicator_unselected);
			mHeadIndicatorContainer.addView(mIndicators[i]);
			//布局参数，在使用的时候要根据父容器的类型来选择对应的LayoutParams
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, 
					LayoutParams.WRAP_CONTENT);
			params.rightMargin = 12;
			mIndicators[i].setLayoutParams(params);
		}
		//默认让第一个ImageView选中
		mIndicators[0].setImageResource(R.drawable.guide_indicator_selected);
		mHeadViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mIndicators[mCurPos].setImageResource(R.drawable.guide_indicator_unselected);
				mIndicators[position].setImageResource(R.drawable.guide_indicator_selected);
				mCurPos = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	private int mCurPos;
	private List<GoodsItem> mGoodslist;
	private HomeGridViewAdapter mGridViewAdapter;
	/**
	 * 获取第一个头部视图
	 * @return
	 */
	private View getHeader1(){
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.v_home_header1, null);
		ViewUtils.inject(this, view);//初始化头部视图里的控件
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
		mHeadViewPager.stopTask();
	}
	@Override
	public void onResume() {
		super.onResume();
		if(mHeadViewPager.getAdapter() != null)
			mHeadViewPager.startTask();
		registNetworkReceiver();
	}

	/****
	 * 与网络状态相关
	 */
	private BroadcastReceiver receiver;
	private void registNetworkReceiver(){
		if(receiver == null){
			receiver = new NetworkReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			getActivity().registerReceiver(receiver, filter );
		}
	}
	private void unregistNetworkReceiver(){
		getActivity().unregisterReceiver(receiver);
	}
	public class NetworkReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
				ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
				NetworkInfo networkInfo = manager.getActiveNetworkInfo();
				if(networkInfo != null && networkInfo.isAvailable()){
					int type = networkInfo.getType(); 
					if(ConnectivityManager.TYPE_WIFI == type){

					}else if(ConnectivityManager.TYPE_MOBILE == type){

					}else if(ConnectivityManager.TYPE_ETHERNET == type){

					}
					//有网络
					Toast.makeText(getActivity(), "有网络", 0).show();
					LogUtils.d("有网络");
					if(nextPage == 1){
						initDatas();
					}
					isNoNetwork = false;
				}else{
					//没有网络
					LogUtils.d("没有网络");
					Toast.makeText(getActivity(), "没有网络", 0).show();
					if(nextPage == 1){
						mLoadingView.noNetwork();
					}
					isNoNetwork = true;
				}
			}
		}
	}
	private boolean isNoNetwork;
}
