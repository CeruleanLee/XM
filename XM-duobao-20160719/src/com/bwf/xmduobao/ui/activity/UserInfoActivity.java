package com.bwf.xmduobao.ui.activity;


import java.util.ArrayList;
import java.util.List;

import com.bwf.xmduobao.R;
import com.bwf.xmduobao.adapter.MyFragmentPagerAdapter;
import com.bwf.xmduobao.ui.fragment.ListFragment;
import com.bwf.xmduobao.ui.fragment.MineFragment;
import com.bwf.xmduobao.ui.view.MyViewPager;
import com.bwf.xmduobao.ui.view.MyViewPager.OnScrollChangedListener;
import com.bwf.xmduobao.utils.BitmapHandler;
import com.bwf.xmduobao.utils.MBitmapHolder;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckedTextView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

public class UserInfoActivity extends FragmentActivity implements OnScrollChangedListener, OnPageChangeListener, OnClickListener{
	@ViewInject(R.id.viewPager)
	private MyViewPager mViewPager;
	private List<Fragment> fragments;
	@ViewInject(R.id.tv_duobaoRecord)
	private CheckedTextView mDuobaoRecordTv;
	@ViewInject(R.id.line)
	private View mLine;
	@ViewInject(R.id.lineContainer)
	private ViewGroup mLineContainer;
	@ViewInject(R.id.layout_tab)
	private ViewGroup mTabLayout;
	@ViewInject(R.id.hscrollView)
	private HorizontalScrollView mScrollView;

	private CheckedTextView[] mCtvs;
	private BitmapUtils mBitmapUtils;
	
	@ViewInject(R.id.img_avatar)
	private ImageView mAvatarImg;
	@ViewInject(R.id.tv_nickname)
	private TextView mNicknameTv;
	@ViewInject(R.id.tv_uid)
	private TextView mUidTv;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_user_info);
		mBitmapUtils = MBitmapHolder.getBitmapUtils(this);
		initViews();
		initDatas();
	}
	private void initDatas() {
		Intent intent = getIntent();
		mBitmapUtils.display(mAvatarImg, intent.getStringExtra("avatar"),new BitmapLoadCallBack<ImageView>() {
			
			@Override
			public void onLoadCompleted(ImageView arg0, String arg1, Bitmap arg2, BitmapDisplayConfig arg3,
					BitmapLoadFrom arg4) {
				Bitmap bitmap = BitmapHandler.createCircleBitmap(arg2);
				arg0.setImageBitmap(bitmap);
			}

			@Override
			public void onLoadFailed(ImageView arg0, String arg1, Drawable arg2) {
				
			}
		});
		mNicknameTv.setText(intent.getStringExtra("nickname"));
		mUidTv.setText("ID:"+intent.getIntExtra("uid",0));
	}
	private int mTabWidth;
	private int mViewPagerWidth;
	private MyFragmentPagerAdapter adapter;
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus){
			mTabWidth = mDuobaoRecordTv.getWidth();
			LayoutParams params = mLine.getLayoutParams();
			params.width = mTabWidth;
			mLine.setLayoutParams(params);
			mViewPagerWidth = mViewPager.getWidth();
		}
	}
	private void initViews() {
		ViewUtils.inject(this);
		fragments = new ArrayList<Fragment>();
		fragments.add(new ListFragment());
		fragments.add(new MineFragment());
		//		fragments.add(new ListFragment());
		//		fragments.add(new MineFragment());
		adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(adapter);
		mViewPager.setOnScrollChangedListener(this);
		mViewPager.setOnPageChangeListener(this);
		mCtvs = new CheckedTextView[mTabLayout.getChildCount()];
		for (int i = 0; i < mCtvs.length; i++) {
			mCtvs[i] = (CheckedTextView) mTabLayout.getChildAt(i);
			mCtvs[i].setOnClickListener(this);
		}
	}
	@Override
	public void onScrollChanged(int l, int t, int oldl, int oldt) {

		int mTabLayoutScrollX = l*(mTabLayout.getWidth()-mScrollView.getWidth())/(mViewPagerWidth*(adapter.getCount()-1));
		mScrollView.scrollTo(mTabLayoutScrollX, 0);
		int lineScrollX  = -l*mTabWidth/mViewPagerWidth;
		mLineContainer.scrollTo(lineScrollX, 0);
		/**
		 *  mTabWidth               lineScrollX
		 *  ---------        =  ----------- 
		 *  mViewPagerWidth             l
		 * 
		 */


		/** mTabLayout.getWidth()-mScrollView.getWidth         x
		 * ---------------------------------------------  = ------------
		 *   mViewPagerWidth*3                                 l
		 */
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}
	@Override
	public void onPageSelected(int index) {
		for (int i = 0; i < mCtvs.length; i++) {
			if(i == index){
				mCtvs[i].setChecked(true);
			}else{
				mCtvs[i].setChecked(false);
			}
		}
	}
	@Override
	public void onClick(View v) {
		for (int i = 0; i < mCtvs.length; i++) {
			if(mCtvs[i] == v){
				mViewPager.setCurrentItem(i, true);
				return;
			}
		}
	}

}
