package com.bwf.xmduobao.ui.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.bwf.xmduobao.Constants;
import com.bwf.xmduobao.R;
import com.bwf.xmduobao.adapter.NetImageViewPagerAdapter;
import com.bwf.xmduobao.adapter.RecordListAdapter;
import com.bwf.xmduobao.entity.PeriodInfo;
import com.bwf.xmduobao.entity.PeriodInfo.LuckUser;
import com.bwf.xmduobao.entity.Record;
import com.bwf.xmduobao.entity.ResopnseRecord;
import com.bwf.xmduobao.entity.ResponseGoods;
import com.bwf.xmduobao.entity.ResponsePeriodInfo;
import com.bwf.xmduobao.entity.ResponseUserinfo.Userinfo;
import com.bwf.xmduobao.ui.view.CutdownTextView;
import com.bwf.xmduobao.ui.view.CutdownTextView.OnCountDownFinishListener;
import com.bwf.xmduobao.utils.MBitmapHolder;
import com.bwf.xmduobao.utils.MHttpHolder;
import com.bwf.xmduobao.utils.SpannableStringBuild;
import com.bwf.xmduobao.utils.UrlHandler;
import com.bwf.xmduobao.utils.UserInfoHandler;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class GoodsPeriodActivity extends Activity implements OnClickListener, OnItemClickListener{
	@ViewInject(R.id.viewPager)
	private ViewPager mHeaderViewPager;
	@ViewInject(R.id.listView)
	private ListView mListView;
	private HttpUtils mHttpUtils;
	private long period;
	private RecordListAdapter mListAdapter;
	@ViewInject(R.id.bottomLayout1)
	private ViewGroup mLayoutBottom1;
	@ViewInject(R.id.bottomLayout2)
	private ViewGroup mLayoutBottom2;
	@ViewInject(R.id.bt_joinNow)
	private View mJoinNowBtn;
	@ViewInject(R.id.bt_goNow)
	private View mGoNowBtn;
	@ViewInject(R.id.shareImg)
	private View mShareImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_period);
		initViews();
		initDatas();
	}
	private void initDatas() {
		mHttpUtils=MHttpHolder.getHttpUtils();
		if((period = getIntent().getLongExtra("period", -1)) != -1){
			loadViewPagerDatas();
		}else{
			loadLatestPeriod();
		}
		//		loadRecords();
	}

	private void loadLatestPeriod() {

		String url =UrlHandler.handlUrl(Constants.URL_LATEST_PERIOD, getIntent().getIntExtra("gid", -1));
		mHttpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String content = arg0.result;
				ResponseGoods response = new Gson().fromJson(content, ResponseGoods.class);
				period = response.getResult().getPeriod();
				loadViewPagerDatas();
			}
		});
	}

	private int mNextPage = 1;
	private long mSinceTime;
	private void loadRecords() {
		isLoading = true;
		mLoadmoreFailedTv.setVisibility(View.GONE);
		if(mNextPage == 1){
			mSinceTime = System.currentTimeMillis();
		}
		String url =UrlHandler.handlUrl(Constants.URL_DUOBAO_RECORD, period,mNextPage,mSinceTime);
		mHttpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				//加载失败怎么处理
				if(mNextPage != 1){
					mLoadmoreFailedTv.setVisibility(View.VISIBLE);
				}
				isLoading = false;
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String content = arg0.result;
				ResopnseRecord response = new Gson().fromJson(content, ResopnseRecord.class);
				List<Record> list = response.getResult().list;
				mListAdapter.addDatas(list);
				mNextPage++;
				isLoading = false;
				//判断数据是否已获取到所有数据
				if(response.getResult().totalCnt <=  mListAdapter.getCount()){
					mLoadmoreProgressBar.setVisibility(View.GONE);
					mLoadmoreTv.setText("没有更多数据了!");
					isLoading = true;
				}
			}
		});


	}
	private void loadViewPagerDatas() {
		String url =UrlHandler.handlUrl(Constants.URL_PERIOD_DETAIL, period);
		mHttpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String content = arg0.result;
				setHeaderViewPagerDatas(content);
			}
		});

	}
	protected void setHeaderViewPagerDatas(String content) {
		ResponsePeriodInfo response = new Gson().fromJson(content, ResponsePeriodInfo.class);
		periodInfo = response.result;
		goodsId = periodInfo.goodsId;
		List<String> imgUrls = periodInfo.bigImgs;
		List<ImageView> imgviewList = new ArrayList<ImageView>();
		for (int i = 0; i < imgUrls.size(); i++) {
			ImageView imgView = new ImageView(this);
			imgView.setScaleType(ScaleType.CENTER_CROP);
			imgviewList.add(imgView);
		}
		NetImageViewPagerAdapter pagerAdapter = new NetImageViewPagerAdapter(GoodsPeriodActivity.this, imgviewList, imgUrls);
		mHeaderViewPager.setAdapter(pagerAdapter);
		mIndicators = new ImageView[imgviewList.size()];
		LayoutInflater inflater = LayoutInflater.from(this);
		for (int i = 0; i < mIndicators.length; i++) {
			View view = inflater.inflate(R.layout.v_indicator, null);
			mIndicators[i] = (ImageView) view.findViewById(R.id.img);
			mIndicators[i].setBackgroundResource(R.drawable.shape_indicator_unselected);
			mHeadIndicatorContainer.addView(view);
			//布局参数，在使用的时候要根据父容器的类型来选择对应的LayoutParams
			//			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, 
			//					LayoutParams.WRAP_CONTENT);
			//			params.rightMargin = 4;
			//			mIndicators[i].setLayoutParams(params);
		}
		//默认让第一个ImageView选中
		mIndicators[0].setBackgroundResource(R.drawable.shape_indicator_selected);
		mHeaderViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mIndicators[mCurPos].setBackgroundResource(R.drawable.shape_indicator_unselected);
				mIndicators[position].setBackgroundResource(R.drawable.shape_indicator_selected);
				mCurPos = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		String s = periodInfo.goodsName+" "+periodInfo.description;
		SpannableString spannableString = new SpannableString(s);
		spannableString.setSpan(new ForegroundColorSpan(0xAAff0000), 
				periodInfo.goodsName.length(), s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mGoodsTitleTv.setText(spannableString);
		//根据开奖状态 让不同的视图显示
		switch (periodInfo.status) {
		case 0:
			setStatusLayout0(periodInfo);
			mLayoutBottom2.setVisibility(View.VISIBLE);
			break;
		case 1:
			setStatusLayout1(periodInfo);
			mLayoutBottom1.setVisibility(View.VISIBLE);
			break;
		case 2:
			setStatusLayout2(periodInfo);
			mLayoutBottom1.setVisibility(View.VISIBLE);
			break;
		}

		//专区图片
		//		if(periodInfo.min == 10){
		//			mTagImg.setVisibility(View.VISIBLE);
		//			mTagImg.setImageResource(R.drawable.ico_area_10);
		//		}else if(periodInfo.unit == 100){
		//			mTagImg.setVisibility(View.VISIBLE);
		//			mTagImg.setImageResource(R.drawable.ico_area_100);
		//		}else{
		//			mTagImg.setVisibility(View.GONE);
		//		}
	}

	@ViewInject(R.id.img_avatar)
	private ImageView mLuckAvatarImg;
	@ViewInject(R.id.tv_revealed_onName)
	private TextView mRevealedONameTv;
	@ViewInject(R.id.tv_revealed_onLocal)
	private TextView mRevealedOnLocal;
	@ViewInject(R.id.tv_revealed_onId)
	private TextView mRevealedOnIdTv;
	@ViewInject(R.id.tv_open_qihao)
	private TextView mOpenPeriodTv;
	@ViewInject(R.id.tv_revealed_onCost)
	private TextView mRevealedOnCostTv;
	@ViewInject(R.id.tv_revealed_onTime)
	private TextView mRevealedOnTimeTv;
	@ViewInject(R.id.bt_revealed_calaDet)
	private View mRevealedCalaDetTv;
	@ViewInject(R.id.tv_revealed_luckcode)
	private TextView mRevealedLuckCode;
	private void setStatusLayout2(PeriodInfo periodInfo) {
		mLayoutStatus2.setVisibility(View.VISIBLE);
		mLayoutStatus1.setVisibility(View.GONE);
		mLayoutStatus0.setVisibility(View.GONE);
		LuckUser luckUser = periodInfo.luckUser;
		System.out.println("luckuser:"+luckUser);
		MBitmapHolder.getBitmapUtils(this).display(mLuckAvatarImg, luckUser.avatar);
		String revealedOName = "获得者："+luckUser.nickname;
		SpannableString spannableString = SpannableStringBuild.build(revealedOName, 0xff436EEE, luckUser.nickname);
		mRevealedONameTv.setText(spannableString);
		mRevealedOnLocal.setText("用户IP："+luckUser.IP+"("+luckUser.IPAddress+")");
		mRevealedOnIdTv.setText("用户ID："+luckUser.uid+"(唯一不变的标识)");
		mOpenPeriodTv.setText("期号："+periodInfo.period);

		String revealedOnCost = "本期参与："+luckUser.duobaoTimes+"人次";
		SpannableString spannableString2 = SpannableStringBuild.build(revealedOnCost, 0xffff2233, luckUser.duobaoTimes);
		mRevealedOnCostTv.setText(spannableString2);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		mRevealedOnTimeTv.setText("揭晓时间："+sdf.format(Long.parseLong(periodInfo.revealedTime)));
		mRevealedLuckCode.setText(periodInfo.luckCode+"");

		mRevealedCalaDetTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GoodsPeriodActivity.this,ComputeDetailActivity.class);
				intent.putExtra("period", period);
				startActivity(intent);
			}
		});
	}
	private void setStatusLayout1(PeriodInfo periodInfo) {
		mLayoutStatus1.setVisibility(View.VISIBLE);
		mPeriodNumTv2.setText(periodInfo.period+"");
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss:SS");
		mCountdownTimeTv.startCutdown(periodInfo.remainTime,50);
		mCountdownTimeTv.setOnCountDownFinishListener(new OnCountDownFinishListener() {

			@Override
			public void onFinish() {
				//TODO
				mCountdownTimeTv.setText("正在获取开奖信息...");
				mHeadIndicatorContainer.removeAllViews();
				loadViewPagerDatas();
			}
		});
	}
	private void setStatusLayout0(PeriodInfo periodInfo) {
		mLayoutStatus0.setVisibility(View.VISIBLE);
		mPeriodNumTv.setText(String.format(getString(R.string.goods_period)+"", periodInfo.period));
		mTotalTimesTv.setText(String.format(getString(R.string.goods_total_times_text), periodInfo.totalTimes));
		mRemainTimesTv.setText(periodInfo.remainTimes+"");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(periodInfo.startDuobaoTime);
		mStartTimeTv.setText(String.format(getString(R.string.period_start_date)+"", time));
		mPb.setMax(periodInfo.totalTimes);
		mPb.setProgress(periodInfo.totalTimes-periodInfo.remainTimes);

		remainTimes = periodInfo.remainTimes;
	}
	private int mCurPos;
	private void initViews() {
		ViewUtils.inject(this);
		mListAdapter = new RecordListAdapter(this, new ArrayList<Record>());
		mListView.addHeaderView(getHeaderView());
		mListView.addFooterView(getLoadMoreFooterView());
		mListView.setAdapter(mListAdapter);
		mListView.setOnScrollListener(onScrollListener);
		mListView.setOnItemClickListener(this);
		mJoinNowBtn.setOnClickListener(this);
		mGoNowBtn.setOnClickListener(this);
		mShareImg.setOnClickListener(this);
	}
	private OnScrollListener onScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			if(lastVisibleItem == visibleItemCount+firstVisibleItem-1){
				return;
			}
			lastVisibleItem = visibleItemCount+firstVisibleItem-1;
			if(!isLoading && firstVisibleItem + visibleItemCount == totalItemCount){
				//满足条件后，加载下一页数据
				loadRecords();
			}
		}
	};
	/**最后一个可见item的位置**/
	private int lastVisibleItem = 0;
	private boolean isLoading = false;
	@ViewInject(R.id.v_loadmore_progressBar)
	private ProgressBar mLoadmoreProgressBar;
	@ViewInject(R.id.v_loadmore_textView)
	private TextView mLoadmoreTv;
	@ViewInject(R.id.v_loadmore_failedTv)
	private TextView mLoadmoreFailedTv;
	/**
	 * 初始化并返回加载更多的那个脚视图
	 * @return
	 */
	private View getLoadMoreFooterView() {
		View mFooterView = LayoutInflater.from(this).inflate(R.layout.v_loadmore, null);
		ViewUtils.inject(this, mFooterView);
		mLoadmoreFailedTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mLoadmoreFailedTv.setVisibility(View.GONE);
				loadRecords();
			}
		});
		return mFooterView;
	}
	@ViewInject(R.id.indicatorContainer)
	private LinearLayout mHeadIndicatorContainer;
	/**作为指示器的那些小圆点**/
	private ImageView[] mIndicators;
	@ViewInject(R.id.goodsTitleTv)
	private TextView mGoodsTitleTv;
	@ViewInject(R.id.periodTv)
	private TextView mPeriodNumTv;
	@ViewInject(R.id.totalTimesTv)
	private TextView mTotalTimesTv;
	@ViewInject(R.id.progressBar)
	private ProgressBar mPb;
	@ViewInject(R.id.remainTimesTv)
	private TextView mRemainTimesTv;
	@ViewInject(R.id.imgTextDetailLayout)
	private View mImgTextDetailLayout;
	@ViewInject(R.id.winRecordLayout)
	private View mWinHistoryLayout;;
	private PeriodInfo periodInfo;
	@ViewInject(R.id.startTimeTv)
	private TextView mStartTimeTv;
	@ViewInject(R.id.tagImg)
	private ImageView mTagImg;
	@ViewInject(R.id.layout_status_0)
	private ViewGroup mLayoutStatus0;
	@ViewInject(R.id.layout_status_1)
	private ViewGroup mLayoutStatus1;
	@ViewInject(R.id.layout_status_2)
	private ViewGroup mLayoutStatus2;
	@ViewInject(R.id.tv_period)
	private TextView mPeriodNumTv2;
	@ViewInject(R.id.tv_countdownTime)
	private CutdownTextView mCountdownTimeTv;
	@ViewInject(R.id.bt_computeDetail)
	private View mComputeDetailBtn;
	private View getHeaderView() {
		View view = LayoutInflater.from(this).inflate(R.layout.v_head_goods_period, null);
		ViewUtils.inject(this, view);
		mImgTextDetailLayout.setOnClickListener(this);
		mWinHistoryLayout.setOnClickListener(this);
		return view;
	}
	private int goodsId;
	@Override
	public void onClick(View v) {
		if(v == mImgTextDetailLayout){
			Intent intent = new Intent(this, ImgTextDetailActivity.class);
			intent.putExtra("goodsId", periodInfo.goodsId);
			startActivity(intent);
		}else if(v == mWinHistoryLayout){
			Intent intent = new Intent(this, WinHistoryActivity.class);
			intent.putExtra("goodsId", goodsId);
			startActivity(intent);
		}else if(v == mJoinNowBtn){
			if(UserInfoHandler.isLogin(this)){
				Userinfo userInfo = UserInfoHandler.getUserInfo(this);
				String url = UrlHandler.handlUrl(Constants.URL_JOIN, remainTimes,period,userInfo.token,userInfo.IP,userInfo.IPAddress);
				mHttpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						
					}
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						finish();
					}
				});
			}
		}else if(v == mGoNowBtn){
			Intent intent = new Intent(GoodsPeriodActivity.this, GoodsPeriodActivity.class);
			intent.putExtra("gid", goodsId);
			startActivity(intent);
		}else if(v == mShareImg){
			UMImage image = new UMImage(this, R.drawable.meng);
			new ShareAction(this).setDisplayList(displaylist )
			.withTitle("呆萌呆萌的")
			.withText( "动物搞笑大合集~笑死不偿命\n                 三条 →" )
			.withTargetUrl("http://m.miaopai.com/show/channel/97eM365VrkOnizhgx02~pA_")
			.withMedia( image  )
			.setListenerList(umShareListener)
			.open();
		}
	}
	UMShareListener umShareListener = new UMShareListener() {

		@Override
		public void onResult(SHARE_MEDIA arg0) {
			if(arg0 == SHARE_MEDIA.WEIXIN){
				Toast.makeText(getApplicationContext(), "share is success", 0).show();
			}else{
				Toast.makeText(getApplicationContext(), "share is success", 0).show();
			}
		}

		@Override
		public void onError(SHARE_MEDIA arg0, Throwable arg1) {
			Toast.makeText(getApplicationContext(), "share is error", 0).show();

		}

		@Override
		public void onCancel(SHARE_MEDIA arg0) {
			Toast.makeText(getApplicationContext(), "share is cancel", 0).show();
		}
	};
	int remainTimes;
	final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
			{
					SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
					SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
			};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get( this ).onActivityResult( requestCode, resultCode, data);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(id >= 0){
			Record record = mListAdapter.getItem((int) id);
			Intent intent = new Intent(this, UserInfoActivity.class);
			intent.putExtra("uid", record.getUser().getUid());
			intent.putExtra("nickname", record.getUser().getNickName());
			intent.putExtra("avatar", record.getUser().getAvatar());
			startActivity(intent);
		}
	}
}
