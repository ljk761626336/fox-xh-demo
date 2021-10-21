package com.titan.mobile.arcruntime.tools.core;

import android.view.View;

import com.titan.mobile.arcruntime.core.IContainer;


/**
 * Created by zy on 2018/12/6.
 */

public interface ITool extends IContainer {


    /**
     * 是否通过地图事件触发||是否为多状态按钮
     *
     * @return
     */
    public boolean isCheckBtn();

    public boolean isRegisterMapEvent();

    /**
     * 图片
     *
     * @return
     */
    public int getResId();

    /**
     * 按下图片
     *
     * @return
     */
    public int getCheckedResId();

    /**
     * 是否可用
     *
     * @return
     */
    public boolean getEnabled();

    /**
     * 获取按钮状态
     *
     * @return
     */
    public boolean getChecked();

    public void setVisible(boolean visible);

    public boolean getVisible();

    public void simulateTouch();
    /**
     * *********************************************************************************************
     */

    /**
     * view
     */
    public View getView();

    //-------------------------------------------初始化方法-----------------------------------------

    /**
     * 激活
     */
    void activate();

    /**
     * 失效
     */
    void deactivate();

    /**
     * 刷新
     */
    public void refresh();

}
