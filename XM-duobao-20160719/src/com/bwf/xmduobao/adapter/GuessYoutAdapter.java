package com.bwf.xmduobao.adapter;

import java.util.List;

import com.bwf.xmduobao.R;
import com.bwf.xmduobao.entity.GoodsItem;
import com.bwf.xmduobao.utils.MBitmapHolder;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GuessYoutAdapter extends RecyclerView.Adapter<ViewHolder>{
	private List<GoodsItem> list;
	private BitmapUtils mBitmapUtils;
	public GuessYoutAdapter(Context context,List<GoodsItem> list) {
		super();
		this.list = list;
		mBitmapUtils = MBitmapHolder.getBitmapUtils(context);
	}

	@Override
	public int getItemCount() {
		return list.size();
	}
	public GoodsItem getItem(int position) {
		return list.get(position);
	}
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		MyViewHolder mHolder = (MyViewHolder) holder;
		GoodsItem item = getItem(position);
		mBitmapUtils.display(mHolder.goodsImg, item.getImg());
		mHolder.goodsTitle.setText(item.getTitle());
		mHolder.pb.setMax(item.getTotalTimes());
		mHolder.pb.setProgress(item.getTotalTimes()-item.getRemainTimes());
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
		View v = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.item_guess_you_like, null);
		return new MyViewHolder(v);
	}
	
	private static class  MyViewHolder extends RecyclerView.ViewHolder{

		public ImageView goodsImg;
		public TextView goodsTitle;
		public ProgressBar pb;
		public MyViewHolder(View itemView) {
			super(itemView);
			goodsImg = (ImageView) itemView.findViewById(R.id.goodsImg);
			goodsTitle = (TextView) itemView.findViewById(R.id.tvTitle);
			pb = (ProgressBar) itemView.findViewById(R.id.progressBar);
		}
	}
}
