package com.newbee.floatwindow.hdmi;

import android.content.Context;

import com.lixiao.build.mybase.appliction.BaseApplication;
import com.lixiao.build.mybase.service.BaseServiceDao;

/**
 * @author lixiaogege!
 * @description: one day day ,no zuo no die !
 * @date :2021/3/26 0026 15:51
 */
public class LzHdmiServiceDao extends BaseServiceDao {
    private static LzHdmiServiceDao serviceDao;

    private LzHdmiServiceDao(Context context) {
        super(context);
    }

    public static LzHdmiServiceDao getInstance(){
        if(null==serviceDao){
            synchronized (LzHdmiServiceDao.class){
                if(null==serviceDao){
                    serviceDao = new LzHdmiServiceDao(BaseApplication.getContext());
                }
            }
        }
        return serviceDao;
    }

    @Override
    protected Class getSsCls() {
        return LzHdmiService.class;
    }
}
