package com.titan.mobile.arcruntime.core.draw;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

public class SimPath extends Path {

    List<PointF> list = new ArrayList<>();

    @Override
    public void moveTo(float x, float y) {
        super.moveTo(x, y);
        list.add(new PointF(x,y));
    }

    @Override
    public void lineTo(float x, float y) {
        super.lineTo(x, y);
        list.add(new PointF(x,y));
    }

    public List<PointF> getList() {
        return list;
    }

    public PointF getLastP(){
        return list.get(list.size()-1);
    }
}
