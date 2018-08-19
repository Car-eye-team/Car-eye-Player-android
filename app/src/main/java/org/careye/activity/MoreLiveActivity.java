/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */
package org.careye.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.careye.rtmp.careyeplayer.R;

import org.careye.player.media.EyeVideoView;
import org.careye.util.PicUtils;

public class MoreLiveActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "DemoActivity";

    private String       mURL       = "rtmp://live.hkstv.hk.lxdns.com/live/hks";

    private EyeVideoView mVideoPlayer1;
    private EyeVideoView mVideoPlayer2;
    private EyeVideoView mVideoPlayer3;
    private EyeVideoView mVideoPlayer4;

    private Button mBtnMorePlay;
    private Button mBtnMoreStop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        initView();
        initListener();
    }

    private void initListener() {
        mBtnMorePlay.setOnClickListener(this);
        mBtnMoreStop.setOnClickListener(this);
    }

    private void initView() {
        mVideoPlayer1 = findViewById(R.id.video_player1);
        mVideoPlayer2 = findViewById(R.id.video_player2);
        mVideoPlayer3 = findViewById(R.id.video_player3);
        mVideoPlayer4 = findViewById(R.id.video_player4);

        mBtnMoreStop = findViewById(R.id.btn_more_stop);
        mBtnMorePlay = findViewById(R.id.btn_more_play);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        play();
    }

    @Override
    public void onPause() {
        super.onPause();
        stop();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private void play() {

        stop();

        mVideoPlayer1.setVideoPath(mURL);
        mVideoPlayer1.start();

        mVideoPlayer2.setVideoPath(mURL);
        mVideoPlayer2.start();

        mVideoPlayer3.setVideoPath(mURL);
        mVideoPlayer3.start();

        mVideoPlayer4.setVideoPath(mURL);
        mVideoPlayer4.start();
    }

    public void stop() {
        mVideoPlayer1.stopPlayback();
        mVideoPlayer1.release(true);
        mVideoPlayer1.stopBackgroundPlay();

        mVideoPlayer2.stopPlayback();
        mVideoPlayer2.release(true);
        mVideoPlayer2.stopBackgroundPlay();

        mVideoPlayer3.stopPlayback();
        mVideoPlayer3.release(true);
        mVideoPlayer3.stopBackgroundPlay();

        mVideoPlayer4.stopPlayback();
        mVideoPlayer4.release(true);
        mVideoPlayer4.stopBackgroundPlay();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_more_play) {
            play();
        } else if (id == R.id.btn_more_stop) {
            stop();
        }
    }
}

