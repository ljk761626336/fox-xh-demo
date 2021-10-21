package com.titan.mobile.arcruntime.util;


import android.graphics.Color;

import com.esri.arcgisruntime.arcgisservices.LabelDefinition;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.symbology.Renderer;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.titan.mobile.arcruntime.core.ArcMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.titan.mobile.arcruntime.util.PropertyFactory.LayerProperty.RENDER;

public final class PropertyFactory {


    public static <T> T getProperty(Map property, String name) {
        T t = ObjectUtil.Maps.searchValue(property, name, "_");
        return t;
    }

    public static <T> T getProperty(Map property, String name, Class clazz) {
        T t;
        if (name.equals(RENDER)) {
            return (T) property.get(RENDER);
        }
        if (clazz != Color.class) {
            Object val = ObjectUtil.Maps.searchValue(property, name, "_");
            t = ObjectUtil.Common.convert(val, clazz);
            return t;
        } else {
            List<Long> array = getProperty(property, name, Long.class);
            Integer color = ObjectUtil.Rgb.argbArray2IntColor(array);
            return (T) color;
        }
    }

    public static <T> T getSimpleMap(Map property, String name) {
        T t = (T) property.get(name);
        return t;
    }

    public static <T> T getSimpleMap(Map property, String name, Class clazz) {
        T t;
        if (clazz != Color.class) {
            Object val = getSimpleMap(property, name);
            t = ObjectUtil.Common.convert(val, clazz);
        } else {
            Integer symbol_outline_color_int = getSimpleMap(property, name);
            t = (T) ObjectUtil.Rgb.int2ListRgba(symbol_outline_color_int);
        }
        return t;
    }

    public static void setProperty(Map property, String index, Object value) {
        if (value == null) return;
        //ObjectUtil.Maps.setValue(property, index, "_", values);
        if (index == null) return;
        String[] split = index.split("_");
        if (split == null) return;
        if (split.length == 1) {
            property.put(index, value);
        } else {
            Map map = (Map) property.get(split[0]);
            for (int i = 1; i < split.length - 1; i++) {
                if (map == null) continue;
                if (!map.containsKey(split[i])) {
                    map.put(split[i], new HashMap<>());
                }
                map = (Map) map.get(split[i]);
            }
            map.put(split[split.length - 1], value);
        }
    }


    public static class LayerProperty {

        public final static String OPACITY = "opacity";
        public final static String MINSCALE = "minScale";
        public final static String MAXSCALE = "maxScale";
        public final static String VISIBLE = "visible";
        public final static String RENDER = "render";

        protected static Map getLayerProperties(FeatureLayer layer) {
            if (layer == null) return null;
            float opacity = layer.getOpacity();
            double minScale = layer.getMinScale();
            if (minScale == Double.NaN) minScale = 0d;
            double maxScale = layer.getMaxScale();
            if (maxScale == Double.NaN) maxScale = 0d;
            boolean visible = layer.isVisible();
            Renderer renderer = layer.getRenderer();
            String json = renderer == null ? null : renderer.toJson();
            Map map = ObjectUtil.Common.convert(json, HashMap.class);
            Map style = new HashMap();
            style.put(OPACITY, opacity);
            style.put(MINSCALE, minScale);
            style.put(MAXSCALE, maxScale);
            style.put(VISIBLE, visible);
            style.put(RENDER, map);
            return style;
        }

        /**
         * 设置样式
         *
         * @param layer
         * @param set
         * @return
         */
        public static void setRender(FeatureLayer layer, Map set) {
            try {
                Float opacity = getProperty(set, OPACITY, Float.class);
                Double minScale = getProperty(set, MINSCALE, Double.class);
                Double maxScale = getProperty(set, MAXSCALE, Double.class);
                Boolean visible = getProperty(set, VISIBLE, Boolean.class);

                if (visible != null && !visible)
                    ArcMap.arcMap.getSelectionSet().removeSetByIndex(layer.getId()).renderSet();

                Map renderMap = getProperty(set, RENDER, Map.class);
                String renderJson = ObjectUtil.Common.convert(renderMap);
                if (renderJson != null) {
                    Renderer renderer = Renderer.fromJson(renderJson);
                    layer.setRenderer(renderer);
                }
                if (opacity != null) layer.setOpacity(opacity);
                if (visible != null) layer.setVisible(visible);
                if (minScale != null) layer.setMinScale(minScale);
                if (maxScale != null) layer.setMaxScale(maxScale);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /**
         * 创建默认渲染样式
         *
         * @param layer
         */
        public static void setDefaultFeatureRender(FeatureLayer layer) {
            if (layer == null) return;
            FeatureTable featureTable = layer.getFeatureTable();
            if (featureTable == null) return;
            GeometryType type = featureTable.getGeometryType();
            if (type == GeometryType.POINT) {
                layer.setRenderer(defaultSymbolRender());
            }
            if (type == GeometryType.POLYLINE) {
                layer.setRenderer(defaultLineRender());
            }
            if (type == GeometryType.POLYGON) {
                layer.setRenderer(defaultFillRender());
            }
            if (type == GeometryType.UNKNOWN) {
                System.err.println("未知类型");
            }
        }

        private static Renderer defaultFillRender() {
            SimpleLineSymbol simpleLineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLACK, 0.5f);
            SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.CYAN, simpleLineSymbol);
            Renderer renderer = new SimpleRenderer(simpleFillSymbol);
            String json = renderer.toJson();
            return renderer;
        }

        private static Renderer defaultLineRender() {
            SimpleLineSymbol simpleLineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 1f);
            return new SimpleRenderer(simpleLineSymbol);
        }

        private static Renderer defaultSymbolRender() {
            SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 1f);
            return new SimpleRenderer(simpleMarkerSymbol);
        }

        public static String readStyleAsJson(FeatureLayer layer) {
            Map map = LayerProperty.getLayerProperties(layer);
            String json = ObjectUtil.Common.convert(map);
            return json;
        }

    }

    public final static class FillProperty extends LayerProperty {

        public final static String EmptyTemplate = "{\"visible\":null,\"maxScale\":null,\"minScale\":null,\"opacity\":null,\"render\":{\"symbol\":{\"outline\":{\"color\":null,\"width\":null,\"style\":null,\"type\":null},\"color\":null,\"style\":null,\"type\":null},\"type\":null}}";

        public final static String RENDER_TYPE = "render_type";
        public final static String RENDER_SYMBOL_OUTLINE_COLOR = "render_symbol_outline_color";
        public final static String RENDER_SYMBOL_OUTLINE_WIDTH = "render_symbol_outline_width";
        public final static String RENDER_SYMBOL_OUTLINE_STYLE = "render_symbol_outline_style";
        public final static String RENDER_SYMBOL_OUTLINE_TYPE = "render_symbol_outline_type";
        public final static String RENDER_SYMBOL_COLOR = "render_symbol_color";
        public final static String RENDER_SYMBOL_STYLE = "render_symbol_style";
        public final static String RENDER_SYMBOL_TYPE = "render_symbol_type";


        public static Map readFillStyle(FeatureLayer layer) {
            Map fillProperty = FillProperty.getLayerProperties(layer);
            Float opacity = getProperty(fillProperty, FillProperty.OPACITY, Float.class);
            //Double min = getProperty(fillProperty, FillProperty.MINSCALE, Double.class);
            //Double max = getProperty(fillProperty, FillProperty.MAXSCALE, Double.class);
            Boolean visible = getProperty(fillProperty, FillProperty.VISIBLE, Boolean.class);

            String outline_style = getProperty(fillProperty, FillProperty.RENDER_SYMBOL_OUTLINE_STYLE, String.class);
            String outline_type = getProperty(fillProperty, FillProperty.RENDER_SYMBOL_OUTLINE_TYPE, String.class);

            String symbol_style = getProperty(fillProperty, FillProperty.RENDER_SYMBOL_STYLE, String.class);
            String symbol_type = getProperty(fillProperty, FillProperty.RENDER_SYMBOL_TYPE, String.class);

            String fillType = null;
            if (symbol_style != null && symbol_type != null) {
                fillType = symbol_style.replace(symbol_type, "").toUpperCase();
            }
            Double render_symbol_outline_width = getProperty(fillProperty, FillProperty.RENDER_SYMBOL_OUTLINE_WIDTH, Double.class);
            Integer render_symbol_color = getProperty(fillProperty, FillProperty.RENDER_SYMBOL_COLOR, Color.class);
            Integer render_symbol_outline_color = getProperty(fillProperty, FillProperty.RENDER_SYMBOL_OUTLINE_COLOR, Color.class);
            Map map = new HashMap();
            if (opacity != null) map.put("opacity", opacity);
            if (visible != null) map.put("visible", visible);
            if (render_symbol_color != null) map.put("render_symbol_color", render_symbol_color);
            if (render_symbol_outline_color != null)
                map.put("render_symbol_outline_color", render_symbol_outline_color);
            if (render_symbol_outline_width != null)
                map.put("render_symbol_outline_width", render_symbol_outline_width);
            if (fillType != null) map.put("fillType", fillType);
            return map;
        }


        /**
         * @param property
         * @param setting
         * @return
         */
        private static Map combineRenderFillStyle(Map property, Map setting) {
            setProperty(property, OPACITY, getSimpleMap(setting, OPACITY));
            setProperty(property, VISIBLE, getSimpleMap(setting, VISIBLE));
            setProperty(property, RENDER_SYMBOL_COLOR, getSimpleMap(setting, "render_symbol_color", Color.class));
            setProperty(property, RENDER_SYMBOL_OUTLINE_COLOR, getSimpleMap(setting, "render_symbol_outline_color", Color.class));
            setProperty(property, RENDER_SYMBOL_OUTLINE_WIDTH, getSimpleMap(setting, "render_symbol_outline_width"));
            String fillType = getSimpleMap(setting, "fillType", String.class);
            String render_symbol_type = getProperty(property, RENDER_SYMBOL_TYPE, String.class);
            String render_symbol_style = (render_symbol_type == null ? "esriSFS" : render_symbol_type) + StringEngine.onlyUpperFirst(fillType);
            setProperty(property, RENDER_SYMBOL_STYLE, render_symbol_style);
            return property;
        }

        private static Map combineRender(FeatureLayer layer, Map setting) {
            Map map = PropertyFactory.FillProperty.getLayerProperties(layer);
            map = PropertyFactory.FillProperty.combineRenderFillStyle(map, setting);
            return map;
        }

        public static void setFillRender(FeatureLayer layer, Map setting) {
            Map map = PropertyFactory.FillProperty.combineRender(layer, setting);
            PropertyFactory.LayerProperty.setRender(layer, map);
        }
    }

    public final static class LineProperty extends LayerProperty {

    }

    public final static class SymbolProperty extends LayerProperty {

    }

    //----------------------------------------------------------------------------------------------
    //-----------------------------------------标注样式读写-----------------------------------------
    //----------------------------------------------------------------------------------------------

    public static class LabelProperty {

        private static String readLabelDefinition(FeatureLayer layer) {
            List<LabelDefinition> labelDefinitions = layer.getLabelDefinitions();
            if (ObjectUtil.Common.isEmpty(labelDefinitions)) return null;
            List<Map> list = new ArrayList<>();
            Map map;
            for (LabelDefinition label : labelDefinitions) {
                map = ObjectUtil.Common.convert(label.toJson(), HashMap.class);
                list.add(map);
            }
            String json = ObjectUtil.Common.convert(list);
            return json;
        }

        public static String readLabelAsJson(FeatureLayer layer) {
            Map map = readLabel(layer);
            String json = ObjectUtil.Common.convert(map);
            return json;
        }

        /**
         * 读取字体标注
         *
         * @param layer
         * @return
         */
        public static Map<String, Object> readLabel(FeatureLayer layer) {
            String json = readLabelDefinition(layer);
            List<Map> list = ObjectUtil.Common.convert(json, HashMap.class);
            if (ObjectUtil.Common.isEmpty(list)) return null;
            Map<String, Object> label = list.get(0);

            String expression = getProperty(label, "expression", String.class);
            expression = expression == null ? expression : expression.replace("$feature.", "");
            String labelPlacement = getProperty(label, "labelPlacement", String.class);
            Float symbol_angle = getProperty(label, "symbol_angle", Float.class);
            Integer symbol_backgroundColor = getProperty(label, "symbol_backgroundColor", Color.class);
            Integer symbol_borderLineColor = getProperty(label, "symbol_borderLineColor", Color.class);
            Float symbol_borderLineSize = getProperty(label, "symbol_borderLineSize", Float.class);
            Integer symbol_color = getProperty(label, "symbol_color", Color.class);
            String symbol_font_decoration = getProperty(label, "symbol_font_decoration", String.class);
            Float symbol_font_size = getProperty(label, "symbol_font_size", Float.class);

            symbol_font_size = symbol_font_size == null ? null : Float.parseFloat(symbol_font_size / 0.75 + "");

            String symbol_font_style = getProperty(label, "symbol_font_style", String.class);
            String symbol_font_weight = getProperty(label, "symbol_font_weight", String.class);
            Integer symbol_halo_color = getProperty(label, "symbol_haloColor", Color.class);
            Float symbol_halo_size = getProperty(label, "symbol_haloSize", Float.class);
            String symbol_horizontalAlignment = getProperty(label, "symbol_horizontalAlignment", String.class);
            Boolean symbol_kerning = getProperty(label, "symbol_kerning", Boolean.class);
            String symbol_type = getProperty(label, "symbol_type", String.class);
            String symbol_verticalAlignment = getProperty(label, "symbol_verticalAlignment", String.class);
            Float symbol_xoffset = getProperty(label, "symbol_xoffset", Float.class);
            Float symbol_yoffset = getProperty(label, "symbol_yoffset", Float.class);
            String where = getProperty(label, "where", String.class);

            Map<String, Object> simpleMap = new HashMap<>();
            simpleMap.put("symbol_angle", symbol_angle);
            if (symbol_color != null)
                simpleMap.put("symbol_color", symbol_color);
            simpleMap.put("symbol_backgroundColor", symbol_backgroundColor);
            simpleMap.put("symbol_borderLineColor", symbol_borderLineColor);
            simpleMap.put("symbol_borderLineSize", symbol_borderLineSize);
            simpleMap.put("symbol_halo_color", symbol_halo_color);
            simpleMap.put("symbol_halo_size", symbol_halo_size);
            simpleMap.put("symbol_kerning", symbol_kerning);
            simpleMap.put("symbol_type", symbol_type);
            simpleMap.put("symbol_horizontalAlignment", symbol_horizontalAlignment);
            simpleMap.put("symbol_verticalAlignment", symbol_verticalAlignment);
            simpleMap.put("symbol_xoffset", symbol_xoffset);
            simpleMap.put("symbol_yoffset", symbol_yoffset);
            simpleMap.put("symbol_font_decoration", symbol_font_decoration);
            simpleMap.put("symbol_font_size", symbol_font_size);
            simpleMap.put("symbol_font_style", symbol_font_style);
            simpleMap.put("symbol_font_weight", symbol_font_weight);
            simpleMap.put("expression", expression);
            simpleMap.put("labelPlacement", labelPlacement);
            simpleMap.put("where", where);
            simpleMap.put("enable", layer.isLabelsEnabled());
            return simpleMap;
        }

        /**
         * @param layer
         * @param setting
         */
        public static void setLabel(FeatureLayer layer, Map setting) {

            Boolean enable = getProperty(setting, "enable", Boolean.class);
            TextSymbol textSymbol = new TextSymbol();
            Float symbol_angle = getProperty(setting, "symbol_angle", Float.class);
            if (symbol_angle != null) textSymbol.setAngle(symbol_angle);
            Integer symbol_color = getProperty(setting, "symbol_color", Integer.class);
            if (symbol_color != null) textSymbol.setColor(symbol_color);
            Integer symbol_backgroundColor = getProperty(setting, "symbol_backgroundColor", Integer.class);
            if (symbol_backgroundColor != null)
                textSymbol.setBackgroundColor(symbol_backgroundColor);
            Integer symbol_borderLineColor = getProperty(setting, "symbol_borderLineColor", Integer.class);
            if (symbol_borderLineColor != null) textSymbol.setOutlineColor(symbol_borderLineColor);
            Float symbol_borderLineSize = getProperty(setting, "symbol_borderLineSize", Float.class);
            if (symbol_borderLineSize != null) textSymbol.setOutlineWidth(symbol_borderLineSize);
            Integer symbol_halo_color = getProperty(setting, "symbol_halo_color", Integer.class);
            if (symbol_halo_color != null) textSymbol.setHaloColor(symbol_halo_color);
            Float symbol_halo_size = getProperty(setting, "symbol_halo_size", Float.class);
            if (symbol_halo_size != null) textSymbol.setHaloWidth(symbol_halo_size);
            //textSymbol.setKerning();

            String symbol_horizontal_alignment = getProperty(setting, "symbol_horizontal_alignment", String.class);

            if ("CENTER".equals(symbol_horizontal_alignment))
                textSymbol.setHorizontalAlignment(TextSymbol.HorizontalAlignment.CENTER);
            if ("LEFT".equals(symbol_horizontal_alignment))
                textSymbol.setHorizontalAlignment(TextSymbol.HorizontalAlignment.LEFT);
            if ("RIGHT".equals(symbol_horizontal_alignment))
                textSymbol.setHorizontalAlignment(TextSymbol.HorizontalAlignment.RIGHT);

            String symbol_vertical_alignment = getProperty(setting, "symbol_vertical_alignment", String.class);
            if ("MIDDLE".equals(symbol_vertical_alignment))
                textSymbol.setVerticalAlignment(TextSymbol.VerticalAlignment.MIDDLE);
            if ("BOTTOM".equals(symbol_vertical_alignment))
                textSymbol.setVerticalAlignment(TextSymbol.VerticalAlignment.BOTTOM);
            if ("TOP".equals(symbol_vertical_alignment))
                textSymbol.setVerticalAlignment(TextSymbol.VerticalAlignment.TOP);

            Float symbol_font_size = getProperty(setting, "symbol_font_size", Float.class);
            if (symbol_font_size != null) textSymbol.setSize(symbol_font_size);
            Float symbol_xoffset = getProperty(setting, "symbol_xoffset", Float.class);
            if (symbol_xoffset != null) textSymbol.setOffsetX(symbol_xoffset);
            Float symbol_yoffset = getProperty(setting, "symbol_yoffset", Float.class);
            if (symbol_yoffset != null) textSymbol.setOffsetY(symbol_yoffset);

            //textSymbol.setFontFamily();
            String symbol_font_weight = getProperty(setting, "symbol_font_weight", String.class);
            if ("BOLD".equals(symbol_font_weight))
                textSymbol.setFontWeight(TextSymbol.FontWeight.BOLD);
            if ("NORMAL".equals(symbol_font_weight))
                textSymbol.setFontWeight(TextSymbol.FontWeight.NORMAL);

            String symbol_font_style = getProperty(setting, "symbol_font_style", String.class);
            if ("NORMAL".equals(symbol_font_style))
                textSymbol.setFontStyle(TextSymbol.FontStyle.NORMAL);
            if ("ITALIC".equals(symbol_font_style))
                textSymbol.setFontStyle(TextSymbol.FontStyle.ITALIC);
            if ("OBLIQUE".equals(symbol_font_style))
                textSymbol.setFontStyle(TextSymbol.FontStyle.OBLIQUE);

            String symbol_font_decoration = getProperty(setting, "symbol_font_decoration", String.class);
            if ("LINE_THROUGH".equals(symbol_font_decoration))
                textSymbol.setFontDecoration(TextSymbol.FontDecoration.LINE_THROUGH);
            if ("NONE".equals(symbol_font_decoration))
                textSymbol.setFontDecoration(TextSymbol.FontDecoration.NONE);
            if ("UNDERLINE".equals(symbol_font_decoration))
                textSymbol.setFontDecoration(TextSymbol.FontDecoration.UNDERLINE);

            String where = getProperty(setting, "where", String.class);
            String expression = getProperty(setting, "expression", String.class);

            String symbolJson = textSymbol.toJson();
            Map symbol = ObjectUtil.Common.convert(symbolJson, HashMap.class);
            Map labelMap = new HashMap();
            labelMap.put("symbol", symbol);
            if (ObjectUtil.Common.isEmpty(where)) labelMap.put("where", where);
            Map expressionInfo = new HashMap();
            expressionInfo.put("expression", "$feature." + expression);
            labelMap.put("labelExpressionInfo", expressionInfo);
            labelMap.put("labelPlacement", "esriServerPolygonPlacementAlwaysHorizontal");
            labelMap.put("symbol", symbol);

            String labelJson = ObjectUtil.Common.convert(labelMap);
            LabelDefinition definition = LabelDefinition.fromJson(labelJson);
            layer.getLabelDefinitions().clear();
            layer.getLabelDefinitions().add(definition);
            layer.setLabelsEnabled(enable);

            if (enable != null) layer.setLabelsEnabled(enable);
        }
    }


}
