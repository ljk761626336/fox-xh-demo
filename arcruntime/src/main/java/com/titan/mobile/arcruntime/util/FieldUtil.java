package com.titan.mobile.arcruntime.util;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.layers.FeatureLayer;

import java.util.List;

/**
 * @Description:
 * @Author: ljk
 * @CreateDate: 2021/4/14 15:25
 */
public class FieldUtil {

    public static boolean checkField(FeatureLayer featureLayer, String name) {
        if (featureLayer == null) return false;
        FeatureTable table = featureLayer.getFeatureTable();
        if (table == null) return false;
        List<Field> fieldList = table.getFields();
        for (Field field : fieldList) {
            if (field.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkField(Feature feature, String name) {
        if (feature == null) return false;
        FeatureTable table = feature.getFeatureTable();
        if (table == null) return false;
        List<Field> fieldList = table.getFields();
        for (Field field : fieldList) {
            if (field.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkField(FeatureTable table, String name) {
        if (table == null) return false;
        List<Field> fieldList = table.getFields();
        for (Field field : fieldList) {
            if (field.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

}
