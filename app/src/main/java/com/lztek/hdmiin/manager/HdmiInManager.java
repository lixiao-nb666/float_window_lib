package com.lztek.hdmiin.manager;


import com.lixiao.build.mybase.appliction.BaseApplication;
import com.lztek.hdmiin.IHdmiIn;
import com.lztek.hdmiin.service.HdmiInStatusServiceDao;
import com.lztek.hdmiin.service.HdmiInStatusServiceDaoImp;

public class HdmiInManager {

    private static HdmiInManager hdmiInManager;
    private HdmiInStatusServiceDao serviceDao;
    private HdmiInStatusServiceDaoImp hdmiInStatusServiceDaoImp=new HdmiInStatusServiceDaoImp() {
        @Override
        public void set(IHdmiIn mHdmiIn) {
            if(hdmiInPlayUtil!=null){
                hdmiInPlayUtil.setIHdminIn(mHdmiIn);
            }
        }
    };
    private HdmiInPlayUtil hdmiInPlayUtil;

    private HdmiInManager() {
        hdmiInPlayUtil=new HdmiInPlayUtil();
        serviceDao = new HdmiInStatusServiceDao(BaseApplication.getContext(),hdmiInStatusServiceDaoImp);
        serviceDao.startService();
    }


    public static HdmiInManager getInstance() {
        if (hdmiInManager == null) {
            synchronized (HdmiInManager.class) {
                if (hdmiInManager == null) {
                    hdmiInManager = new HdmiInManager();
                }
            }
        }
        return hdmiInManager;
    }

    public void close() {
        if (serviceDao != null)
            serviceDao.stopService();
        if(hdmiInPlayUtil!=null)
            hdmiInPlayUtil.close();
        hdmiInManager=null;

    }

    public IHdmiIn getHdmiIn(){
        if(hdmiInPlayUtil!=null){
            return hdmiInPlayUtil.getmHdmiIn();
        }
        return null;
    }
}
