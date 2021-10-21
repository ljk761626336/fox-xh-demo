package com.titan.mobile.arcruntime.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.esri.arcgisruntime.geometry.AreaUnit;
import com.esri.arcgisruntime.geometry.AreaUnitId;
import com.esri.arcgisruntime.geometry.GeodeticCurveType;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.LinearUnit;
import com.esri.arcgisruntime.geometry.LinearUnitId;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.titan.mobile.arcruntime.core.draw.SimPath;
import com.titan.mobile.arcruntime.event.IArcMapEvent;
import com.titan.mobile.arcruntime.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 流状图形绘制工具,以长按地图开始绘制
 */
public class SketchStream extends FrameLayout implements IArcMapEvent {

    boolean interceptEvent;
    Context context;
    ArcMap arcMap;
    Type type;


    private PointF startP, moveP;
    private Paint paint;

    //绘制的矩形
    private RectF rect;
    //绘制的矩形
    private Circle circle;
    //绘制流状线
    private SimPath freeLine;
    //绘制流状面
    private Path freeArea;


    public SketchStream(@NonNull ArcMap arcMap) {
        super(arcMap.getContext());
        this.context = arcMap.getContext();
        this.arcMap = arcMap;
        init();
    }

    void init() {
        this.setWillNotDraw(false);
        this.arcMap.removeView(this);
        this.arcMap.addView(this);

        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth(5);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setColor(Color.RED);
    }

    public SketchStream activate(Type type) {
        this.type = type;
        arcMap.setEvent(this);
        return this;
    }

    public void deactivate() {
        this.type = null;
        arcMap.setEvent(null);
    }

    /**
     * 拦截地图事件
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouchStart(MotionEvent motionEvent) {
        interceptEvent = false;
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (type != Type.pSel &&
                type != Type.pSel_rect &&
                type != Type.pSel_circle &&
                type != Type.pSel_freeFace) return false;
        PointF pf = new PointF(e.getX(), e.getY());
        List<PointF> pfs = new ArrayList<>();
        pfs.add(pf);
        Geometry geometry = getGeometry(pfs, Type.pSel);

        if (iCallBack != null) {
            iCallBack.callBack(pfs, geometry);
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (type == Type.pSel) return;//点选不执行事件拦截
        SystemUtil.vibrate(context, 300);
        interceptEvent = true;
        this.rect = new RectF();
        this.circle = new Circle();
        this.freeLine = new SimPath();
        this.startP = new PointF(e.getX(), e.getY());

        if (type == Type.pSel_rect) {
            _onLongPress(Type.rect);
        } else if (type == Type.pSel_circle) {
            _onLongPress(Type.circle);
        } else if (type == Type.pSel_freeFace) {
            _onLongPress(Type.freeFace);
        } else {
            _onLongPress(type);
        }

    }

    private void _onLongPress(Type type) {
        if (type == Type.rect) {
            rect.left = (int) startP.x;
            rect.top = (int) startP.y;
        }
        if (type == Type.circle) {
            circle.setCenter(startP);
        }
        if (type == Type.freeLine) {
            freeLine.moveTo(startP.x, startP.y);
        }
        if (type == Type.freeFace) {
            freeLine.moveTo(startP.x, startP.y);
        }
    }

    @Override
    public boolean onTouchMoving(MotionEvent motionEvent) {
        if (!interceptEvent) return false;
        moveP = new PointF(motionEvent.getX(), motionEvent.getY());

        if (type == Type.pSel_rect) {
            _onTouchMoving(Type.rect);
        } else if (type == Type.pSel_circle) {
            _onTouchMoving(Type.circle);
        } else if (type == Type.pSel_freeFace) {
            _onTouchMoving(Type.freeFace);
        } else {
            _onTouchMoving(type);
        }

        invalidate();
        return true;
    }

    private void _onTouchMoving(Type type) {
        if (type == Type.rect) {
            rect.right = (int) moveP.x;
            rect.bottom = (int) moveP.y;
        }
        if (type == Type.circle) {
            circle.setPoint(moveP);
        }
        if (type == Type.freeLine) {
            Point point = arcMap.screenToLocation((int) moveP.x, (int) moveP.y);
            if(lenght(point) < 2){
                return;
            }
            freeLine.lineTo(moveP.x, moveP.y);
        }
        if (type == Type.freeFace) {
            freeLine.lineTo(moveP.x, moveP.y);
            freeArea = new Path(freeLine);
            freeArea.close();
        }
    }

    @Override
    public boolean onTouchCancel(MotionEvent motionEvent) {
        System.out.println("---------------onTouchCancel");
        if (!interceptEvent) return false;
        if (interceptEvent) {

            List<PointF> pfs;
            Geometry geometry;
            if (type == Type.pSel_rect) {
                pfs = getPoints(Type.rect);
                geometry = getGeometry(pfs, Type.rect);
            } else if (type == Type.pSel_circle) {
                pfs = getPoints(Type.circle);
                geometry = getGeometry(pfs, Type.circle);
            } else if (type == Type.pSel_freeFace) {
                pfs = getPoints(Type.freeFace);
                geometry = getGeometry(pfs, Type.freeFace);
            } else {
                pfs = getPoints(type);
                geometry = getGeometry(pfs, type);
            }
            if (iCallBack != null) iCallBack.callBack(pfs, geometry);
        }

        interceptEvent = false;
        startP = null;
        moveP = null;

        rect = null;
        circle = null;
        freeLine = null;
        invalidate();
        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (startP == null) {
            //
            //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            return;
        }
        if (type == Type.rect || type == Type.pSel_rect) {
            canvas.drawRect(rect, paint);
        }
        if (type == Type.circle || type == Type.pSel_circle) {
            canvas.drawCircle(circle.center.x, circle.center.y, circle.radius, paint);
        }
        if (type == Type.freeLine) {
            canvas.drawPath(freeLine, paint);
        }
        if (type == Type.freeFace || type == Type.pSel_freeFace) {
            canvas.drawPath(freeArea, paint);
        }
    }

    private List<PointF> getPoints(Type type) {
        if (type == Type.rect) {
            List<PointF> list = new ArrayList<>();
            list.add(new PointF(rect.left, rect.top));
            list.add(new PointF(rect.right, rect.top));
            list.add(new PointF(rect.right, rect.bottom));
            list.add(new PointF(rect.left, rect.top));
            return list;
        }
        if (type == Type.circle) {
            int steps = 100;
            List<PointF> positions = new ArrayList<>();
            for (int i = 0; i < steps; i++) {
                double x = circle.center.x + Math.cos(360 * i / steps) * circle.radius;
                double y = circle.center.y + Math.sin(360 * i / steps) * circle.radius;
                positions.add(new PointF((float) x, (float) y));
            }
            positions.add(positions.get(0));
            return positions;
        }
        if (type == Type.freeLine) {
            return freeLine.getList();
        }
        if (type == Type.freeFace) {
            List<PointF> list = freeLine.getList();
            list.add(list.get(0));
            return list;
        }
        return null;
    }

    private Geometry getGeometry(List<PointF> list, Type type) {
        if (list == null || list.size() == 0) return null;
        List<Point> points = new ArrayList<>(list.size());
        PointF pf;
        Point point;
        Geometry geometry;
        SpatialReference reference = arcMap.getMapView().getSpatialReference();
        for (int i = 0, len = list.size(); i < len; i++) {
            pf = list.get(i);
            point = arcMap.screenToLocation((int) pf.x, (int) pf.y);
            points.add(point);
        }

        if (type == Type.rect) {
            geometry = new Polygon(new PointCollection(points), reference);
            return geometry;
        }
        if (type == Type.circle) {
            geometry = new Polygon(new PointCollection(points), reference);
            return geometry;
        }
        if (type == Type.freeLine) {
            geometry = new Polyline(new PointCollection(points), reference);
            return geometry;
        }
        if (type == Type.freeFace) {
            geometry = new Polygon(new PointCollection(points), reference);
            return geometry;
        }
        if (type == Type.pSel) {
            geometry = new Point(points.get(0).getX(), points.get(0).getY(), reference);
            return geometry;
        }
        return null;
    }

    ICallBack iCallBack;

    public SketchStream setCallBack(ICallBack iCallBack) {
        this.iCallBack = iCallBack;
        return this;
    }

    public interface ICallBack {
        public void callBack(List<PointF> list, Geometry geometry);
    }

    IDoubleConfirm iDoubleConfirm;

    public SketchStream setDoubleConfirm(IDoubleConfirm iDoubleConfirm) {
        this.iDoubleConfirm = iDoubleConfirm;
        return this;
    }

    public interface IDoubleConfirm {
        public void callBack();
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    public enum Type {
        //点选
        pSel,
        //绘制矩形
        rect,
        //绘制圆
        circle,
        //绘制自由线
        freeLine,
        //绘制自由面
        freeFace,

        pSel_rect,

        pSel_circle,

        pSel_freeFace,
    }

    public class Circle {

        private PointF center;
        private float radius;

        public Circle() {
        }

        public Circle(PointF center, float radius) {
            this.center = center;
            this.radius = radius;
        }

        public PointF getCenter() {
            return center;
        }

        public void setCenter(PointF center) {
            this.center = center;
        }

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public void setPoint(PointF point) {
            this.radius = (float) Math.sqrt(Math.pow((center.x - point.x), 2) + Math.pow((center.y - point.y), 2));
        }

        public RectF getRectF() {
            return new RectF((int) (center.x - radius), (int) (center.y + radius), (int) (center.x + radius), (int) (center.y - radius));
        }
    }
    //----------------------------------------------------------------------------------------------

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (iDoubleConfirm != null) {
            iDoubleConfirm.callBack();
            return true;
        }
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTouchDrag(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onMultiPointerTap(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onRotate(MotionEvent event, double rotationAngle) {
        return false;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onUp(MotionEvent e) {
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    /**自由曲线时计算两点之间的距离*/
    private double lenght(Point point){
        PointF start = freeLine.getLastP();
        Point st_point = arcMap.screenToLocation((int) start.x,(int)start.y);
        PointCollection points = new PointCollection(arcMap.getMapView().getSpatialReference());
        points.add(st_point);
        points.add(point);
        Polyline line = new Polyline(points);
        double distance = GeometryEngine.lengthGeodetic(line, new LinearUnit(LinearUnitId.METERS), GeodeticCurveType.GEODESIC);
        //double length = GeometryEngine.length(line);
        //Log.e("=================",""+distance);
        return distance;
    }

    private double area(Polygon polygon){
        double area = GeometryEngine.areaGeodetic(polygon,new AreaUnit(AreaUnitId.SQUARE_METERS),GeodeticCurveType.GEODESIC);
        return area;
    }

}
