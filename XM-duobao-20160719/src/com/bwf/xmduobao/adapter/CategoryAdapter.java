package com.bwf.xmduobao.adapter;

import java.util.List;

import com.bwf.xmduobao.R;
import com.bwf.xmduobao.entity.Category;
import com.bwf.xmduobao.utils.MBitmapHolder;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryAdapter extends BaseAdapter{
	private List<Category> list;
	private LayoutInflater inflater;
	private BitmapUtils bitmapUtils;
	public CategoryAdapter(List<Category> list, Context context) {
		super();
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		bitmapUtils = MBitmapHolder.getBitmapUtils(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Category getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_category, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.categoryImg);
			holder.title = (TextView) convertView.findViewById(R.id.categoryTitle);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Category category = getItem(position);
		holder.title.setText(category.getCategoryName());
		bitmapUtils.display(holder.img, category.getImgUrl());
		return convertView;
	}

	private class ViewHolder{
		ImageView img;
		TextView title;
	}
}
