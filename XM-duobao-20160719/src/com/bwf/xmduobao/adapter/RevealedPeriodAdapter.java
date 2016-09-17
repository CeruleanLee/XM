package com.bwf.xmduobao.adapter;

import java.util.List;

import com.bwf.xmduobao.Constants;
import com.bwf.xmduobao.R;
import com.bwf.xmduobao.entity.ResponseRevealedPeriodInfo2;
import com.bwf.xmduobao.entity.ResponseRevealedPeriodInfo.LuckUser;
import com.bwf.xmduobao.entity.ResponseRevealedPeriodInfo.RevealedPeriodInfo;
import com.bwf.xmduobao.ui.view.CutdownTextView;
import com.bwf.xmduobao.ui.view.CutdownTextView.OnCountDownFinishListener;
import com.bwf.xmduobao.utils.MBitmapHolder;
import com.bwf.xmduobao.utils.MHttpHolder;
import com.bwf.xmduobao.utils.UrlHandler;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class RevealedPeriodAdapter extends BaseAdapter{
	private List<RevealedPeriodInfo> list;
	private LayoutInflater inflater;
	private BitmapUtils mBitmapUtils;
	private AdapterView<?> adapterView;
	public RevealedPeriodAdapter(List<RevealedPeriodInfo> list, Context context,AdapterView<?> adapterView) {
		super();
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		mBitmapUtils = MBitmapHolder.getBitmapUtils(context);
		mHttpUtils = MHttpHolder.getHttpUtils();
		this.adapterView = adapterView;
	}
	@Override
	public int getCount() {
		return list.size();
	}
	@Override
	public RevealedPeriodInfo getItem(int arg0) {
		return list.get(arg0);
	}
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	@Override
	public View getView( int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_revealed_lastest, null);
			holder.revealingLayout = (ViewGroup) convertView.findViewById(R.id.layout_revealing);
			holder.revealedLayout = (ViewGroup) convertView.findViewById(R.id.layout_revealed);
			holder.periodTv = (TextView) convertView.findViewById(R.id.tv_revealed_onPeriod);
			holder.cutdownTimeTv = (CutdownTextView) convertView.findViewById(R.id.tv_countdownTime);
			holder.imgView = (ImageView) convertView.findViewById(R.id.goodsImg);
			holder.titleTv = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.periodTv = (TextView) convertView.findViewById(R.id.tv_revealed_onPeriod);
			holder.nickNameTv = (TextView) convertView.findViewById(R.id.tv_revealed_onName);
			holder.joinTimesTv = (TextView) convertView.findViewById(R.id.tv_revealed_onJoinTimes);
			holder.luckCodeTv = (TextView) convertView.findViewById(R.id.tv_revealed_onLuckCode);
			holder.revealedTimeTv = (TextView) convertView.findViewById(R.id.tv_revealed_onTime);
			holder.tagImg = (ImageView) convertView.findViewById(R.id.tagImg);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		RevealedPeriodInfo item = getItem(position);
		switch (item.status) {
		case 1:
			holder.revealingLayout.setVisibility(View.VISIBLE);
			holder.revealedLayout.setVisibility(View.INVISIBLE);
			holder.periodTv.setText("期    号："+item.period);
			holder.titleTv.setText(item.goodsName);
			mBitmapUtils.display(holder.imgView, item.img);
			startCutdown(holder.cutdownTimeTv, position,Long.parseLong(item.remainTime)-(System.currentTimeMillis()-item.enterTime), 50);
			break;
		case 2:
			holder.revealingLayout.setVisibility(View.GONE);
			holder.revealedLayout.setVisibility(View.VISIBLE);
			holder.titleTv.setText(item.goodsName);
			holder.periodTv.setText("期    号："+item.period);
			LuckUser luckUser = item.luckUser;
			holder.nickNameTv.setText("获得者："+luckUser.nickname);
			holder.joinTimesTv.setText("参与次数："+luckUser.duobaoTimes);
			holder.luckCodeTv.setText("幸运号码："+item.luckCode);
			holder.revealedTimeTv.setText("揭晓时间："+item.revealedTime);
			mBitmapUtils.display(holder.imgView, item.img);
			break;
		}
		return convertView;
	}
	private HttpUtils mHttpUtils;

	private void startCutdown(CutdownTextView view,final int position,long time,int period){
		view.setOnCountDownFinishListener(new OnCountDownFinishListener() {

			@Override
			public void onFinish() {
				String url = UrlHandler.handlUrl(Constants.URL_UPDATE_PERIOD_INFO, getItem(position).period);
				mHttpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						
					}
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						try {
							System.out.println("getView---------"+position+"被回调拉！！！");
							String content = arg0.result;
							ResponseRevealedPeriodInfo2 response = new Gson().fromJson(content, ResponseRevealedPeriodInfo2.class);
							list.set(position, response.result);
							int viewPosition = position - adapterView.getFirstVisiblePosition();
							getView(position, adapterView.getChildAt(viewPosition), adapterView);
						} catch (Exception e) {
						}
					}
				});
			}
		});
		view.startCutdown(position,time, period);
		
	}
	private class ViewHolder{
		ViewGroup revealingLayout;
		ViewGroup revealedLayout;
		ImageView imgView;
		TextView titleTv;
		TextView periodTv;
		TextView nickNameTv;
		TextView joinTimesTv;
		TextView luckCodeTv;
		TextView revealedTimeTv;
		CutdownTextView  cutdownTimeTv;
		ImageView tagImg;
	}
	/**
	 * 封装了一个向数据源中添加数据的方法
	 * @param list
	 */
	public void addDatas(List<RevealedPeriodInfo> list) {
		long enterTime = System.currentTimeMillis();
		for (RevealedPeriodInfo revealedPeriodInfo : list) {
			if(revealedPeriodInfo.status == 1){
				revealedPeriodInfo.enterTime = enterTime;
			}
		}
		this.list.addAll(list);
		notifyDataSetChanged();
	}
	public void clearDatas() {
		this.list.clear();
		notifyDataSetChanged();
	}

	private void updateData(int position){

	}
}
