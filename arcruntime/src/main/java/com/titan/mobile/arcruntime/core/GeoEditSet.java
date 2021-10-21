package com.titan.mobile.arcruntime.core;

import com.esri.arcgisruntime.geometry.Geometry;
import com.titan.mobile.arcruntime.fea.SpaFeature;
import com.titan.mobile.arcruntime.util.NotifyArrayList;


public class GeoEditSet extends BaseContainer {

    private SpaFeature spaFeature;
    private NotifyArrayList<Geometry> cacheStack;
    private NotifyArrayList<Geometry> backStack;

    public GeoEditSet() {
        cacheStack = new NotifyArrayList<>();
        backStack = new NotifyArrayList<>();
    }

    public SpaFeature getSpaFeature() {
        return spaFeature;
    }

    public GeoEditSet setSpaFeature(SpaFeature spaFeature) {
        this.spaFeature = spaFeature;
        addGeometry(spaFeature.geometry());
        return this;
    }

    public GeoEditSet addGeometry(Geometry geometry) {
        if (cacheStack != null) cacheStack.add(geometry);
        return this;
    }

    public Geometry geometry() {
        return cacheStack.getLastItem();
    }

    public void clear() {
        cacheStack.clear();
        backStack.clear();
        spaFeature = null;
    }

    public GeoEditSet canDo() {
        try {
            cacheStack.add(backStack.removeLastItem());
        } catch (Exception e) {
            System.err.println("ReDo无法继续操作");
        } finally {
            return this;
        }
    }

    public GeoEditSet unDo() {
        try {
            backStack.add(cacheStack.removeLastItem());
        } catch (Exception e) {
            System.err.println("UnDo无法继续操作");
        } finally {
            return this;
        }
    }
}
