package org.careye.libcareyeplayer.player;

import android.view.Surface;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayerKernelIJK extends PlayerKernel
implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnBufferingUpdateListener {
    IjkMediaPlayer mediaPlayer;

    public PlayerKernelIJK(){
    }

    @Override
    public int Play(Object context, String url, Surface surface, int w, int h, PlayerParam params){
        mediaPlayer = new IjkMediaPlayer();
        mPlayerParam = params;
        mUrl = url;

        if (mPlayerParam == null) {
            mPlayerParam = new PlayerParam();
        }

        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //mediaPlayer准备工作
        mediaPlayer.setOnPreparedListener(this);
        //MediaPlayer完成
        mediaPlayer.setOnCompletionListener(this);

        mediaPlayer.setOnBufferingUpdateListener(this);//当前加载进度的监听

        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 60);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-fps", 0);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "fps", 15);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_YV12);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0);
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "fflags", "nobuffer");

        mIsLive = mUrl.startsWith("http://") && mUrl.endsWith(".m3u8") || mUrl.startsWith("rtmp://") || mUrl.startsWith("rtsp://");
        if (mIsLive && params.isLowDelay == 1) {
            mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "max-buffer-size", 200);
            mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "min-frames", 1);
            mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);
            mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probsize", "4096");
            mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", "2000000");
            mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", "4096");
        }

        if (params.isRtspTransportTcp == 1) {
            mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp");
        }
        if (params.isUseMediacodec == 1) {
            mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "mediacodec", 1);
        }

        //连接ijkPlayer 和surfaceHOLDER
        //mediaPlayer.setDisplay();
        if (surface != null) {
            mediaPlayer.setSurface(surface);
        } else {
            mediaPlayer.setSurface(mSurface);
        }

        mediaPlayer.prepareAsync();
        return 0;
    }

    @Override
    public int Stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        return -1;
    }

    @Override
    public int Pause(){
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            return 0;
        }
        return -1;
    }

    @Override
    public int Seek(long ms){
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(ms);
            return 0;
        }
        return -1;
    }

    @Override
    public void SetDisplaySurface(Surface surface) {
        mSurface = surface;
        if (mediaPlayer != null)
            mediaPlayer.setSurface(surface);
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        iMediaPlayer.start();
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        iMediaPlayer.seekTo(0);
        iMediaPlayer.start();
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {

    }
}
