package com.bwf.xmduobao.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.bwf.xmduobao.R;
import com.bwf.xmduobao.ui.fragment.FindFragment;
import com.bwf.xmduobao.ui.fragment.HomeFragment;
import com.bwf.xmduobao.ui.fragment.ListFragment;
import com.bwf.xmduobao.ui.fragment.MineFragment;
import com.bwf.xmduobao.ui.fragment.WinFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.socialize.UMShareAPI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

public class MainActivity extends BaseTabActivity {
	@ViewInject(R.id.viewPager)
	private ViewPager mViewPager;
	private List<Fragment> fragments;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_activity);
		mShareAPI = UMShareAPI.get(this);
		ViewGroup tabContainer = (ViewGroup) findViewById(R.id.bottom_tab);
		fragments = new ArrayList<Fragment>();
		fragments.add(new HomeFragment());
		fragments.add(new WinFragment());
		fragments.add(new FindFragment());
		fragments.add(new ListFragment());
		fragments.add(new MineFragment());
		//		int[][] res = {{R.drawable.ic_home,R.drawable.ic_home_selected},{R.drawable.ic_win,R.drawable.ic_win_selected},
		//				{R.drawable.ic_find,R.drawable.ic_find_selected},{R.drawable.ic_listing,R.drawable.ic_listing_selected},
		//				{R.drawable.ic_user,R.drawable.ic_user_selected}};
		int[][] res = {
				{R.drawable.tab_home_normal,R.drawable.tab_home_press},
				{R.drawable.tab_announce_normal,R.drawable.tab_announce_press},
				{R.drawable.tab_rank_unselect,R.drawable.tab_rank_selected},
				{R.drawable.tab_list_normal,R.drawable.tab_list_press},
				{R.drawable.tab_me_normal,R.drawable.tab_me_press}};
		int[] textColors = {getResources().getColor(R.color.tab_text),getResources().getColor(R.color.tab_text_selected)};
		initViews();
		init(mViewPager,tabContainer, fragments,res,textColors );
		switchTab(0);
	}
	
	public void switchToUser(){
		switchTab(4);
	}
	private void initViews() {
		ViewUtils.inject(this);
	}
	public UMShareAPI mShareAPI ;
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mShareAPI.onActivityResult(requestCode, resultCode, data);
	}
}
