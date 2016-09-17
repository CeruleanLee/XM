package com.bwf.xmduobao.ui.view;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Scroller;

public class AutoScrollViewPager extends ViewPager{
	private class MyScroller extends Scroller{
		private  int mDuration;
		public MyScroller(Context context) {
			super(context);
		}
		@Override
		public void startScroll(int startX, int startY, int dx, int dy, int duration) {
			super.startScroll(startX, startY, dx, dy, mDuration);
		}
		public void setDUration(int duration){
			mDuration = duration;
		}
	}
	public AutoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		//通过反射修改ViewPager切换页面的过度时间
		try {
			Field field = ViewPager.class.getDeclaredField("mScroller");
			field.setAccessible(true);
			scroller = new MyScroller(context);
			scroller.setDUration(2000);
			field.set(this, scroller);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setAdapter(PagerAdapter arg0) {
		super.setAdapter(arg0);
		totalPageCount = arg0.getCount();
		startTask();
	}
	private int totalPageCount;
	private Handler handler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message arg0) {
			int index = getCurrentItem()+1;
			index = index%totalPageCount;
			if(index == 0){
				scroller.setDUration(500);
			}else{
				scroller.setDUration(1500);
			}
			setCurrentItem(index,true);
			return false;
		}
	});
	private Timer timer = new Timer();
	private TimerTask task;
	private MyScroller scroller;
	public void startTask() {
		if(task == null){
			task = new TimerTask() {

				@Override
				public void run() {
					handler.sendEmptyMessage(0);
				}
			};
			timer.schedule(task, 3000, 3000);
		}
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			stopTask();
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		switch (arg0.getAction()) {
		case MotionEvent.ACTION_DOWN:
			stopTask();
			return true;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if(getAdapter() != null){
				startTask();
			}
			break;
		}
		return super.onTouchEvent(arg0);
	}
	public void stopTask() {
		handler.removeMessages(0);
		if(task != null){
			task.cancel();
			task = null;
		}
	}
}
