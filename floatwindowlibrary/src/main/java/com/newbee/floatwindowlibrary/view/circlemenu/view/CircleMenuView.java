package com.newbee.floatwindowlibrary.view.circlemenu.view;


import android.content.Context;
import android.util.AttributeSet;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.newbee.floatwindowlibrary.R;
import com.newbee.floatwindowlibrary.view.circlemenu.model.CircleMenuStatus;
import com.newbee.floatwindowlibrary.view.circlemenu.view.adapter.CircleMenuAdapter;
import com.newbee.floatwindowlibrary.view.circlemenu.view.bean.CircleItemInfo;

import java.util.List;


/**
 * @author lixiaogege!
 * @description: one day day ,no zuo no die !
 * @date :2021/1/18 0018 18:05
 */
public class CircleMenuView extends RelativeLayout {
    private final String tag=getClass().getSimpleName()+">>>>";

    private RelativeLayout showRl;
    private CircleMenu mCircleMenu;
    private ImageView ivCenter;
    private Listen listen;

    public CircleMenuView(Context context) {
        this(context,null);
    }

    public CircleMenuView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public CircleMenuView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    public void setListen(Listen listen, List<CircleItemInfo> infoList){
        this.listen=listen;
        init(infoList);
    }

    private void init(final List<CircleItemInfo> data){
        inflate(getContext(), R.layout.view_circle_mune,this);
        mCircleMenu = (CircleMenu) findViewById(R.id.cm_show);
        ivCenter = (ImageView) findViewById(R.id.iv_center_main);
        ivCenter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(null!=listen){
                    listen.needHide();
                }

            }
        });
        mCircleMenu.setOnItemClickListener(new CircleMenu.OnMenuItemClickListener() {

            @Override
            public void onClick(View view, int position) {
                if(null!=listen&&null!=data){
                    listen.selectItem(data.get(position));
                }
            }
        });
        mCircleMenu.setOnStatusChangedListener(new CircleMenu.OnMenuStatusChangedListener() {

            @Override
            public void onStatusChanged(CircleMenuStatus status, double rotateAngle) {
                // TODO 可在此处定制各种动画
//                odAnimation(status, (float)rotateAngle);
            }

        });
        mCircleMenu.setAdapter(new CircleMenuAdapter(data));
    }

    private float startRotate;
    private float startFling;

//    ObjectAnimator animRotate = null;
//    ObjectAnimator animFling = null;
//
//    private void odAnimation(CircleMenuStatus status, float rotateAngle) {
//
//        switch (status) {
//            case IDLE:
//                Log.i(tag, "--- -IDLE-----");
//                animRotate.cancel();
//                animRotate.cancel();
//                break;
//            case START_ROTATING:
//                Log.i(tag, "--- -START_ROTATING-----");
//                break;
//            case ROTATING:
//                animRotate = ObjectAnimator.ofFloat(ivCenter, "rotation", startRotate, startRotate + rotateAngle);
//                animRotate.setDuration(200).start();
//                startRotate += rotateAngle;
//                // Log.i(TAG, "--- -ROTATING-----");
//                break;
//            case STOP_ROTATING:
//                Log.i(tag, "--- -STOP_ROTATING-----");
//                break;
//            case START_FLING:
//                Log.i(tag, "--- -START_FLING-----");
//                break;
//
//            case FLING:
//                // Log.i(TAG, "--- -FLING-----");
//                animFling = ObjectAnimator.ofFloat(ivCenter, "rotation", startFling, startFling + rotateAngle);
//                animFling.setDuration(200).start();
//                startFling += rotateAngle;
//                break;
//            case STOP_FLING:
//                Log.i(tag, "--- -STOP_FLING-----");
//                break;
//
//            default:
//                break;
//        }
//
//    }

    public interface Listen{

        public void needHide();

        public void selectItem(CircleItemInfo itemInfo);
    }

}
