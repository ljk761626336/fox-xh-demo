package com.titan.mobile.arcruntime.event;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * Created by zy on 2019/6/5.
 */

public class EasyMapEvent implements IArcMapEvent {
    protected boolean isMapTouch;


    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTouchDrag(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        isMapTouch = true;
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onMultiPointerTap(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onRotate(MotionEvent event, double rotationAngle) {
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onUp(MotionEvent e) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onTouchStart(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onTouchMoving(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onTouchCancel(MotionEvent motionEvent) {
        isMapTouch = false;
        return false;
    }
}
