package com.bwf.xmduobao.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
	private List<Fragment> fragments;
	public MyFragmentPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	//重写这个，并且将super调用去掉，是为了解决 ViewPager滚动回来的时候，之前的fragment会重新加载的问题
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
	}
}