/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */
package org.careye.player;

import android.content.Context;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Map;

public class PlayerView extends RelativeLayout {
    private OnSizeChangedListener mListener = null;
    TextView textView;
    Context mContext;

    public PlayerView(Context context) {
        super(context);
        mContext = context;
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mListener != null) {
            mListener.onSizeChanged(w, h, oldw, oldh);
        }
    }

    public void addText(Context mContext, int x, int y, int clore, String str) {
        textView = new TextView(mContext);
        textView.setText(str);
        textView.setTextColor(clore);
        this.addView(textView);
        textView.setTranslationX(x);
        textView.setTranslationY(y);
    }

    public interface OnSizeChangedListener {
        public void onSizeChanged(int w, int h, int oldw, int oldh);
    }

    public void setOnSizeChangedListener(OnSizeChangedListener l) {
        mListener = l;
    }
}

