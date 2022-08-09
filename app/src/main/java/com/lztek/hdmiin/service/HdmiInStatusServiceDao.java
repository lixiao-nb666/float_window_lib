package com.lztek.hdmiin.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.lztek.hdmiin.IHdmiIn;

public class HdmiInStatusServiceDao {
    private final String tag=getClass().getName()+">>>>>";
    private ServiceConnection sc;
    private HdmiInStatusService sv;
    private Context context;
    private IHdmiIn mHdmiIn;


    public HdmiInStatusServiceDao(Context context, final HdmiInStatusServiceDaoImp hdmiInStatusServiceDaoImp) {
        this.context = context.getApplicationContext();
        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder iBinder) {
                HdmiInStatusService.HdmiIn serviceBinder = (HdmiInStatusService.HdmiIn) iBinder;
                sv = serviceBinder.getService();
                mHdmiIn = IHdmiIn.Stub.asInterface(iBinder);
                hdmiInStatusServiceDaoImp.set(mHdmiIn);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mHdmiIn = null;
                hdmiInStatusServiceDaoImp.set(mHdmiIn);
            }
        };
    }


    public void startService() {
        try {
            Intent intent = new Intent(context, HdmiInStatusService.class);
//            context.startService(intent);

            context.bindService(intent, sc, Context.BIND_AUTO_CREATE);
            Log.i(tag, "服务启动成功");

        } catch (Exception e) {
            Log.i(tag, "服务启动失败");
        }
    }


    public void stopService() {
        try {
            context.unbindService(sc);
        } catch (Exception e) {
        }
    }

}
