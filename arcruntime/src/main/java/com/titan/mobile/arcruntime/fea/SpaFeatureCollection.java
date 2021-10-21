package com.titan.mobile.arcruntime.fea;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.geometry.Geometry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpaFeatureCollection implements Serializable {

    private String layerIndex;

    //private String layerName;

    private List<SpaFeature> collection;

    private Object tag;

    private SpaFeatureCollection(String layerIndex) {
        this.layerIndex = layerIndex;
    }

    private SpaFeatureCollection(Object tag) {
        this.tag = tag;
    }

    private SpaFeatureCollection(String layerIndex, Object tag) {
        this.layerIndex = layerIndex;
        this.tag = tag;
    }

    private SpaFeatureCollection(List<SpaFeature> collection) {
        this.collection = collection;
    }

    @Deprecated
    private SpaFeatureCollection(String layerIndex, List<SpaFeature> collection) {
        this.layerIndex = layerIndex;
        this.collection = collection;
    }

    @Deprecated
    private SpaFeatureCollection(String layerIndex, List<SpaFeature> collection, Object tag) {
        this.layerIndex = layerIndex;
        this.collection = collection;
        this.tag = tag;
    }

    public SpaFeatureCollection addSpaFeature(SpaFeature spaFeature) {
        if (collection == null) collection = new ArrayList<>();
        collection.add(spaFeature);
        return this;
    }

    public List<SpaFeature> getCollection() {
        return collection;
    }

    public String getLayerIndex() {
        return layerIndex;
    }

    /*@Deprecated
    public String getLayerName() {
        return layerName;
    }*/

    public Object getTag() {
        return tag;
    }

    public SpaFeatureCollection setLayerIndex(String layerIndex) {
        this.layerIndex = layerIndex;
        return this;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public boolean isEmpty() {
        return collection == null || collection.size() == 0;
    }

    public int count() {
        if (isEmpty()) return 0;
        return collection.size();
    }

    public List<Geometry> geometries() {
        if (collection == null) return new ArrayList<>();
        List<Geometry> geometries = new ArrayList<>(collection.size());
        for (int i = 0, len = collection.size(); i < len; i++) {
            geometries.add(collection.get(i).geometry());
        }
        return geometries;
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    public static SpaFeatureCollection createEmpty() {
        return new SpaFeatureCollection(new ArrayList<>());
    }

    @Deprecated
    public static SpaFeatureCollection createEmpty(String layerIndex) {
        return new SpaFeatureCollection(layerIndex);
    }

    public static SpaFeatureCollection fromLayerIndex(String layerIndex) {
        return new SpaFeatureCollection(layerIndex);
    }

    public static SpaFeatureCollection fromLayerIndex(String layerIndex, Object tag) {
        return new SpaFeatureCollection(layerIndex, tag);
    }

    public static SpaFeatureCollection fromFeatures(List<SpaFeature> collection) {
        return new SpaFeatureCollection(collection);
    }

    public static SpaFeatureCollection _fromFeatures(List<Feature> collection) {
        SpaFeatureCollection spaFeatureCollection = SpaFeatureCollection.createEmpty();
        for (int i = 0, len = collection.size(); i < len; i++) {
            spaFeatureCollection.addSpaFeature(SpaFeature.fromFeature(collection.get(i)));
        }
        return spaFeatureCollection;
    }

    public static SpaFeatureCollection fromFeatures(SpaFeature[] collection) {
        return new SpaFeatureCollection(Arrays.asList(collection));
    }

    public static SpaFeatureCollection fromFeatures(String layerIndex, List<SpaFeature> collection) {
        return new SpaFeatureCollection(layerIndex, collection);
    }

    public static SpaFeatureCollection fromFeatures(String layerIndex, SpaFeature[] collection) {
        return new SpaFeatureCollection(layerIndex, Arrays.asList(collection));
    }

    public static SpaFeatureCollection fromFeatures(String layerIndex, List<SpaFeature> collection, Object tag) {
        return new SpaFeatureCollection(layerIndex, collection, tag);
    }

    public static SpaFeatureCollection fromFeatures(String layerIndex, SpaFeature[] collection, Object tag) {
        return new SpaFeatureCollection(layerIndex, Arrays.asList(collection), tag);
    }

    public static List<SpaFeature> toSpaFeatures(List<SpaFeatureCollection> list) {
        List<SpaFeature> spaFeatures = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            spaFeatures.addAll(list.get(i).collection);
        }
        return spaFeatures;
    }

    public static List<Geometry> toGeometries(List<SpaFeatureCollection> list) {
        List<Geometry> geometries = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            geometries.addAll(list.get(i).geometries());
        }
        return geometries;
    }
}
