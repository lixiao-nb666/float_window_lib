package com.newbee.floatwindow.hdmi.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import com.lixiao.build.mybase.service.floatwindow.BaseFloatView;

/**
 * @author lixiaogege!
 * @description: one day day ,no zuo no die !
 * @date :2021/3/27 0027 10:50
 */
public class HdmiFloatView extends BaseFloatView {
    public Object updateViewIndeX;

    public HdmiFloatView(Context context, WindowManager windowManager, Listen listen, int viewId) {
        super(context, windowManager, listen, viewId);
    }

    public HdmiFloatView(Context context, WindowManager windowManager, Listen listen, View view) {
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

    public void selectShow(){
        view.post(new Runnable() {
            @Override
            public void run() {
                if(null!=view){
                    if(View.GONE==view.getVisibility()){
                        view.setVisibility(View.VISIBLE);
                    }else {
                        view.setVisibility(View.GONE);
                    }
                }
            }
        });
    }


    public void updateViewXAndY(int x,int y){
        if(null!=windowManager&&null!=layoutParams&&null!=view){
            layoutParams.width=x;
            layoutParams.height=y;
            windowManager.updateViewLayout(view,layoutParams);
        }
    }

    @Override
    protected int getW() {
        return 192;
    }

    @Override
    protected int getH() {
        return 108;
    }
}
