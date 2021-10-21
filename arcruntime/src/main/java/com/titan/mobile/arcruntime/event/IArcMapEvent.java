package com.titan.mobile.arcruntime.event;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * Created by zy on 2019/5/28.
 */

public interface IArcMapEvent extends IArcMapExtendEvent {

    public boolean onDoubleTap(MotionEvent e);

    public boolean onDoubleTapEvent(MotionEvent e);

    public boolean onDoubleTouchDrag(MotionEvent event);

    public boolean onDown(MotionEvent e);

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);

    public boolean onMultiPointerTap(MotionEvent event);

    public boolean onRotate(MotionEvent event, double rotationAngle);

    public boolean onScale(ScaleGestureDetector detector);

    public boolean onScaleBegin(ScaleGestureDetector detector);

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);

    public boolean onSingleTapConfirmed(MotionEvent e);

    public boolean onSingleTapUp(MotionEvent e);

    public boolean onTouch(View view, MotionEvent event);

    public boolean onUp(MotionEvent e);

    public void onLongPress(MotionEvent e);

    public void onScaleEnd(ScaleGestureDetector detector);

    public void onShowPress(MotionEvent e);

}
