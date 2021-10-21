/**
 * Copyright 2019 bejson.com
 */
package com.titan.mobile.arcruntime.layer.info;

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2019-06-04 14:6:24
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class DrawingInfo implements Serializable {

    private Renderer renderer;
    private int transparency;
    private List<LabelingInfo> labelingInfo;;

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public void setTransparency(int transparency) {
        this.transparency = transparency;
    }

    public int getTransparency() {
        return transparency;
    }

    public List<LabelingInfo> getLabelingInfo() {
        return labelingInfo;
    }

    public void setLabelingInfo(List<LabelingInfo> labelingInfo) {
        this.labelingInfo = labelingInfo;
    }
}