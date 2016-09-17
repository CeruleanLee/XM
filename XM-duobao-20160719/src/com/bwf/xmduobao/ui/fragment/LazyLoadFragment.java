package com.bwf.xmduobao.ui.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

 abstract class LazyLoadFragment extends Fragment{
	protected boolean isPrepared;
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()){
			onVisible();
		}else{
			onInVisible();
		}
	}
	protected  void onVisible(){
		if(!isPrepared){
			lazyLoad();
			isPrepared = true;
		}else{
			awaysLoad();
		}
	}
	protected abstract void lazyLoad();
	protected void awaysLoad(){
		
	}
	protected void onInVisible(){
		
	}
	protected View findViewById(int id) {
		return getView().findViewById(id);
	}
	
}
