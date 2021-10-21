package com.titan.mobile.arcruntime.fea;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.geometry.Geometry;
import com.titan.mobile.arcruntime.util.ArcUtil;
import com.titan.mobile.arcruntime.util.ObjectUtil;

import java.io.Serializable;
import java.util.Map;

public final class SpaFeature implements Serializable {

    private String layerIndex;

    private Long id;

    private String geoJson;

    private Map property;

    private Object tag;

    private SpaFeature(String geoJson) {
        this.geoJson = geoJson;
    }

    private SpaFeature(String geoJson, Map property) {
        this.geoJson = geoJson;
        this.property = property;
    }

    private SpaFeature(String layerIndex, String geoJson) {
        this.layerIndex = layerIndex;
        this.geoJson = geoJson;
    }

    private SpaFeature(String layerIndex, String geoJson, Map property) {
        this.layerIndex = layerIndex;
        this.geoJson = geoJson;
        this.property = property;
    }

    public Long getId() {
        return id;
    }

    public SpaFeature setId(Long id) {
        this.id = id;
        return this;
    }

    public SpaFeature setLayerIndex(String layerIndex) {
        this.layerIndex = layerIndex;
        return this;
    }

    public String getLayerIndex() {
        return layerIndex;
    }

    public String getGeoJson() {
        return geoJson;
    }

    public Map getProperty() {
        return property;
    }

    public Object getProperty(String key) {
        if (property == null) return null;
        else return property.get(key);
    }

    public SpaFeature setProperty(Map property) {
        this.property = property;
        return this;
    }

    public Geometry geometry() {
        return Geometry.fromJson(geoJson);
    }

    public SpaFeature setGeometry(Geometry geometry) {
        if (geometry == null) return null;
        geoJson = geometry.toJson();
        return this;
    }

    public Object getTag() {
        return tag;
    }

    public SpaFeature setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    public String toJson() {
        String json = ObjectUtil.Common.convert(this);
        return json;
    }

    public boolean isEmpty() {
        if (geoJson == null && property == null) return true;
        return false;
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    public static SpaFeature createEmpty() {
        SpaFeature spaFeature = new SpaFeature(null);
        return spaFeature;
    }

    public static SpaFeature createEmpty(String layerIndex, Long id) {
        SpaFeature spaFeature = new SpaFeature(null);
        return spaFeature.setId(id).setLayerIndex(layerIndex);
    }

    public static SpaFeature fromFeature(String layerIndex, Feature feature) {
        SpaFeature spaFeature = new SpaFeature(feature.getGeometry().toJson(), feature.getAttributes());
        spaFeature.setLayerIndex(layerIndex);
        return spaFeature;
    }

    public static SpaFeature fromFeature(Feature feature) {
        Geometry geometry = feature.getGeometry();
        if (geometry == null) return SpaFeature.createEmpty();
        SpaFeature spaFeature = new SpaFeature(geometry.toJson(), feature.getAttributes());
        long id = ArcUtil.Attr.getPk(feature);
        String layerIndex = ArcUtil.Layer.getLayerIndex(feature);
        return spaFeature.setId(id).setLayerIndex(layerIndex);
    }

    public static SpaFeature fromGeometry(Geometry geometry) {
        return new SpaFeature(geometry.toJson());
    }

    public static SpaFeature fromGeometry(String layerIndex, Geometry geometry) {
        return new SpaFeature(layerIndex, geometry.toJson());
    }

    public static SpaFeature from(Geometry geometry, Map attr) {
        return new SpaFeature(geometry.toJson(), attr);
    }

    public static SpaFeature from(String layerIndex, Geometry geometry, Map attr) {
        return new SpaFeature(layerIndex, geometry.toJson(), attr);
    }

    public static SpaFeature from(String geoJson, Map attr) {
        return new SpaFeature(geoJson, attr);
    }

    public static SpaFeature fromJson(String json) {
        SpaFeature spaFeature = ObjectUtil.Common.convert(json, SpaFeature.class);
        return spaFeature;
    }

}
