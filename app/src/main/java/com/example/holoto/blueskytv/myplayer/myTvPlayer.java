package com.example.holoto.blueskytv.myplayer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.holoto.blueskytv.R;
import com.shuyu.gsyvideoplayer.video.GSYADVideoPlayer;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.jar.Attributes;

public class myTvPlayer extends StandardGSYVideoPlayer
{
    public myTvPlayer(Context context,boolean fullFlag)
    {
        super(context,fullFlag);
    }
    public myTvPlayer(Context context)
    {
        super(context);
    }
    public myTvPlayer(Context context, AttributeSet attributeSet)
    {
        super(context,attributeSet);
    }

    @Override
    public int getLayoutId() {
        return R.layout.my_tv_player;
    }

    @Override
    public void startPlayLogic() {
        super.startPlayLogic();
    }

    @Override
    public void onVideoPause() {
        super.onVideoPause();
    }

    @Override
    public void onVideoResume() {
//        super.onVideoResume();
    }

    @Override
    public void onVideoReset() {
        super.onVideoReset();
    }

    @Override
    public void setDialogProgressBar(Drawable drawable) {
        super.setDialogProgressBar(drawable);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
    }


    @Override
    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
//        mChangePosition=false;
//        mChangeVolume=false;
//        mBrightness=false;
    }

    @Override
    protected void touchDoubleUp() {

        setDoubleclick1.setvvd();
        super.touchDoubleUp();

    }
private setDoubleclick setDoubleclick1;
    public  static interface setDoubleclick
    {
        void setvvd();
    }
    public void  setVV(setDoubleclick setDoubleclick2)
    {
this.setDoubleclick1=setDoubleclick2;
    }

    @Override
    protected void onClickUiToggle() {
        super.onClickUiToggle();
    }
}
