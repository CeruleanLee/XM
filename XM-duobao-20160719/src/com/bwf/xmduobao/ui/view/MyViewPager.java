package com.bwf.xmduobao.ui.view;

import com.lidroid.xutils.util.LogUtils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class MyViewPager extends ViewPager{

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(listener != null){
			listener.onScrollChanged(l, t, oldl, oldt);
		}
	}
	
	public interface OnScrollChangedListener{
		void onScrollChanged(int l, int t, int oldl, int oldt);
	}
	private OnScrollChangedListener listener;
	public void setOnScrollChangedListener(OnScrollChangedListener listener){
		this.listener = listener;
	}
}
