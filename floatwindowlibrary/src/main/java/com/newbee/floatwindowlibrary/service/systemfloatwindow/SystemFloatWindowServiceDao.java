package com.newbee.floatwindowlibrary.service.systemfloatwindow;

import android.content.Context;

import com.lixiao.build.mybase.appliction.BaseApplication;
import com.lixiao.build.mybase.service.BaseServiceDao;


/**
 * @author lixiaogege!
 * @description: one day day ,no zuo no die !
 * @date :2021/1/18 0018 10:51
 */
public class SystemFloatWindowServiceDao extends BaseServiceDao {
    private static SystemFloatWindowServiceDao dao;

    private SystemFloatWindowServiceDao(Context context) {
        super(context);
    }

    public static SystemFloatWindowServiceDao getInstance(){
        if(null==dao){
            synchronized (SystemFloatWindowServiceDao.class){
                if(null==dao){
                    dao=new SystemFloatWindowServiceDao(BaseApplication.getContext());
                }
            }
        }
        return dao;
    }

    @Override
    protected Class getSsCls() {
        return SystemFloatWindowService.class;
    }
}
