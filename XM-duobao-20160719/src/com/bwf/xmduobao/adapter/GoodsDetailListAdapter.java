package com.bwf.xmduobao.adapter;

import java.util.HashMap;
import java.util.List;

import com.bwf.xmduobao.R;
import com.bwf.xmduobao.utils.MBitmapHolder;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class GoodsDetailListAdapter extends BaseAdapter{
	private List<String> list;
	private LayoutInflater inflater;
	private BitmapUtils bitmapUtils;
	public GoodsDetailListAdapter(Context context,List<String> list) {
		super();
		this.list = list;
		inflater = LayoutInflater.from(context);
		bitmapUtils = MBitmapHolder.getBitmapUtils(context);
		bitmapUtils.configThreadPoolSize(1);
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public String getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_goods_detail, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(infos.containsKey(position)){
			LayoutParams params = (LayoutParams) holder.img.getLayoutParams();
			BitmapInfo info = infos.get(position);
			params.height = holder.img.getWidth()*info.height/info.width;
			holder.img.setLayoutParams(params);
		}
		bitmapUtils.display(holder.img, getItem(position),new BitmapLoadCallBack<ImageView>() {

			@Override
			public void onLoadCompleted(ImageView arg0, String arg1, Bitmap arg2, BitmapDisplayConfig arg3,
					BitmapLoadFrom arg4) {
				if(!infos.containsKey(position)){
					infos.put(position, new BitmapInfo(arg2.getWidth(), arg2.getHeight()));
					//计算 修改ImageView的高
					LayoutParams params = (LayoutParams) arg0.getLayoutParams();
					BitmapInfo info = infos.get(position);
					params.height = arg0.getWidth()*info.height/info.width;
					arg0.setLayoutParams(params);
				}
				arg0.setImageBitmap(arg2);
			}
			@Override
			public void onLoadFailed(ImageView arg0, String arg1, Drawable arg2) {
				
			}
		});
		return convertView;
	}
	
	private static class ViewHolder{
		ImageView img;
	}
	private HashMap<Integer,BitmapInfo> infos = new HashMap<Integer,BitmapInfo>();
	private class BitmapInfo{
		int width;
		int height;
		public BitmapInfo(int width, int height) {
			super();
			this.width = width;
			this.height = height;
		}
	}
}
