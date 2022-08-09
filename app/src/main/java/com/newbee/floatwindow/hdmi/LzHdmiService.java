package com.newbee.floatwindow.hdmi;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import com.lixiao.build.mybase.service.floatwindow.BaseFloatView;
import com.lixiao.build.mybase.service.floatwindow.BaseFloatWindowService;
import com.lztek.hdmiin.view.HdmiPlayerView;
import com.newbee.floatwindow.R;
import com.newbee.floatwindow.hdmi.view.HdmiFloatView;
import com.newbee.floatwindowlibrary.service.source.event.SourceEventBusObserver;
import com.newbee.floatwindowlibrary.service.source.event.SourceEventBusSubscriptionSubject;
import com.newbee.floatwindowlibrary.service.source.event.SourceEventType;


/**
 * @author lixiaogege!
 * @description: one day day ,no zuo no die !
 * @date :2021/3/26 0026 20:21
 */
public class LzHdmiService extends BaseFloatWindowService {
    private HdmiPlayerView hdmiPlayerView;
    private HdmiInShowView hdmiInShowView;
    private HdmiFloatView hdmiFloatView;
    private BaseFloatView.Listen baseListen=new BaseFloatView.Listen() {
        @Override
        public void initView(final View view, WindowManager windowManager, WindowManager.LayoutParams layoutParams) {
            view.setOnTouchListener(new View.OnTouchListener() {
                private Boolean isMove = false;
                private float lastX;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    float x = event.getRawX();
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            isMove = false;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (Math.abs(x - lastX) > ViewConfiguration.get(view.getContext()).getScaledTouchSlop()){
                                isMove = true;
                                int nowX=(int) (event.getRawX() - view.getWidth() / 2);;
                                int nowY=(int) (event.getRawY() - view.getHeight() / 2);
                                hdmiFloatView.updateViewIndex(nowX,nowY);
                            }
                            return true;
                        case MotionEvent.ACTION_UP:
                            if (isMove){
                                return true;
                            }
                    }
                    lastX = x;
                    return false;
                }
            });
        }
    };
    private SourceEventBusObserver sourceEventBusObserver=new SourceEventBusObserver() {
        int i=0;
        long lastTime;
        @Override
        public void eventListen(SourceEventType eventType, Object... objects) {
            switch (eventType){
                case Select_Show:
                    long nowTime=System.currentTimeMillis();
                    if(nowTime-lastTime<1000){
                        return;
                    }
                    lastTime=nowTime;
                    int needX=0;
                    int needY=0;
                    i++;
                    int nowI=i%4;
                    switch (nowI){
                        case 0:
                            needX=192;
                            needY=108;
                            break;
                        case 1:
                            needX=192*3;
                            needY=108*3;
                            break;
                        case 2:
                            needX=192*6;
                            needY=108*6;
                            break;
                        case 3:
                            needX=192*10;
                            needY=108*10;
                            break;
                    }
                    hdmiFloatView.updateViewXAndY(needX,needY);
                    break;
            }
        }
    };


    @Override
    protected void init(WindowManager windowManager) {
        View view= View.inflate(getBaseContext(), R.layout.view_source,null);
        hdmiPlayerView=view.findViewById(R.id.hpv);
        hdmiInShowView=new HdmiInShowView(hdmiPlayerView);
        hdmiFloatView=new HdmiFloatView(getBaseContext(),windowManager,baseListen,view);
        SourceEventBusSubscriptionSubject.getInstance().addObserver(sourceEventBusObserver);
    }

    @Override
    protected BaseFloatView getBaseFloatView() {
        return hdmiFloatView;
    }

    @Override
    public void close() {
        super.close();
        hdmiInShowView.stop();
        hdmiFloatView.close();
        SourceEventBusSubscriptionSubject.getInstance().delectObjserver(sourceEventBusObserver);
    }
}


