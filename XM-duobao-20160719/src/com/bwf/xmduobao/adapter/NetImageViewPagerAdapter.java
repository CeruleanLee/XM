package com.bwf.xmduobao.adapter;

import java.util.List;

import com.bwf.xmduobao.utils.MBitmapHolder;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class NetImageViewPagerAdapter extends PagerAdapter{
	private List<ImageView> imgviews;
	private BitmapUtils mBitmapUtils;
	private List<String> datas;
	public NetImageViewPagerAdapter(Context context,List<ImageView> imgviews,List<String> datas) {
		super();
		this.imgviews = imgviews;
		mBitmapUtils = MBitmapHolder.getBitmapUtils(context);
		this.datas = datas;
	}
	@Override
	public int getCount() {
		return imgviews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView view = imgviews.get(position);
		if(view.getParent() == null){
			container.addView(view);
			mBitmapUtils.display(view, datas.get(position));
		}
		return view;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		//		View view = imgviews.get(position);
		//		container.removeView(view);
	}
}
