package com.bwf.xmduobao.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import com.bwf.xmduobao.R;
import com.bwf.xmduobao.entity.Record;
import com.bwf.xmduobao.entity.Record.User;
import com.bwf.xmduobao.utils.BitmapHandler;
import com.bwf.xmduobao.utils.MBitmapHolder;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordListAdapter extends BaseAdapter{
	private List<Record> list;
	private LayoutInflater inflater;
	private BitmapUtils bitmapUtils;
	private SimpleDateFormat sdf;
	public RecordListAdapter(Context context,List<Record> list) {
		super();
		this.list = list;
		inflater = LayoutInflater.from(context);
		bitmapUtils = MBitmapHolder.getBitmapUtils(context);
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Record getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder  holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_record,null);
			holder = new ViewHolder();
			holder.ipTv = (TextView) convertView.findViewById(R.id.tv_ip);
			holder.nickNameTv = (TextView) convertView.findViewById(R.id.tv_nickname);
			holder.joininfoTv = (TextView) convertView.findViewById(R.id.tv_joininfo);
			holder.joinTimeTv = (TextView) convertView.findViewById(R.id.tv_jointime);
			holder.userImg = (ImageView) convertView.findViewById(R.id.img_user);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Record record = getItem(position);
		User user = record.getUser();
		holder.nickNameTv.setText(user.getNickName());
		holder.ipTv.setText("("+user.getIPAddress()+" "+user.getIP()+")");
		String s = "参与了"+record.getTimes()+"人次";
		SpannableString spannableString = new SpannableString(s);
		spannableString.setSpan(new ForegroundColorSpan(0xffff0000), "参与了".length(),
				("参与了"+record.getTimes()).length(), 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder.joininfoTv.setText(spannableString);
		holder.joinTimeTv.setText(sdf.format(Long.parseLong(record.getTime())));
		bitmapUtils.display(holder.userImg, user.getAvatar(), new BitmapLoadCallBack<ImageView>() {
			
			@Override
			public void onLoadCompleted(ImageView arg0, String arg1, Bitmap arg2, BitmapDisplayConfig arg3,
					BitmapLoadFrom arg4) {
				Bitmap bitmap = BitmapHandler.createCircleBitmap(arg2);
				arg0.setImageBitmap(bitmap);
			}

			@Override
			public void onLoadFailed(ImageView arg0, String arg1, Drawable arg2) {
				
			}
		});
		return convertView;
	}
	private class ViewHolder{
		ImageView userImg;
		TextView nickNameTv;
		TextView ipTv;
		TextView joininfoTv;
		TextView joinTimeTv;
	}

	public void addDatas(List<Record> list) {
		this.list.addAll(list);
		notifyDataSetChanged();
	}
}
