package com.titan.mobile.arcruntime.core;

import android.content.Context;

import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.List;

/**
 * Created by zy on 2019/5/9.
 */

public abstract class BaseContainer implements IContainer {

    protected Context context;
    protected ArcMap arcMap;
    protected MapView mapView;
    protected MapControl mapControl;

    @Override
    public void create(ArcMap arcMap) {
        this.arcMap = arcMap;
        this.mapView = arcMap.getMapView();
        this.context = arcMap.getContext();
        this.mapControl = arcMap.getMapControl();
    }

    @Override
    public void ready() {

    }

    @Override
    public void destroy() {

    }


    public void activate(Object o) {

    }

    public void deactivate(Object o) {

    }

    public void refresh() {

    }
}
