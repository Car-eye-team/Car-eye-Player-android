package org.careye.libcareyeplayer.player;

public class PlayerParam {
    public int  isUseMediacodec     = 1; // 使用硬件编码
    public int  isRtspTransportTcp  = 1; // rtsp 使用tcp
    public int  isLowDelay          = 1; // 流媒体时，使用低延时配置
    public PlayerParam(){

    }
}
