package org.careye.libcareyeplayer.player;

import android.view.Surface;

public abstract class PlayerKernel {
        PlayerParam mPlayerParam;
        boolean mIsLive = false;
        String mUrl;
        Surface mSurface;

        public PlayerKernel(){
        }
        public abstract int Play(Object context, String url, Surface surface, int w, int h, PlayerParam params);
        public abstract int Pause();
        public abstract int Stop();
        public abstract int Seek(long ms);
        public abstract void SetDisplaySurface(Surface surface);
}
