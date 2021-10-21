package com.titan.mobile.arcruntime.core;

import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.ArcGISMapImageSublayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.layers.LayerContent;
import com.esri.arcgisruntime.mapping.LayerList;
import com.esri.arcgisruntime.util.ListChangedEvent;
import com.esri.arcgisruntime.util.ListChangedListener;
import com.titan.mobile.arcruntime.layer.core.LayerNode;
import com.titan.mobile.arcruntime.layer.core.Parser;
import com.titan.mobile.arcruntime.layer.util.DataSourceRead;
import com.titan.mobile.arcruntime.util.FieldUtil;
import com.titan.mobile.arcruntime.util.ObjectUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by zy on 2019/5/9.
 */

public class LayerContainer extends BaseContainer implements ListChangedListener<Layer> {
    /**
     * 自然图层结构
     */
    private List<LayerNode> layerNodes = new ArrayList<>();
    private List<Layer> layerList = new ArrayList<>();

    public interface LayerDoneLoadingListener{
        void loaded(Layer layer);
    }

    private LayerDoneLoadingListener layerDoneLoadingListener;

    public LayerDoneLoadingListener getLayerDoneLoadingListener() {
        return layerDoneLoadingListener;
    }

    public void setLayerDoneLoadingListener(LayerDoneLoadingListener layerDoneLoadingListener) {
        this.layerDoneLoadingListener = layerDoneLoadingListener;
    }

    public List<LayerNode> getLayerNodes() {
        return layerNodes;
    }

    public FeatureTable getLayerByName(String name){
        if(layerNodes.size() == 0) {
            return null;
        }
        for(LayerNode node : layerNodes){
            if(node.getName().contains(name)){
                return node.tryGetFeaTable();
            }
        }
        return layerNodes.get(0).tryGetFeaTable();
    }


    //临时存储LayerNode
    private HashMap<String,LayerNode> tempNodes = new HashMap<>();

    private List<ILayerLoaded> layersLoaded = new ArrayList<>();

    @Override
    public void ready() {
        LayerList layerList = mapView.getMap().getOperationalLayers();
        layerList.addListChangedListener(this);
        for (Layer layer : layerList) {
            convert(layer, ListChangedEvent.Action.ADDED);
        }
    }

    public void loadLocSpatialData(List<String> paths) {
        LayerList list = mapView.getMap().getOperationalLayers();
        DataSourceRead.readLocalFile(null, list, paths);
    }

    public void addLayer(List<Layer> layers) {
        LayerList list = mapView.getMap().getOperationalLayers();
        list.addAll(layers);
    }

    public void addLayer(Layer layer) {
        LayerList list = mapView.getMap().getOperationalLayers();
        List<Layer> layers = new ArrayList<>();
        layers.add(layer);
        list.addAll(layers);
    }

    public void addLayerContent(LayerContent content) {
        if (content instanceof Layer) {
            Layer layer = (Layer) content;
            mapView.getMap().getOperationalLayers().add(layer);
        } else if (content instanceof ArcGISMapImageLayer) {
            Layer layer = (ArcGISMapImageLayer) content;
            mapView.getMap().getOperationalLayers().add(layer);
        } else if (content instanceof ArcGISMapImageSublayer) {
            ArcGISMapImageSublayer sublayer = (ArcGISMapImageSublayer) content;
            sublayer = DataSourceRead.loadImgSubLayerSync(sublayer);
            String url = sublayer.getUri();
            FeatureLayer featureLayer = new FeatureLayer(new ServiceFeatureTable(url));
            featureLayer.getLabelDefinitions().addAll(sublayer.getLabelDefinitions());
            featureLayer.setName(content.getName());
            featureLayer.setDescription(url);
            mapView.getMap().getOperationalLayers().add(featureLayer);
        }
    }

    public LayerContainer addLayers(List<LayerNode> layerNodes,String expression) {
        this.tempNodes.clear();
        if (layerNodes == null) return this;
        for (LayerNode node : layerNodes) {
            this.tempNodes.put(node.getName(),node);
            addLayer(node,expression);
        }
        return this;
    }

    public LayerContainer addLayers(List<LayerNode> layerNodes) {
        this.tempNodes.clear();
        if (layerNodes == null) return this;
        for (LayerNode node : layerNodes) {
            this.tempNodes.put(node.getName(),node);
            addLayer(node);
        }
        return this;
    }

    public void addLayer(LayerNode layerNode) {
        String index = layerNode.getIndex();
        LayerNode node = getLayerNodeByIndex(index);
        if (node != null) return;
        LayerList list = mapView.getMap().getOperationalLayers();
        LayerContent content = layerNode.getLayerContent();
        if(content == null) return;
        if (content instanceof FeatureLayer) {
            FeatureLayer layer = (FeatureLayer) content;
            list.add(layer);
            layer.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    if(layerDoneLoadingListener != null){
                        layerDoneLoadingListener.loaded(layer);
                    }
                }
            });
        } else if (content instanceof ArcGISMapImageLayer) {
            Layer layer = (ArcGISMapImageLayer) content;
            list.add(layer);
        } else if (content instanceof ArcGISMapImageSublayer) {
            ArcGISMapImageSublayer sublayer = (ArcGISMapImageSublayer) content;
            sublayer = DataSourceRead.loadImgSubLayerSync(sublayer);
            String url = sublayer.getUri();
            FeatureLayer featureLayer = new FeatureLayer(new ServiceFeatureTable(url));
            featureLayer.getLabelDefinitions().addAll(sublayer.getLabelDefinitions());
            featureLayer.setName(layerNode.getName());
            featureLayer.setDescription(layerNode.getUri());
            list.add(featureLayer);
        }
    }

    public void addLayer(LayerNode layerNode,String expression) {
        String index = layerNode.getIndex();
        LayerNode node = getLayerNodeByIndex(index);
        if (node != null) return;
        LayerList list = mapView.getMap().getOperationalLayers();
        LayerContent content = layerNode.getLayerContent();
        if(content == null) return;
        if (content instanceof FeatureLayer) {
            FeatureLayer layer = (FeatureLayer) content;
            list.add(layer);
            layer.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    if(layerDoneLoadingListener != null){
                        layerDoneLoadingListener.loaded(layer);
                    }
                }
            });
        } else if (content instanceof ArcGISMapImageLayer) {
            Layer layer = (ArcGISMapImageLayer) content;
            list.add(layer);
        } else if (content instanceof ArcGISMapImageSublayer) {
            ArcGISMapImageSublayer sublayer = (ArcGISMapImageSublayer) content;
            sublayer = DataSourceRead.loadImgSubLayerSync(sublayer);
            String url = sublayer.getUri();
            FeatureLayer featureLayer = new FeatureLayer(new ServiceFeatureTable(url));
            featureLayer.getLabelDefinitions().addAll(sublayer.getLabelDefinitions());
            featureLayer.setName(layerNode.getName());
            featureLayer.setDescription(layerNode.getUri());
            list.add(featureLayer);
        }

    }

    public void removeLayerNode(LayerNode layerNode) {
        String index = layerNode.getIndex();
        LayerNode node = getLayerNodeByIndex(index);
        if (node == null) return;
        LayerList list = mapView.getMap().getOperationalLayers();
        LayerContent content = node.getLayerContent();
        if (content == null) return;
        list.remove(content);
    }

    public void removeLayer(LayerNode layerNode) {
        String index = layerNode.getIndex();
        LayerNode node = getLayerNodeByIndex(index);
        if (node == null) return;
        LayerList list = mapView.getMap().getOperationalLayers();
        LayerContent content = node.getLayerContent();
        if (content == null) return;
        list.remove(content);
        removeNode(layerNode);
    }

    private void removeNode(LayerNode node){
        Iterator<LayerNode> iterator = layerNodes.iterator();
        while (iterator.hasNext()){
            LayerNode layerNode = iterator.next();
            if(node.hashCode() == layerNode.hashCode()){
                iterator.remove();
                break;
            }
        }
    }

    public LayerContainer removeAll() {
        List<LayerNode> nodes = getLeafLayerNodes();
        if (nodes == null) return this;
        for (LayerNode node : nodes) removeLayer(node);
        return this;
    }

    /**
     * 动态添加
     *
     * @param listChangedEvent
     */
    @Override
    public void listChanged(ListChangedEvent<Layer> listChangedEvent) {
        List<Layer> layers = listChangedEvent.getItems();
        ListChangedEvent.Action action = listChangedEvent.getAction();
        for (Layer layer : layers) {
            convert(layer, action);
        }
    }

    /**
     * 图层状态监听
     *
     * @param layer
     * @param action
     */
    private void convert(Layer layer, ListChangedEvent.Action action) {
        if (action == ListChangedEvent.Action.ADDED) {
            LayerNode node = new Parser(layer).getLayerNode();
            if (node == null) return;
            node.setAction(ListChangedEvent.Action.ADDED);
            node.setAlias(tempNodes.get(node.getName()).getAlias());
            this.layerNodes.add(node);
            notifyLayerLoad(node);
            this.layerList.add(layer);
        }
        if (action == ListChangedEvent.Action.REMOVED) {
            String id = layer.getId();
            LayerNode node = getLayerNodeByIndex(id);
            if (node == null) return;
            node.setAction(ListChangedEvent.Action.REMOVED);
            this.layerNodes.remove(node);
            notifyLayerLoad(node);
            this.layerList.remove(layer);
        }
    }

    public void swapLayer(String layerIndex1, String layerIndex2) {
        LayerList list = mapView.getMap().getOperationalLayers();
        //图层排序时不不触发监听事件
        list.removeListChangedListener(this);

        int idx1 = -1, idx2 = -1;
        for (int i = 0; i < layerNodes.size(); i++) {
            LayerNode node = layerNodes.get(i);
            String index = node.getIndex();
            if (index.equals(layerIndex1)) {
                idx1 = i;
                continue;
            }
            if (index.equals(layerIndex2)) {
                idx2 = i;
                continue;
            }
        }
        ObjectUtil.Collect.removeDragSort(layerNodes, idx1, idx2);
        ObjectUtil.Collect.removeDragSort(list, idx1, idx2);

        //图层排序时不不触发监听事件
        list.addListChangedListener(this);
    }


    /**
     * 交换图层位置
     */
    public void swapLayer(int idx1, int idx2) {
        LayerList list = mapView.getMap().getOperationalLayers();
        //图层排序时不不触发监听事件
        list.removeListChangedListener(this);

        ObjectUtil.Collect.removeDragSort(layerNodes, idx1, idx2);
        ObjectUtil.Collect.removeDragSort(list, idx1, idx2);

        //图层排序时不不触发监听事件
        list.addListChangedListener(this);
    }

    public interface ILayerLoaded {
        public void iLayerLoaded(LayerNode node);
    }

    public void addILayerLoaded(ILayerLoaded layerLoad) {
        layersLoaded.add(layerLoad);
    }

    public void removeILayerLoaded(ILayerLoaded layerLoad) {
        layersLoaded.remove(layerLoad);
    }

    private void notifyLayerLoad(LayerNode node) {
        for (ILayerLoaded layerLoad : layersLoaded) {
            layerLoad.iLayerLoaded(node);
        }
    }


    public LayerNode getLayerNodeByIndex(String index) {
        Stack<LayerNode> stack = new Stack<>();
        stack.addAll(layerNodes);
        while (!stack.empty()) {
            LayerNode node = stack.pop();
            if (node.getIndex().equals(index)) return node;
            List<LayerNode> nodes = node.getNodes();
            if (nodes != null) stack.addAll(nodes);
        }
        return null;
    }

    public LayerNode getLayerNodeByUrl(String url) {
        if (layerNodes == null) return null;
        for (LayerNode node : layerNodes) {
            if (node.getUri().equals(url)) return node;
        }
        return null;
    }

    public LayerNode getLayerNodeByName(String name) {
        if (layerNodes == null) return null;
        for (LayerNode node : layerNodes) {
            if (node.getName().equals(name)) return node;
        }
        return null;
    }

    public LayerNode getLayerNodeByAlias(String alias) {
        if (layerNodes == null) return null;
        for (LayerNode node : layerNodes) {
            if (node.getAlias().equals(alias)) return node;
        }
        return null;
    }

    /**
     * 图层集合顺序 和 图层显示顺序  相反
     *
     * @return
     */
    public List<LayerNode> getLeafLayerNodes() {
        if (layerNodes == null) return null;
        List<LayerNode> list = new ArrayList<>();
        List<LayerNode> temp;
        for (LayerNode node : layerNodes) {
            temp = node.getLeafNode();
            if (temp == null) continue;
            list.addAll(temp);
        }
        return list;
    }

    public List<LayerNode> getLayerCanGeoQuery() {
        List<LayerNode> temp = getLeafLayerNodes();
        //Collections.reverse(temp);
        List<LayerNode> _temp = new ArrayList<>();
        for (LayerNode node : temp) {
            if (node.getVisible() &&
                    node.getSetting().isCanQuery() &&
                    node.tryGetFeaLayer() != null) {
                _temp.add(node);
            }
        }

        return _temp;
    }

    public List<LayerNode> getLayerCanGeoEdit() {
        List<LayerNode> temp = getLeafLayerNodes();
        Collections.reverse(temp);
        List<LayerNode> _temp = new ArrayList<>();
        for (LayerNode node : temp) {
            if (node.getVisible() &&
                    node.isLocal() &&
                    node.tryGetFeaLayer() != null) {
                _temp.add(node);
            }
        }
        return _temp;
    }

    /**
     * 图层集合顺序 和 图层显示顺序一致
     *
     * @return
     */
    public List<LayerNode> getLayerNodeReverse() {
        List<LayerNode> temp = getLeafLayerNodes();
        Collections.reverse(temp);
        return temp;
    }

    /**
     * 获取可见与可编辑的图层
     *
     * @return
     */
    public List<LayerNode> getLeafLayerNodesVisibleAndEditAble() {
        List<LayerNode> list = getLeafLayerNodes();
        if (list == null) return null;
        List<LayerNode> res = new ArrayList<>();
        for (LayerNode node : list) {
            if (node.getVisible() && node.isLocal()) res.add(node);
        }
        return res;
    }

    public List<LayerNode> getLeafLayerNodesVisible() {
        List<LayerNode> list = getLeafLayerNodes();
        if (list == null) return null;
        List<LayerNode> res = new ArrayList<>();
        for (LayerNode node : list) {
            if (node.getVisible()) res.add(node);
        }
        return res;
    }

    /*public List<LayerNode> getLeafLayerNodesInVisible() {
        List<LayerNode> list = getLeafLayerNodes();
        if (list == null) return null;
        List<LayerNode> res = new ArrayList<>();
        for (LayerNode node : list) {
            if (!node.getVisible()) res.add(node);
        }
        return res;
    }*/

    private LayerNode editNode;

    public LayerNode getEditNode() {
        if (editNode == null || !editNode.getVisible()) editNode = null;
        return editNode;
    }

    public void setEditNode(LayerNode editNode) {
        this.editNode = editNode;
    }

    public SketchStream.Type getEditType() {
        if (editNode == null) return null;
        GeometryType geometryType = editNode.tryGetGeometryType();
        if (geometryType == GeometryType.POINT || geometryType == GeometryType.MULTIPOINT) {
            return SketchStream.Type.pSel;
        }
        if (geometryType == GeometryType.POLYLINE) {
            return SketchStream.Type.freeLine;
        }
        if (geometryType == GeometryType.POLYGON) {
            return SketchStream.Type.freeFace;
        }
        return null;
    }

    /**
     * 当底图变化，重新set ArcMap 时保存已经加载的图层状态
     * */
    public void saveLayerState(){
        tempNodes.clear();
        for(LayerNode layerNode : layerNodes){
            tempNodes.put(layerNode.getName(),layerNode);
        }
        Iterator<LayerNode> it = layerNodes.iterator();
        while (it.hasNext()) {
            LayerNode node = it.next();
            removeLayerNode(node);
        }
        layerNodes.clear();
    }

    /**
     * 当底图变化，重新set ArcMap 后重新添加之前保存的图层
     * */
    public void reLoadLayer(){
        LayerList list = mapView.getMap().getOperationalLayers();
        list.addListChangedListener(this);
        for(Map.Entry<String, LayerNode> key : tempNodes.entrySet()){
            addLayer(key.getValue());
        }
    }

}
