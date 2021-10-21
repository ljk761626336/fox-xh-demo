package com.titan.mobile.arcruntime.core;

import android.graphics.Color;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.geometry.ImmutablePart;
import com.esri.arcgisruntime.geometry.ImmutablePartCollection;
import com.esri.arcgisruntime.geometry.ImmutablePointCollection;
import com.esri.arcgisruntime.geometry.Multipoint;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.titan.mobile.arcruntime.util.ArcUtil;
import com.titan.mobile.arcruntime.util.TransformUtil;
import com.vividsolutions.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by zy on 2019/5/12.
 */

public class GraphicContainer extends BaseContainer {

    SimpleMarkerSymbol symbolMark = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.YELLOW, 10);
    SimpleMarkerSymbol editSymbolMark = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 10);
    SimpleLineSymbol symbolLine = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.YELLOW, 3);
    SimpleFillSymbol symbolArea = new SimpleFillSymbol(SimpleFillSymbol.Style.NULL, Color.YELLOW, symbolLine);
    TextSymbol textSymbol = new TextSymbol();

    GraphicsOverlay editOverlay;
    GraphicsOverlay pointOverlay;
    GraphicsOverlay lineOverlay;
    GraphicsOverlay polygonOverlay;
    GraphicsOverlay textOverlay;

    public GraphicsOverlay getEditOverlay() {
        return editOverlay;
    }

    public void setEditOverlay(GraphicsOverlay editOverlay) {
        this.editOverlay = editOverlay;
    }

    public GraphicsOverlay getPointOverlay() {
        return pointOverlay;
    }

    public void setPointOverlay(GraphicsOverlay pointOverlay) {
        this.pointOverlay = pointOverlay;
    }

    public GraphicsOverlay getLineOverlay() {
        return lineOverlay;
    }

    public void setLineOverlay(GraphicsOverlay lineOverlay) {
        this.lineOverlay = lineOverlay;
    }

    public GraphicsOverlay getPolygonOverlay() {
        return polygonOverlay;
    }

    public void setPolygonOverlay(GraphicsOverlay polygonOverlay) {
        this.polygonOverlay = polygonOverlay;
    }

    public GraphicsOverlay getTextOverlay() {
        return textOverlay;
    }

    public void setTextOverlay(GraphicsOverlay textOverlay) {
        this.textOverlay = textOverlay;
    }

    @Override
    public void create(ArcMap arcMap) {
        super.create(arcMap);
        this.editOverlay = new GraphicsOverlay();
        this.pointOverlay = new GraphicsOverlay();
        this.lineOverlay = new GraphicsOverlay();
        this.polygonOverlay = new GraphicsOverlay();
        this.textOverlay = new GraphicsOverlay();

        this.editOverlay.setRenderer(new SimpleRenderer(editSymbolMark));
        this.pointOverlay.setRenderer(new SimpleRenderer(symbolMark));
        this.lineOverlay.setRenderer(new SimpleRenderer(symbolLine));
        this.polygonOverlay.setRenderer(new SimpleRenderer(symbolArea));

        {
            textSymbol.setFontFamily("DroidSansFallback.ttf");
            textSymbol.setOffsetX(0);
            textSymbol.setOffsetY(10);
            textSymbol.setSize(15);
            textSymbol.setFontStyle(TextSymbol.FontStyle.NORMAL);
            textSymbol.setColor(Color.YELLOW);
        }

        this.textOverlay.setRenderer(new SimpleRenderer(textSymbol));

        this.mapView.getGraphicsOverlays().add(polygonOverlay);
        this.mapView.getGraphicsOverlays().add(lineOverlay);
        this.mapView.getGraphicsOverlays().add(pointOverlay);
        this.mapView.getGraphicsOverlays().add(editOverlay);
        this.mapView.getGraphicsOverlays().add(textOverlay);

    }

    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    private Point editPoint;
    private Geometry editGeometry;

    /**
     * geometry节点编辑
     *
     * @param geometry
     */
    public void geoEdit(Geometry geometry) {
        editPoint = null;
        editGeometry = geometry;
        List<Point> points = ArcUtil.Geo.getPoints(geometry);
        for (Point point : points) {
            add(point);
        }
        add(geometry);
    }


    /**
     * 当前编辑点
     *
     * @param point
     */
    public void startEditGeometry(Geometry geometry, Point point) {
        if (this.editPoint == null) { //高亮选中点
            this.editPoint = point;
            this.editGeometry = geometry;
            editOverlay.getGraphics().add(new Graphic(point));
        }
    }

    public GraphicContainer editGeometryPoint(Point newPoint) {
        editGeometry(editGeometry, editPoint, newPoint);

        return this;
    }

    private GraphicContainer editGeometry(Geometry geometry, Point selPoint, Point newPoint) {
        com.vividsolutions.jts.geom.Point jtsSelPoint = (com.vividsolutions.jts.geom.Point) TransformUtil.Convert.arcGeometry2JtsGeometry(selPoint);
        com.vividsolutions.jts.geom.Point jtsNewPoint = (com.vividsolutions.jts.geom.Point) TransformUtil.Convert.arcGeometry2JtsGeometry(newPoint);
        com.vividsolutions.jts.geom.Geometry jtsGeometry = TransformUtil.Convert.arcGeometry2JtsGeometry(geometry);
        Coordinate[] coordinates = jtsGeometry.getCoordinates();
        for (Coordinate coordinate : coordinates) {
            if (coordinate.x == jtsSelPoint.getX() &&
                    coordinate.y == jtsSelPoint.getY()) {
                coordinate.x = jtsNewPoint.getX();
                coordinate.y = jtsNewPoint.getY();
            }
        }
        editGeometry = TransformUtil.Convert.jtsGeometry2ArcGeometry(jtsGeometry, geometry.getSpatialReference().getWkid());
        return this;
    }

    public boolean isMoveTo() {
        return this.editPoint != null;
    }

    public Point getEditPoint() {
        return editPoint;
    }

    public Geometry getEditGeometry() {
        return editGeometry;
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

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

    public void add(Graphic graphic) {
        Geometry geometry = graphic.getGeometry();
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
        Graphic graphic = new Graphic(geometry);
        if (geometry instanceof Point) {
            pointOverlay.getGraphics().remove(graphic);
        }
        if (geometry instanceof Polyline) {
            lineOverlay.getGraphics().remove(graphic);
        }
        if (geometry instanceof Polygon) {
            polygonOverlay.getGraphics().remove(graphic);
        }
    }

    public void remove(Graphic graphic) {
        Geometry geometry = graphic.getGeometry();
        if (geometry instanceof Point) {
            pointOverlay.getGraphics().remove(graphic);
        }
        if (geometry instanceof Polyline) {
            lineOverlay.getGraphics().remove(graphic);
        }
        if (geometry instanceof Polygon) {
            polygonOverlay.getGraphics().remove(graphic);
        }
    }


    public void text(Geometry geometry) {
        Graphic graphic;
        double res;
        if (geometry instanceof Polygon) {
            res = GeometryEngine.area((Polygon) geometry);
            textSymbol.setText(String.format("%.2f", Math.abs(res)) + "㎡");
            graphic = new Graphic(geometry, textSymbol);
            textOverlay.getGraphics().add(graphic);
        }
        if (geometry instanceof Polyline) {
            res = GeometryEngine.length((Polyline) geometry);
            textSymbol.setText(String.format("%.2f", Math.abs(res)) + "m");
            graphic = new Graphic(geometry, textSymbol);
            textOverlay.getGraphics().add(graphic);
        }

        if (geometry instanceof Point) {
            Point point = (Point) geometry;
            textSymbol.setText(point.getX() + "m" + point.getY());
            graphic = new Graphic(geometry, textSymbol);
            textOverlay.getGraphics().add(graphic);
        }

    }

    public GraphicContainer clear() {
        pointOverlay.getGraphics().clear();
        editOverlay.getGraphics().clear();
        lineOverlay.getGraphics().clear();
        polygonOverlay.getGraphics().clear();
        textOverlay.getGraphics().clear();
        return this;
    }

}
