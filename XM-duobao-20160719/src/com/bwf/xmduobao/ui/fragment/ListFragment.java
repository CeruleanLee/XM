package com.bwf.xmduobao.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.bwf.xmduobao.Constants;
import com.bwf.xmduobao.R;
import com.bwf.xmduobao.adapter.DuobaoListAdapter;
import com.bwf.xmduobao.adapter.DuobaoListAdapter.OnNumerPickerChangeListener;
import com.bwf.xmduobao.db.DuobaoListHandler;
import com.bwf.xmduobao.entity.GoodsItem;
import com.bwf.xmduobao.entity.ResponseUnpayList;
import com.bwf.xmduobao.entity.ResponseUnpayList.UnpayGoods;
import com.bwf.xmduobao.entity.ResponseUserinfo.Userinfo;
import com.bwf.xmduobao.ui.activity.GoodsPeriodActivity;
import com.bwf.xmduobao.ui.activity.MainActivity;
import com.bwf.xmduobao.ui.view.LoadingView;
import com.bwf.xmduobao.utils.MHttpHolder;
import com.bwf.xmduobao.utils.UrlHandler;
import com.bwf.xmduobao.utils.UserInfoHandler;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListFragment extends LazyLoadFragment implements OnNumerPickerChangeListener, OnClickListener, OnItemClickListener{
	@ViewInject(R.id.layout_list)
	private ViewGroup mListLayout;
	@ViewInject(R.id.recyclerView)
	private RecyclerView mRecyclerView;
	@ViewInject(R.id.loadingView)
	private LoadingView mLoadingView;
	@ViewInject(R.id.emptyView)
	private View mEmptyView;
	@ViewInject(R.id.listView)
	private ListView mListView;
	@ViewInject(R.id.layout_footer)
	private View mFooterLayout;
	@ViewInject(R.id.tv_Settlement)
	private TextView mSettlementTv;
	@ViewInject(R.id.tv_goodsCount)
	private TextView mGoodsCountTv;
	@ViewInject(R.id.tv_coinTotal)
	private TextView mCoinTotalTv;


	private DuobaoListAdapter mListAdapter;

	private View contentView;
	private HttpUtils mHttpUtils;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		contentView = LayoutInflater.from(activity).inflate(R.layout.fragment_list, null);
		initViews();
		duobaoListHandler = new DuobaoListHandler(getActivity());
		mHttpUtils = MHttpHolder.getHttpUtils();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return contentView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
	private void initViews() {
		ViewUtils.inject(this, contentView);
		//创建布局管理器
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
		mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mLoadingView.startLoading();
		mSettlementTv.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
	}
	private void loadDatas(){
		if(UserInfoHandler.isLogin(getActivity())){
			//如果已经登入，则直接获取服务器上的清单信息
			String url = UrlHandler.handlUrl(Constants.URL_GET_DUOBAO_LIST, UserInfoHandler.getUserInfo(getActivity()).uid);
			mHttpUtils.send(HttpMethod.GET, url,new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {

				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					mLoadingView.loadSuccess();
					mListLayout.setVisibility(View.VISIBLE);
					String json = arg0.result;
					ResponseUnpayList responseUnpayList = new Gson().fromJson(json, ResponseUnpayList.class);
					List<UnpayGoods> list = responseUnpayList.result.list;
					List<GoodsItem> list2 = new ArrayList<GoodsItem>();
					for (UnpayGoods goods : list) {
						GoodsItem goodsItem = new GoodsItem();
						goodsItem.setCategory(goods.category);
						goodsItem.setEachTimeMoney(1);
						goodsItem.setGoodsId(goods.goodsId);
						goodsItem.setImg(goods.img);
						goodsItem.setPeriod(goods.period);
						goodsItem.setRemainTimes(goods.remainTimes);
						goodsItem.setTotalTimes(goods.totalTimes);
						goodsItem.setTimesInCar(goods.joinTimes);
						goodsItem.setTitle(goods.title);
						list2.add(goodsItem);
					}
					mListAdapter = new DuobaoListAdapter(list2 , getActivity());
					mListAdapter.setOnNumerPickerChangeListener(ListFragment.this);
					mListView.setEmptyView(mEmptyView);
					mListView.setAdapter(mListAdapter);
					if(list2.size() == 0){
						mFooterLayout.setVisibility(View.GONE);
					}else{
						mFooterLayout.setVisibility(View.VISIBLE);
						mCoinTotal = responseUnpayList.result.moneyAll;
						mGoodsCountTv.setText(String.format(getResources().getString(R.string.goods_count), responseUnpayList.result.totalCnt));
						mCoinTotalTv.setText(String.format(getResources().getString(R.string.coin_total), mCoinTotal));
					}
				}
			});
		}else{
			//没有登入就显示本地保存的清单信息
			mListLayout.setVisibility(View.VISIBLE);
			mLoadingView.loadSuccess();
			List<GoodsItem> list = getListFromLocal();
			if(list.size() == 0){
				mFooterLayout.setVisibility(View.GONE);
			}else{
				mFooterLayout.setVisibility(View.VISIBLE);
				mGoodsCountTv.setText(String.format(getResources().getString(R.string.goods_count), list.size()));
				mCoinTotal = 0;
				for (GoodsItem goodsItem : list) {
					mCoinTotal+=goodsItem.getEachTimeMoney()*goodsItem.getTimesInCar();
				}
				mCoinTotalTv.setText(String.format(getResources().getString(R.string.coin_total), mCoinTotal));
			}
			mListAdapter = new DuobaoListAdapter(list , getActivity());
			mListAdapter.setOnNumerPickerChangeListener(this);
			mListView.setEmptyView(mEmptyView);
			mListView.setAdapter(mListAdapter);
		}

	}
	@Override
	protected void lazyLoad() {
		loadDatas();
	}
	@Override
	protected void awaysLoad() {
		//		List<GoodsItem> list = getListFromLocal();
		//		mListAdapter.resetDatas(list);
		loadDatas();
	}
	private DuobaoListHandler duobaoListHandler;
	private List<GoodsItem> getListFromLocal(){
		List<GoodsItem> list = duobaoListHandler.getList();
		return list;
	}
	private int mCoinTotal;
	@Override
	public void OnNumerPickerChange(int position, int numberOffset) {
		GoodsItem goods = mListAdapter.getItem(position);
		mCoinTotal += goods.getEachTimeMoney()*numberOffset;
		mCoinTotalTv.setText(String.format(getResources().getString(R.string.coin_total), mCoinTotal));
	}
	@Override
	public void onClick(View v) {
		if(v == mSettlementTv){
			if(!UserInfoHandler.isLogin(getActivity())){
				MainActivity aty = (MainActivity) getActivity();
				aty.switchToUser();
			}else{
				mLoadingView.startLoading();
				RequestParams params = new RequestParams();
				Userinfo userinfo = UserInfoHandler.getUserInfo(getActivity());
				params.addBodyParameter("IP", userinfo.IP);
				params.addBodyParameter("IPAddress", userinfo.IPAddress);
				params.addBodyParameter("token", userinfo.token);
				List<GoodsItem> datas = mListAdapter.getDatas();
				List<Long> periodList = new ArrayList<Long>();
				List<Integer> timesList = new ArrayList<Integer>();
				for (GoodsItem good : datas) {
					periodList.add(good.getPeriod());
					timesList.add(good.getTimesInCar());
				}
				params.addBodyParameter("periods", new Gson().toJson(periodList));
				params.addBodyParameter("times", new Gson().toJson(timesList));
				String url = Constants.URL_DUOBAO;
				mHttpUtils.send(HttpMethod.POST, url , params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						mLoadingView.loadSuccess();
					}
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						mLoadingView.loadSuccess();
						Toast.makeText(getActivity(), "夺宝成功", 0).show();
						mListAdapter.clearDatas();
						mFooterLayout.setVisibility(View.GONE);
					}
				});
			}
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(getActivity(), GoodsPeriodActivity.class);
		intent.putExtra("period", mListAdapter.getItem((int)id).getPeriod());
		startActivity(intent);
	}
}
