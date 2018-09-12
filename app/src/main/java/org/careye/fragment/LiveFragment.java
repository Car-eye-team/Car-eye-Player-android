/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */
package org.careye.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.careye.rtmp.careyeplayer.R;

import org.careye.widgets.MediaView;

import static android.widget.Toast.LENGTH_LONG;

public class LiveFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "LiveFragment";

    private View mRoot;
    private EditText mEtLiveUrl;

    private MediaView mCurrPlayer;
    private MediaView mMvPlayer1;
    private MediaView mMvPlayer2;
    private MediaView mMvPlayer3;
    private MediaView mMvPlayer4;

    private ImageView mIvPlay;
    private ImageView mIvVoice;
    private ImageView mIvVideo;
    private ImageView mIvRec;

    private String URL = "rtmp://live.hkstv.hk.lxdns.com:1935/live/hks";
    //    private String URL = "rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov";
//    private String URL = "rtmp://www.car-eye.cn:10077/live/15922222222&channel=4";

    private OnFragmentInteractionListener mListener;
    public LiveFragment() {
    }

    public static LiveFragment newInstance() {
        LiveFragment fragment = new LiveFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        mEtLiveUrl = mRoot.findViewById(R.id.et_live_url);
        mEtLiveUrl.setText(URL);

        mIvPlay = mRoot.findViewById(R.id.iv_play);
        mIvVoice = mRoot.findViewById(R.id.iv_voice);
        mIvVideo = mRoot.findViewById(R.id.iv_video);
        mIvRec = mRoot.findViewById(R.id.iv_rec);

        mMvPlayer1 = mRoot.findViewById(R.id.mv_player_1);
        mMvPlayer2 = mRoot.findViewById(R.id.mv_player_2);
        mMvPlayer3 = mRoot.findViewById(R.id.mv_player_3);
        mMvPlayer4 = mRoot.findViewById(R.id.mv_player_4);

        mMvPlayer1.setSelect(true);
        mCurrPlayer = mMvPlayer1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.fragment_live, container, false);

        initView();
        initListener();
        return mRoot;
    }

    private void initListener() {

        mIvPlay.setOnClickListener(this);
        mIvVoice.setOnClickListener(this);
        mIvVideo.setOnClickListener(this);
        mIvRec.setOnClickListener(this);

        mMvPlayer1.setOnClickListener(this);
        mMvPlayer2.setOnClickListener(this);
        mMvPlayer3.setOnClickListener(this);
        mMvPlayer4.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
        mListener = null;
        release();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.mv_player_1) {
            switchSelect(mMvPlayer1);
        } else if (id == R.id.mv_player_2) {
            switchSelect(mMvPlayer2);
        } else if (id == R.id.mv_player_3) {
            switchSelect(mMvPlayer3);
        } else if (id == R.id.mv_player_4) {
            switchSelect(mMvPlayer4);
        } else if (id == R.id.iv_play) {
            play(mCurrPlayer);
        } else if (id == R.id.iv_voice) {
            setMuteEnable(mCurrPlayer);
        } else if (id == R.id.iv_video) {
            setVideoEnable(mCurrPlayer);
        } else if (id == R.id.iv_rec) {
            setRecEnable(mCurrPlayer);
        }
    }

    /**
     * 设置Rec使能
     * @param mCurrPlayer   当前活动窗口
     */
    private void setRecEnable(MediaView mCurrPlayer) {
        boolean isRec = mCurrPlayer.getRecState();
        if (isRec) {
            mCurrPlayer.enableRec(false, "", "");
            Toast.makeText(getActivity(), "录制停止！", LENGTH_LONG).show();
        } else {
            String filePath= Environment.getExternalStorageDirectory().getAbsolutePath() + "/careye/video/";
            String fileName =  SystemClock.uptimeMillis() + ".mp4";
            mCurrPlayer.enableRec(true, filePath, fileName);
            Toast.makeText(getActivity(), "录制开始！", LENGTH_LONG).show();
        }
        updateRecView(!isRec);
    }

    /**
     * 设置视频使能
     * @param mCurrPlayer   当前活动窗口
     */
    private void setVideoEnable(MediaView mCurrPlayer) {
        boolean isShow = mCurrPlayer.getShowVideoState();
        mCurrPlayer.enableVideo(!isShow);
        updateVideoView(!isShow);
    }

    /**
     * 设置静音使能
     * @param mCurrPlayer   当前活动窗口
     */
    private void setMuteEnable(MediaView mCurrPlayer) {
        boolean isMute = mCurrPlayer.getMuteState();
        mCurrPlayer.enableVolume(!isMute);
        updateMuteView(!isMute);
    }

    private void release() {

        mMvPlayer1.stop();
        mMvPlayer2.stop();
        mMvPlayer3.stop();
        mMvPlayer4.stop();
    }

    private void play(MediaView player) {

        String url = mEtLiveUrl.getText().toString().trim();
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(getActivity(), "播放地址不能为空!", LENGTH_LONG);
            return;
        }

        boolean isPlaying = player.isPlaying();
        if (isPlaying) {
            player.stop();
        } else {
            player.stop();
            player.play(url);
        }
        updatePlayView(!isPlaying);

        //音量状态
        updateMuteView(player.getMuteState());
        //视频显示状态
        updateVideoView(player.getShowVideoState());
        //更新录制状态
        updateRecView(player.getRecState());
    }

    /**
     * 切换播放窗口对应的功能区状态
     * @param mMvPlayer     切换窗口
     */
    private void switchSelect(MediaView mMvPlayer) {
        mCurrPlayer.setSelect(false);
        mCurrPlayer = mMvPlayer;
        mCurrPlayer.setSelect(true);

        //播放状态
        updatePlayView(mCurrPlayer.isPlaying());
        //音量状态
        updateMuteView(mCurrPlayer.getMuteState());
        //视频显示状态
        updateVideoView(mCurrPlayer.getShowVideoState());
        //更新录制状态
        updateRecView(mCurrPlayer.getRecState());
    }

    public void updatePlayView(boolean isPlaying) {
        if (isPlaying) {
            mIvPlay.setImageResource(R.drawable.ic_pause);
        } else {
            mIvPlay.setImageResource(R.drawable.ic_play);
        }
    }

    private void updateVideoView(boolean isVideo) {
        if (isVideo) {
            mIvVideo.setImageResource(R.drawable.ic_video_show);
        } else {
            mIvVideo.setImageResource(R.drawable.ic_video_hide);
        }
    }

    private void updateMuteView(boolean isMute) {
        if (isMute) {
            mIvVoice.setImageResource(R.drawable.ic_no_voice);
        } else {
            mIvVoice.setImageResource(R.drawable.ic_voice);
        }
    }

    private void updateRecView(boolean isRec) {
        if (isRec) {
            mIvRec.setImageResource(R.drawable.ic_recording);
        } else {
            mIvRec.setImageResource(R.drawable.ic_record);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
