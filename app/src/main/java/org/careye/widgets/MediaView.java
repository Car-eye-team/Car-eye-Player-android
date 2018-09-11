/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */
package org.careye.widgets;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.careye.rtmp.careyeplayer.R;

import org.careye.player.media.EyeVideoView;

public class MediaView extends RelativeLayout implements View.OnClickListener {

    private RelativeLayout mRlMediaEdge;
    private EyeVideoView mEvvMedia;

    public MediaView(Context context) {
        super(context);
        init(context);
    }

    public MediaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MediaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MediaView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_video_view, this);

        initView();
        initListener();
    }

    private void initListener() {
//        mRlMediaEdge.setOnClickListener(this);
    }

    private void initView() {
        mRlMediaEdge = findViewById(R.id.rl_media_edge);
        mEvvMedia = findViewById(R.id.evv_media);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
    }

    public void setSelect(boolean isSelect) {
        if (isSelect) {
            mRlMediaEdge.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            mRlMediaEdge.setBackgroundColor(getResources().getColor(R.color.color_no_select));
        }
    }

    public boolean isVoice() {
        return false;
    }

    public boolean isPlaying() {
        return mEvvMedia.isPlaying();
    }

    public boolean getMuteState() {
        return mEvvMedia.getMuteState();
    }

    public boolean getShowVideoState(){
        return mEvvMedia.getShowVideoState();
    }

    public void enableVolume(boolean isMute) {
        mEvvMedia.enableVolume(isMute);
    }

    public void enableVideo(boolean isVideo) {
        mEvvMedia.enableVideo(isVideo);
    }

    public void stop() {
        mEvvMedia.stopPlayback();
        mEvvMedia.release(true);
        mEvvMedia.stopBackgroundPlay();
    }

    public void play(String url) {
        mEvvMedia.setVideoPath(url);
        mEvvMedia.start();
    }
}
