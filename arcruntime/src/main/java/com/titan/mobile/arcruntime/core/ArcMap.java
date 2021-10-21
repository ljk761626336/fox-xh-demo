package com.titan.mobile.arcruntime.core;

import android.content.Context;
import android.content.res.TypedArray;
import android.service.quicksettings.Tile;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.layers.RasterLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.loadable.LoadStatusChangedEvent;
import com.esri.arcgisruntime.loadable.LoadStatusChangedListener;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.LayerList;
import com.esri.arcgisruntime.mapping.view.BackgroundGrid;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.raster.Raster;
import com.esri.arcgisruntime.util.ListChangedEvent;
import com.esri.arcgisruntime.util.ListChangedListener;
import com.titan.mobile.arcruntime.R;
import com.titan.mobile.arcruntime.event.ArcMapEventDispatch;
import com.titan.mobile.arcruntime.event.IArcMapEvent;
import com.titan.mobile.arcruntime.layer.core.LayerNode;
import com.titan.mobile.arcruntime.layer.openlayer.BaseImgLayer;
import com.titan.mobile.arcruntime.layer.openlayer.TileInterceptor;
import com.titan.mobile.arcruntime.layer.openlayer.enums.Type;
import com.titan.mobile.arcruntime.layer.util.DataSourceRead;
import com.titan.mobile.arcruntime.util.ArcUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zy on 2019/5/9.
 */

public class ArcMap extends FrameLayout implements IContainer, LoadStatusChangedListener, ArcGISMap.BasemapChangedListener {

    public static ArcMap arcMap;

    public static ArcMap getInstance(MapView mapView) {
        if (arcMap == null) {
            synchronized (ArcMap.class) {
                if (arcMap == null) {
                    arcMap = new ArcMap(mapView);
                }
            }
        }
        return arcMap;
    }

    private boolean canRotate = false;
    private boolean canOffline = false;
    private Context context;
    /**
     * 卫星影像地图
     */
    private List<String> baseMapUrls = new ArrayList<>();
    private List<Basemap> basemapList = new ArrayList<>();

    private LayerContainer layerContainer = new LayerContainer();
    private RenderContainer renderContainer = new RenderContainer();
    private MapControl mapControl = new MapControl();

    private SketchStream sketchStream;

    private GraphicContainer graphicContainer = new GraphicContainer();
    private SelectionSet selectionSet = new SelectionSet();
    private OperateSet operateSet = new OperateSet();
    private GeoEditSet geoEditSet = new GeoEditSet();

    private MapView mapView;

    private IMapReady iMapReady;

    //事件
    private ArcMapEventDispatch arcMapEventDispatch;

    private List<Feature> sectFeatures = new ArrayList<>();

    public ArcMapEventDispatch getArcMapEventDispatch() {
        return arcMapEventDispatch;
    }

    public List<Feature> getSectFeatures() {
        return sectFeatures;
    }

    public void setSectFeatures(List<Feature> sectFeatures) {
        this.sectFeatures = sectFeatures;
    }

    public ArcMap create(MapView mapView) {
        initMap(mapView);
        create(this);
        arcMap = this;
        return this;
    }

    public ArcMap(MapView mapView) {
        super(mapView.getContext());
        this.context = mapView.getContext();
        init(mapView);
    }

    public ArcMap(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ArcMap(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcMap);
        canRotate = typedArray.getBoolean(R.styleable.ArcMap_canRotate, false);
        canOffline = typedArray.getBoolean(R.styleable.ArcMap_canOffline, false);
        typedArray.recycle();

        this.context = context;
        init();
    }

    public ArcMap(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    void init() {
        initMap();
        create(this);
        arcMap = this;
    }

    void init(MapView mapView) {
        initMap(mapView);
        create(this);
        arcMap = this;
    }

    /**隐藏水印*/
    void setRuntime() {
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud8065403504,none,RP5X0H4AH7CLJ9HSX018");
    }

    void initMap() {
        if (!canOffline) {
            //ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud8065403504,none,RP5X0H4AH7CLJ9HSX018");
        }
        this.removeAllViews();
        this.mapView = new MapView(context);
        this.mapView.setAttributionTextVisible(false);
        this.addView(mapView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.arcMapEventDispatch = new ArcMapEventDispatch(this);
        //注册地图事件
        this.mapView.setOnTouchListener(arcMapEventDispatch);
        this.sketchStream = new SketchStream(this);
    }

    void initMap(MapView mapView) {
        this.mapView = mapView;
        if (!canOffline) {
            //ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud8065403504,none,RP5X0H4AH7CLJ9HSX018");
        }
        this.removeAllViews();
        this.mapView.setAttributionTextVisible(false);
        this.arcMapEventDispatch = new ArcMapEventDispatch(this);
        //注册地图事件
        this.mapView.setOnTouchListener(arcMapEventDispatch);
        this.sketchStream = new SketchStream(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev == null) return false;
        IArcMapEvent iMapEvent = arcMapEventDispatch.getCurIMapEvent();
        boolean continueDispatch;
        if (iMapEvent == null) return super.dispatchTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            continueDispatch = iMapEvent.onTouchStart(ev);
            if (continueDispatch) return true;
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            continueDispatch = iMapEvent.onTouchMoving(ev);
            if (continueDispatch) return true;
        }
        if (ev.getAction() == MotionEvent.ACTION_UP ||
                ev.getAction() == MotionEvent.ACTION_CANCEL) {
            continueDispatch = iMapEvent.onTouchCancel(ev);
            if (continueDispatch) return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setEvent(IArcMapEvent event) {
        arcMapEventDispatch.setCurIMapEvent(event);
        this.mapView.setOnTouchListener(arcMapEventDispatch);
    }

    private TileInterceptor interceptor;
    private Type type = Type.ARCGIS_IMAGE_MERCATOR;

    public void mapLoad(IMapReady iMapReady, TileInterceptor interceptor, List<String> baseMapUrls) {
        this.iMapReady = iMapReady;
        this.interceptor = interceptor;
        ArcGISMap gisMap = new ArcGISMap(); //加载底图
        gisMap.addLoadStatusChangedListener(this);
        if (baseMapUrls == null || baseMapUrls.size() == 0) {
            BaseImgLayer baseImageLayer = BaseImgLayer.createSmart(type, interceptor);
            Basemap basemap = new Basemap(baseImageLayer);
            basemap.setName("初始影像");
            gisMap.setBasemap(basemap);
        } else {
            ArcGISTiledLayer tiledLayer = new ArcGISTiledLayer(baseMapUrls.get(0));
            Basemap basemap = new Basemap(tiledLayer);
            basemap.setName("初始影像");
            gisMap.setBasemap(basemap);
        }
        mapView.setMap(gisMap);
        //隐藏网格
        initGrid();
    }

    public void mapLoad(IMapReady iMapReady, TileInterceptor interceptor) {
        this.iMapReady = iMapReady;
        this.interceptor = interceptor;
        ArcGISMap gisMap = new ArcGISMap(); //加载底图
        gisMap.addLoadStatusChangedListener(this);
        if (baseMapUrls == null || baseMapUrls.size() == 0) {
            BaseImgLayer baseImageLayer = BaseImgLayer.createSmart(type, interceptor);
            gisMap.setBasemap(new Basemap(baseImageLayer));
        } else {
            ArcGISTiledLayer tiledLayer = new ArcGISTiledLayer(baseMapUrls.get(0));
            gisMap.setBasemap(new Basemap(tiledLayer));
        }
        mapView.setMap(gisMap);
        //隐藏网格
        initGrid();
    }

    public void reMapLoad(IMapReady iMapReady, TileInterceptor interceptor) {
        this.iMapReady = iMapReady;
        this.interceptor = interceptor;
        ArcGISMap gisMap = new ArcGISMap(); //加载底图
        gisMap.addLoadStatusChangedListener(this);
        gisMap.addBasemapChangedListener(this);
        if (baseMapUrls == null || baseMapUrls.size() == 0) {
            BaseImgLayer baseImageLayer = BaseImgLayer.createSmart(type, interceptor);
            gisMap.setBasemap(new Basemap(baseImageLayer));
        } else {
            ArcGISTiledLayer tiledLayer = new ArcGISTiledLayer(baseMapUrls.get(0));
            gisMap.setBasemap(new Basemap(tiledLayer));
        }
        mapView.setMap(gisMap);
        //隐藏网格
        initGrid();
    }

    private void initGrid() {
        //隐藏网格
        BackgroundGrid mainBackgroundGrid = new BackgroundGrid();
        mainBackgroundGrid.setColor(0xffffffff);
        mainBackgroundGrid.setGridLineColor(0xffffffff);
        mainBackgroundGrid.setGridLineWidth(0);
        mapView.setBackgroundGrid(mainBackgroundGrid);
        mapView.setAttributionTextVisible(false);
    }

    /**
     * 添加本地影像底图
     */
    public void addMapLayer(Layer layer) {
        mapView.getMap().getBasemap().getBaseLayers().add(layer);
        layer.addLoadStatusChangedListener(new LoadStatusChangedListener() {
            @Override
            public void loadStatusChanged(LoadStatusChangedEvent loadStatusChangedEvent) {
                System.out.println(loadStatusChangedEvent.getNewLoadStatus().toString());
                if (loadStatusChangedEvent.getNewLoadStatus() == LoadStatus.LOADED) {
                    Envelope envelope = layer.getFullExtent();
                    if (envelope != null) {
                        arcMap.getMapControl().zoomP(layer.getFullExtent().getCenter());
                    }
                }
            }
        });
    }

    public void changeBaseMap(Layer layer) {

        List<LayerNode> layerNodes = arcMap.getLayerContainer().getLayerNodes();
        Iterator<LayerNode> it = layerNodes.iterator();
        while (it.hasNext()) {
            LayerNode node = it.next();
            it.remove();
            arcMap.getLayerContainer().removeLayerNode(node);
        }
        arcMap.getLayerContainer().getLayerNodes().clear();

        ArcGISMap gisMap = new ArcGISMap(); //加载底图
        //gisMap.addLoadStatusChangedListener(this);
        gisMap.addBasemapChangedListener(this);
        gisMap.setBasemap(new Basemap(layer));
        mapView.setMap(gisMap);
        ready();
    }

    public void reAddGisMap() {
        ArcGISMap gisMap = new ArcGISMap(); //加载底图
        gisMap.addLoadStatusChangedListener(this);
        gisMap.addBasemapChangedListener(this);
        if (baseMapUrls == null || baseMapUrls.size() == 0) {
            BaseImgLayer baseImageLayer = BaseImgLayer.createSmart(type, interceptor);
            gisMap.setBasemap(new Basemap(baseImageLayer));
        } else {
            ArcGISTiledLayer tiledLayer = new ArcGISTiledLayer(baseMapUrls.get(0));
            gisMap.setBasemap(new Basemap(tiledLayer));
        }
        mapView.setMap(gisMap);
    }

    public void resetOldBaseMap() {
        if (basemapList.size() == 0) {
            return;
        }
        Basemap basemap = basemapList.get(0);
        mapView.getMap().setBasemap(basemap);
        mapView.getMap().addBasemapChangedListener(this);
    }

    public void changeBaseMap(File[] files) {
        LayerList layerList = mapView.getMap().getBasemap().getBaseLayers();
        for (File file : files) {
            if (!(file.getAbsolutePath().endsWith(".tpk") || file.getAbsolutePath().endsWith(".tif")))
                continue;

            if (file.getAbsolutePath().endsWith(".tpk")) {
                ArcGISTiledLayer tiledLayer = new ArcGISTiledLayer(file.getAbsolutePath());
                layerList.add(tiledLayer);
                tiledLayer.setVisible(true);
                continue;
            }

            if (file.getAbsolutePath().endsWith(".tif")) {
                Raster raster = new Raster(file.getAbsolutePath());
                RasterLayer rasterLayer = new RasterLayer(raster);
                layerList.add(rasterLayer);
                rasterLayer.setVisible(true);
            }
        }
    }

    public void addBaseMap(Layer layer) {
        ArcGISMap _arcGISMap = mapView.getMap();
        // _arcGISMap.getBasemap().getBaseLayers().clear();
        _arcGISMap.getBasemap().getBaseLayers().add(layer);

        DataSourceRead.loadLayer(layer);
        Geometry geometry = layer.getFullExtent();

        getMapControl().zoomG(geometry);
    }

    public void changeBaseMap(Type type) {
        this.type = type;
        ArcGISMap arcGISMap = mapView.getMap();
        BaseImgLayer baseImageLayer = BaseImgLayer.createSmart(type, interceptor);
        arcGISMap.setBasemap(new Basemap(baseImageLayer));
    }

    public void refreshBaseMap(Type type) {
        if (type != this.type) return;
        ArcGISMap arcGISMap = mapView.getMap();
        BaseImgLayer baseImageLayer = BaseImgLayer.createSmart(type, interceptor);
        arcGISMap.setBasemap(new Basemap(baseImageLayer));
    }

    public Type getBaseImgType() {
        return type;
    }

    @Override
    public void loadStatusChanged(LoadStatusChangedEvent loadStatusChangedEvent) {
        if (LoadStatus.LOADING == loadStatusChangedEvent.getNewLoadStatus()) {
            System.out.println(">>>>>>>:LOADING");
        }
        if (LoadStatus.NOT_LOADED == loadStatusChangedEvent.getNewLoadStatus()) {
            System.out.println(">>>>>>>:NOT_LOADED");
        }
        if (LoadStatus.LOADED == loadStatusChangedEvent.getNewLoadStatus()) {
            System.out.println(">>>>>>>:LOADED");
            ready();
            if (iMapReady != null) iMapReady.onMapReady();
        }
        if (loadStatusChangedEvent.getNewLoadStatus() == LoadStatus.FAILED_TO_LOAD) {
            System.out.println(">>>>>>>:FAILED_TO_LOAD");
        }
    }

    @Override
    public void create(ArcMap arcMap) {
        layerContainer.create(arcMap);
        renderContainer.create(arcMap);
        selectionSet.create(arcMap);
        operateSet.create(arcMap);
        geoEditSet.create(arcMap);
        mapControl.create(arcMap);
        graphicContainer.create(arcMap);
    }

    @Override
    public void ready() {
        layerContainer.ready();
        renderContainer.ready();
        selectionSet.ready();
        operateSet.ready();
        geoEditSet.ready();
        mapControl.ready();
        graphicContainer.ready();
    }

    @Override
    public void destroy() {
        layerContainer.destroy();
        renderContainer.destroy();
        mapControl.destroy();
        selectionSet.destroy();
        operateSet.destroy();
        geoEditSet.destroy();
        graphicContainer.destroy();
    }

    public void resume() {
        // mapView.resume();
        mapControl.resume();
    }

    public void pause() {
        //  mapView.pause();
        mapControl.pause();
    }


    @Override
    public void destroyDrawingCache() {
        super.destroyDrawingCache();
        //destroy();
    }

    @Override
    public void basemapChanged(ArcGISMap.BasemapChangedEvent basemapChangedEvent) {
        Basemap oldBasemap = basemapChangedEvent.getOldBasemap();
        basemapList.add(oldBasemap);
    }

    public interface IMapReady {
        public void onMapReady();
    }

    public LayerContainer getLayerContainer() {
        return layerContainer;
    }

    public GraphicContainer getGraphicContainer() {
        return graphicContainer;
    }

    public RenderContainer getRenderContainer() {
        return renderContainer;
    }

    public SelectionSet getSelectionSet() {
        return selectionSet;
    }

    public OperateSet getOperateSet() {
        return operateSet;
    }

    public GeoEditSet getGeoEditSet() {
        return geoEditSet;
    }

    public MapControl getMapControl() {
        return mapControl;
    }

    public SketchStream getSketchStream() {
        return sketchStream;
    }

    public MapView getMapView() {
        return mapView;
    }

    public Point screenToLocation(MotionEvent e) {
        return mapView.screenToLocation(new android.graphics.Point((int) e.getX(), (int) e.getY()));
    }

    public Point screenToLocation(int x, int y) {
        return mapView.screenToLocation(new android.graphics.Point(x, y));
    }

    /**
     * **************************************地图属性设置*******************************************
     */
    public boolean isCanRotate() {
        return canRotate;
    }

    public void setCanRotate(boolean canRotate) {
        this.canRotate = canRotate;
    }

    public boolean isCanOffline() {
        return canOffline;
    }

    public void setCanOffline(boolean canOffline) {
        this.canOffline = canOffline;
    }
}
