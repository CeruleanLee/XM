package com.bwf.xmduobao.ui.view;

import com.bwf.xmduobao.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

public class LoadingView extends FrameLayout implements View.OnClickListener{

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	private View mLodingImg;
	private RotateAnimation anim;
	
	private ViewGroup mLoadingLayout,mFailedLayout;
	private TextView mTipsTv;
	private TextView mTryAgainTv;
	private void init() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.v_loading, null);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		addView(view, params );
		mLodingImg = view.findViewById(R.id.img_loading_progress);
		mLoadingLayout = (ViewGroup) view.findViewById(R.id.layout_loading);
		mFailedLayout = (ViewGroup) view.findViewById(R.id.layout_failed);
		mTipsTv = (TextView) view.findViewById(R.id.tv_tips);
		mTryAgainTv = (TextView) view.findViewById(R.id.tv_tryagain);
		mTryAgainTv.setOnClickListener(this);
		anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(1000);
		anim.setRepeatCount(-1);
		anim.setInterpolator(new LinearInterpolator()); //设置为匀速
	}
	/**
	 * 开始加载的时候调用这个方法
	 */
	public  void startLoading(){
		mLoadingLayout.setVisibility(View.VISIBLE);
		mFailedLayout.setVisibility(View.GONE);
		mLodingImg.startAnimation(anim);
	}
	/**
	 * 加载失败的时候调用这个方法
	 */
	public void loadFailed(){
		mLoadingLayout.setVisibility(View.GONE);
		anim.cancel();
		mLodingImg.clearAnimation();
		mFailedLayout.setVisibility(View.VISIBLE);
		mTipsTv.setText("接收网络数据失败");
	}
	/**
	 * 加载成功的时候 调用这个方法
	 */
	public void loadSuccess(){
		mLoadingLayout.setVisibility(View.GONE);
		mFailedLayout.setVisibility(View.GONE);
		anim.cancel();
		mLodingImg.clearAnimation();
	}
	
	public void noNetwork(){
		mLoadingLayout.setVisibility(View.GONE);
		anim.cancel();
		mLodingImg.clearAnimation();
		mFailedLayout.setVisibility(View.VISIBLE);
		mTipsTv.setText("没有网络，请检查网络设置");
	}
	@Override
	public void onClick(View v) {
		if(v == mTryAgainTv){
			if(listener != null){
				listener.onTryAgainClick();
			}
		}
	}
	private OnLoadingViewListener listener;
	public void setOnLoadingViewListener(OnLoadingViewListener listener){
		this.listener = listener;
	}
	public interface OnLoadingViewListener{
		void onTryAgainClick();
	}
}
