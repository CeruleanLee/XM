package com.bwf.xmduobao.ui.activity;

import java.util.LinkedList;

import com.bwf.xmduobao.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

@SuppressLint("NewApi")
public class SearchActivity extends Activity implements OnClickListener{
	@ViewInject(R.id.et_search)
	private EditText mSearchEt;
	@ViewInject(R.id.img_delete)
	private View mDeleteImg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		preferences = getSharedPreferences("app", MODE_PRIVATE);
		initViews();
		
		String searchKeysJson = preferences.getString("search_keys", null);
		if(searchKeysJson != null){
			mSearchKeys = new Gson().fromJson(searchKeysJson, new TypeToken<LinkedList<String>>(){}.getType()); 
		}else{
			mSearchKeys = new LinkedList<String>();
		}
		Toast.makeText(this, mSearchKeys.toString(), 0).show();
	}
	private void initViews() {
		ViewUtils.inject(this);
		//绑定输入法按键的监听
		mSearchEt.setOnEditorActionListener(onEditorActionListener);
		//绑定物理按键的监听
		mSearchEt.setOnKeyListener(onKeyListener);
		//监听EditText的文本变化
		mSearchEt.addTextChangedListener(textWatcher);
		mDeleteImg.setOnClickListener(this);
	}
	private TextWatcher textWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if(TextUtils.isEmpty(s)){
				mDeleteImg.setVisibility(View.GONE);
			}else{
				mDeleteImg.setVisibility(View.VISIBLE);
			}
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
		}
		@Override
		public void afterTextChanged(Editable s) {
			
		}
	};
	private OnKeyListener onKeyListener = new OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
				clickSearch();
			}
			return false;
		}
	};
	private OnEditorActionListener onEditorActionListener = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if(actionId == EditorInfo.IME_ACTION_SEARCH){
				clickSearch();
			}
			return false;
		}
	};
	private SharedPreferences preferences;
	private LinkedList<String> mSearchKeys;
	@SuppressLint("NewApi")
	protected void clickSearch() {
		String key = mSearchEt.getText().toString();
		mSearchKeys.add(0,key);
		preferences.edit().putString("search_keys", new Gson().toJson(mSearchKeys)).commit();
	}
	@Override
	public void onClick(View v) {
		if(v == mDeleteImg){
			mSearchEt.getText().clear();
		}
	}
}
