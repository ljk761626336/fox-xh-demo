package com.titan.mobile.arcruntime.tools.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.titan.mobile.arcruntime.util.MeasureScreen;

/**
 * Created by zy on 2018/12/17.
 */
public class ToolView extends androidx.appcompat.widget.AppCompatCheckBox implements View.OnTouchListener {

    private int with = 45;
    private int high = 45;

    private boolean initOk = false;

    boolean isNormalBnt = false;

    private int normalRes;

    private int pressedRes;

    private IClick iClick;
    private ILongClick iLongClick;

    public ToolView(Context context, int normalRes, int pressedRes, boolean isNormalBnt) {
        super(context);
        this.isNormalBnt = isNormalBnt;
        this.normalRes = normalRes;
        this.pressedRes = pressedRes;
        init();
    }

    public ToolView(Context context, ITool iTool) {
        super(context);
        this.isNormalBnt = !iTool.isCheckBtn();
        this.normalRes = iTool.getResId();
        this.pressedRes = iTool.getCheckedResId();
        init();
    }

    public ToolView(Context context, AttributeSet attrs) {
        super(context, attrs);
        throw new RuntimeException("暂不支持xml写法");
    }

    public ToolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        throw new RuntimeException("暂不支持xml写法");
    }

    private void init() {
        this.setBackgroundColor(Color.WHITE);
        this.setWidth(MeasureScreen.dip2px(getContext(), with));
        this.setHeight(MeasureScreen.dip2px(getContext(), high));
        this.setGravity(Gravity.CENTER);
        this.setButtonDrawable(normalRes);

        this.setOnTouchListener(this);
        initOk = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    public void setNormalBnt(boolean _isNormalBnt) {
        this.isNormalBnt = _isNormalBnt;
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if (initOk) {
            if (checked) {
                this.setButtonDrawable(pressedRes);
            } else {
                this.setButtonDrawable(normalRes);
            }
        }
    }

    private float _x, _y;
    private double _dix = 20;
    private long startTime;
    private long interval = 500;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            _x = event.getX();
            _y = event.getY();
            startTime = System.currentTimeMillis();
            if (longPressRunnable != null) {
                removeCallbacks(longPressRunnable);
                int eventPointCount = event.getPointerCount();
                if (event.getAction() == MotionEvent.ACTION_DOWN && eventPointCount == 1) {
                    postCheckForLongTouch(event.getX(), event.getY());
                }
            }
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (isNormalBnt) {
                this.setButtonDrawable(pressedRes);
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isNormalBnt) {
                this.setButtonDrawable(normalRes);
            } else {
                if (isChecked()) {
                    this.setButtonDrawable(normalRes);
                    this.setChecked(false);
                } else {
                    this.setButtonDrawable(pressedRes);
                    this.setChecked(true);
                }
            }
            if (System.currentTimeMillis() - startTime < interval) {
                removeCallbacks(longPressRunnable);
                if (iClick != null) iClick.viewClick(this);
            }
        }
        if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            if (isNormalBnt) {
                this.setButtonDrawable(normalRes);
            }
            removeCallbacks(longPressRunnable);
        }
        return true;
    }

    public interface ILongClick {
        public void viewLongClick(View view);
    }

    public interface IClick {
        public void viewClick(View view);
    }

    public void setIClick(IClick iClick) {
        this.iClick = iClick;
    }

    public void setILongClick(ILongClick iLongClick) {
        this.iLongClick = iLongClick;
    }

    private void postCheckForLongTouch(float x, float y) {
        longPressRunnable.setPressLocation(x, y);
        postDelayed(longPressRunnable, interval);
    }

    /**
     * 当长按事件发生时，要触发的任务
     */
    private LongPressRunnable longPressRunnable = new LongPressRunnable();

    private class LongPressRunnable implements Runnable {

        private int x, y;

        public void setPressLocation(float x, float y) {
            this.x = (int) x;
            this.y = (int) y;
        }

        @Override
        public void run() {
            if (iLongClick != null) iLongClick.viewLongClick(ToolView.this);
        }

    }
}
