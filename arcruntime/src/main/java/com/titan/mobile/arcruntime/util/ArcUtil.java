package com.titan.mobile.arcruntime.util;

import android.graphics.PointF;

import com.esri.arcgisruntime.data.CodedValue;
import com.esri.arcgisruntime.data.CodedValueDomain;
import com.esri.arcgisruntime.data.Domain;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.Field;
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
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zy on 2019/5/14.
 */

public final class ArcUtil {

    public static class Convert {

        public static boolean checkArcJsonVerify(String arcJson) {
            boolean flag = true;
            try {
                Geometry.fromJson(arcJson);
            } catch (Exception e) {
                flag = false;
            }
            return flag;
        }

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
            if (json == null) return null;
            Geometry geometry = Geometry.fromJson(json);
            return geometry;
        }

        public static String wkt2Json(String wkt, int wkid) {
            if (wkt == null) return null;
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

    public static class Tp {

        public static Class arcType2javaType(Field field) {
            return arcType2javaType(field.getFieldType());
        }

        public static Class arcType2javaType(Field.Type arcType) {
            if (arcType == Field.Type.SHORT) {
                return Short.class;
            } else if (arcType == Field.Type.INTEGER) {
                return Integer.class;
            } else if (arcType == Field.Type.GUID) {
                return Integer.class;
            } else if (arcType == Field.Type.FLOAT) {
                return Float.class;
            } else if (arcType == Field.Type.DOUBLE) {
                return Double.class;
            } else if (arcType == Field.Type.DATE) {
                return GregorianCalendar.class;
            } else if (arcType == Field.Type.TEXT) {
                return String.class;
            } else if (arcType == Field.Type.OID) {
                return Integer.class;
            } else if (arcType == Field.Type.GLOBALID) {
                return Integer.class;
            } else if (arcType == Field.Type.BLOB) {
                return byte[].class;
            } else if (arcType == Field.Type.GEOMETRY) {
                return String.class;
            } else if (arcType == Field.Type.RASTER) {
                return String.class;
            } else if (arcType == Field.Type.XML) {
                return String.class;
            } else {
                return String.class;
            }
        }
    }

    public static class Fld {

        public static List<Field> getFields(FeatureLayer fLayer) {
            FeatureTable table = fLayer.getFeatureTable();
            if (table == null) return new ArrayList<>();
            List<Field> fields = table.getFields();
            return fields;
        }

        public static List<Field> getFields(FeatureTable table) {
            if (table == null) return new ArrayList<>();
            List<Field> fields = table.getFields();
            return fields;
        }

        public static Field getField(List<Field> fields, String name) {
            Field field;
            for (int i = 0, len = fields.size(); i < len; i++) {
                field = fields.get(i);
                if (field.getName().equals(name)) return field;
            }
            return null;
        }

        public static Field getPkField(List<Field> fields) {
            Field field = null;
            for (int i = 0, len = fields.size(); i < len; i++) {
                field = fields.get(i);
                if (field.getFieldType() == Field.Type.OID) return field;
            }
            return field;
        }

        public static Field getPkField(FeatureTable table) {
            if (table == null) return null;
            List<Field> fields = table.getFields();
            return getPkField(fields);
        }

        public static String getPkFieldName(FeatureTable table) {
            if (table == null) return "";
            List<Field> fields = table.getFields();
            Field field = getPkField(fields);
            if (field == null) return "";
            return field.getName();
        }

        public static String getFeaIdName(Feature feature) {
            FeatureTable table = feature.getFeatureTable();
            if (table == null) return "";
            List<Field> fields = table.getFields();
            Field field = getPkField(fields);
            if (field == null) return "";
            return field.getName();
        }

        public static long getFeaId(Feature feature) {
            String id = getFeaIdName(feature);
            return (long) feature.getAttributes().get(id);
        }

        public static String getPkFieldName(FeatureLayer layer) {
            return getPkFieldName(layer.getFeatureTable());
        }

        public static List<CodedValue> getFieldMapping(Feature feature, String filedName) {
            if (feature == null) return new ArrayList<>();
            FeatureTable fTable = feature.getFeatureTable();
            if (fTable == null) return new ArrayList<>();
            Field field = fTable.getField(filedName);
            Domain domain = field.getDomain();
            if (domain instanceof CodedValueDomain) {
                CodedValueDomain valueDomain = (CodedValueDomain) domain;
                return valueDomain.getCodedValues();
            }
            return null;
        }
    }

    public static class Layer {
        public static String getLayerName(Feature feature) {
            FeatureTable featureTable = feature.getFeatureTable();
            if (featureTable == null) return "";
            return featureTable.getFeatureLayer().getName();
        }


        public static String getLayerIndex(Feature feature) {
            FeatureTable featureTable = feature.getFeatureTable();
            if (featureTable == null) return "";
            FeatureLayer layer = featureTable.getFeatureLayer();
            if (layer == null) {
                return "";
            }
            String id = layer.getId();
            return id;
        }
    }

    public static class Geo {
        private Geo() {

        }

        public static List<Point> getPoints(Geometry geometry) {
            if (geometry instanceof Polyline) {
                Polyline polyline = (Polyline) geometry;
                ImmutablePartCollection parts = polyline.getParts();
                List<Point> points = new ArrayList<>();
                for (int i = 0; i < parts.size(); i++) {
                    ImmutablePart part = parts.get(i);
                    Iterator<Point> iterable = part.getPoints().iterator();
                    while (iterable.hasNext()) {
                        points.add(iterable.next());
                    }
                }
                return points;

            } else if (geometry instanceof Polygon) {
                Polygon polygon = (Polygon) geometry;
                ImmutablePartCollection parts = polygon.toPolyline().getParts();
                List<Point> points = new ArrayList<>();
                for (int i = 0; i < parts.size(); i++) {
                    ImmutablePart part = parts.get(i);
                    Iterator<Point> iterable = part.getPoints().iterator();
                    while (iterable.hasNext()) {
                        points.add(iterable.next());
                    }
                }
                return points;
            } else if (geometry instanceof Multipoint) {
                Multipoint multipoint = (Multipoint) geometry;
                ImmutablePointCollection collection = multipoint.getPoints();
                List<Point> points = new ArrayList<>(collection);
                return points;
            } else if (geometry instanceof Envelope) {
                Envelope envelope = (Envelope) geometry;
                return new ArrayList<>();
            } else {
                List<Point> points = new ArrayList<>();
                points.add((Point) geometry);
                return points;
            }
        }

        public static List<Geometry> getExpectMaxAreaGeo(List<Geometry> geometries) {
            Geometry temp;
            Geometry maxGeo = null;
            int maxIndex = -1;
            for (int i = 0, len = geometries.size(); i < len; i++) {
                temp = geometries.get(i);
                if (!(temp instanceof Polygon)) continue;
                if (maxGeo == null) {
                    maxGeo = temp;
                    maxIndex = i;
                }
                if (GeometryEngine.area((Polygon) temp) > GeometryEngine.area((Polygon) maxGeo)) {
                    maxGeo = temp;
                    maxIndex = i;
                }
            }
            List<Geometry> _geometries = new ArrayList<>();
            for (int i = 0, len = geometries.size(); i < len; i++) {
                if (maxIndex == i) continue;
                _geometries.add(geometries.get(i));
            }
            return _geometries;
        }

        public static Polygon getMaxAreaGeo(List<Geometry> geometries) {
            Geometry temp;
            Geometry maxGeo = geometries.get(0);
            for (int i = 1, len = geometries.size(); i < len; i++) {
                temp = geometries.get(i);
                if (!(temp instanceof Polygon)) continue;
                if (maxGeo == null) maxGeo = temp;
                if (GeometryEngine.area((Polygon) temp) > GeometryEngine.area((Polygon) maxGeo))
                    maxGeo = temp;
            }
            return (Polygon) maxGeo;
        }

        public static Polygon getMinAreaGeo(List<Geometry> geometries) {
            Geometry temp;
            Geometry minGeo = null;
            for (int i = 0, len = geometries.size(); i < len; i++) {
                temp = geometries.get(i);
                if (!(temp instanceof Polygon)) continue;
                if (minGeo == null) minGeo = temp;
                if (GeometryEngine.area((Polygon) temp) < GeometryEngine.area((Polygon) minGeo))
                    minGeo = temp;
            }
            return (Polygon) minGeo;
        }

        public static List<PointF> toScreenPoints(MapView mapView, List<Point> points) {
            if (points == null) return null;
            List<PointF> pointFS = new ArrayList<>();
            for (Point point : points) {
                android.graphics.Point _p = mapView.locationToScreen(point);
                PointF pointF = new PointF(_p);
                pointFS.add(pointF);
            }
            return pointFS;
        }

        public static int getTouchIndex(PointF pointF, List<PointF> points, float dis) {
            if (points == null || pointF == null) return -1;
            int index = 0;
            for (PointF point : points) {
                double _dis = Math.sqrt((pointF.x - point.x) * (pointF.x - point.x) + (pointF.y - point.y) * (pointF.y - point.y));
                if (dis > _dis) {
                    return index;
                }
                index++;
            }
            return -1;
        }
    }

    public static class Fea {

        private Fea() {

        }


        public static Object getFeatureAttr(Feature feature, String key) {
            if (feature == null) return null;
            return feature.getAttributes().get(key);
        }


        /**
         * 比较feature和entity 的属性是否相同
         *
         * @param feature
         * @param entity
         * @param attr
         * @return
         */
        public static boolean compareFeature(Feature feature, Object entity, Map<String, String> attr) {
            Object o1, o2;
            String key2;
            Boolean equal = true;
            for (String key1 : attr.keySet()) {
                key2 = attr.get(key1);
                o1 = getFeatureAttr(feature, key1);
                o2 = ObjectUtil.Reflect.getObjFieldVal(entity, key2);
                if (!ObjectUtil.Common.baseTypeIsEqual(o1, o2)) {
                    equal = false;
                    break;
                }
            }
            return equal;
        }
    }

    public static class Attr {

        public static Attr replaceMapping(Map attribute, String key, Map mapping) {
            Object original = attribute.get(key);
            for (Object item : mapping.keySet()) {
                if (item.toString().equals(original)) {
                    attribute.put(key, mapping.get(item));
                }
            }
            return null;
        }

        public static Attr replaceMaps(List<Map> attribute, String key, Map mapping) {
            if (attribute == null) return null;
            for (Map map : attribute) {
                replaceMapping(map, key, mapping);
            }
            return null;
        }

        public static long getPk(FeatureLayer layer, Feature feature) {
            String name = Fld.getPkFieldName(layer);
            long pk = ObjectUtil.Maps.searchValue(feature.getAttributes(), name, Long.class);
            return pk;
        }

        public static long getPk(Feature feature) {
            FeatureTable fTable = feature.getFeatureTable();
            if (fTable == null) return 0l;
            String name = Fld.getPkFieldName(fTable);
            long pk = ObjectUtil.Maps.searchValue(feature.getAttributes(), name, Long.class);
            return pk;
        }

        public static long getPk(FeatureTable fTable, Feature feature) {
            if (fTable == null) return 0l;
            String name = Fld.getPkFieldName(fTable);
            long pk = ObjectUtil.Maps.searchValue(feature.getAttributes(), name, Long.class);
            return pk;
        }

        public static long getPk(FeatureLayer layer, Map attribute) {
            String name = Fld.getPkFieldName(layer);
            long pk = ObjectUtil.Maps.searchValue(attribute, name, Long.class);
            return pk;
        }

        public static long getPk(FeatureTable fTable, Map attribute) {
            if (fTable == null) return 0l;
            String name = Fld.getPkFieldName(fTable);
            long pk = ObjectUtil.Maps.searchValue(attribute, name, Long.class);
            return pk;
        }

        public static <T> T getProperty(Map attribute, String name, Class clazz) {
            T t = ObjectUtil.Maps.searchValue(attribute, name, clazz);
            return t;
        }

        public static Map cloneAttrEffect(Map attribute) {
            Map map = new HashMap();
            Map.Entry<String, Object> entry;
            Set<Map.Entry<String, Object>> entrySet = attribute.entrySet();
            Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                entry = iterator.next();
                map.put(entry.getKey(), entry.getValue());
            }
            return map;
        }

        public static Map cloneAndMappingAttr(Map<String, Field> mapping, Feature feature) {
            Map attribute = feature.getAttributes();
            Map map = new HashMap();
            Map.Entry<String, Object> entry;
            Set<Map.Entry<String, Object>> entrySet = attribute.entrySet();
            Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
            String key;
            Field field;
            Object value;

            CodedValueDomain codedValueDomain;
            List<CodedValue> list;
            while (iterator.hasNext()) {
                entry = iterator.next();
                key = entry.getKey();
                field = mapping.get(key);
                Domain domain = field.getDomain();
                value = entry.getValue();
                map.put(key, value);
                if (domain instanceof CodedValueDomain) {
                    codedValueDomain = (CodedValueDomain) domain;
                    list = codedValueDomain.getCodedValues();
                    for (CodedValue codedValue : list) {
                        if (codedValue.getCode().toString().equals(value.toString())) {
                            value = codedValue.getName();
                            map.put(key + "-NAME", value);
                            break;
                        }
                    }
                }
            }
            return map;
        }

        public static Map obtainAttr(Map attribute) {
            Map map = new LinkedHashMap();
            for (Object key : attribute.keySet()) {
                map.put(key, attribute.get(key));
            }
            return map;
        }

        public static Map handleAttr(FeatureTable fTable, Map attribute) {
            if (fTable == null) return new HashMap();
            List<Field> fields = Fld.getFields(fTable);
            Map map = new HashMap();
            Field field;
            String name;
            Object orgValue;
            Object newValue;
            Class type;
            for (int i = 0, len = fields.size(); i < len; i++) {
                field = fields.get(i);
                if (!field.isEditable()) continue;
                name = field.getName();
                if (!attribute.containsKey(name)) continue;
                orgValue = attribute.get(name);
                type = Tp.arcType2javaType(field);
                newValue = ObjectUtil.Common.convert(orgValue, type);
                map.put(name, newValue);
            }
            return map;
        }

        public static Map handleAddAttr(FeatureTable table, Map attribute) {
            if (table == null) return new HashMap();
            List<Field> fields = Fld.getFields(table);
            Map map = new HashMap();
            Field field;
            String name;
            Object orgValue;
            Object newValue;
            Class type;
            for (int i = 0, len = fields.size(); i < len; i++) {
                field = fields.get(i);
                name = field.getName();
                if (!attribute.containsKey(name)) continue;
                orgValue = attribute.get(name);
                type = Tp.arcType2javaType(field);
                newValue = ObjectUtil.Common.convert(orgValue, type);
                map.put(name, newValue);
            }
            return map;
        }

        public static Map handleAttr(FeatureLayer fLayer, Map attribute) {
            List<Field> fields = Fld.getFields(fLayer);
            Map map = new HashMap();
            Field field;
            String name;
            Object orgValue;
            Object newValue;
            Class type;
            for (int i = 0, len = fields.size(); i < len; i++) {
                field = fields.get(i);
                name = field.getName();
                if (!attribute.containsKey(name)) continue;
                orgValue = attribute.get(name);
                type = Tp.arcType2javaType(field);
                newValue = ObjectUtil.Common.convert(orgValue, type);
                map.put(name, newValue);
            }
            return map;
        }

        public static Feature editAttr(Feature feature, Map editAttr) {
            Field field;
            Object value;
            Class type;
            List<Field> fields = Fld.getFields(feature.getFeatureTable());
            for (Object name : editAttr.keySet()) {
                field = Fld.getField(fields, name + "");
                assert field != null;
                if (!field.isEditable()) continue;
                if (field.getFieldType() == Field.Type.OID) continue;
                value = editAttr.get(name);
                type = Tp.arcType2javaType(field);
                value = ObjectUtil.Common.convert(value, type);
                feature.getAttributes().put(name + "", value);
            }
            return feature;
        }
    }
}
