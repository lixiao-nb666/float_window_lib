package com.lztek.hdmiin.view;


import com.lztek.hdmiin.manager.HdmiInManager;

public class HdmiPlayViewUtil {
    private final String tag=getClass().getName()+">>>>";
    private HdmiPlayerView hdmiPlayerView;
    private HdmiPlayViewUtilImp hdmiPlayViewUtilImp;
    /**
     * 自动播放
     */
    private boolean autoPlay=true;

    /**
     * 显示区域是否OK
     */
    private boolean mSurfacePrepared = false;
    /**
     * 播放错误的次数
     */
    private int mPlayerErrorCount = -1;

    private HdmiPlayerView.OnPlayEventListener onPlayEventListener=new HdmiPlayerView.OnPlayEventListener() {

        @Override
        public void onSurfacePrepared(HdmiPlayerView playerView) {
            mSurfacePrepared = true;
            if(autoPlay){
                play();
            }
            hdmiPlayViewUtilImp.surfaceIsok();
        }

        @Override
        public void onHdmiStart(HdmiPlayerView playerView) {
            mPlayerErrorCount = 0;
            hdmiPlayViewUtilImp.onStart();
        }

        @Override
        public void onHdmiError(HdmiPlayerView playerView) {
            mPlayerErrorCount = mPlayerErrorCount <= 0? 1 : mPlayerErrorCount + 1;

            stop();

            /**
             * 说明播放的时候出错了
             */
            hdmiPlayViewUtilImp.err();
        }
    };

    public HdmiPlayViewUtil(HdmiPlayerView hdmiPlayerView, HdmiPlayViewUtilImp hdmiPlayViewUtilImp){
        this.hdmiPlayerView=hdmiPlayerView;
        this.hdmiPlayerView.setOnPlayEventListener(onPlayEventListener);
        this.hdmiPlayViewUtilImp=hdmiPlayViewUtilImp;
    }

    public void setAutoPlay(boolean setAutoPlay){
        this.autoPlay=setAutoPlay;
    }


    public void play(){
        if(mSurfacePrepared&& HdmiInManager.getInstance().getHdmiIn()!=null&&hdmiPlayerView!=null){
            hdmiPlayerView.startPlay();
        }
    }

    public void stop(){
        if(hdmiPlayerView!=null){
            hdmiPlayerView.stopPlay();
        }
    }


}
