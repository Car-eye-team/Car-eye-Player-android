package org.careye;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;

import org.careye.libcareyeplayer.player.PlayerKernel;
import org.careye.libcareyeplayer.player.PlayerKernelCarEye;
import org.careye.libcareyeplayer.player.PlayerKernelIJK;
import org.careye.libcareyeplayer.player.PlayerParam;

interface CarPlayerCallback{
    /**
     *
     * @param cmd   命令命令
     * @param param 参数
     * @param obj
     */
    void callback(Object obj, int cmd, String param);
}

public class CarPlayer {
    static public final int PLAYER_KERNEL_CAREYE = 0;
    static public final int PLAYER_KERNEL_IJK    = 1;

    final String TAG = "CarPlayer";

    CarPlayerCallback mCallback;
    Object mCallBackobj;
    final int mPlayerKernelType;
    PlayerKernel mPlayerKernel;
    PlayerParam mPlayerParam;
    Context mContext;
    Surface mVideoSurface;
    HandlerThread mHandlerThread;
    PlayerEventHandler mPlayerEventHandler;

    public CarPlayer(Context context, int playerKernel, CarPlayerCallback callback, Object callbackObj,PlayerParam param) {
        mCallback       = callback;
        mCallBackobj    = callbackObj;
        mContext        = context;
        if (param == null) {
            param = new PlayerParam();
        }
        mPlayerParam    = param;

        if (playerKernel == PLAYER_KERNEL_CAREYE) {
            mPlayerKernel = new PlayerKernelCarEye();
        }  else if (playerKernel == PLAYER_KERNEL_IJK) {
            mPlayerKernel = new PlayerKernelIJK();
        } else {
            Log.e(TAG, "no support player kernel:" + mPlayerKernel + ", used PlayerKernelIJK");
            playerKernel  = PLAYER_KERNEL_IJK;
            mPlayerKernel = new PlayerKernelIJK();
        }

        mPlayerKernelType   = playerKernel;

        mHandlerThread = new HandlerThread("HandlerThread");
        mHandlerThread.start();
        mPlayerEventHandler  = new PlayerEventHandler(mHandlerThread.getLooper());
    }

    public int open(Surface view, String url) {
        mVideoSurface = view;
        if (mVideoSurface == null) {
            Log.e(TAG, "mVideoSurface is null");
        }
       return mPlayerKernel.Play(mContext, url, mVideoSurface, 0, 0, mPlayerParam);
    }


    public int stop() {
        return mPlayerKernel.Stop();
    }

    public void setDisplaySurface(Surface surface) {
        if (mPlayerKernel != null)
            mPlayerKernel.SetDisplaySurface(surface);
    }


    public class PlayerEventHandler extends Handler {
        public PlayerEventHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what)
            {
                default:
                    break;
            }
        }
    }
}
