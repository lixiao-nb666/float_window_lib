package com.newbee.floatwindow;

import com.lixiao.build.mybase.appliction.BaseApplication;
import com.lztek.hdmiin.manager.HdmiInManager;

import com.newbee.floatwindow.hdmi.LzHdmiServiceDao;
import com.newbee.floatwindowlibrary.service.systemfloatwindow.SystemFloatWindowServiceDao;


/**
 * @author lixiaogege!
 * @description: one day day ,no zuo no die !
 * @date :2021/2/1 0001 11:03
 */
public class MyApplication extends BaseApplication {


    @Override
    protected void init() {
//        HdmiInManager.getInstance();

//        LzHdmiServiceDao.getInstance().startService();
        SystemFloatWindowServiceDao.getInstance().startService();
    }

    @Override
    protected void needClear(String str) {

    }

    @Override
    protected void close() {
//        HdmiInManager.getInstance().close();
//        LzHdmiServiceDao.getInstance().stopService();
        SystemFloatWindowServiceDao.getInstance().stopService();
    }
}
