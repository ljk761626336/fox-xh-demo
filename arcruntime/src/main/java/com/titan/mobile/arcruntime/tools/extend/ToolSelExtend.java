package com.titan.mobile.arcruntime.tools.extend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.titan.mobile.arcruntime.core.ArcMap;
import com.titan.mobile.arcruntime.util.TransformUtil;


public class ToolSelExtend extends FrameLayout {

    public static ToolSelExtend create(ArcMap foxMap) {
        return new ToolSelExtend(foxMap);
    }

    private ArcMap foxMap;
    private Context context;
    private PointF startP, moveP;
    private Paint paint;

    //绘制类型
    Type type;
    //绘制的矩形
    private Rect rect;
    //绘制的矩形
    private Circle circle;
    //绘制流状线

    //绘制流状面


    private ToolSelExtend(@NonNull ArcMap foxMap) {
        super(foxMap.getContext());
        init(foxMap);
    }

    void init(@NonNull ArcMap foxMap) {
        this.foxMap = foxMap;
        //this.foxMap.removeView(this);
        this.foxMap.addView(this);
        this.context = getContext();
        this.setWillNotDraw(false);

        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth(5);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setColor(Color.RED);

        this.rect = new Rect();
        this.circle = new Circle();
    }

    public ToolSelExtend drawType(Type type) {
        this.type = type;
        return this;
    }

    public ToolSelExtend startDraw(PointF startP) {
        this.startP = startP;
        return this;
    }

    public ToolSelExtend underDraw(PointF moveP) {
        this.moveP = moveP;
        invalidate();
        return this;
    }

    public ToolSelExtend cancelDraw(PointF cancelP) {
        this.moveP = cancelP;
        invalidate();
        clear();
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (startP == null) return;
        if (type == Type.rect) {
            rect.left = (int) startP.x;
            rect.top = (int) startP.y;
            rect.right = (int) moveP.x;
            rect.bottom = (int) moveP.y;
            canvas.drawRect(rect, paint);
        }
        if (type == Type.circle) {
            circle.setCenter(startP);
            circle.setPoint(moveP);
            canvas.drawCircle(circle.center.x, circle.center.y, circle.radius, paint);
        }
    }

    public void clear() {
        startP = null;
        moveP = null;
    }

    public Geometry getGeometry() {
        if (type == Type.rect) {
            Point p1 = foxMap.screenToLocation(rect.left, rect.top);
            Point p2 = foxMap.screenToLocation(rect.right, rect.bottom);
            return new Envelope(p1, p2);
        }
        if (type == Type.circle) {
            return TransformUtil.circle2Polygon(foxMap, circle.center.x, circle.center.y, circle.radius, 100);
        }
        return null;
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    public enum Type {
        //绘制矩形
        rect,
        //绘制圆
        circle,
        //绘制自由线
        freeLine,
        //绘制自由面
        freeFace
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
    }
}
