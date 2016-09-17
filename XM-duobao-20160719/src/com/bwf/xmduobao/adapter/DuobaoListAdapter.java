package com.bwf.xmduobao.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bwf.xmduobao.Constants;
import com.bwf.xmduobao.R;
import com.bwf.xmduobao.db.DuobaoListHandler;
import com.bwf.xmduobao.entity.GoodsItem;
import com.bwf.xmduobao.ui.view.OneListenerEditText;
import com.bwf.xmduobao.utils.MBitmapHolder;
import com.bwf.xmduobao.utils.MHttpHolder;
import com.bwf.xmduobao.utils.UserInfoHandler;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DuobaoListAdapter extends BaseAdapter{
	private List<GoodsItem> list;
	private LayoutInflater layoutInflater;
	private BitmapUtils mBitmapUtils;
	private Context context;
	private DuobaoListHandler duobaoListHandler;
	private HttpUtils mHttpUtils;
	private Gson gson;
	public DuobaoListAdapter(List<GoodsItem> list,Context context) {
		super();
		this.list = list;
		mBitmapUtils = MBitmapHolder.getBitmapUtils(context);
		if(this.list == null){
			this.list = new ArrayList<GoodsItem>();
		}
		this.layoutInflater = LayoutInflater.from(context);
		this.context = context;
		duobaoListHandler = new DuobaoListHandler(context);
		mHttpUtils = MHttpHolder.getHttpUtils();
		mHttpUtils.configRequestThreadPoolSize(1);
		gson = new Gson();
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
		final ViewHolder holder;
		final GoodsItem item = getItem(position);
		if(convertView == null){
			convertView  = layoutInflater.inflate(R.layout.item_duobao_list, null);
			holder = new ViewHolder();
			holder.addImg = (ImageView) convertView.findViewById(R.id.img_add);
			holder.goodsImg = (ImageView) convertView.findViewById(R.id.goodsImg);
			holder.minusImg = (ImageView) convertView.findViewById(R.id.img_minus);
			holder.numsInCarEt = (OneListenerEditText) convertView.findViewById(R.id.et_numsInCar);
			holder.tagView = (ImageView) convertView.findViewById(R.id.tagImg);
			holder.timesInfo = (TextView) convertView.findViewById(R.id.tv_timesInfo);
			holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(item.getTimesInCar() == 1){
			holder.minusImg.setEnabled(false);
		}else{
			holder.minusImg.setEnabled(true);
		}
		if(item.getTimesInCar() == item.getRemainTimes()){
			holder.addImg.setEnabled(false);
		}else{
			holder.addImg.setEnabled(true);
		}
		isChange = false;
		setEditText(holder.numsInCarEt,item.getTimesInCar());
		isChange = true;
		mBitmapUtils.display(holder.goodsImg, item.getImg());
		//专区图片
		if(item.getCategory() == 1){
			holder.tagView.setVisibility(View.VISIBLE);
			holder.tagView.setImageResource(R.drawable.ico_area_10);
		}else if(item.getCategory() == 2){
			holder.tagView.setVisibility(View.VISIBLE);
			holder.tagView.setImageResource(R.drawable.ico_area_100);
		}else{
			holder.tagView.setVisibility(View.GONE);
		}
		holder.timesInfo.setText("总需 "+item.getTotalTimes()+" 人次，剩余  "+item.getRemainTimes()+" 人次");
		holder.title.setText(item.getTitle());
		holder.minusImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				minus(position,holder);
			}
		});
		holder.addImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				add(position,holder);
			}
		});

		holder.numsInCarEt.setTextWatcher(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if(!isChange){
					return;
				}
				int times;
				if(TextUtils.isEmpty(s) || (times = Integer.parseInt(s.toString())) <= 1){
					holder.addImg.setEnabled(true);
					holder.minusImg.setEnabled(false);
					isChange = false;
					setEditText(holder.numsInCarEt,1);
					updateData(position, 1);
					isChange = true;
					return;
				}
				if(times >= item.getRemainTimes()){
					holder.addImg.setEnabled(false);
					holder.minusImg.setEnabled(true);
					isChange = false;
					setEditText(holder.numsInCarEt,item.getRemainTimes());
					updateData(position, item.getRemainTimes());
					isChange = true;
					return;
				}
				holder.minusImg.setEnabled(true);
				holder.addImg.setEnabled(true);
				updateData(position, times);
			}
		});
		return convertView;
	}
	/**
	 * 数字发生变化后，更新数据
	 * @param position
	 * @param numNow
	 */
	private void updateData(int position,int numNow){
		GoodsItem goods = getItem(position);
		int numBefore = goods.getTimesInCar();
		goods.setTimesInCar(numNow);
		if(!UserInfoHandler.isLogin(context)){
			duobaoListHandler.update(getItem(position));
		}else{
			//如果已经登入，上传服务器
			String url = Constants.URL_UPDATE_DUOBAO_LIST;
			RequestParams params = new RequestParams();
			params.addBodyParameter("token", UserInfoHandler.getUserInfo(context).token);
			List<Long> periodList = new ArrayList<Long>();
			List<Integer> timesList = new ArrayList<Integer>();
			periodList.add(goods.getPeriod());
			timesList.add(numNow);
			params.addBodyParameter("periods",gson.toJson(periodList));
			params.addBodyParameter("times",gson.toJson(timesList));
			mHttpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {

				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {

				}
			});
		}
		performNumerPickerChange(position, numNow-numBefore);
	}
	/**
	 * 为EditText设置内容（主要是让光标停在内容的末尾）
	 * @param et
	 * @param times
	 */
	private void setEditText(EditText et,int times){
		et.getText().clear();
		et.getText().append(times+"");
	}
	/**
	 * 是否需要执行 EditText设置的监听器TextWatcher中afterTextChanged方法的代码
	 * 如果是在afterTextChanged中改变的内容则 不执行 (执行会出问题)
	 */
	private boolean isChange = true;
	/**
	 * 减少次数
	 * @param position
	 * @param holder
	 */
	protected void minus(int position, ViewHolder holder) {
		addOrminus(position, holder, "minus");
	}
	/**
	 * 追加次数
	 * @param position
	 * @param holder
	 */
	protected void add(int position, ViewHolder holder) {
		addOrminus(position, holder, "add");
	}
	/**
	 * 追加或减少次数 都调用这个方法
	 * @param position
	 * @param holder
	 * @param tag
	 */
	private void addOrminus(int position, ViewHolder holder,String tag){
		GoodsItem item = getItem(position);
		int timesInCar = item.getTimesInCar();
		if("add".equals(tag)){
			timesInCar++;
		}else{
			timesInCar--;
		}
		isChange = true;
		holder.numsInCarEt.setText(timesInCar+"");
		isChange = false;
		startEtAnimation(holder.numsInCarEt);
	}
	//	private 
	private ScaleAnimation anim1;
	private ScaleAnimation anim2;
	private void startEtAnimation(final View view){
		if(anim1 == null){
			anim1 = new ScaleAnimation(1, 2f, 1, 2f, 
					Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			anim1.setDuration(150);
			anim2 = new ScaleAnimation(2, 1f, 2, 1f, 
					Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			anim2.setDuration(150);
			AnimationListener listener = new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					if(animation == anim1){
						//						view.startAnimation(anim2);
					}else{
						view.clearAnimation();
					}
				}
			};
			view.clearAnimation();
			anim1.setAnimationListener(listener);
			anim2.setAnimationListener(listener);
		}
		view.clearAnimation();
		view.startAnimation(anim1);
	}
	private class ViewHolder{
		ImageView tagView;
		ImageView goodsImg;
		TextView title;
		TextView timesInfo;
		ImageView minusImg;
		ImageView addImg;
		OneListenerEditText numsInCarEt;

	}
	public void resetDatas(List<GoodsItem> list){
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();
	}
	public List<GoodsItem> getDatas(){
		return this.list;
	}
	private OnNumerPickerChangeListener listener;
	public void setOnNumerPickerChangeListener(OnNumerPickerChangeListener listener){
		this.listener = listener;
	}
	public interface OnNumerPickerChangeListener{
		void OnNumerPickerChange(int position,int numberAfterChange);
	}
	private void performNumerPickerChange(int position,int numberAfterChange){
		if(listener != null){
			listener.OnNumerPickerChange(position, numberAfterChange);
		}
	}
	/**
	 * 清空数据
	 */
	public void clearDatas() {
		this.list.clear();
		notifyDataSetChanged();
	}
}
