package com.bwf.xmduobao.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

/**
 * drawableLeft与文本一起居中显示
 * 
 * @author 农民伯伯
 * @see http://www.cnblogs.com/over140/p/3464348.html
 * 
 */
public class DrawableCenterTextView extends CheckedTextView {

    public DrawableCenterTextView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }

    public DrawableCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableCenterTextView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
        	//获取图片数组（左上右下）
            Drawable drawableRight = drawables[2];
            if (drawableRight != null) {
            	//计算TextView宽度
                float textWidth = getPaint().measureText(getText().toString());
                //获取设置的图片与文字的间距
                int drawablePadding = getCompoundDrawablePadding();
                //获取图片的宽度
               int drawableWidth = drawableRight.getIntrinsicWidth();
                //图片+间距+文字 的总宽度
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                int paddingRight = getPaddingRight();
                canvas.translate(-(getWidth() - bodyWidth) / 2+paddingRight, 0);
            }
        }
        super.onDraw(canvas);
    }
}