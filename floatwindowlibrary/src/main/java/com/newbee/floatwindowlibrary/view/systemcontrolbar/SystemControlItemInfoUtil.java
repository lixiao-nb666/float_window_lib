package com.newbee.floatwindowlibrary.view.systemcontrolbar;



import com.newbee.floatwindowlibrary.R;
import com.newbee.floatwindowlibrary.view.circlemenu.view.bean.CircleItemInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * @author lixiaogege!
 * @description: one day day ,no zuo no die !
 * @date :2021/1/19 0019 11:13
 */
public class SystemControlItemInfoUtil {

    public static CircleItemInfo getInfo(SystemControlType systemControlType){
        if(null==systemControlType){
            return null;
        }
        CircleItemInfo itemInfo=new CircleItemInfo();
        itemInfo.setObject(systemControlType);
        switch (systemControlType){
            case SWITCH_APP:
                itemInfo.setImgId(R.drawable.icon_float_ball_switch_app);
                break;
            case TO_MAIN:
                itemInfo.setImgId(R.drawable.icon_float_ball_to_main);
                break;
            case BACK:
                itemInfo.setImgId(R.drawable.icon_float_ball_back);
                break;
            case SOUND_UP:
                itemInfo.setImgId(R.drawable.icon_float_ball_sound_up);
                break;
            case SOUND_DOWN:
                itemInfo.setImgId(R.drawable.icon_float_ball_sound_down);
                break;
            case DRAW_PIZHU:
                itemInfo.setImgId(R.drawable.icon_float_ball_draw_pizhu);
                break;
//            case SHOW_HDMIIN:
//                itemInfo.setImgId(R.drawable.icon_float_ball_hdmi);
//                break;
//            case FILE_MANAGER:
//                itemInfo.setImgId(R.drawable.icon_float_ball_file_manager);
//                break;
//
//            case SHARE_SCREEN:
//                itemInfo.setImgId(R.drawable.icon_float_ball_share_screen);
//                break;
        }
        return itemInfo;
    }

    public static List<CircleItemInfo> getAll(){
        List<CircleItemInfo> list=new ArrayList<>();
        for(SystemControlType type:SystemControlType.values()){
            CircleItemInfo info=getInfo(type);
            if(null!=info){
                list.add(info);
            }
        }
        return list;
    }
}
