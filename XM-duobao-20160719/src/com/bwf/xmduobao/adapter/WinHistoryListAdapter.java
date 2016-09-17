package com.bwf.xmduobao.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import com.bwf.xmduobao.R;
import com.bwf.xmduobao.entity.ResponseWinHistory.LuckUser;
import com.bwf.xmduobao.entity.ResponseWinHistory.WinHistoryItem;
import com.bwf.xmduobao.utils.BitmapHandler;
import com.bwf.xmduobao.utils.MBitmapHolder;
import com.bwf.xmduobao.utils.SpannableStringBuild;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WinHistoryListAdapter extends BaseAdapter{
	private List<WinHistoryItem> list;
	private LayoutInflater inflater;
	private BitmapUtils bitmapUtils;
	private Context context;
	public WinHistoryListAdapter(Context context,List<WinHistoryItem> list) {
		super();
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		bitmapUtils = MBitmapHolder.getBitmapUtils(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public WinHistoryItem getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_win_history, null);
			holder.mPeriodInfoRevealedTv = (TextView) convertView.findViewById(R.id.tv_period_info_revealed);
			holder.mPeriodInfoIngTv = (TextView) convertView.findViewById(R.id.tv_period_info_ing);
			holder.mAvatarimg = (ImageView) convertView.findViewById(R.id.img_avatar);
			holder.mLuckNicknameTv = (TextView) convertView.findViewById(R.id.tv_revealed_onName);
			holder.mAddressAndIP = (TextView) convertView.findViewById(R.id.tv_revealed_onLocalAndIp);
			holder.mUidTv = (TextView) convertView.findViewById(R.id.tv_revealed_onUid);
			holder.mLuckcode = (TextView) convertView.findViewById(R.id.tv_luckcode);
			holder.mLuckTimes = (TextView) convertView.findViewById(R.id.tv_luckTimes);
			holder.mWinLayout = convertView.findViewById(R.id.layout_win);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		WinHistoryItem winHistoryItem = list.get(position);
		LuckUser luckUser = winHistoryItem.luckUser;
		if(winHistoryItem.status == 1){
			//正在揭晓
			holder.mPeriodInfoIngTv.setVisibility(View.VISIBLE);
			holder.mPeriodInfoIngTv.setText("期号："+winHistoryItem.period+"  即将揭晓，正在倒计时...");
			holder.mWinLayout.setVisibility(View.GONE);
		}else{
			holder.mWinLayout.setVisibility(View.VISIBLE);
			holder.mPeriodInfoIngTv.setVisibility(View.GONE);
			//已经揭晓
			bitmapUtils.display(holder.mAvatarimg, luckUser.avatar,new BitmapLoadCallBack<ImageView>() {
				
				@Override
				public void onLoadCompleted(ImageView arg0, String arg1, Bitmap arg2, BitmapDisplayConfig arg3,
						BitmapLoadFrom arg4) {
					Bitmap bitmap = BitmapHandler.createRoundBitmap(arg2);
					arg0.setImageBitmap(bitmap);
				}

				@Override
				public void onLoadFailed(ImageView arg0, String arg1, Drawable arg2) {
					
				}
			});
			String luckNickName = String.format(context.getString(R.string.luck_nickname), luckUser.nickname);
			SpannableString luckNickNameSpannableString = SpannableStringBuild.build(luckNickName, 0xff436EEE, luckUser.nickname);
			holder.mLuckNicknameTv.setText(luckNickNameSpannableString);
			holder.mAddressAndIP.setText("( "+luckUser.IPAddress+" IP:"+luckUser.IP+" )");
			holder.mUidTv.setText(String.format(context.getString(R.string.luck_uid), luckUser.uid));
			
			String luckCodeText = String.format(context.getString(R.string.luck_code), winHistoryItem.luckCode);
			SpannableString luckCodeSpannableString = SpannableStringBuild.build(luckCodeText, 0xffff0000, winHistoryItem.luckCode);
			holder.mLuckcode.setText(luckCodeSpannableString);
			
			String luckTimesText = String.format(context.getString(R.string.luck_times), luckUser.duobaoTimes);
			SpannableString luckTimesSpannableString = SpannableStringBuild.build(luckTimesText, 0xffff0000, luckUser.duobaoTimes);
			holder.mLuckTimes.setText(luckTimesSpannableString);
			holder.mPeriodInfoRevealedTv.setText(String.format(context.getString(R.string.period_infor_evealed), winHistoryItem.period+"",sdf.format(Long.parseLong(winHistoryItem.revealedTime))));
		}
		return convertView;
	}
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

	private static class ViewHolder{
		TextView mPeriodInfoRevealedTv;
		TextView mPeriodInfoIngTv;
		ImageView mAvatarimg;
		TextView mLuckNicknameTv;
		TextView mAddressAndIP;
		TextView mUidTv;
		TextView mLuckcode;
		TextView mLuckTimes;
		View mWinLayout;
	}
	
}
