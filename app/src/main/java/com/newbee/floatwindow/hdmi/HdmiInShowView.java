package com.newbee.floatwindow.hdmi;

import android.os.Handler;
import android.os.Message;


import com.lixiao.build.mybase.LG;
import com.lztek.hdmiin.view.HdmiPlayViewUtil;
import com.lztek.hdmiin.view.HdmiPlayViewUtilImp;
import com.lztek.hdmiin.view.HdmiPlayerView;


public class HdmiInShowView {
    private String tag = getClass().getName() + ">>>>";

    private Handler viewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case SHOW:

                        hdmiPlayViewUtil.play();

                        break;
                }
            } catch (Exception e) {
                LG.i(tag, "------kankan err:" + e.toString());
            }

        }
    };
    private final int SHOW = 0;
    private HdmiPlayViewUtilImp hdmiPlayViewUtilImp = new HdmiPlayViewUtilImp() {
        @Override
        public void surfaceIsok() {
            if (hdmiPlayViewUtil != null && isShow)
                hdmiPlayViewUtil.play();
        }

        @Override
        public void onStart() {

        }

        @Override
        public void err() {
            if (hdmiPlayViewUtil != null)
                hdmiPlayViewUtil.stop();
        }
    };
    private HdmiPlayViewUtil hdmiPlayViewUtil;
    private boolean isShow = false;

    public HdmiInShowView(HdmiPlayerView hdmiPlayerView) {

        hdmiPlayViewUtil = new HdmiPlayViewUtil(hdmiPlayerView, hdmiPlayViewUtilImp);
        hdmiPlayViewUtil.setAutoPlay(true);
    }






    public void show(Object filePath) {
        isShow = true;
        viewHandler.removeCallbacksAndMessages(null);
        viewHandler.sendEmptyMessage(SHOW);
    }


    public void stop() {
        isShow = false;
        viewHandler.removeCallbacksAndMessages(null);
        if (hdmiPlayViewUtil != null)
            hdmiPlayViewUtil.stop();
    }



    public void close() {
        isShow = false;
        viewHandler.removeCallbacksAndMessages(null);
        if (hdmiPlayViewUtil != null)
            hdmiPlayViewUtil.stop();
    }


    public void checkSamePlayTime(long needStartTime, long needEndTime) {

    }


}
