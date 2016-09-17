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

public class CategoryGoodsListAdapter extends BaseAdapter{
	private List<GoodsItem> list;
	private LayoutInflater inflater;
	private BitmapUtils bitmapUtils;
	private AddToCarHandler mAddToCarHandler;
	public CategoryGoodsListAdapter(Context context,List<GoodsItem> list) {
		super();
		this.list = list;
		inflater = LayoutInflater.from(context);
		bitmapUtils = MBitmapHolder.getBitmapUtils(context);
		this.context = context;
		mAddToCarHandler = new AddToCarHandler(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public GoodsItem getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder ;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_category_goods_list,null);
			holder = new ViewHolder();
			holder.addListBtn = convertView.findViewById(R.id.btnAddList);
			holder.goodsImg = (ImageView) convertView.findViewById(R.id.goodsImg);
			holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
			holder.remainTimesTv = (TextView) convertView.findViewById(R.id.remainTimesTv);
			holder.titleTv = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tagImg = (ImageView) convertView.findViewById(R.id.tagImg);
			holder.totalTimesTv = (TextView) convertView.findViewById(R.id.totalTimesTv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		GoodsItem item = getItem(position);
		holder.titleTv.setText(item.getTitle());
				int progress = 100*(item.getTotalTimes()-item.getRemainTimes())/item.getTotalTimes();
//		int progress = (int) (Math.random()*100);
		holder.progressBar.setProgress(progress);
		holder.totalTimesTv.setText(item.getTotalTimes()+"");
		holder.remainTimesTv.setText(item.getRemainTimes()+"");
		bitmapUtils.display(holder.goodsImg, item.getImg());
		holder.addListBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startAnimation(holder.goodsImg);
				//加入清单 
				addToList(position);
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
		return convertView;
	}

	private static class ViewHolder{
		ImageView goodsImg;
		TextView titleTv;
		ProgressBar progressBar;
		TextView totalTimesTv;
		TextView remainTimesTv;
		View addListBtn;
		ImageView tagImg;
	}
	/**
	 * 执行加入清单操作
	 * @param period
	 * @param times
	 */
	private void addToList(int position){
		List<GoodsItem> goodsList = new ArrayList<GoodsItem>();
		goodsList.add(getItem(position));
		mAddToCarHandler.addToList( goodsList);
	}
	private CarAnimation carAnimation;
	private void startAnimation(ImageView img) {
		if(carAnimation == null){
			carAnimation = new CarAnimation();
		}
		carAnimation.startAnimation((Activity)context, img, ((Activity)context).findViewById(R.id.shoppingCartBtn));
	}
	private Context context;
}
