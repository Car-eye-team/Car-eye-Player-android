package org.careye.libcareyeplayer.player;

import android.view.Surface;

import org.careye.jni.MediaPlayer;

public class PlayerKernelCarEye extends PlayerKernel {

    MediaPlayer mMediaPlayer;

    public PlayerKernelCarEye(){
    }

    @Override
    public int Play(Object context, String url, Surface surface, int w, int h, PlayerParam params){
        mMediaPlayer = new MediaPlayer();
        return 0;
    }

    @Override
    public int Pause(){
        return 0;
    }
    @Override
    public  int Stop() {
        return 0;
    }
    @Override
    public int Seek(long ms){
        return 0;
    }

    @Override
    public void SetDisplaySurface(Surface surface) {
    }
}
