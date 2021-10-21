package com.titan.mobile.arcruntime.util;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.geometry.ImmutablePart;
import com.esri.arcgisruntime.geometry.ImmutablePartCollection;
import com.esri.arcgisruntime.geometry.ImmutablePointCollection;
import com.esri.arcgisruntime.geometry.Multipoint;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.titan.mobile.arcruntime.core.ArcMap;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2018/1/5.
 */

public class TransformUtil {

    public static class Convert {

        public static String jstGeometry2Wkt(com.vividsolutions.jts.geom.Geometry geometry) {
            try {
                if (geometry == null) return null;
                WKTWriter wktWriter = new WKTWriter();
                StringWriter stringWriter = new StringWriter();
                wktWriter.write(geometry, stringWriter);
                String wkt = stringWriter.toString();
                return wkt;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static com.vividsolutions.jts.geom.Geometry wkt2JtsGeometry(String wkt) {
            try {
                WKTReader wktReader = new WKTReader();
                return wktReader.read(wkt);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static Geometry jtsGeometry2ArcGeometry(com.vividsolutions.jts.geom.Geometry jtsGeometry, int wkid) {
            String wkt = jstGeometry2Wkt(jtsGeometry);
            return wkt2ArcGeometry(wkt, wkid);
        }

        public static com.vividsolutions.jts.geom.Geometry arcGeometry2JtsGeometry(Geometry arcGeometry) {
            String wkt = getWkt(arcGeometry);
            return wkt2JtsGeometry(wkt);
        }

        public static String getWkt(Geometry geometry) {
            String wkt = null;
            if (geometry != null) {
                GeometryType geometryType = geometry.getGeometryType();
                if (geometryType == GeometryType.POINT) {
                    wkt = getPOINT2Wkt(geometry);
                }
                if (geometryType == GeometryType.MULTIPOINT) {
                    wkt = getMULTIPOINT2Wkt(geometry);
                }
                if (geometryType == GeometryType.POLYGON) {
                    wkt = getPOLYGON2Wkt(geometry);
                }
                if (geometryType == GeometryType.ENVELOPE) {
                    wkt = getENVELOPE2Wkt(geometry);
                }
                if (geometryType == GeometryType.POLYLINE) {
                    wkt = getPOLYLINE2Wkt(geometry);
                }
                if (geometryType == GeometryType.UNKNOWN) {
                    wkt = geometry.toString();
                }
            }
            return wkt;
        }

        private static String getPOINT2Wkt(Geometry geometry) {
            String wkt = "";
            Point point = (Point) geometry;
            wkt = "POINT (" + point.getX() + " " + point.getY() + ")";
            return wkt;
        }

        private static String getMULTIPOINT2Wkt(Geometry geometry) {
            String wkt = "";
            StringBuilder sb = new StringBuilder("MULTIPOINT");
            Multipoint multipoint = (Multipoint) geometry;
            ImmutablePointCollection points = multipoint.getPoints();
            Point point;
            for (int i = 0; points != null && i < points.size(); i++) {
                if (i == 0) {
                    sb.append("(");
                }
                point = points.get(i);
                sb.append(point.getX() + " " + point.getY());
                if (i != points.size() - 1) {
                    sb.append(",");
                } else {
                    sb.append(")");
                }
            }
            wkt = sb.toString();
            return wkt;
        }

        private static String getPOLYGON2Wkt(Geometry geometry) {
            String wkt = "";
            StringBuilder sb = new StringBuilder("POLYGON (");
            Polygon polygon = (Polygon) geometry;
            ImmutablePartCollection partCollection = polygon.getParts();
            int partSize = partCollection.size();
            ImmutablePart immutablePart;
            Iterator<Point> iterator;
            Point point;
            for (int i = 0; i < partSize; i++) {
                sb.append("(");
                immutablePart = partCollection.get(i);
                iterator = immutablePart.getPoints().iterator();
                boolean isStart = true;
                String startStr = "";
                while (iterator.hasNext()) {
                    point = iterator.next();
                    if (isStart) {
                        startStr = point.getX() + " " + point.getY();
                        isStart = false;
                    }
                    sb.append(point.getX() + " " + point.getY()).append(",");
                }
                sb.append(startStr);
                sb.append(")");
                if (i != partSize - 1) {
                    sb.append(",");
                }
            }
            sb.append(")");
            wkt = sb.toString();
            return wkt;
        }

        private static String getPOLYLINE2Wkt(Geometry geometry) {
            String wkt = "";
            StringBuilder sb;
            Polyline polyline = (Polyline) geometry;
            ImmutablePartCollection partCollection = polyline.getParts();
            int partSize = partCollection.size();
            if (partSize == 1) {
                sb = new StringBuilder("LINESTRING (");
            } else {
                sb = new StringBuilder("MULTILINESTRING (");
            }
            ImmutablePart immutablePart;
            Iterator<Point> iterator;
            Point point;
            for (int i = 0; i < partSize; i++) {
                sb.append("(");
                immutablePart = partCollection.get(i);
                iterator = immutablePart.getPoints().iterator();
                while (iterator.hasNext()) {
                    point = iterator.next();
                    sb.append(point.getX() + " " + point.getY()).append(",");
                }
                sb.substring(0, sb.length() - 1);
                sb.append(")");
                if (i != partSize - 1) {
                    sb.append(",");
                }
            }
            sb.append(")");
            wkt = sb.toString();
            return wkt;
        }

        private static String getENVELOPE2Wkt(Geometry geometry) {
            StringBuilder sb = new StringBuilder("ENVELOPE");
            Envelope env = (Envelope) geometry;
            sb.append("(").append(env.getXMin()).append(",").append(env.getYMin()).append(",").append(env.getXMax()).append(",").append(env.getYMax()).append(")");
            return sb.toString();
        }


        private static class PointObject {
            private double x;
            private double y;
            private HashMap<String, Integer> spatialReference;

            public double getX() {
                return x;
            }

            public void setX(double x) {
                this.x = x;
            }

            public double getY() {
                return y;
            }

            public void setY(double y) {
                this.y = y;
            }

            public HashMap<String, Integer> getSpatialReference() {
                return spatialReference;
            }

            public void setSpatialReference(HashMap<String, Integer> spatialReference) {
                this.spatialReference = spatialReference;
            }
        }

        private static class MultiIPointObject {
            private List<Double[]> points;
            private HashMap<String, Integer> spatialReference;

            public List<Double[]> getPoints() {
                return points;
            }

            public void setPoints(List<Double[]> points) {
                this.points = points;
            }

            public HashMap<String, Integer> getSpatialReference() {
                return spatialReference;
            }

            public void setSpatialReference(HashMap<String, Integer> spatialReference) {
                this.spatialReference = spatialReference;
            }
        }

        private static class LineStringObject {
            private List<List<Double[]>> paths;
            private HashMap<String, Integer> spatialReference;

            public List<List<Double[]>> getPaths() {
                return paths;
            }

            public void setPaths(List<List<Double[]>> paths) {
                this.paths = paths;
            }

            public HashMap<String, Integer> getSpatialReference() {
                return spatialReference;
            }

            public void setSpatialReference(HashMap<String, Integer> spatialReference) {
                this.spatialReference = spatialReference;
            }
        }

        private static class MultLinesStringObject {
            private List<List<Double[]>> rings;
            private HashMap<String, Integer> spatialReference;

            public List<List<Double[]>> getRings() {
                return rings;
            }

            public void setRings(List<List<Double[]>> rings) {
                this.rings = rings;
            }

            public HashMap<String, Integer> getSpatialReference() {
                return spatialReference;
            }

            public void setSpatialReference(HashMap<String, Integer> spatialReference) {
                this.spatialReference = spatialReference;
            }
        }

        private static class PolygonObject {
            private List<List<Double[]>> rings;
            private HashMap<String, Integer> spatialReference;

            public List<List<Double[]>> getRings() {
                return rings;
            }

            public void setRings(List<List<Double[]>> rings) {
                this.rings = rings;
            }

            public HashMap<String, Integer> getSpatialReference() {
                return spatialReference;
            }

            public void setSpatialReference(HashMap<String, Integer> spatialReference) {
                this.spatialReference = spatialReference;
            }
        }

        public static Geometry wkt2ArcGeometry(String wkt, int wkid) {
            String json = wkt2Json(wkt, wkid);
            Geometry geometry = Geometry.fromJson(json);
            return geometry;
        }

        public static String wkt2Json(String wkt, int wkid) {
            String _wkt = wkt.toUpperCase();
            String json;
            if (_wkt.startsWith("POINT")) {
                json = getPOINTWktToJson(wkt, wkid);
            } else if (_wkt.startsWith("MULTIPOINT")) {
                json = getMULTIPOINTWktToJson(wkt, wkid);
            } else if (_wkt.startsWith("LINESTRING")) {
                json = getLINESTRINGWktToJson(wkt, wkid);
            } else if (_wkt.startsWith("MULTILINESTRING")) {
                json = getMULTILINESTRINGWktToJson(wkt, wkid);
            } else if (_wkt.startsWith("POLYGON")) {
                json = getPOLYGONWktToJson(wkt, wkid);
            } else {
                json = getMULTIPOLYGONWktToJson(wkt, wkid);
            }
            return json;
        }

        /**
         * 点 转换 JSON
         *
         * @param wkt
         * @param wkid
         * @return
         */
        private static String getPOINTWktToJson(String wkt, int wkid) {
            String[] strHead = wkt.split("\\(");
            String strContent = strHead[1].substring(0, strHead[1].length() - 1);
            String[] strResult = strContent.split(" ");
            PointObject pointObject = new PointObject();
            pointObject.setX(Double.parseDouble(strResult[0]));
            pointObject.setY(Double.parseDouble(strResult[1]));
            HashMap<String, Integer> spatialReference = new HashMap<>();
            spatialReference.put("wkid", wkid);
            pointObject.setSpatialReference(spatialReference);

            String json = ObjectUtil.Common.convert(pointObject);
            return json;
        }

        /**
         * 多点 转换 JSON
         *
         * @param wkt
         * @param wkid
         * @return
         */
        private static String getMULTIPOINTWktToJson(String wkt, int wkid) {
            MultiIPointObject multiIPointObject = new MultiIPointObject();
            String ToTailWkt = wkt.substring(0, wkt.length() - 1);
            String[] strHead = ToTailWkt.split("\\(\\(");
            String strMiddle = strHead[1].substring(0, strHead[1].length() - 1);
            String[] strMiddles = strMiddle.split(",");
            List<Double[]> list = new ArrayList<>();
            for (int i = 0; i < strMiddles.length; i++) {
                if (i == 0) {
                    String item = strMiddles[i].substring(0,
                            strMiddles[i].length() - 1);
                    String[] items = item.split(" ");
                    Double[] listResult = new Double[]{
                            Double.parseDouble(items[0]),
                            Double.parseDouble(items[1])};
                    list.add(listResult);
                } else if (i == strMiddles.length) {
                    String item = strMiddles[i]
                            .substring(1, strMiddles[i].length());
                    String[] items = item.split(" ");
                    Double[] listResult = new Double[]{
                            Double.parseDouble(items[0]),
                            Double.parseDouble(items[1])};
                    list.add(listResult);
                } else {
                    String strItem = strMiddles[i].trim();
                    String item = strItem.substring(1, strItem.length() - 1);
                    String[] items = item.split(" ");
                    Double[] listResult = new Double[]{
                            Double.parseDouble(items[0]),
                            Double.parseDouble(items[1])};
                    list.add(listResult);
                }
            }
            HashMap<String, Integer> spatialReference = new HashMap<>();
            spatialReference.put("wkid", wkid);
            multiIPointObject.setPoints(list);
            multiIPointObject.setSpatialReference(spatialReference);
            String json = ObjectUtil.Common.convert(multiIPointObject);
            return json;
        }

        /**
         * 线 转换 JSON
         *
         * @param wkt
         * @param wkid
         * @return
         */
        private static String getLINESTRINGWktToJson(String wkt, int wkid) {
            LineStringObject lineStringObject = new LineStringObject();
            List<List<Double[]>> lists = new ArrayList<>();
            List<Double[]> list = new ArrayList<>();
            String[] strHead = wkt.split("\\(");
            String strContent = strHead[1].substring(0, strHead[1].length() - 1);
            String[] strResult = strContent.split(",");
            for (int i = 0; i < strResult.length; i++) {
                String itme = strResult[i].trim();
                String[] items = itme.split(" ");
                Double[] listResult = new Double[]{Double.parseDouble(items[0]),
                        Double.parseDouble(items[1])};
                list.add(listResult);
            }
            lists.add(list);
            HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
            spatialReference.put("wkid", wkid);
            lineStringObject.setPaths(lists);
            lineStringObject.setSpatialReference(spatialReference);
            String json = ObjectUtil.Common.convert(lineStringObject);
            return json;
        }

        /**
         * 多线 转换 JSON
         *
         * @param wkt
         * @param wkid
         * @return
         */
        private static String getMULTILINESTRINGWktToJson(String wkt, int wkid) {
            MultLinesStringObject lineStringObject = new MultLinesStringObject();
            List<List<Double[]>> lists = new ArrayList<>();
            String ToTailWkt = wkt.substring(0, wkt.length() - 1);
            String[] strHead = ToTailWkt.split("\\(", 2);
            String[] strList = strHead[1].split("\\),\\(");
            for (int i = 0; i < strList.length; i++) {
                String item = strList[i].trim();
                item = item.substring(1, item.length() - 1);
                String[] items = item.split(",");
                List<Double[]> list = new ArrayList<>();
                for (int j = 0; j < items.length; j++) {
                    String jItem = items[j].trim();
                    String[] jItems = jItem.split(" ");
                    Double[] listResult = new Double[]{
                            Double.parseDouble(jItems[0]),
                            Double.parseDouble(jItems[1])};
                    list.add(listResult);
                }
                lists.add(list);
            }
            HashMap<String, Integer> spatialReference = new HashMap<>();
            spatialReference.put("wkid", wkid);
            lineStringObject.setRings(lists);
            lineStringObject.setSpatialReference(spatialReference);
            String json = ObjectUtil.Common.convert(lineStringObject);
            return json;
        }

        /**
         * 多边形 转换 JSON
         *
         * @param wkt
         * @param wkid
         * @return
         */
        private static String getPOLYGONWktToJson(String wkt, int wkid) {
            PolygonObject polygonObject = new PolygonObject();
            List<List<Double[]>> lists = new ArrayList<>();
            String ToTailWkt = wkt.substring(0, wkt.length() - 1);
            String[] strHead = ToTailWkt.split("\\(", 2);
            String[] strList = strHead[1].split("\\), \\(");
            for (int i = 0; i < strList.length; i++) {
                String item = strList[i].trim();
                item = item.substring(1, item.length() - 1);
                String[] items = item.split(",");
                List<Double[]> list = new ArrayList<>();
                for (int j = 0; j < items.length; j++) {
                    String jItem = items[j].trim();
                    String[] jItems = jItem.split(" ");
                    Double[] listResult = new Double[]{
                            Double.parseDouble(jItems[0]),
                            Double.parseDouble(jItems[1])};
                    list.add(listResult);
                }
                lists.add(list);
            }
            HashMap<String, Integer> spatialReference = new HashMap<>();
            spatialReference.put("wkid", wkid);
            polygonObject.setRings(lists);
            polygonObject.setSpatialReference(spatialReference);
            String json = ObjectUtil.Common.convert(polygonObject);
            return json;
        }

        /**
         * 多个多边形 转换 JSON
         *
         * @param wkt
         * @param wkid
         * @return
         */
        private static String getMULTIPOLYGONWktToJson(String wkt, int wkid) {
            PolygonObject polygonObject = new PolygonObject();
            List<List<Double[]>> lists = new ArrayList<>();

            String ToTailWkt = wkt.substring(0, wkt.length() - 1);
            String[] strHead = ToTailWkt.split("\\(", 2);
            ToTailWkt = strHead[1].substring(0, strHead[1].length() - 1);
            String[] strHeads = ToTailWkt.split("\\(", 2);
            String[] strList = strHeads[1].split("\\), \\(");
            if (strList.length == 1) {
                for (int i = 0; i < strList.length; i++) {
                    String item = strList[i].trim();
                    item = item.substring(1, item.length() - 1);
                    String[] items = item.split(",");
                    List<Double[]> list = new ArrayList<>();
                    for (int j = 0; j < items.length; j++) {
                        String jItem = items[j].trim();
                        String[] jItems = jItem.split(" ");
                        Double[] listResult = new Double[]{
                                Double.parseDouble(jItems[0]),
                                Double.parseDouble(jItems[1])};
                        list.add(listResult);
                    }
                    lists.add(list);
                }
            } else {
                for (int i = 0; i < strList.length; i++) {
                    String item = strList[i].trim();
                    item = item.substring(1, item.length() - 1);
                    String[] items = item.split(",");
                    List<Double[]> list = new ArrayList<>();
                    for (int j = 1; j < items.length; j++) {
                        String jItem = items[j].trim();
                        String[] jItems = jItem.split(" ");
                        Double[] listResult = new Double[]{
                                Double.parseDouble(jItems[0]),
                                Double.parseDouble(jItems[1])};
                        list.add(listResult);
                    }
                    lists.add(list);
                }
            }
            HashMap<String, Integer> spatialReference = new HashMap<>();
            spatialReference.put("wkid", wkid);
            polygonObject.setRings(lists);
            polygonObject.setSpatialReference(spatialReference);
            String json = ObjectUtil.Common.convert(polygonObject);
            return json;
        }
    }


    //----------------------------------------------------------------------
    //----------------------------------------------------------------------
    //----------------------------------------------------------------------


    public static Map<String, Object> feaConvertMap(@NonNull Feature feature) {
        Map<String, Object> map = new HashMap<>();
        String wkt = Convert.getWkt(feature.getGeometry());
        map.put("SHAPE", wkt);
        Map<String, Object> property = feature.getAttributes();
        Object val;
        for (String key : property.keySet()) {
            val = property.get(key);
            if (ObjectUtil.Time.isDate(val)) {
                val = ObjectUtil.Time.convert2DateStr(val);
            }
            map.put(key, val);
        }
        return map;
    }

    public static Envelope mergeEnvelope(List<Envelope> envelopes) {
        Envelope envelope = null;
        if (envelopes != null && envelopes.size() > 0) {
            double tempMinX = Double.MAX_VALUE, tempMaxX = Double.MIN_VALUE, tempMinY = Double.MAX_VALUE, tempMaxY = Double.MIN_VALUE;
            Envelope e1 = envelopes.get(0);
            if (e1 == null) return null;
            SpatialReference sr = e1.getSpatialReference();
            for (Envelope e : envelopes) {
                if (e.getXMin() < tempMinX) {
                    tempMinX = e.getXMin();
                }
                if (e.getXMax() > tempMaxX) {
                    tempMaxX = e.getXMax();
                }

                if (e.getYMin() < tempMinY) {
                    tempMinY = e.getYMin();
                }
                if (e.getYMax() > tempMaxY) {
                    tempMaxY = e.getYMax();
                }
            }
            envelope = new Envelope(tempMinX, tempMinY, tempMaxX, tempMaxY, sr);
        }
        return envelope;
    }

    public static String esriFeature2GeoJson(Feature feature) {
       /* String wkt = getWkt(feature.getGeometry());
        Map<String, Object> attributes = feature.getAttributes();

        WKBReader wkbReader = new WKBReader();
        GeometryJSON gJson = new GeometryJSON(12);

        JSONObject geoJson = new JSONObject();
        JSONObject properties = new JSONObject();
        geoJson.put("geometry", new JSONObject(wkt));

        for (String key : attributes.keySet()) {
            if (key.toLowerCase().contains("shape") || key.toString().toLowerCase().contains("geom")) {
                feature.put("geometry", new JSONObject(geoJson));
                continue;
            }
            val = property.get(key);
            if (val == null) {
                properties.put(key, JSONObject.NULL);
                continue;
            }
            if (val instanceof String) {
                properties.put(key.toString(), val.toString());
            }
            if (val instanceof Number) {
                properties.put(key.toString(), val);
            }*/
        return null;
    }


    /**
     * @param arcMap
     * @param circleX 屏幕坐标
     * @param circleY 屏幕坐标
     * @param radius  距离 屏幕坐标
     * @param steps
     * @return
     */
    public static Geometry circle2Polygon(ArcMap arcMap, float circleX, float circleY, float radius, int steps) {
        List<Point> coordinates = destination(arcMap, circleX, circleY, radius, steps);
        PointCollection collection = new PointCollection(coordinates);
        Polygon polygon = new Polygon(collection, arcMap.getMapView().getSpatialReference());
        return polygon;
    }


    public static Polygon path2Polygon(ArcMap arcMap, Path path) {
        return null;
    }

    public static RectF getPathEnvelope(ArcMap arcMap, Path path) {
        return null;
    }


    /**
     * @param arcMap
     * @param circleX
     * @param circleY
     * @param radius
     * @param steps   分割份数
     * @return
     */
    public static List<Point> destination(ArcMap arcMap, float circleX, float circleY, float radius, int steps) {
        List<Point> positions = new ArrayList<>();
        Point temp;
        for (int i = 0; i < steps; i++) {
            double x = circleX + Math.cos(360 * i / steps) * radius;
            double y = circleY + Math.sin(360 * i / steps) * radius;
            temp = arcMap.screenToLocation((int) x, (int) y);
            positions.add(temp);
        }
        positions.add(positions.get(0));
        return positions;
    }

    public static Polygon screenBuffer(ArcMap arcMap, PointF pointF, float radius, int steps) {
        List<Point> points = destination(arcMap, pointF.x, pointF.y, radius, steps);
        PointCollection collection = new PointCollection(points);
        Polygon polygon = new Polygon(collection, arcMap.getMapView().getSpatialReference());
        return polygon;
    }
}
