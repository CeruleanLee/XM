package com.bwf.xmduobao.utils;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CarAnimation {
	private ViewGroup mDecorView;
	private FrameLayout imgContainer;
	private int[] mCarLocation;
	private Handler mHandler;
	public  void startAnimation(Activity aty,View srcView,View dstView){
		if(mDecorView == null){
			mDecorView = (ViewGroup) aty.getWindow().getDecorView();
			imgContainer = new FrameLayout(aty);
			mDecorView.addView(imgContainer);
			//找到购物车View
			//获取购物车View在窗口中的位置
			mCarLocation = new int[2];
			dstView.getLocationInWindow(mCarLocation);
			mHandler = new Handler();
		}
		if(srcView instanceof ImageView){
			ImageView srcImageView = (ImageView) srcView;
			//当前Activity所在窗口的根容器
			final ImageView imageView = new ImageView(aty);
			Drawable drawable = srcImageView.getDrawable();
			imageView.setImageDrawable(drawable);
			//创建ImageView的布局参数
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
					srcImageView.getWidth(), srcImageView.getHeight());

			//获取 源img在窗口中的位置
			int[] location = new int[2]; //用来存图片的x，y坐标
			srcImageView.getLocationInWindow(location);
			params.leftMargin = location[0];
			params.topMargin = location[1];
			//将View添加进imgContainer容器
			imgContainer.addView(imageView, params);

			
			//创建要执行的动画
			AnimationSet set = new AnimationSet(true);
			AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0.4f);
			ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.5f, 1, 0.5f, 
					Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			
			float toXDelta = mCarLocation[0] - location[0] - (srcImageView.getWidth()-dstView.getWidth())/2;
			float toYDelta = mCarLocation[1] - location[1] - (srcImageView.getHeight()-dstView.getHeight())/2;
			TranslateAnimation translateAnimation = new TranslateAnimation(0, toXDelta, 0, toYDelta);
			
			//将动画添加到AnimationSet
			set.addAnimation(alphaAnimation);
			set.addAnimation(scaleAnimation);
			set.addAnimation(translateAnimation);
			set.setDuration(1000);
			imageView.startAnimation(set);
			set.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							imgContainer.removeView(imageView);
						}
					});
					
				}
			});
		}
	}
}
