package com.titan.mobile.arcruntime.core;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.geometry.Geometry;
import com.titan.mobile.arcruntime.fea.SpaFeature;
import com.titan.mobile.arcruntime.fea.SpaFeatureCollection;
import com.titan.mobile.arcruntime.layer.core.LayerNode;
import com.titan.mobile.arcruntime.util.ObjectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择集
 */
public class SelectionSet extends BaseContainer {

    public final static int SEL_MODE_TOP = 0;
    public final static int SEL_MODE_ALL = 1;

    private List<SpaFeatureCollection> original;

    private RenderContainer renderContainer;

    public SelectionSet() {
        this.original = new ArrayList<>();
    }


    @Override
    public void create(ArcMap arcMap) {
        super.create(arcMap);
        renderContainer = arcMap.getRenderContainer();
    }

    public void renderSet() {
        renderContainer.clear();
        List<Geometry> geometries = SpaFeatureCollection.toGeometries(original);
        for (Geometry geometry : geometries) {
            renderContainer.add(geometry);
        }
    }

    public List<SpaFeatureCollection> getSet() {
        return original;
    }

    /**
     * @param layerIndex
     * @return
     */
    public List<SpaFeatureCollection> getSetByLayerIndex(String layerIndex) {
        List<SpaFeatureCollection> list = new ArrayList<>();
        if (layerIndex == null) return list;
        for (SpaFeatureCollection collection : original) {
            if (layerIndex.equals(collection.getLayerIndex())) {
                list.add(collection);
                return list;
            }
        }
        return list;
    }

    public List<SpaFeatureCollection> getSetExpectLayerIndex(String layerIndex) {
        if (layerIndex == null) return original;
        List<SpaFeatureCollection> collections = new ArrayList<>();
        for (SpaFeatureCollection collection : original) {
            if (layerIndex.equals(collection.getLayerIndex())) continue;
            collections.add(collection);
        }
        return collections;
    }

    public List<String> selLayerIndex() {
        List<String> list = new ArrayList<>();
        if (isEmpty()) return list;
        for (SpaFeatureCollection collection : original) {
            if (collection.isEmpty()) continue;
            list.add(collection.getLayerIndex());
        }
        return list;
    }

    public List<LayerNode> selLayerNode() {
        List<LayerNode> list = new ArrayList<>();
        if (isEmpty()) return list;
        for (SpaFeatureCollection collection : original) {
            if (collection.isEmpty()) continue;
            LayerNode layerNode = arcMap.getLayerContainer().getLayerNodeByIndex(collection.getLayerIndex());
            list.add(layerNode);
        }
        return list;
    }

    public SelectionSet removeSetByIndex(String layerIndex) {
        if (isEmpty()) return this;
        int index = -1;
        SpaFeatureCollection collection;
        for (int i = 0; i < original.size(); i++) {
            collection = original.get(i);
            if (layerIndex != null && layerIndex.equals(collection.getLayerIndex())) {
                index = i;
            }
        }
        if (index != -1) original.remove(index);
        return this;
    }

    public SelectionSet clearSet() {
        this.original.clear();
        return this;
    }

    public boolean isEmpty() {
        if (original == null || original.size() == 0) return true;
        boolean flag = true;
        for (SpaFeatureCollection collection : original) {
            flag = collection.isEmpty();
            if (!flag) return false;
        }
        return flag;
    }

    public int count() {
        if (original == null || original.size() == 0) return 0;
        int count = 0;
        for (SpaFeatureCollection collection : original) {
            count += collection.count();
        }
        return count;
    }

    public SpaFeature getDefaultOne() {
        if (isEmpty()) return null;
        SpaFeatureCollection collection = original.get(0);
        SpaFeature spaFeature = collection.getCollection().get(0);
        spaFeature.setLayerIndex(collection.getLayerIndex());
        return spaFeature;
    }

    /**
     * 结果集与原始求异
     *
     * @param set
     * @return
     */
    public SelectionSet diff(List<SpaFeatureCollection> set) {
        if (set == null || set.size() == 0) return this;
        if (original == null || original.size() == 0) {
            original = set;
            return this;
        }
        String layerIndex1, layerIndex2;
        SpaFeatureCollection c1, c2 = null;
        List<SpaFeatureCollection> remove = new ArrayList<>();
        List<SpaFeatureCollection> collections = new ArrayList<>();
        for (int i = 0, lenI = original.size(); i < lenI; i++) {
            c1 = original.get(i);
            layerIndex1 = c1.getLayerIndex();
            boolean hasLayer = false;
            for (int j = 0, lenJ = set.size(); j < lenJ; j++) {
                c2 = set.get(j);
                layerIndex2 = c2.getLayerIndex();
                hasLayer = ObjectUtil.Common.faceEqual(layerIndex1, layerIndex2);
                if (hasLayer) {
                    remove.add(c2);
                    break;
                }
            }
            if (hasLayer) {
                List<SpaFeature> temp = setDiff(c1.getCollection(), c2.getCollection());
                SpaFeatureCollection collection = SpaFeatureCollection.fromFeatures(layerIndex1, temp);
                collections.add(collection);
            } else {
                collections.add(c1);
            }
        }
        for (SpaFeatureCollection c : remove) {
            set.remove(c);
        }
        collections.addAll(set);
        original = collections;
        return this;
    }

    public SelectionSet union(SpaFeatureCollection set) {
        if (set == null || set.isEmpty()) return this;
        List<SpaFeatureCollection> collections = new ArrayList<>();
        collections.add(set);
        return union(collections);
    }


    public SelectionSet union(SpaFeature spaFeature) {
        if (spaFeature.isEmpty() || spaFeature.getLayerIndex() == null) return this;
        SpaFeatureCollection collection = SpaFeatureCollection.fromFeatures(new SpaFeature[]{spaFeature});
        collection.setLayerIndex(spaFeature.getLayerIndex());
        return union(collection);
    }

    /**
     * 选择集求并集
     *
     * @param set
     * @return
     */
    public SelectionSet union(List<SpaFeatureCollection> set) {
        if (set == null || set.size() == 0) return this;
        if (original == null || original.size() == 0) {
            original = set;
            return this;
        }
        String layerIndex1, layerIndex2;
        SpaFeatureCollection c1, c2 = null;
        List<SpaFeatureCollection> remove = new ArrayList<>();
        List<SpaFeatureCollection> collections = new ArrayList<>();
        for (int i = 0, lenI = original.size(); i < lenI; i++) {
            c1 = original.get(i);
            layerIndex1 = c1.getLayerIndex();
            boolean hasLayer = false;
            for (int j = 0, lenJ = set.size(); j < lenJ; j++) {
                c2 = set.get(j);
                layerIndex2 = c2.getLayerIndex();
                hasLayer = ObjectUtil.Common.faceEqual(layerIndex1, layerIndex2);
                if (hasLayer) {
                    remove.add(c2);
                    break;
                }
            }
            if (hasLayer) {
                List<SpaFeature> temp = setUnion(c1.getCollection(), c2.getCollection());
                SpaFeatureCollection collection = SpaFeatureCollection.fromFeatures(layerIndex1, temp);
                collections.add(collection);
            } else {
                collections.add(c1);
            }
        }
        for (SpaFeatureCollection c : remove) {
            set.remove(c);
        }
        collections.addAll(set);
        original = collections;
        return this;
    }

    public SelectionSet inset(SpaFeature spaFeature) {
        if (spaFeature.isEmpty() || spaFeature.getLayerIndex() == null) return this;
        SpaFeatureCollection collection = SpaFeatureCollection.fromFeatures(new SpaFeature[]{spaFeature});
        collection.setLayerIndex(spaFeature.getLayerIndex());
        List<SpaFeatureCollection> collections = new ArrayList<>();
        collections.add(collection);
        return inset(collections);
    }

    public SelectionSet inset(SpaFeatureCollection collection) {
        if (collection.isEmpty() || collection.getLayerIndex() == null) return this;
        List<SpaFeatureCollection> collections = new ArrayList<>();
        collections.add(collection);
        return inset(collections);
    }

    /**
     * 选择集求交
     *
     * @param set
     * @return
     */
    public SelectionSet inset(List<SpaFeatureCollection> set) {
        if (set == null || set.size() == 0) return this;
        if (original == null || original.size() == 0) {
            original = set;
            return this;
        }
        String layerIndex1, layerIndex2;
        SpaFeatureCollection c1, c2 = null;
        List<SpaFeatureCollection> remove = new ArrayList<>();
        List<SpaFeatureCollection> collections = new ArrayList<>();
        for (int i = 0, lenI = original.size(); i < lenI; i++) {
            c1 = original.get(i);
            layerIndex1 = c1.getLayerIndex();
            boolean hasLayer = false;
            for (int j = 0, lenJ = set.size(); j < lenJ; j++) {
                c2 = set.get(j);
                layerIndex2 = c2.getLayerIndex();
                hasLayer = ObjectUtil.Common.faceEqual(layerIndex1, layerIndex2);
                if (hasLayer) {
                    remove.add(c2);
                    break;
                }
            }
            if (hasLayer) {
                List<SpaFeature> temp = setInst(c1.getCollection(), c2.getCollection());
                SpaFeatureCollection collection = SpaFeatureCollection.fromFeatures(layerIndex1, temp);
                collections.add(collection);
            } else {
                collections.add(c1);
            }
        }
        for (SpaFeatureCollection c : remove) {
            set.remove(c);
        }
        collections.addAll(set);
        original = collections;
        return this;
    }

    /**
     * 求异    A u B - A n B
     *
     * @param set1
     * @param set2
     * @return
     */
    private List<SpaFeature> setDiff(List<SpaFeature> set1, List<SpaFeature> set2) {
        List<SpaFeature> diff1 = new ArrayList<>();
        List<SpaFeature> diff2 = new ArrayList<>();
        diff1.addAll(set1);
        diff2.addAll(set2);
        SpaFeature f1, f2;
        for (int i = 0, lenI = set1.size(); i < lenI; i++) {
            f1 = set1.get(i);
            for (int j = 0, lenJ = set2.size(); j < lenJ; j++) {
                f2 = set2.get(j);
                boolean equal = ObjectUtil.Common.faceEqual(f1.getId(), f2.getId());
                if (equal) {
                    diff1.remove(f1);
                    diff2.remove(f2);
                }
            }
        }
        diff1.addAll(diff2);
        return diff1;
    }

    /**
     * 求交  A n B
     *
     * @param set1
     * @param set2
     * @return
     */
    private List<SpaFeature> setInst(List<SpaFeature> set1, List<SpaFeature> set2) {
        List<SpaFeature> intersection = new ArrayList<>();
        SpaFeature f1, f2;
        for (int i = 0, lenI = set1.size(); i < lenI; i++) {
            f1 = set1.get(i);
            for (int j = 0, lenJ = set2.size(); j < lenJ; j++) {
                f2 = set2.get(j);
                boolean equal = ObjectUtil.Common.faceEqual(f1.getId(), f2.getId());
                if (equal) {
                    intersection.add(f2); //以后面集合为基准
                    break;
                }
            }
        }
        return intersection;
    }

    /**
     * 求交  A u B
     *
     * @param set1
     * @param set2
     * @return
     */
    private List<SpaFeature> setUnion(List<SpaFeature> set1, List<SpaFeature> set2) {
        List<SpaFeature> union = new ArrayList<>();
        union.addAll(set1);
        SpaFeature f1, f2;
        for (int i = 0, lenI = set1.size(); i < lenI; i++) {
            f1 = set1.get(i);
            for (int j = 0, lenJ = set2.size(); j < lenJ; j++) {
                f2 = set2.get(j);
                boolean equal = ObjectUtil.Common.faceEqual(f1.getId(), f2.getId());
                if (equal) {
                    union.remove(f1);
                    break;
                }
            }
        }
        union.addAll(set2);
        return union;
    }

}
