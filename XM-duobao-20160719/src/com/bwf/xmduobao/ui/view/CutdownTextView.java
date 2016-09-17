package com.bwf.xmduobao.ui.view;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

public class CutdownTextView extends TextView{

	public CutdownTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	private SimpleDateFormat dateFormat;
	private CountDownTimer countDownTimer;
	private String patternHour = "HH:mm:ss";
	private String patternMinute = "mm:ss.SS";
	private static final int STATE_HOUR = 1;
	private static final int STATE_MINUTE = 2;
	private int currentState;

	public void startCutdown(long time,long period){
		stopCutdown();
		if(time <= 0){
			if(listener != null){
				listener.onFinish();
			}
			return;
		}
		if(time >= 60*60*1000){
			dateFormat = new SimpleDateFormat(patternHour);
			currentState = STATE_HOUR;
		}else{
			dateFormat = new SimpleDateFormat(patternMinute);
			currentState = STATE_MINUTE;
		}
		if(countDownTimer == null){
			countDownTimer = new CountDownTimer(time, period) {

				@Override
				public void onTick(long millisUntilFinished) {
					if(currentState == STATE_HOUR && millisUntilFinished < 60*60*1000){
						dateFormat.applyPattern(patternMinute);
						currentState = STATE_MINUTE;
					}
					setText(dateFormat.format(millisUntilFinished+3600000*16));
				}
				@Override
				public void onFinish() {
					stopCutdown();
					setText(dateFormat.format(0));
					setText("正在揭晓...");
					if(listener != null){
						listener.onFinish();
					}
				}
			};
			countDownTimer.start();
			System.out.println("启动---------");
		}
	}
	public void startCutdown(int position,long time,long period){
		print(position, position+"进来1---------"+countDownTimer+","+time);
		stopCutdown();
		print(position, position+"进来2---------"+countDownTimer+","+time);
		if(time <= 0){
			if(listener != null){
				listener.onFinish();
				print(position, position+"进来3---------回调");
			}
			return;
		}
		if(time >= 60*60*1000){
			dateFormat = new SimpleDateFormat(patternHour);
			currentState = STATE_HOUR;
		}else{
			dateFormat = new SimpleDateFormat(patternMinute);
			currentState = STATE_MINUTE;
		}
		print(position, position+"进来4---------"+countDownTimer+","+time);
		if(countDownTimer == null){
			print(position, position+"进来5---------"+countDownTimer+","+time);
			countDownTimer = new CountDownTimer(time, period) {
				
				@Override
				public void onTick(long millisUntilFinished) {
					if(currentState == STATE_HOUR && millisUntilFinished < 60*60*1000){
						dateFormat.applyPattern(patternMinute);
						currentState = STATE_MINUTE;
					}
					setText(dateFormat.format(millisUntilFinished+3600000*16));
				}
				@Override
				public void onFinish() {
					setText(dateFormat.format(0));
					setText("正在揭晓...");
					if(listener != null){
						listener.onFinish();
					}
					stopCutdown();
				}
			};
			print(position, position+"进来6---------"+countDownTimer+","+time);
			countDownTimer.start();
			print(position, position+"进来7---------"+countDownTimer+","+time);
			print(position, position+"启动---------");
		}
	}
	public void stopCutdown(){
		if(countDownTimer != null){
			countDownTimer.cancel();
			countDownTimer = null;
		}
	}
	private OnCountDownFinishListener listener;
	public void setOnCountDownFinishListener(OnCountDownFinishListener listener){
		this.listener = listener;
	}
	public interface OnCountDownFinishListener{
		public void onFinish();
	}
	
	private void print(int position,String str){
		if(position != 0){
			System.out.println(str);
		}
	}
}
