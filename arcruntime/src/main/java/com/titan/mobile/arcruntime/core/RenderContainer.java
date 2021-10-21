package com.titan.mobile.arcruntime.core;


import android.graphics.Color;

import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;

import java.util.HashMap;
import java.util.Map;

public class RenderContainer extends BaseContainer {
    SimpleMarkerSymbol symbolMark = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.YELLOW, 10);
    SimpleLineSymbol symbolLine = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.YELLOW, 3);
    SimpleFillSymbol symbolArea = new SimpleFillSymbol(SimpleFillSymbol.Style.NULL, Color.YELLOW, symbolLine);

    GraphicsOverlay pointOverlay;
    GraphicsOverlay lineOverlay;
    GraphicsOverlay polygonOverlay;

    @Override
    public void create(ArcMap arcMap) {
        super.create(arcMap);
        this.pointOverlay = new GraphicsOverlay();
        this.lineOverlay = new GraphicsOverlay();
        this.polygonOverlay = new GraphicsOverlay();

        this.pointOverlay.setRenderer(new SimpleRenderer(symbolMark));
        this.lineOverlay.setRenderer(new SimpleRenderer(symbolLine));
        this.polygonOverlay.setRenderer(new SimpleRenderer(symbolArea));


        this.mapView.getGraphicsOverlays().add(pointOverlay);
        this.mapView.getGraphicsOverlays().add(lineOverlay);
        this.mapView.getGraphicsOverlays().add(polygonOverlay);
    }

    public void add(Geometry geometry) {
        Graphic graphic = new Graphic(geometry);
        if (geometry instanceof Point) {
            pointOverlay.getGraphics().add(graphic);
        }
        if (geometry instanceof Polyline) {
            lineOverlay.getGraphics().add(graphic);
        }
        if (geometry instanceof Polygon) {
            polygonOverlay.getGraphics().add(graphic);
        }
    }


    public void remove(Geometry geometry) {

    }

    public boolean hasFeatures() {
        return false;
    }

    public void clear() {
        pointOverlay.getGraphics().clear();
        lineOverlay.getGraphics().clear();
        polygonOverlay.getGraphics().clear();
    }
}