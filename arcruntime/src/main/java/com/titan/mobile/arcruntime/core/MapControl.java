package com.titan.mobile.arcruntime.core;

import android.location.Location;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.location.LocationDataSource;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapScaleChangedEvent;
import com.esri.arcgisruntime.mapping.view.MapScaleChangedListener;
import com.mobile.foxjar.msg.FuncManager;
import com.mobile.foxjar.msg.FuncNoParamWithResult;
import com.mobile.foxjar.msg.FuncWithParamNoResult;
import com.mobile.foxjar.msg.FuncWithParamWithResult;
import com.titan.mobile.arcruntime.util.ObjectUtil;
import com.titan.mobile.arcruntime.util.SPfUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by zy on 2019/5/9.
 * 地图操作控制
 */

public class MapControl extends BaseContainer implements MapScaleChangedListener, LocationDisplay.LocationChangedListener {

    /**
     * 当前地图的Viewpoint
     */
    private Viewpoint viewpoint;
    /**
     * 当前用户所在位置
     */
    private Location location;

    /**
     * arcgis 系统自带位置
     */
    private LocationDataSource.Location arcLocation;
    /**
     *
     */
    private LocationDisplay locationDisplay;

    private ArcLocationChangedListener arcLocationChangedListener;

    public ArcLocationChangedListener getArcLocationChangedListener() {
        return arcLocationChangedListener;
    }

    public void setArcLocationChangedListener(ArcLocationChangedListener arcLocationChangedListener) {
        this.arcLocationChangedListener = arcLocationChangedListener;
    }

    @Override
    public void create(ArcMap arcMap) {
        super.create(arcMap);
        arcMap.getMapView().addMapScaleChangedListener(this);
        dispatchFun();
    }

    @Override
    public void ready() {
        String json = SPfUtil.readT(context, "VIEW_POINT_STATE");
        if (json != null) {
            new Handler().postDelayed(() -> {
                viewpoint = Viewpoint.fromJson(json);
                setViewPoint();
            }, 1000);
        }
    }

    public void zoomIn() {
        mapView.setViewpointScaleAsync(mapView.getMapScale() * 0.5);
        viewpoint = mapView.getCurrentViewpoint(Viewpoint.Type.BOUNDING_GEOMETRY);//存储viewpoint
    }

    public void zoomOut() {
        mapView.setViewpointScaleAsync(mapView.getMapScale() * 2);
        viewpoint = mapView.getCurrentViewpoint(Viewpoint.Type.BOUNDING_GEOMETRY); //存储viewpoint
    }

    public void zoomG(Geometry... geometries) {
        if (geometries == null) return;
        List<Geometry> list = ObjectUtil.Collect.array2List(geometries);
        if (list.size() == 1) {
            Geometry geometry = GeometryEngine.simplify(list.get(0));
            if (geometry.isEmpty()) return;
            if (geometry instanceof Point) {
                viewpoint = new Viewpoint((Point) geometry, 2000d);
                setViewPoint();
            } else {
                viewpoint = new Viewpoint(geometry.getExtent());
                setViewPoint();
            }
        } else {
            Envelope envelope = GeometryEngine.combineExtents(list);
            if (envelope.isEmpty()) return;
            viewpoint = new Viewpoint(envelope);
            setViewPoint();
        }

    }

    public void zoomG(List<Geometry> geometries) {
        if (geometries == null) return;
        Envelope envelope = GeometryEngine.combineExtents(geometries);
        viewpoint = new Viewpoint(envelope);
        setViewPoint();
    }

    public void zoomF(Feature... features) {
        if (features == null) return;
        List<Geometry> list = new ArrayList<>();
        for (int i = 0; i < features.length; i++) {
            list.add(features[i].getGeometry());
        }
        zoomG(list);
    }

    public void zoomF(List<Feature> features) {
        if (features == null) return;
        List<Geometry> list = new ArrayList<>();
        for (int i = 0; i < features.size(); i++) {
            list.add(features.get(i).getGeometry());
        }
        zoomG(list);
    }

    public void zoomF(Feature feature) {
        if (feature == null) return;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                zoomG(feature.getGeometry());
            }
        }, 100);
    }

    public void zoomP(double lon, double lat) {
        Point point = (Point) GeometryEngine.project(new Point(lon, lat), arcMap.getMapView().getSpatialReference());
        if (point == null) return;
        //存储viewpoint
        viewpoint = new Viewpoint(point, 5000);
        setViewPoint();
    }

    public void zoomP(Point point) {
        Point mPoint = (Point) GeometryEngine.project(point, arcMap.getMapView().getSpatialReference());
        if (mPoint == null) return;
        //存储viewpoint
        viewpoint = new Viewpoint(mPoint, 5000);
        setViewPoint();
    }

    public void locCurrent() {
        if (location == null)
            Toast.makeText(context, "未能获取位置信息,请确认位置服务是否开启!", Toast.LENGTH_LONG).show();
        Point gpsPoint = new Point(location.getLongitude(), location.getLatitude(), SpatialReferences.getWgs84());
        Point point = (Point) GeometryEngine.project(gpsPoint, arcMap.getMapView().getSpatialReference());
        if (point == null) return;
        //存储viewpoint
        viewpoint = new Viewpoint(point, 5000);
        setViewPoint();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocationDataSource.Location getArcLocation() {
        return arcLocation;
    }

    public void setArcLocation(LocationDataSource.Location arcLocation) {
        this.arcLocation = arcLocation;
    }

    Map map = new HashMap();

    void dispatchFun() {

        FuncManager.getInstance().addFunc(new FuncWithParamNoResult("MAP_STATE_GPS_JSON") {
            @Override
            public void function(Object json) {
                Map esriMap = ObjectUtil.Common.convert(json, Map.class);
                if (esriMap != null && esriMap.containsKey("location")) {
                    map = (Map) esriMap.get("location");
                }
            }
        });

        FuncManager.getInstance().addFunc(new FuncNoParamWithResult<Location>("BC.ACTION.LOC") {
            @Override
            public Location function() {
                if (arcLocation == null) {
                    Location location = new Location("");
                    if (map.containsKey("mAltitude")) {
                        location.setAltitude(ObjectUtil.Common.convert(map.get("mAltitude"), Double.class));
                    } else {
                        location.setAltitude(0);
                    }

                    location.setLatitude(0);
                    location.setLongitude(0);
                    return location;
                }
                Point point = arcLocation.getPosition();
                if (point == null) return null;
                Location _l = new Location("");
                if (map.containsKey("mAltitude")) {
                    _l.setAltitude(ObjectUtil.Common.convert(map.get("mAltitude"), Double.class));
                } else {
                    _l.setAltitude(point.getZ());
                }
                _l.setLatitude(point.getY());
                _l.setLongitude(point.getX());
                return _l;
            }
        });
    }

    public MapControl initDefaultLocation() {
        locationDisplay = arcMap.getMapView().getLocationDisplay();
//        locationDisplay.setNavigationPointHeightFactor(0.5f);//设置定位模式
//        locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
//        locationDisplay.setInitialZoomScale(20000);
        locationDisplay.addLocationChangedListener(this);
        return this;
    }

    public void startLocation(long delay) {
        new Handler().postDelayed(() -> {
            if (locationDisplay != null) {
                locationDisplay.startAsync();
            }
        }, delay);
    }

    public void useDefaultLocation(long delay) {
        new Handler().postDelayed(() -> {
            if (locationDisplay != null) {
                locationDisplay.setNavigationPointHeightFactor(0.5f);//设置定位模式
                locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
                locationDisplay.setInitialZoomScale(20000);
                locationDisplay.startAsync();
            }
        }, delay);
    }

    void resume() {
        setViewPoint();
    }

    void pause() {
        viewpoint = mapView.getCurrentViewpoint(Viewpoint.Type.BOUNDING_GEOMETRY);
    }

    synchronized void setViewPoint() {
        new Handler().postDelayed(() -> {
            if (viewpoint == null) return;
            mapView.setViewpoint(viewpoint);
        }, 50);
    }

    public Viewpoint getViewPoint() {
        return viewpoint;
    }

    public void syncCameraInfo() {
        viewpoint = mapView.getCurrentViewpoint(Viewpoint.Type.CENTER_AND_SCALE);
        if (cameraListeners == null) return;
        for (IMapCameraListener camera : cameraListeners) {
            if (camera == null) continue;
            camera.mapCamera(viewpoint);
        }
    }

    @Override
    public void mapScaleChanged(MapScaleChangedEvent mapScaleChangedEvent) {
        syncCameraInfo();
    }


    @Override
    public void destroy() {
        super.destroy();
        //保存地图状态
        SPfUtil.putT(context, "VIEW_POINT_STATE", viewpoint.toJson());
    }

    private List<IMapCameraListener> cameraListeners = new ArrayList<>();

    @Override
    public void onLocationChanged(LocationDisplay.LocationChangedEvent event) {
        if (event == null) return;
        setArcLocation(event.getLocation());
        if (arcLocationChangedListener != null) {
            arcLocationChangedListener.onLocationChanged(event);
        }
    }

    public interface IMapCameraListener {
        public void mapCamera(Viewpoint viewpoint);
    }

    public void addCameraListener(IMapCameraListener cameraListener) {
        cameraListeners.add(cameraListener);
    }

    public void removeCameraListener(IMapCameraListener cameraListener) {
        cameraListeners.remove(cameraListener);
    }

    public void addArcLocationListener(ArcLocationChangedListener arcLocationChangedListener) {
        setArcLocationChangedListener(arcLocationChangedListener);
    }
}
