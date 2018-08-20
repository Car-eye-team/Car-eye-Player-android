/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */
package org.careye.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
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

public class DemoActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "DemoActivity";

    private String       mURL       = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
//    private String mURL = "rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov";

    private EditText mEtInputUrl;
    private EyeVideoView mVideoPlayer1;
    private Button mBtnPlay;
    private Button mBtnStop;
    private Button mBtnPic;
    private Button mBtnEnableVolume;

    private String picName = "careye_";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        Intent intent = getIntent();
        String action = intent.getAction();
        if (intent.ACTION_VIEW.equals(action)) {
            Uri    uri    = (Uri) intent.getData();
            String scheme = uri.getScheme();
            if (scheme.equals("file")) {
                mURL = uri.getPath();
            } else if (  scheme.equals("http" )
                      || scheme.equals("https")
                      || scheme.equals("rtsp" )
                      || scheme.equals("rtmp" ) ) {
                mURL = uri.toString();
            } else if (scheme.equals("content")) {
                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor cursor = managedQuery(uri, proj, null, null, null);
                int    colidx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                mURL = cursor.getString(colidx);
            }
        }

        initView();
        initListener();
    }

    private void initListener() {
        mBtnStop.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);
        mBtnPic.setOnClickListener(this);
        mBtnEnableVolume.setOnClickListener(this);
    }

    private void initView() {
        mVideoPlayer1 = findViewById(R.id.video_player1);
        mBtnPlay = findViewById(R.id.btn_play);
        mBtnStop = findViewById(R.id.btn_stop);
        mEtInputUrl = findViewById(R.id.et_input_url);
        mBtnPic = findViewById(R.id.btn_pic);
        mBtnEnableVolume = findViewById(R.id.btn_enable_volume);
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
    }

    public void stop() {
        mVideoPlayer1.stopPlayback();
        mVideoPlayer1.release(true);
        mVideoPlayer1.stopBackgroundPlay();
    }

    private void testPlayerPlay(boolean play) {
    }

    private void showUIControls(boolean show, boolean autohide) {
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_stop) {
            stop();
        } else if (id == R.id.btn_play){
            String url = mEtInputUrl.getText().toString().trim();
            if (TextUtils.isEmpty(url)) {
                Toast.makeText(this, "请输入URL地址", Toast.LENGTH_SHORT).show();
            }
            mURL = url;
            play();
        } else if (id == R.id.btn_pic) {
            //抓拍
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (mVideoPlayer1 != null) {
                        Bitmap bitmap = mVideoPlayer1.getPic();
                        final boolean []result = new boolean[1];
                        final String []path = new String[1];
                        if (bitmap != null) {
                            String filaName = picName + SystemClock.uptimeMillis() + ".jpg";
                            path[0] = PicUtils.saveImage(bitmap, filaName);
                            if (TextUtils.isEmpty(path[0])) {
                                result[0] = false;
                            } else {
                                result[0] = true;
                            }
                        } else {
                            result[0] = false;
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result[0]) {
                                    Toast.makeText(DemoActivity.this, path[0], Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(DemoActivity.this, "抓拍失败", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }
            }).start();
        } else if(id == R.id.btn_enable_volume) {
            //静音
            if ("静音关".equals(mBtnEnableVolume.getText().toString())) {
                mVideoPlayer1.enableVolume(true);
                mBtnEnableVolume.setText("静音开");
            } else{
                mVideoPlayer1.enableVolume(false);
                mBtnEnableVolume.setText("静音关");
            }
        }

    }
}

