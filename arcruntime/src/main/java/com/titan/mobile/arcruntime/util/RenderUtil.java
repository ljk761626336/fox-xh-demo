package com.titan.mobile.arcruntime.util;

import android.graphics.Color;


import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.symbology.FillSymbol;
import com.esri.arcgisruntime.symbology.LineSymbol;
import com.esri.arcgisruntime.symbology.MarkerSymbol;
import com.esri.arcgisruntime.symbology.MultilayerPointSymbol;
import com.esri.arcgisruntime.symbology.MultilayerPolygonSymbol;
import com.esri.arcgisruntime.symbology.MultilayerPolylineSymbol;
import com.esri.arcgisruntime.symbology.Renderer;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.esri.arcgisruntime.symbology.Symbol;
import com.esri.arcgisruntime.symbology.UniqueValueRenderer;
import com.titan.mobile.arcruntime.core.ArcMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class RenderUtil {

    public static Symbol defaultFillSymbol() {
        SimpleLineSymbol simpleLineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2f);
        SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.TRANSPARENT, simpleLineSymbol);
        simpleFillSymbol.setOutline(simpleLineSymbol);
        return simpleFillSymbol;
    }

    public static Symbol defaultLineSymbol() {
        SimpleLineSymbol simpleLineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 2f);
        return simpleLineSymbol;
    }

    public static Symbol defaultMarkerSymbol() {
        SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.CYAN, 15f);
        return simpleMarkerSymbol;
    }


    /***
     * featureLayer 图层
     * unique 唯一字段
     * values 渲染值
     * */
    public static void setUniqueRender(FeatureLayer featureLayer, String unique, List<Integer> colors, String... values) {
        boolean flag = FieldUtil.checkField(featureLayer, unique);
        if (!flag) return;
        colors.add(Color.RED);
        GeometryType geometryType = featureLayer.getFeatureTable().getGeometryType();
        if (geometryType == GeometryType.POLYGON) {
            setUniquePolygon(featureLayer, unique, colors, values);
            return;
        }

        if (geometryType == GeometryType.POLYLINE) {
            setUniqueLine(featureLayer, unique, colors, values);
            return;
        }

        if (geometryType == GeometryType.POINT) {
            setUniquePoint(featureLayer, unique, colors, values);
            return;
        }

        if (geometryType == GeometryType.UNKNOWN) {
            return;
        }

    }

    /**
     * 面图层设置唯一渲染
     */
    public static void setUniquePolygon(FeatureLayer featureLayer, String unique, List<Integer> colors, String... values) {
        UniqueValueRenderer uniqueValueRenderer = new UniqueValueRenderer();
        // 设置默认Symbol（符号）
        uniqueValueRenderer.getFieldNames().add(unique);
        uniqueValueRenderer.setDefaultSymbol(defaultFillSymbol());
        uniqueValueRenderer.setDefaultLabel("未完成");

        for (int i = 0; i < values.length; i++) {
            List<Object> list = new ArrayList<>();
            list.add(values[i]);
            SimpleFillSymbol fillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.TRANSPARENT, new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, colors.get(i), 2));
            UniqueValueRenderer.UniqueValue uniqueValue = new UniqueValueRenderer.UniqueValue(values[i], values[i], fillSymbol, list);
            uniqueValueRenderer.getUniqueValues().add(uniqueValue);
        }

        featureLayer.setRenderer(uniqueValueRenderer);
    }

    /**
     * 线图层设置唯一渲染
     */
    public static void setUniqueLine(FeatureLayer featureLayer, String unique, List<Integer> colors, String... values) {
        UniqueValueRenderer uniqueValueRenderer = new UniqueValueRenderer();
        // 设置默认Symbol（符号）
        uniqueValueRenderer.getFieldNames().add(unique);
        uniqueValueRenderer.setDefaultSymbol(defaultLineSymbol());
        uniqueValueRenderer.setDefaultLabel("未完成");

        for (int i = 0; i < values.length; i++) {
            List<Object> list = new ArrayList<>();
            list.add(values[i]);
            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, colors.get(i), 2);
            UniqueValueRenderer.UniqueValue uniqueValue = new UniqueValueRenderer.UniqueValue(values[i], values[i], lineSymbol, list);
            uniqueValueRenderer.getUniqueValues().add(uniqueValue);
        }

        featureLayer.setRenderer(uniqueValueRenderer);
    }

    /**
     * 点图层设置唯一渲染
     */
    public static void setUniquePoint(FeatureLayer featureLayer, String unique, List<Integer> colors, String... values) {
        UniqueValueRenderer uniqueValueRenderer = new UniqueValueRenderer();
        // 设置默认Symbol（符号）
        uniqueValueRenderer.getFieldNames().add(unique);
        uniqueValueRenderer.setDefaultSymbol(defaultMarkerSymbol());
        uniqueValueRenderer.setDefaultLabel("未完成");

        for (int i = 0; i < values.length; i++) {
            List<Object> list = new ArrayList<>();
            list.add(values[i]);
            SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, colors.get(i), 15f);
            UniqueValueRenderer.UniqueValue uniqueValue = new UniqueValueRenderer.UniqueValue(values[i], values[i], markerSymbol, list);
            uniqueValueRenderer.getUniqueValues().add(uniqueValue);
        }

        featureLayer.setRenderer(uniqueValueRenderer);
    }


    public static void setRender(FeatureLayer featureLayer) {
        String tbName = featureLayer.getFeatureTable().getTableName();
        if (tbName.contains("_YD_PRE")) return;
        if (!tbName.contains("_YD")) {
            setRTranp(featureLayer);
            return;
        }

        SimpleLineSymbol lineSymbol = new SimpleLineSymbol();
        lineSymbol.setColor(Color.RED);
        lineSymbol.setWidth(5.0f);
        lineSymbol.setStyle(SimpleLineSymbol.Style.SOLID);

        SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol();
        if (tbName.contains("_JGYD")) {
            markerSymbol.setColor(Color.RED);
        } else {
            markerSymbol.setColor(Color.GREEN);
        }

        markerSymbol.setOutline(lineSymbol);
        markerSymbol.setSize(10.0f);
        markerSymbol.setStyle(SimpleMarkerSymbol.Style.CIRCLE);

        SimpleMarkerSymbol _markerSymbol = new SimpleMarkerSymbol();
        if (tbName.contains("_JGYD")) {
            _markerSymbol.setColor(Color.BLUE);
        } else {
            _markerSymbol.setColor(Color.GRAY);
        }
        _markerSymbol.setOutline(lineSymbol);
        _markerSymbol.setSize(10.0f);
        _markerSymbol.setStyle(SimpleMarkerSymbol.Style.CIRCLE);

        boolean flag = FieldUtil.checkField(featureLayer, "SHZT");
        if (!flag) return;
        UniqueValueRenderer uniqueValueRenderer = new UniqueValueRenderer();
        // 设置默认Symbol（符号）
        uniqueValueRenderer.getFieldNames().add("SHZT");
        uniqueValueRenderer.setDefaultSymbol(markerSymbol);
        uniqueValueRenderer.setDefaultLabel("未完成");

        // 设置california的值  默认为空  0  1
        List<Object> defValue = new ArrayList<>();
        defValue.add("");
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue("未完成", "未完成", markerSymbol, defValue));
        // 设置california的值
        List<Object> defValue0 = new ArrayList<>();
        defValue0.add(0);
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue("未完成", "未完成", markerSymbol, defValue0));
        // 设置arizona的值
        List<Object> defValue1 = new ArrayList<>();
        defValue1.add(1);
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue("已完成", "已完成", _markerSymbol, defValue1));

        featureLayer.setRenderer(uniqueValueRenderer);
    }


    /*图层透明度设置
     * 透明只针对面图层
     * */
    public static void setRTranp(FeatureLayer featureLayer) {
        FeatureTable table = featureLayer.getFeatureTable();
        if (table == null) return;
        String tbName = table.getTableName();
        boolean flag = tbName.equals("HLJ_ED_XBM");
        Renderer renderer = featureLayer.getRenderer();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (tbName.contains("_XBM")) {
            Geometry geometry = featureLayer.getFullExtent();
            if (geometry != null) {
                ArcMap.arcMap.getMapView().setViewpointGeometryAsync(geometry);
            }
        }

        if (renderer == null) {
            SimpleFillSymbol fillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.TRANSPARENT, null);
            if (flag) {
                SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2);
                fillSymbol.setOutline(lineSymbol);
                setUnRender(featureLayer, fillSymbol);
            } else {
                GeometryType type = featureLayer.getFeatureTable().getGeometryType();
                if (type == GeometryType.POINT || tbName.contains("HLJ_ED_FZD")) {
                    int color = Color.argb(255, 0, 0, 0);
                    SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, color, 15);
                    markerSymbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 2));
                    featureLayer.setRenderer(new SimpleRenderer(markerSymbol));
                } else if (type == GeometryType.POLYLINE || tbName.contains("HLJ_ED_FZX") || tbName.contains("HLJ_ED_LBX") || tbName.contains("HLJ_ED_DGX")) {
                    int color = 0;
                    if (tbName.contains("HLJ_ED_FZX")) {
                        color = Color.argb(255, 218, 165, 105);
                    } else if (tbName.contains("HLJ_ED_DGX")) {
                        color = Color.argb(255, 250, 235, 210);
                    } else if (tbName.contains("HLJ_ED_LBX")) {
                        color = Color.argb(255, 127, 255, 212);
                    } else {
                        color = interpColor();
                    }
                    SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, color, 2);
                    featureLayer.setRenderer(new SimpleRenderer(lineSymbol));
                } else if (type == GeometryType.POLYGON) {
                    fillSymbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 2));
                    featureLayer.setRenderer(new SimpleRenderer(fillSymbol));
                } else if (type == GeometryType.UNKNOWN) {
                    fillSymbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 2));
                    featureLayer.setRenderer(new SimpleRenderer(fillSymbol));
                }
            }
            return;
        }
        Symbol symbol = new SimpleFillSymbol();
        if (renderer instanceof SimpleRenderer) {
            symbol = ((SimpleRenderer) renderer).getSymbol();
        } else if (renderer instanceof UniqueValueRenderer) {
            symbol = ((UniqueValueRenderer) renderer).getDefaultSymbol();
        }

        SimpleFillSymbol fillSymbol = null;
        if (symbol instanceof FillSymbol) {
            fillSymbol = (SimpleFillSymbol) symbol;
            if (fillSymbol.getColor() != Color.TRANSPARENT) {
                fillSymbol.setColor(Color.TRANSPARENT);
                if (flag) {
                    fillSymbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2));
                    setUnRender(featureLayer, fillSymbol);
                } else {
                    int color = interpColor();
                    fillSymbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, color, 2));
                    featureLayer.setRenderer(new SimpleRenderer(fillSymbol));
                }
            } else {
                featureLayer.resetRenderer();
            }
            return;
        }

        if (symbol instanceof MultilayerPolygonSymbol) {
            fillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.TRANSPARENT, null);
            if (flag) {
                SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2);
                fillSymbol.setOutline(lineSymbol);
                setUnRender(featureLayer, fillSymbol);
            } else {
                int color = interpColor();
                fillSymbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, color, 2));
                featureLayer.setRenderer(new SimpleRenderer(fillSymbol));
            }
            return;
        }

        if (symbol instanceof LineSymbol) {
            int color = interpColor();
            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, color, 2);
            featureLayer.setRenderer(new SimpleRenderer(lineSymbol));
            return;
        }

        if (symbol instanceof MultilayerPolylineSymbol) {
            int color = interpColor();
            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, color, 2);
            featureLayer.setRenderer(new SimpleRenderer(lineSymbol));
            return;
        }

        if (symbol instanceof MultilayerPointSymbol) {
            int color = interpColor();
            SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, color, 10);
            markerSymbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 5));
            featureLayer.setRenderer(new SimpleRenderer(markerSymbol));
            return;
        }
        if (symbol instanceof MarkerSymbol) {
            int color = interpColor();
            SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, color, 10);
            markerSymbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 5));
            featureLayer.setRenderer(new SimpleRenderer(markerSymbol));
            return;
        }

        fillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.TRANSPARENT, null);
        if (flag) {
            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2);
            fillSymbol.setOutline(lineSymbol);
            setUnRender(featureLayer, fillSymbol);
        } else {
            int color = interpColor();
            fillSymbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, color, 2));
            featureLayer.setRenderer(new SimpleRenderer(fillSymbol));
        }
    }

    private static void setUnRender(FeatureLayer featureLayer, SimpleFillSymbol defaultFillSymbol) {
        UniqueValueRenderer uniqueValueRenderer = new UniqueValueRenderer();
        // 设置默认Symbol（符号）
        uniqueValueRenderer.getFieldNames().add("SHZT");

        // 设置用于渲染的Symbol（符号）
        SimpleFillSymbol californiaFillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.TRANSPARENT, new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2));
        SimpleFillSymbol arizonaFillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.TRANSPARENT, new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 2));

        // 设置默认Symbol（符号）
        uniqueValueRenderer.setDefaultSymbol(defaultFillSymbol);
        uniqueValueRenderer.setDefaultLabel("未完成");

        // 设置california的值
        List<Object> californiaValue = new ArrayList<>();
        californiaValue.add(0);
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue("未完成", "未完成", californiaFillSymbol, californiaValue));
        // 设置arizona的值
        List<Object> arizonaValue = new ArrayList<>();
        arizonaValue.add(1);
        uniqueValueRenderer.getUniqueValues().add(new UniqueValueRenderer.UniqueValue("已完成", "已完成", arizonaFillSymbol, arizonaValue));

        featureLayer.setRenderer(uniqueValueRenderer);
    }

    private static int interpColor() {
        final int[] colors = new int[]{0xFFFF0000, 0xFFFFFF00, 0xFF00FF00, 0xFF00FFFF,
                0xFF0000FF, 0xFFFF00FF, 0xFFFF0000, 0x8A360F, 0x6B8E23};
        float unit = testRandom3();

        float p = unit / 7;
        int i = (int) p;
        p -= i;

        if (i < 0) {
            i = 0;
        } else if (i > 5) {
            i = 5;
        }
        // now p is just the fractional part [0...1) and i is the index
        int c0 = colors[i];
        int c1 = colors[i + 1];
        int a = ave(Color.alpha(c0), Color.alpha(c1), p);
        int r = ave(Color.red(c0), Color.red(c1), p);
        int g = ave(Color.green(c0), Color.green(c1), p);
        int b = ave(Color.blue(c0), Color.blue(c1), p);
        return Color.argb(a, r, g, b);
    }

    private static int ave(int s, int d, float p) {
        return s + Math.round(p * (d - s));
    }

    private static int testRandom3() {
        int randomInt = 0;
        HashSet integerHashSet = new HashSet();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            randomInt = random.nextInt(1000);
            if (!integerHashSet.contains(randomInt)) {
                integerHashSet.add(randomInt);
            }
        }
        return randomInt;
    }


}
