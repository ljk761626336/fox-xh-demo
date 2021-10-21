package com.titan.mobile.arcruntime.tools.core;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.LinearLayout;

import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.titan.mobile.arcruntime.core.ArcMap;
import com.titan.mobile.arcruntime.core.IP;
import com.titan.mobile.arcruntime.core.select.ISelectChange;
import com.titan.mobile.arcruntime.event.IArcMapEvent;
import com.titan.mobile.arcruntime.util.MeasureScreen;

import java.util.List;

/**
 * Created by zy on 2019/3/21.
 */

public abstract class BaseTool implements IP, ITool, ToolView.IClick, IArcMapEvent, ISelectChange {

    protected String id;
    protected String name;
    protected int resId;
    protected int checkedResId;
    protected boolean enable = false;
    protected boolean isRegisterMapEvent;

    protected ToolView view;

    protected Context context;
    protected ArcMap arcMap;
    protected MapView mapView;

    protected int selCount;

    public BaseTool() {
        this.isRegisterMapEvent = isCheckBtn();
    }


    @Override
    public void create(ArcMap arcMap) {
        this.arcMap = arcMap;
        this.mapView = arcMap.getMapView();
        this.context = mapView.getContext();

        view = new ToolView(this.context, this);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, MeasureScreen.dip2px(context, 45), (float) 0.5));
        view.setIClick(this);
    }

    @Override
    public void ready() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isCheckBtn() {
        return false;
    }

    @Override
    public boolean isRegisterMapEvent() {
        return isRegisterMapEvent;
    }

    @Override
    public int getResId() {
        return resId;
    }

    @Override
    public int getCheckedResId() {
        return checkedResId;
    }

    @Override
    public boolean getEnabled() {
        return enable;
    }

    @Override
    public boolean getChecked() {
        return view.isChecked();
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.GONE);
    }

    @Override
    public boolean getVisible() {
        return view.getVisibility() == View.VISIBLE;
    }

    @Override
    public void simulateTouch() {
        view.setChecked(!view.isChecked());
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void activate() {
        //arcMap.getToolContainer().activate(this);
    }

    @Override
    public void deactivate() {
        if (view != null) view.setChecked(false);
        //arcMap.getToolContainer().deactivate(this);
    }

    @Override
    public void refresh() {

    }

    /**
     * 按钮点击事件监听
     *
     * @param view
     */
    @Override
    public void viewClick(View view) {
        if (isCheckBtn()) {
            if (getChecked()) {
                activate();
            } else {
                deactivate();
            }
        }
    }

    /**********************************************************************************************/
    @Override
    public void selectChange(int size) {
        this.selCount = size;
    }

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
        return false;
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
        return false;
    }
}
