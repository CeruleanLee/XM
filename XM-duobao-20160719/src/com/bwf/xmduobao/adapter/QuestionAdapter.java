package com.bwf.xmduobao.adapter;

import java.util.List;

import com.bwf.xmduobao.R;
import com.bwf.xmduobao.entity.Question;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QuestionAdapter extends BaseAdapter{
	private List<Question> list;
	private LayoutInflater inflater;
	public QuestionAdapter(List<Question> list, Context context) {
		super();
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Question getItem(int position) {
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
			convertView = inflater.inflate(R.layout.item_question_list,null);
			holder = new ViewHolder();
			holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
			holder.contentTv = (TextView) convertView.findViewById(R.id.contentTv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Question question = getItem(position);
		holder.titleTv.setText(position+1+"."+question.getTitle());
		if(showIndex == position){
			holder.contentTv.setText(question.getContent());
			holder.contentTv.setVisibility(View.VISIBLE);
		}else{
			holder.contentTv.setVisibility(View.GONE);
		}
		return convertView;
	}

	private static class ViewHolder{
		TextView titleTv;
		TextView contentTv;
	} 
	private int showIndex = -1;
	public void pressItem(int position) {
		if(position == showIndex){
			showIndex = -1;
		}else{
			showIndex = position;
		}
		notifyDataSetChanged();
	}
}
