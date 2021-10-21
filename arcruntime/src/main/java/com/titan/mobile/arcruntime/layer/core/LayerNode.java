package com.titan.mobile.arcruntime.layer.core;

import androidx.annotation.NonNull;

import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.ArcGISMapImageSublayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.layers.LayerContent;
import com.esri.arcgisruntime.layers.RasterLayer;
import com.esri.arcgisruntime.util.ListChangedEvent;
import com.titan.mobile.arcruntime.core.ArcMap;
import com.titan.mobile.arcruntime.layer.info.Extent;
import com.titan.mobile.arcruntime.layer.info.LayerInfo;
import com.titan.mobile.arcruntime.layer.util.DataSourceRead;
import com.titan.mobile.arcruntime.util.HttpSimpleUtil;
import com.titan.mobile.arcruntime.util.ObjectUtil;
import com.titan.mobile.arcruntime.util.RxSimpleUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2019/5/24.
 */

public class LayerNode implements Serializable {

    private String index;

    private Object id;

    private String name;

    private String alias;

    private String uri;

    private boolean isQuery = true;

    private LayerSetting setting;

    private transient LayerContent layerContent;

    private transient List<LayerNode> nodes;

    private transient boolean isValid = false;

    private transient LayerInfo info;

    private transient ListChangedEvent.Action action;

    private transient Object tag;

    @NonNull
    @Override
    public String toString() {
        String json = ObjectUtil.Common.convert(this);
        return json;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public boolean isQuery() {
        return isQuery;
    }

    public void setQuery(boolean query) {
        isQuery = query;
    }

    public LayerSetting getSetting() {
        if (setting == null) setting = LayerSetting.create();
        return setting;
    }

    public void setSetting(LayerSetting setting) {
        this.setting = setting;
    }

    public LayerContent getLayerContent() {
        return layerContent;
    }

    public void setLayerContent(LayerContent layerContent) {
        this.layerContent = layerContent;
    }

    public FeatureLayer tryGetFeaLayer() {
        if (layerContent == null) return null;
        if (layerContent instanceof FeatureLayer) return (FeatureLayer) layerContent;
        return null;
    }

    public FeatureTable tryGetFeaTable() {
        if (layerContent == null) return null;
        if (layerContent instanceof FeatureLayer)
            return ((FeatureLayer) layerContent).getFeatureTable();
        if (layerContent instanceof ArcGISMapImageSublayer) {
            ServiceFeatureTable table = ((ArcGISMapImageSublayer) layerContent).getTable();
            if (table == null) table = new ServiceFeatureTable(getUri());
            return table;
        }
        return null;
    }

    public GeometryType tryGetGeometryType() {
        FeatureTable table = tryGetFeaTable();
        if (table != null) return table.getGeometryType();
        return null;
    }

    /**
     * 图层范围
     *
     * @return
     */
    public Envelope getExtent() {
        Envelope extent = null;
        if (layerContent instanceof FeatureLayer) {
            FeatureLayer featureLayer = (FeatureLayer) layerContent;
            featureLayer = DataSourceRead.loadFeaLayerSync(featureLayer);
            extent = featureLayer.getFeatureTable().getExtent();
        }
        if (layerContent instanceof RasterLayer) {
            RasterLayer rasterLayer = (RasterLayer) layerContent;
            rasterLayer = DataSourceRead.loadRasterLayerSync(rasterLayer);
            extent = rasterLayer.getFullExtent();
        }
        if (info != null) {
            Extent _extent = info.getExtent();
            if (_extent != null) {
                com.titan.mobile.arcruntime.layer.info.SpatialReference _ref = _extent.getSpatialReference();
                if (_ref != null)
                    extent = new Envelope(_extent.getXmin(), _extent.getYmin(), _extent.getXmax(), _extent.getYmax(), SpatialReference.create(_ref.getWkid()));
            }
        }
        return extent;
    }

    public void zoom2Extend() {
        if(getExtent() != null){
            ArcMap.arcMap.getMapControl().zoomG(getExtent());
        }else{
            return;
        }

    }

    public List<Field> tryGetFields() {
        List<Field> fields = new ArrayList<>();
        if (isLocal()) {
            FeatureTable featureTable = tryGetFeaTable();
            if (featureTable != null) fields = featureTable.getFields();
            return fields;
        } else {
            if (info == null) return null;
            List<com.titan.mobile.arcruntime.layer.info.Field> _fields = info.getFields();
            Field field;
            String json;
            com.titan.mobile.arcruntime.layer.info.Field _field;
            for (int i = 0, len = _fields.size(); i < len; i++) {
                _field = _fields.get(i);
                json = ObjectUtil.Common.convert(_field);
                field = Field.fromJson(json);
                fields.add(field);
            }
            return fields;
        }
    }

    public String getFileType() {
        String uri = getUri();
        if (uri.toLowerCase().endsWith(".geodatabase")) {
            return "geodatabase";
        }
        if (uri.toLowerCase().endsWith("shp")) {
            //shape
            return "shp";
        }
        if (!isLocal()) {
            return "service";
        }
        return "unknow";
    }


    public List<LayerNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<LayerNode> nodes) {
        this.nodes = nodes;
    }

    public void addNode(LayerNode node) {
        if (this.nodes == null) this.nodes = new ArrayList<>();
        this.nodes.add(node);
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
        if (valid) {
            RxSimpleUtil.simple(new RxSimpleUtil.ISimpleBack<LayerInfo>() {
                @Override
                public LayerInfo run() {
                    String _url = uri + "?f=pjson";
                    String json = HttpSimpleUtil._get(_url);
                    info = ObjectUtil.Common.convert(json, LayerInfo.class);
                    return info;
                }

                @Override
                public void success(LayerInfo _info) {
                    info = _info;
                }
            });
        }
    }

    public boolean getVisible() {
        //return layerContent == null ? false : layerContent.isVisible();
        return layerContent == null ? true : layerContent.isVisible();
    }

    public LayerInfo getInfo() {
        return info;
    }

    public boolean hasChildNode() {
        return nodes != null && nodes.size() > 0;
    }

    /**
     * 判断子图层是否有不可见的
     *
     * @return
     */
    public boolean hasInVisible() {
        List<LayerNode> temp = getLeafNode();
        if (temp == null) return false;
        for (LayerNode node : temp) {
            if (!node.getVisible()) return true;
        }
        return !getVisible();
    }

    public void setVisible(boolean visible) {
        iteration(this, visible);
    }

    /**
     * 判断是否为本地图层
     *
     * @return
     */
    public boolean isLocal() {
        return !uri.toLowerCase().startsWith("http");
    }

    public List<LayerNode> getLeafNode() {
        List<LayerNode> res = new ArrayList<>();
        if (getLayerContent() != null && !hasChildNode()) {
            res.add(this);
        }
        List<LayerNode> nodes = this.getNodes();
        if (nodes == null) return res;
        LayerNode _layerNode;
        for (int i = 0; i < nodes.size(); i++) {
            _layerNode = nodes.get(i);
            getLeafNode(res, _layerNode);
        }
        return res;
    }

    private void getLeafNode(List<LayerNode> in, LayerNode layerNode) {
        if (layerNode.getLayerContent() != null) {
            in.add(layerNode);
        }
        List<LayerNode> nodes = layerNode.getNodes();
        if (nodes == null) return;
        LayerNode _layerNode;
        for (int i = 0; i < nodes.size(); i++) {
            _layerNode = nodes.get(i);
            getLeafNode(in, _layerNode);
        }
    }

    public Layer filterLayer(String uri) {
        List<LayerNode> nodes = getLeafNode();
        LayerNode node;
        for (int i = 0, len = nodes.size(); i < len; i++) {
            node = nodes.get(i);
            if (node.getUri() != null && node.getUri().equals(uri))
                return (Layer) node.getLayerContent();
        }
        return null;
    }

    public ListChangedEvent.Action getAction() {
        return action;
    }

    public void setAction(ListChangedEvent.Action action) {
        this.action = action;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    //----------------------------------------------------------------------------------------------

    public static void iteration(LayerNode layerNode, boolean visible) {
        if (layerNode == null) return;
        if (layerNode.getLayerContent() != null) {
            try {
                layerNode.getLayerContent().setVisible(visible);
                if (!visible)
                    ArcMap.arcMap.getSelectionSet().removeSetByIndex(layerNode.getIndex()).renderSet();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        List<LayerNode> nodes = layerNode.getNodes();
        if (nodes == null) return;
        LayerNode _layerNode;
        for (int i = 0; i < nodes.size(); i++) {
            _layerNode = nodes.get(i);
            iteration(_layerNode, visible);
        }
    }

    public static List<LayerNode> getLayerNodeByGeoType(List<LayerNode> layerNodes, @NonNull GeometryType type) {
        if (layerNodes == null) return null;
        List<LayerNode> res = new ArrayList<>();
        LayerNode layerNode;
        for (int i = 0; i < layerNodes.size(); i++) {
            layerNode = layerNodes.get(i);
            if (layerNode == null) continue;
            if (layerNode.tryGetGeometryType() != null && layerNode.tryGetGeometryType() == type) {
                res.add(layerNode);
            }
        }
        return res;
    }
}
