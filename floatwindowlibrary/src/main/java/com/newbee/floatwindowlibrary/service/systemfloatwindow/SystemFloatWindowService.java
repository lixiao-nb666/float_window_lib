package com.newbee.floatwindowlibrary.service.systemfloatwindow;

import android.view.View;
import android.view.WindowManager;
import com.lixiao.build.mybase.service.floatwindow.BaseFloatView;
import com.lixiao.build.mybase.service.floatwindow.BaseFloatWindowService;
import com.lixiao.build.util.keycode.SystemKeyCode;
import com.lixiao.build.util.keycode.SystemKeyCodeInputUtil;
import com.newbee.floatwindowlibrary.service.source.event.SourceEventBusSubscriptionSubject;
import com.newbee.floatwindowlibrary.service.source.event.SourceEventType;
import com.newbee.floatwindowlibrary.service.systemfloatwindow.view.SystemFloatWindowView;
import com.newbee.floatwindowlibrary.view.systemcontrolbar.SystemControlFloatBall;
import com.newbee.floatwindowlibrary.view.systemcontrolbar.SystemControlType;



/**
 * @author lixiaogege!
 * @description: one day day ,no zuo no die !
 * @date :2021/1/18 0018 10:27
 */
public class SystemFloatWindowService extends BaseFloatWindowService {
    private final String tag=getClass().getSimpleName()+">>>>";
    private SystemFloatWindowView systemFloatWindowView;

    private BaseFloatView.Listen baseListen=new BaseFloatView.Listen() {
        @Override
        public void initView(View view, WindowManager windowManager, WindowManager.LayoutParams layoutParams) {

            if(null!=floatBall){
                floatBall.init(floatBallListen);
            }
        }
    };



    private SystemControlFloatBall floatBall;
    private SystemControlFloatBall.Listen floatBallListen=new SystemControlFloatBall.Listen() {
        @Override
        public void getNowXAndY(int x, int y) {
            systemFloatWindowView.updateViewIndex(x,y);
        }

        @Override
        public void systemControl(SystemControlType systemControlType) {
            if(null==systemControlType){
                return;
            }
            switch (systemControlType){
                case SOUND_DOWN:
                    SystemKeyCodeInputUtil.inputKeyCode(SystemKeyCode.KEYCODE_VOLUME_DOWN);
                    break;
                case SOUND_UP:
                    SystemKeyCodeInputUtil.inputKeyCode(SystemKeyCode.KEYCODE_VOLUME_UP);
                    break;
                case BACK:
                    SystemKeyCodeInputUtil.inputKeyCode(SystemKeyCode.KEYCODE_BACK);
                    break;
                case TO_MAIN:
                    SystemKeyCodeInputUtil.inputKeyCode(SystemKeyCode.KEYCODE_HOME);
                    break;
                case SWITCH_APP:
                    SystemKeyCodeInputUtil.inputKeyCode(SystemKeyCode.KEYCODE_APP_SWITCH);
                    break;
                case DRAW_PIZHU:
//                  DrawBoardAnnotationSubscriptionSubject.getInstance().eventListen(DrawBoardAnnotationType.SWITCH_SHOW);
                    break;
//                case SHOW_HDMIIN:
//                    SourceEventBusSubscriptionSubject.getInstance().eventListen(SourceEventType.Select_Show);
//                    break;
            }
        }
    };

    @Override
    protected void init(WindowManager windowManager) {
        floatBall=new SystemControlFloatBall(getBaseContext());
        systemFloatWindowView=new SystemFloatWindowView(getBaseContext(),windowManager,baseListen,floatBall);
    }

    @Override
    protected BaseFloatView getBaseFloatView() {
        return systemFloatWindowView;
    }



    @Override
    public void close() {
        super.close();
    }
}
