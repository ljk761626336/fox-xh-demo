package com.titan.mobile.arcruntime.util;

import android.graphics.Color;
import android.util.Log;

import com.esri.arcgisruntime.arcgisservices.LabelDefinition;
import com.esri.arcgisruntime.arcgisservices.LabelingPlacement;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.titan.mobile.arcruntime.layer.info.Field;

import java.util.List;

public class LabelUtil {


    private String getExpression(List<Field> selFieldList) {
        //'小班号:'+$feature.XBH +'识别:'+$feature.name
        //'OBJECTID:'+$feature.OBJECTID+'省代码:'+$feature.SHENG_DM
        String json = "'" + selFieldList.get(0).getAlias() + ":'+$feature." + selFieldList.get(0).getName();
        for (int i = 1; i < selFieldList.size(); i++) {
            json = json + "+'" + selFieldList.get(i).getAlias() + ":'+$feature." + selFieldList.get(i).getName();
        }
        Log.e("", json);
        return json;
    }

    public static void setLayerLabel(FeatureLayer featureLayer, String aliea, String fieldName) {

        FeatureTable table = featureLayer.getFeatureTable();
        if (table == null) return;
        if (table.getTableName().contains("_PRE")) return;

        TextSymbol textSymbol = new TextSymbol();
        textSymbol.setSize(12.0f);
        textSymbol.setColor(Color.RED);
        textSymbol.setHaloColor(0xFFFFFF00);
        textSymbol.setHaloWidth(3);

        // 创建label字符串
        // construct the label definition json
        JsonObject json = new JsonObject();
        // prepend 'I - ' (for Interstate) to the route number for the label
        JsonObject expressionInfo = new JsonObject();
        //"$feature.XBH +'---'+$feature.OBJECTID"
        //"'OBJECTID:'+$feature.OBJECTID+'小班号:'+$feature.XBH"
        String expression = "";
        if (table.getTableName().contains("HLJ_ED_FZD")) {
            expression = "$feature.X+'\\n————\\n'+$feature.Y";
        } else if (table.getTableName().contains("_XBM")) {
            //林班号 小班号 面积  林种 地类
            //"'林班:'+$feature.LBH+'小班:'+$feature.XBH+'\\n————\\n面积'+$feature.XBMJ";
            expression = "$feature.LBH+'-'+$feature.XBH+'\\n————\\n'+$feature.XBMJ";
        } else {
            if (table.getTableName().contains("_JGYD")) {
                expression = "'点号:'+$feature.YDH";
            } else {
                if(aliea.equals("")) aliea = "样地号";
                expression = "'" + aliea + ":'+$feature." + fieldName;
            }
        }

        JsonPrimitive jsonPrimitive = new JsonPrimitive(expression);
        expressionInfo.add("expression", jsonPrimitive);
        json.add("labelExpressionInfo", expressionInfo);
        // position the label above and along the direction of the road
        LabelingPlacement labelingPlacement = LabelingPlacement.POINT_ABOVE_CENTER;
        //json.add("labelPlacement", new JsonPrimitive("esriServerPolygonPlacementAlwaysHorizontal"));
        json.add("labelPlacement", new JsonPrimitive(labelingPlacement.toString()));
        // only show labels on the interstate highways (others have an empty rte_num1 attribute)
        //json.add("where", new JsonPrimitive(selFieldList.get(0).getName() + " <> ' '"));
        json.add("where", new JsonPrimitive(" 1=1"));
        //最大比例尺
        //json.add("maxScale", new JsonParser().parse("3000"));
        //最小比例尺
        json.add("minScale", new JsonParser().parse("100000"));
        // set the text symbol as the label symbol
        json.add("symbol", new JsonParser().parse(textSymbol.toJson()));
        String labelStr = json.toString();

        // 构建LabelDefinition
        LabelDefinition labelDefinition = LabelDefinition.fromJson(labelStr);
        featureLayer.getLabelDefinitions().clear();
        featureLayer.getLabelDefinitions().add(labelDefinition);
        boolean flag = featureLayer.isLabelsEnabled();
        // 启用Label标注
        featureLayer.setLabelsEnabled(true);

    }


}
