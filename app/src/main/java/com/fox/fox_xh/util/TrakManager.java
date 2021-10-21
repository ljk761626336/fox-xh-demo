package com.fox.fox_xh.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.util.Log;

import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.PolylineBuilder;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.location.LocationDataSource;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.fox.foxdata.remote.http.custom.NetEasyReq;
import com.fox.foxdata.remote.http.entity.BaseRes;
import com.fox.foxdata.remote.http.listen.NetResListen;
import com.mobile.foxapp.location.ServiceLocation;
import com.titan.mobile.arcruntime.util.SPfUtil;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description:
 * @Author: ljk
 * @CreateDate: 2021/10/20 14:36
 */
public class TrakManager {
    private GraphicsOverlay trakOverlay = new GraphicsOverlay();
    /**
     * 开始记录轨迹后 变化路线及点
     */
    private Graphic trakGraphic;

    private SpatialReference spatialReference = SpatialReference.create(4326);

    private SpatialReference webMocat = SpatialReference.create(3857);

    private PointCollection lineCollection = new PointCollection(webMocat);
    private PointCollection pointsCollection = new PointCollection(spatialReference);

    private Context context;
    private MapView mapView;


    public TrakManager(MapView mapView) {
        this.mapView = mapView;
        this.context = mapView.getContext();
        this.mapView.getGraphicsOverlays().add(trakOverlay);
    }


    public void setInvadit() {
        pointsCollection = new PointCollection(spatialReference);
        lineCollection = new PointCollection(webMocat);
        trakGraphic = null;
    }

    private void sendPoint(Point point, boolean stop) {
        Flowable.create(new FlowableOnSubscribe<Point>() {
            @Override
            public void subscribe(FlowableEmitter<Point> emitter) throws Exception {
                if (point == null) return;
                emitter.onNext(point);
                if (stop) {
                    emitter.onComplete();
                }
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Point>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Point point) {
                        Log.d("TAG", " 观察者 Observer的工作线程是: " + Thread.currentThread().getName());
                        Log.e("TAG", point.getX() + "接收" + point.getY());
                        //PatrolCoreUtil.getInstance(context).addData(point);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 显示行进路线
     */
    public void showLine(Point point, float accury) {
        if (pointsCollection != null) {
            for (Point p : pointsCollection) {
                if (p.getX() == point.getX() && p.getY() == point.getY()) return;
            }
        } else {
            pointsCollection = new PointCollection(spatialReference);
        }
        pointsCollection.add(point);

        Point mappoint = (Point) GeometryEngine.project(point, webMocat);
        lineCollection.add(mappoint);
        if (lineCollection.size() < 2) {
            return;
        }

        PolylineBuilder builder = new PolylineBuilder(lineCollection);
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 3);

        int size = trakOverlay.getGraphics().size();
        //size 为0 时graphic 可能已经被清除
        if (trakGraphic == null || size == 0) {
            trakGraphic = new Graphic(builder.toGeometry());
            trakGraphic.setSymbol(lineSymbol);
            trakOverlay.getGraphics().add(trakGraphic);
        } else {
            trakGraphic.setGeometry(builder.toGeometry());
        }
    }


    private void sendServer(Point point, int accury) {
        Map map = new HashMap();
        map.put("point", point);
        map.put("accury", accury);
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(FlowableEmitter<Object> emitter) throws Exception {
                emitter.onNext(map);
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER).subscribe(new Subscriber<Object>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Object o) {
                HashMap map = (HashMap) o;
                Point point1 = (Point) map.get("point");
                int accury1 = (int) map.get("accury");
                //PatrolCoreUtil.getInstance(context).addData(point1, accury1);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });

    }


}

