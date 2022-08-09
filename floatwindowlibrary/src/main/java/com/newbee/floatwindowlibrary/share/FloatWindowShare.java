package com.newbee.floatwindowlibrary.share;

import com.lixiao.build.mybase.share.BaseShare;

/**
 * @author lixiaogege!
 * @description: one day day ,no zuo no die !
 * @date :2021/3/26 0026 14:36
 */
public class FloatWindowShare extends BaseShare {
    private static FloatWindowShare share;
    private FloatWindowShare(){}

    public static FloatWindowShare getInstance(){
        if(null==share){
            synchronized (FloatWindowShare.class){
                if(null==share){
                    share=new FloatWindowShare();
                }
            }
        }
        return share;
    }

}
