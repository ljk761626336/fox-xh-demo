/**
 * Copyright 2019 bejson.com
 */
package com.titan.mobile.arcruntime.layer.info;

import java.io.Serializable;

/**
 * Auto-generated: 2019-06-04 14:6:24
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Extent implements Serializable {

    private double xmin;
    private double ymin;
    private double xmax;
    private double ymax;
    private SpatialReference spatialReference;

    public void setXmin(double xmin) {
        this.xmin = xmin;
    }

    public double getXmin() {
        return xmin;
    }

    public void setYmin(double ymin) {
        this.ymin = ymin;
    }

    public double getYmin() {
        return ymin;
    }

    public void setXmax(double xmax) {
        this.xmax = xmax;
    }

    public double getXmax() {
        return xmax;
    }

    public void setYmax(double ymax) {
        this.ymax = ymax;
    }

    public double getYmax() {
        return ymax;
    }

    public void setSpatialReference(SpatialReference spatialReference) {
        this.spatialReference = spatialReference;
    }

    public SpatialReference getSpatialReference() {
        return spatialReference;
    }

}