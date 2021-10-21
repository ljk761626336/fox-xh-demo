package com.titan.mobile.arcruntime.layer.core;

import java.io.Serializable;

public class LayerSetting implements Serializable {

    private boolean canQuery = true;

    public LayerSetting() {

    }

    public boolean isCanQuery() {
        return canQuery;
    }

    public LayerSetting setCanQuery(boolean canQuery) {
        this.canQuery = canQuery;
        return this;
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    public static LayerSetting create() {
        return new LayerSetting();
    }
}
