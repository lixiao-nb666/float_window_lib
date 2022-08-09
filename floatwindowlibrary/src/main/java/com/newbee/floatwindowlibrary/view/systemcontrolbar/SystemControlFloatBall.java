package com.newbee.floatwindowlibrary.view.systemcontrolbar;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.lixiao.build.mybase.LG;
import com.newbee.floatwindowlibrary.R;
import com.newbee.floatwindowlibrary.share.FloatWindowShare;
import com.newbee.floatwindowlibrary.view.circlemenu.view.CircleMenuView;
import com.newbee.floatwindowlibrary.view.circlemenu.view.bean.CircleItemInfo;
import java.util.List;

/**
 * @author lixiaogege!
 * @description: one day day ,no zuo no die !
 * @date :2021/1/13 0013 14:10
 */
public class SystemControlFloatBall extends FrameLayout implements View.OnTouchListener {
    private final String tag=getClass().getSimpleName()+">>>>";

    private ImageView defIV;
    private CircleMenuView showCMV;
    private CircleMenuView.Listen showCMVListen=new CircleMenuView.Listen() {
        @Override
        public void needHide() {
            showType= ShowType.DEF_BALL;
            FloatWindowShare.getInstance().putString(shareShowType,showType.ordinal()+"");
            swithTypeToShow();
        }

        @Override
        public void selectItem(CircleItemInfo itemInfo) {
            try {
                SystemControlType systemControlType= (SystemControlType) itemInfo.getObject();
                listen.systemControl(systemControlType);


            }catch (Exception e){

            }
        }
    };

    private ShowType showType;
    private final String shareShowType=tag+"_showType";

    public SystemControlFloatBall(@NonNull Context context) {
        this(context,null);
    }

    public SystemControlFloatBall(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SystemControlFloatBall(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public SystemControlFloatBall(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    private Listen listen;

    public void init( Listen listen){
        this.listen=listen;
        setOnTouchListener(this);
        inflate(getContext(), R.layout.view_system_control_float_ball,this);
        /**
         * 初始页面
         */
        defIV=findViewById(R.id.iv_def);
        defIV.setImageResource(R.drawable.icon_def_system_ctroll_ball);
        /**
         * 圆盘菜单
         */
        showCMV=findViewById(R.id.cmv_show);
        showCMV.setBackgroundResource(R.drawable.bg_float_ball_menu_bg);
        List<CircleItemInfo>infoList=SystemControlItemInfoUtil.getAll();
        showCMV.setListen(showCMVListen,infoList);
        String shareStr=FloatWindowShare.getInstance().getString(shareShowType);
        int showTypeIndex=0;
        if(!TextUtils.isEmpty(shareStr)){
            showTypeIndex=Integer.valueOf(shareStr);
        }
        if(showTypeIndex< ShowType.values().length){
            showType= ShowType.values()[showTypeIndex];
        }

        swithTypeToShow();

    }

    private void swithTypeToShow(){
        post(new Runnable() {
            @Override
            public void run() {
                if(null==showType){
                    showType= ShowType.DEF_BALL;
                }
                switch (showType){
                    case DEF_BALL:
                        defIV.setVisibility(VISIBLE);
                        showCMV.setVisibility(GONE);
                        break;
                    case CIRCLE_MUNE:
                        defIV.setVisibility(GONE);
                        showCMV.setVisibility(VISIBLE);
                        break;
                }
            }
        });

    }


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
                if (Math.abs(x - lastX) > ViewConfiguration.get(getContext()).getScaledTouchSlop()){
                    isMove = true;
                    int nowX=(int) (event.getRawX() - getWidth() / 2);;
                    int nowY=(int) (event.getRawY() - getHeight() / 2);
                    listen.getNowXAndY(nowX,nowY);
                }
                return true;
            case MotionEvent.ACTION_UP:
                if (isMove){
                    return true;
                }else {
                    if(null!=showType&&showType== ShowType.DEF_BALL){
                        showType= ShowType.CIRCLE_MUNE;
                        FloatWindowShare.getInstance().putString(shareShowType,showType.ordinal()+"");
                        swithTypeToShow();
                        LG.i(tag,"----------show:change");
                    }
                    LG.i(tag,"----------show");
                }
        }
        lastX = x;
        return super.onTouchEvent(event);
    }

    public enum ShowType{
        DEF_BALL,
        CIRCLE_MUNE,
    }

    public interface Listen{

        public void getNowXAndY(int x, int y);

        public void systemControl(SystemControlType systemControlType);
    }
}
