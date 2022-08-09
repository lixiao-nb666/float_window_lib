package com.newbee.floatwindowlibrary.view.systemcontrolbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.lixiao.build.mybase.LG;
import com.lixiao.build.util.AudioUtil;
import com.lixiao.build.util.keycode.SystemKeyCode;
import com.lixiao.build.util.keycode.SystemKeyCodeInputUtil;
import com.newbee.floatwindowlibrary.R;


/**
 * @author lixiaogege!
 * @description: one day day ,no zuo no die !
 * @date :2021/1/12 0012 16:38
 */
public class SystemControlBottomBar extends LinearLayout {
    private final String tag=getClass().getSimpleName()+">>>>";
    private LinearLayout ll;
    private ImageView soundDownIV,soundUpIV,backIV,mainIV,swichAppIV;
    private OnClickListener onClickListener=new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.iv_sound_down){
                int downNumb= AudioUtil.getInstance().getSystemVolume();
                downNumb-=5;
                AudioUtil.getInstance().setSystemVolume(downNumb);
                AudioUtil.getInstance().setMediaVolume(downNumb);
            }else if(v.getId()==R.id.iv_sound_up){
                int upNumb=AudioUtil.getInstance().getSystemVolume();
                upNumb+=5;
                AudioUtil.getInstance().setSystemVolume(upNumb);
                AudioUtil.getInstance().setMediaVolume(upNumb);
            }else if(v.getId()==R.id.iv_back){
                SystemKeyCodeInputUtil.inputKeyCode(SystemKeyCode.KEYCODE_BACK);
            }else if(v.getId()== R.id.iv_main){
                SystemKeyCodeInputUtil.inputKeyCode(SystemKeyCode.KEYCODE_HOME);
            }else if(v.getId()==R.id.iv_switch_app){
                SystemKeyCodeInputUtil.inputKeyCode(SystemKeyCode.KEYCODE_APP_SWITCH);
            }
        }
    };

    public SystemControlBottomBar(Context context) {
        this(context,null);
    }

    public SystemControlBottomBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SystemControlBottomBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, 0, 0);

    }

    public SystemControlBottomBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        View.inflate(context, R.layout.view_system_control_bottom_bar, this);
        ll=findViewById(R.id.ll);
        ll.setBackgroundResource(R.drawable.bg_system_control_bottom_bar);
        //調低音量
        soundDownIV=findViewById(R.id.iv_sound_down);
        soundDownIV.setOnClickListener(onClickListener);
        soundDownIV.setScaleType(ImageView.ScaleType.CENTER);
        soundDownIV.setImageResource(R.drawable.icon_system_sound_down);
        //調高音量
        soundUpIV=findViewById(R.id.iv_sound_up);
        soundUpIV.setOnClickListener(onClickListener);
        soundUpIV.setScaleType(ImageView.ScaleType.CENTER);
        soundUpIV.setImageResource(R.drawable.icon_system_sound_up);
        //返回鍵
        backIV=findViewById(R.id.iv_back);
        backIV.setOnClickListener(onClickListener);
        backIV.setScaleType(ImageView.ScaleType.CENTER);
        backIV.setImageResource(R.drawable.icon_system_back);
        //主頁鍵
        mainIV=findViewById(R.id.iv_main);
        mainIV.setOnClickListener(onClickListener);
        mainIV.setScaleType(ImageView.ScaleType.CENTER);
        mainIV.setImageResource(R.drawable.icon_system_main);
        //多任務頁面
        swichAppIV=findViewById(R.id.iv_switch_app);
        swichAppIV.setOnClickListener(onClickListener);
        swichAppIV.setScaleType(ImageView.ScaleType.CENTER);
        swichAppIV.setImageResource(R.drawable.icon_system_switch_app);
        LG.i(tag,"--------initOk()");
    }


}
