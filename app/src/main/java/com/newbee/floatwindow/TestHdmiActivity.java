package com.newbee.floatwindow;

import com.lixiao.build.mybase.activity.BaseCompatActivity;
import com.lztek.hdmiin.view.HdmiPlayerView;
import com.newbee.floatwindow.hdmi.HdmiInShowView;

/**
 * @author lixiaogege!
 * @description: one day day ,no zuo no die !
 * @date :2021/3/27 0027 10:39
 */
public class TestHdmiActivity extends BaseCompatActivity {
    private HdmiPlayerView hdmiPlayerView;
    private HdmiInShowView hdmiInShowView;

    @Override
    public int getViewLayoutRsId() {
        return R.layout.view_source;
    }

    @Override
    public void initView() {
        hdmiPlayerView=view.findViewById(R.id.hpv);
        hdmiInShowView=new HdmiInShowView(hdmiPlayerView);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initControl() {

    }

    @Override
    public void closeActivity() {

    }

    @Override
    public void viewIsShow() {

    }

    @Override
    public void viewIsPause() {

    }

    @Override
    public void changeConfig() {

    }
}
