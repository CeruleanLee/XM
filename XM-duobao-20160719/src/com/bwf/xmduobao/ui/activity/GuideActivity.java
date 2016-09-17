package com.bwf.xmduobao.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.bwf.xmduobao.R;
import com.bwf.xmduobao.adapter.BasicViewPagerAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class GuideActivity extends Activity implements OnPageChangeListener{
	@ViewInject(R.id.viewPager)
	private ViewPager mViewPager;
	private BasicViewPagerAdapter mPagerAdapter;
	private List<View> mViews;
	@ViewInject(R.id.enterBtn)
	private Button mEnterBtn;

	/**用来装指示器的容器**/
	@ViewInject(R.id.indicatorContainer)
	private LinearLayout mIndicatorContainer;
	/**作为指示器的那些小圆点**/
	private ImageView[] mIndicators;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		ViewUtils.inject(this);
		mViews = getViewList();
		mPagerAdapter = new BasicViewPagerAdapter(mViews);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(this);
		initIndicators(mViews.size()-1);
	}
	/**
	 * 初始化底部的那些指示点
	 */
	private void initIndicators(int size) {
		mIndicators = new ImageView[size];
		for (int i = 0; i < mIndicators.length; i++) {
			mIndicators[i] = new ImageView(this);
			mIndicators[i].setImageResource(R.drawable.guide_indicator_unselected);
			mIndicatorContainer.addView(mIndicators[i]);
			//布局参数，在使用的时候要根据父容器的类型来选择对应的LayoutParams
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, 
					LayoutParams.WRAP_CONTENT);
			params.rightMargin = 35;
			mIndicators[i].setLayoutParams(params);
		}
		//默认让第一个ImageView选中
		mIndicators[0].setImageResource(R.drawable.guide_indicator_selected);
	}
	/**
	 * 获取导航的几个页面的View对象
	 * @return
	 */
	private List<View> getViewList(){
		List<View> views = new ArrayList<View>();
		LayoutInflater inflater = LayoutInflater.from(this);
		views.add(inflater.inflate(R.layout.v_guide1, null));
		views.add(inflater.inflate(R.layout.v_guide2, null));
		View view = inflater.inflate(R.layout.v_guide3, null);
		views.add(view);
		views.add(new View(this));
		ViewUtils.inject(this, view);
		return views;
	}
	@OnClick(R.id.enterBtn)
	public void onClick(View v){
		if(v == mEnterBtn){
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}
	private int mCurPos;//0 1 2 3  
	@Override
	public void onPageSelected(int position) {
		if(position >= mViews.size()-2){
			mIndicatorContainer.setVisibility(View.GONE);
		}else{
			mIndicatorContainer.setVisibility(View.VISIBLE);
			mIndicators[mCurPos].setImageResource(R.drawable.guide_indicator_unselected);
			mIndicators[position].setImageResource(R.drawable.guide_indicator_selected);
			mCurPos = position;
		}
		
		//如果切换到了最后一页，就直接跳转到主界面
		if(position == mPagerAdapter.getCount()-1){
			onClick(mEnterBtn);
		}
	}
}
