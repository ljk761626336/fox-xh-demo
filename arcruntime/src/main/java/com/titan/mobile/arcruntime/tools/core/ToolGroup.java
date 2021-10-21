package com.titan.mobile.arcruntime.tools.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.titan.mobile.arcruntime.R;
import com.titan.mobile.arcruntime.core.ArcMap;
import com.titan.mobile.arcruntime.util.ObjectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2019/3/28.
 */

public class ToolGroup implements IToolGroup {

    protected final List<Class<? extends BaseTool>> _toolList = new ArrayList<>();

    protected String groupId;
    protected int gravity;
    protected boolean visible = true;

    protected final List<BaseTool> tools = new ArrayList<>();
    protected ViewGroup viewGroup;
    protected Context context;
    protected ArcMap foxMap;
    protected MapView mapView;

    public ToolGroup(String groupId, int gravity) {
        this.groupId = groupId;
        this.gravity = gravity;
    }

    public ToolGroup(String groupId, boolean visible, int gravity) {
        this.groupId = groupId;
        this.gravity = gravity;
        this.visible = visible;
    }

    //**********************************************************************************************

    @Override
    public void create(ArcMap foxMap) {
        this.foxMap = foxMap;
        this.mapView = foxMap.getMapView();
        this.context = mapView.getContext();
        this.viewGroup = (ViewGroup) View.inflate(context, R.layout.gis_box_tool_group, null);

        BaseTool baseTool;
        Class<? extends BaseTool> clazz;
        for (int i = 0; i < _toolList.size(); i++) {
            clazz = _toolList.get(i);
            //baseTool = ReflectUtil.newInstance(clazz);
            baseTool = ObjectUtil.Reflect.newInstance(clazz);
            baseTool.create(foxMap);
            this.viewGroup.removeView(baseTool.getView());
            this.viewGroup.addView(baseTool.getView());
            tools.add(baseTool);
        }
        _toolList.clear();
        if (visible) {
            viewGroup.setVisibility(View.VISIBLE);
        } else {
            viewGroup.setVisibility(View.GONE);
        }
    }

    @Override
    public void ready() {

    }

    /*@Override
    public void ready(List<Layer> layers) {
        BaseTool baseTool;
        for (int i = 0; i < tools.size(); i++) {
            baseTool = tools.get(i);
            baseTool.ready(layers);
        }
    }*/

    @Override
    public void destroy() {
        BaseTool baseTool;
        for (int i = 0; i < tools.size(); i++) {
            baseTool = tools.get(i);
            baseTool.destroy();
        }
    }
    //**********************************************************************************************


    @Override
    public int getCount() {
        return tools.size();
    }

    @Override
    public BaseTool getItem(int i) {
        return tools.get(i);
    }

    @Override
    public void addItem(Class<? extends BaseTool> tool) {
        _toolList.add(tool);
    }

    @Override
    public void addItems(List<Class<? extends BaseTool>> tools) {
        _toolList.addAll(tools);
    }

    @Override
    public ViewGroup getGroupView() {
        return viewGroup;
    }

    @Override
    public boolean getVisible() {
        return false;
    }

    @Override
    public int getGravity() {
        return gravity;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }
}
