package com.bwf.xmduobao.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bwf.xmduobao.R;
import com.bwf.xmduobao.entity.GoodsItem;
import com.bwf.xmduobao.utils.AddToCarHandler;
import com.bwf.xmduobao.utils.CarAnimation;
import com.bwf.xmduobao.utils.MBitmapHolder;
import com.lidroid.xutils.BitmapUtils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HomeGridViewAdapter extends BaseAdapter{
	private List<GoodsItem> list;
	private LayoutInflater inflater;
	private BitmapUtils mBitmapUtils;
	private Context context;
	private AddToCarHandler mAddToCarHandler;
	public HomeGridViewAdapter(List<GoodsItem> list, Context context) {
		super();
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		mBitmapUtils = MBitmapHolder.getBitmapUtils(context);
		this.context = context;
		mAddToCarHandler = new AddToCarHandler(context);
	}
	@Override
	public int getCount() {
		return list.size();
	}
	@Override
	public GoodsItem getItem(int arg0) {
		return list.get(arg0);
	}
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	@Override
	public View getView(final int position, View arg1, ViewGroup arg2) {
		final ViewHolder holder;
		if(arg1 == null){
			arg1 = inflater.inflate(R.layout.item_home_gridview	, null);
			holder = new ViewHolder();
			holder.addListBtn = (TextView) arg1.findViewById(R.id.btnAddList);
			holder.titleTv = (TextView) arg1.findViewById(R.id.tvTitle);
			holder.imgView = (ImageView) arg1.findViewById(R.id.goodsImg);
			holder.tagImg = (ImageView) arg1.findViewById(R.id.tagImg);
			holder.progressTv = (TextView) arg1.findViewById(R.id.tvProgress);
			holder.progressBar = (ProgressBar) arg1.findViewById(R.id.progressBar);
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		final GoodsItem item = getItem(position);
		holder.titleTv.setText(item.getTitle());
		int progress = (item.getTotalTimes()-item.getRemainTimes())*100/item.getTotalTimes();
		holder.progressTv.setText(progress+"%");
		holder.progressBar.setProgress(progress);
		mBitmapUtils.display(holder.imgView, item.getImg());
		holder.addListBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//加入清单 
				addToList(position);
				startAnimation(holder.imgView);
			}
		});
		//专区图片
		if(item.getCategory() == 1){
			holder.tagImg.setVisibility(View.VISIBLE);
			holder.tagImg.setImageResource(R.drawable.ico_area_10);
		}else if(item.getCategory() == 2){
			holder.tagImg.setVisibility(View.VISIBLE);
			holder.tagImg.setImageResource(R.drawable.ico_area_100);
		}else{
			holder.tagImg.setVisibility(View.GONE);
		}
		return arg1;
	}

	private class ViewHolder{
		ImageView imgView;
		TextView titleTv;
		TextView progressTv;
		TextView addListBtn;
		ProgressBar progressBar;
		ImageView tagImg;
	}
	/**
	 * 封装了一个向数据源中添加数据的方法
	 * @param list
	 */
	public void addDatas(List<GoodsItem> list) {
		this.list.addAll(list);
		notifyDataSetChanged();
	}
	public void clearDatas() {
		this.list.clear();
		notifyDataSetChanged();
	}
	/**
	 * 执行加入清单操作
	 * @param period
	 * @param times
	 */
	private void addToList(int position){
		List<GoodsItem> goodsList = new ArrayList<GoodsItem>();
		goodsList.add(getItem(position));
		mAddToCarHandler.addToList(goodsList);
	}
	private CarAnimation carAnimation;
	private void startAnimation(ImageView img) {
		if(carAnimation == null){
			carAnimation = new CarAnimation();
		}
		carAnimation.startAnimation((Activity)context, img, ((Activity)context).findViewById(R.id.layout_car));
	}
}
