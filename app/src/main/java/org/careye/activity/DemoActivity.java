/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */
package org.careye.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.careye.rtmp.careyeplayer.R;

import org.careye.player.media.EyeVideoView;
import org.careye.util.PicUtils;

import java.io.File;
import java.io.IOException;

public class DemoActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "DemoActivity";

    private String       mURL       = "rtmp://live.hkstv.hk.lxdns.com:1935/live/hks";
//    private String mURL = "rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov";
//    private String mURL = "rtmp://www.car-eye.cn:10077/live/15922222222&channel=4";

    private EditText mEtInputUrl;
    private EyeVideoView mVideoPlayer1;
    private Button mBtnPlay;
    private Button mBtnStop;
    private Button mBtnPic;
    private Button mBtnEnableVolume;
    private Button mBtnEnableVideo;
    private TextView mTvRecState;

    private Button mBtnRecStart;
    private Button mBtnRecStop;

    private String picName = "careye_";

    private boolean mBackPressed;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    private boolean isRecRunning = true;

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

        play();
        mRecStateThread.start();
    }

    private void initListener() {
        mBtnStop.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);
        mBtnPic.setOnClickListener(this);
        mBtnEnableVolume.setOnClickListener(this);
        mBtnEnableVideo.setOnClickListener(this);
        mBtnRecStop.setOnClickListener(this);
        mBtnRecStart.setOnClickListener(this);
    }

    private void initView() {
        mVideoPlayer1 = findViewById(R.id.video_player1);
        mBtnPlay = findViewById(R.id.btn_play);
        mBtnStop = findViewById(R.id.btn_stop);
        mEtInputUrl = findViewById(R.id.et_input_url);
        mBtnPic = findViewById(R.id.btn_pic);
        mBtnEnableVolume = findViewById(R.id.btn_enable_volume);
        mBtnEnableVideo = findViewById(R.id.btn_enable_video);
        mBtnRecStart = findViewById(R.id.btn_rec_start);
        mBtnRecStop = findViewById(R.id.btn_rec_stop);
        mTvRecState = findViewById(R.id.tv_rec_state);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
        isRecRunning = false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBackPressed || !mVideoPlayer1.isBackgroundPlayEnabled()) {
            mVideoPlayer1.stopPlayback();
            mVideoPlayer1.release(true);
            mVideoPlayer1.stopBackgroundPlay();
        } else {
            mVideoPlayer1.enterBackground();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        stop();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

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
        } else if (id == R.id.btn_enable_video) {
            if ("画面关".equals(mBtnEnableVideo.getText().toString())) {
                mVideoPlayer1.enableVideo(false);
                mBtnEnableVideo.setText("画面开");
            } else {
                mVideoPlayer1.enableVideo(true);
                mBtnEnableVideo.setText("画面关");
            }
        } else if (id == R.id.btn_rec_start) {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                boolean ret = checkPermission();
                if (!ret)
                    return;
            }
            startRec();
        } else if (id == R.id.btn_rec_stop) {
            mVideoPlayer1.stopRecord();
        }

    }

    private void startRec() {
        String filePath= Environment.getExternalStorageDirectory().getAbsolutePath() + "/careye/video/";
        String fileName =  SystemClock.uptimeMillis() + ".mp4";

        int result = mVideoPlayer1.startRecord(filePath, fileName);
        if (result != 0) {
            Toast.makeText(this, "录制启动失败 : " + result, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && requestCode == REQUEST_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startRec();
        }
    }


    private boolean checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用此功能！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
            return false;
        } else {
           return true;
        }
    }

    private Thread mRecStateThread = new Thread(new Runnable() {
        @Override
        public void run() {

            while (isRecRunning) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mTvRecState != null && mVideoPlayer1 != null) {
                            boolean state = mVideoPlayer1.getRecState();

                            mTvRecState.setText("Rec : " + state);
                        }
                    }
                });

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

}

