package com.titan.mobile.arcruntime.event;

import android.view.MotionEvent;

/**
 * Created by zy on 2019/6/11.
 * android原生事件  map容器最底层事件
 */

public interface IArcMapExtendEvent {
    public boolean onTouchStart(MotionEvent motionEvent);

    public boolean onTouchMoving(MotionEvent motionEvent);

    public boolean onTouchCancel(MotionEvent motionEvent);
}
