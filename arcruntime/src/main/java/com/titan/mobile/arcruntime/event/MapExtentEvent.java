package com.titan.mobile.arcruntime.event;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zy on 2018/4/4.
 */

public abstract class MapExtentEvent implements View.OnTouchListener {

    public abstract boolean onTouchStart(MotionEvent motionEvent);

    public abstract boolean onTouchMoving(MotionEvent motionEvent);

    public abstract boolean onTouchCancel(MotionEvent motionEvent);

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            return onTouchStart(motionEvent);
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            return onTouchMoving(motionEvent);
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
            return onTouchCancel(motionEvent);
        }
        return false;
    }
}
