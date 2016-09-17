package com.bwf.xmduobao.utils;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

public class SpannableStringBuild {

	public static SpannableString build(CharSequence source,int color, Object text){
		SpannableString spannableString = new SpannableString(source);
		ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
		int start = source.toString().indexOf(text.toString());
		spannableString.setSpan(colorSpan, start, start+text.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannableString;
	}
	public static SpannableString buildStyle(CharSequence source,int style, Object text){
		SpannableString spannableString = new SpannableString(source);
		StyleSpan styleSpan = new StyleSpan(style);
		int start = source.toString().indexOf(text.toString());
		spannableString.setSpan(styleSpan, start, start+text.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannableString;
	}
	
}
