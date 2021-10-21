package com.titan.mobile.arcruntime.tools.core;

import android.view.ViewGroup;

import com.titan.mobile.arcruntime.core.IContainer;

import java.util.List;

/**
 * Created by zy on 2018/12/6.
 */

public interface IToolGroup extends IContainer {

    public int getCount();

    public BaseTool getItem(int i);

    public void addItem(Class<? extends BaseTool> tool);

    public void addItems(List<Class<? extends BaseTool>> tools);

    public ViewGroup getGroupView();

    public boolean getVisible();

    public int getGravity();

    public String getGroupId();
}
