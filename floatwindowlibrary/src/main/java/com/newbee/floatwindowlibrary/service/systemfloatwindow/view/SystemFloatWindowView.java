package com.newbee.floatwindowlibrary.service.systemfloatwindow.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.lixiao.build.mybase.service.floatwindow.BaseFloatView;



/**
 * @author lixiaogege!
 * @description: one day day ,no zuo no die !
 * @date :2021/1/18 0018 10:29
 */
public class SystemFloatWindowView extends BaseFloatView {
    public SystemFloatWindowView(Context context, WindowManager windowManager, Listen listen, int viewId) {
        super(context, windowManager, listen, viewId);
    }

    public SystemFloatWindowView(Context context, WindowManager windowManager, Listen listen, View view) {
        super(context, windowManager, listen, view);
    }




    @Override
    protected int getGravity() {
        return Gravity.TOP|Gravity.LEFT;
    }

    @Override
    protected boolean needUseShareXAndY() {
        return true;
    }
}
