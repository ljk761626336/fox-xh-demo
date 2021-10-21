package com.titan.mobile.arcruntime.core;

import com.esri.arcgisruntime.mapping.view.LocationDisplay;

public interface ArcLocationChangedListener {

    void onLocationChanged(LocationDisplay.LocationChangedEvent event);

}
