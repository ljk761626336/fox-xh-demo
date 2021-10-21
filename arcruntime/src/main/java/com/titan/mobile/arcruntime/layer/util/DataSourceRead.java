package com.titan.mobile.arcruntime.layer.util;

import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.Geodatabase;
import com.esri.arcgisruntime.data.GeodatabaseFeatureTable;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.data.ShapefileFeatureTable;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.ArcGISMapImageSublayer;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.layers.RasterLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.LayerList;
import com.esri.arcgisruntime.raster.Raster;
import com.titan.mobile.arcruntime.util.FutureUtil;
import com.titan.mobile.arcruntime.util.ObjectUtil;
import com.titan.mobile.arcruntime.util.RxSimpleObserver;
import com.titan.mobile.arcruntime.util.RxSimpleSubscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public final class DataSourceRead {

    /**
     * 读取本地文件夹
     *
     * @param path
     * @param iListener
     */
    public static void getLocalRasterAsync(String nodeName, String path, IListener iListener) {
        Observable.create(
                new RxSimpleSubscribe<ObjectUtil.Tree.Node>() {
                    @Override
                    public void subscribe(ObservableEmitter<ObjectUtil.Tree.Node> emitter) throws Exception {
                        ObjectUtil.Tree.Node node = ObjectUtil.Tree.createFromPath(nodeName, path, ".tpk", ".tif", ".img");
                        emitter.onNext(node);
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSimpleObserver<ObjectUtil.Tree.Node>() {
                    @Override
                    public void onNext(ObjectUtil.Tree.Node node) {
                        if (iListener != null) iListener.layersCallback(node);
                    }
                });
    }

    /**
     * 读取本地文件夹
     *
     * @param path
     * @param iListener
     */
    public static void getLocalFolderAsync(String nodeName, String path, IListener iListener) {
        Observable.create(
                new RxSimpleSubscribe<ObjectUtil.Tree.Node>() {
                    @Override
                    public void subscribe(ObservableEmitter<ObjectUtil.Tree.Node> emitter) throws Exception {
                        ObjectUtil.Tree.Node node = ObjectUtil.Tree.createFromPath(nodeName, path, ".geodatabase", ".shp", ".tif", ".img");
                        emitter.onNext(node);
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSimpleObserver<ObjectUtil.Tree.Node>() {
                    @Override
                    public void onNext(ObjectUtil.Tree.Node node) {
                        if (iListener != null) iListener.layersCallback(node);
                    }
                });
    }

    /**
     * 读取远程服务图层数据
     *
     * @param uri
     * @param iListener
     */
    public static void getMapLayerFromSerAsync(String uri, IListener iListener) {
        ArcGISMapImageLayer layer = new ArcGISMapImageLayer(uri);
        layer.setId(uri);
        layer.setDescription(uri);
        layer.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                layer.removeDoneLoadingListener(this);
                Observable.create(
                        new RxSimpleSubscribe<Layer>() {
                            @Override
                            public void subscribe(ObservableEmitter<Layer> emitter) throws Exception {
                                emitter.onNext(layer);
                                emitter.onComplete();
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSimpleObserver<Layer>() {
                            @Override
                            public void onNext(Layer _layer) {
                                if (iListener != null) iListener.layersCallback(_layer);
                            }
                        });
            }
        });
        layer.loadAsync();
    }

    public static void getFeaLayerFromGdbAsync(File file, String[] layerNames, IListener iListener) {
        if (file == null) return;
        getFeaLayerFromGdbAsync(file.getAbsolutePath(), layerNames, iListener);
    }

    /**
     * 读取本地Geodatabase数据
     *
     * @param gdbPath
     * @param iListener
     */
    public static void getFeaLayerFromGdbAsync(String gdbPath, IListener iListener) {
        if (gdbPath == null || !gdbPath.toLowerCase().endsWith(".geodatabase")) return;
        Geodatabase geodatabase = new Geodatabase(gdbPath);
        geodatabase.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                List<GeodatabaseFeatureTable> tables = geodatabase.getGeodatabaseFeatureTables();
                List<Layer> layers = new ArrayList<>(tables.size());
                for (GeodatabaseFeatureTable table : tables) {
                    String tableName = table.getTableName();
                    Layer layer = new FeatureLayer(table);
                    String url = gdbPath + File.separator + tableName;
                    layer.setId(url);
                    layer.setDescription(url);
                    layers.add(layer);
                }
                if (iListener != null) iListener.layersCallback(layers);
                geodatabase.removeDoneLoadingListener(this);
            }
        });
        geodatabase.loadAsync();
    }

    /**
     * 读取本地Geodatabase数据
     *
     * @param gdbPath
     * @param layerNames
     * @param iListener
     */
    public static void getFeaLayerFromGdbAsync(String gdbPath, String[] layerNames, IListener iListener) {
        if (gdbPath == null || !gdbPath.toLowerCase().endsWith(".geodatabase")) return;
        Geodatabase geodatabase = new Geodatabase(gdbPath);
        geodatabase.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                List<GeodatabaseFeatureTable> tables = geodatabase.getGeodatabaseFeatureTables();
                List<Layer> layers = new ArrayList<>(tables.size());
                for (GeodatabaseFeatureTable table : tables) {
                    String tableName = table.getTableName();
                    if (layerNames == null) {
                        Layer layer = new FeatureLayer(table);
                        String url = gdbPath + File.separator + tableName;
                        layer.setId(url);
                        layer.setDescription(url);
                        layers.add(layer);
                    } else {
                        for (String name : layerNames) {
                            if (tableName.equals(name)) {
                                Layer layer = new FeatureLayer(table);
                                String url = gdbPath + File.separator + tableName;
                                layer.setId(url);
                                layer.setDescription(url);
                                layers.add(layer);
                                break;
                            }
                        }
                    }
                }
                if (iListener != null) iListener.layersCallback(layers);
                geodatabase.removeDoneLoadingListener(this);
            }
        });
        geodatabase.loadAsync();
    }


    public static List<Layer> getFeaLayerFromGdbSync(String gdbPath, String[] layerNames) {
        if (gdbPath == null || !gdbPath.toLowerCase().endsWith(".geodatabase")) return null;
        FutureUtil.WaitingForYou<List> future = new FutureUtil.WaitingForYou<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Geodatabase geodatabase = new Geodatabase(gdbPath);
                List<Layer> layers = new ArrayList<>();
                geodatabase.addDoneLoadingListener(new Runnable() {
                    @Override
                    public void run() {
                        List<GeodatabaseFeatureTable> tables = geodatabase.getGeodatabaseFeatureTables();
                        for (GeodatabaseFeatureTable table : tables) {
                            String tableName = table.getTableName();
                            if (layerNames == null) {
                                Layer layer = new FeatureLayer(table);
                                String url = gdbPath + File.separator + tableName;
                                layer.setId(url);
                                layer.setDescription(url);
                                layers.add(layer);
                            } else {
                                for (String name : layerNames) {
                                    if (tableName.contains(name)) {
                                        Layer layer = new FeatureLayer(table);
                                        String url = gdbPath + File.separator + tableName;
                                        layer.setId(url);
                                        layer.setDescription(url);
                                        layers.add(layer);
                                        break;
                                    }
                                }
                            }
                        }
                        geodatabase.removeDoneLoadingListener(this);
                        future.setResult(layers);
                    }
                });
                geodatabase.loadAsync();
            }
        }).start();
        return future.get(5, TimeUnit.SECONDS);
    }


    public static Layer getFeaLayerFromShapeSync(String shapePath) {
        if (shapePath == null || !shapePath.toLowerCase().endsWith(".shp")) return null;
        ShapefileFeatureTable shapefileFeatureTable = new ShapefileFeatureTable(shapePath);
        Layer layer = new FeatureLayer(shapefileFeatureTable);
        String name = ObjectUtil.Files.getFileName(shapePath);
        layer.setId(shapePath);
        layer.setName(name);
        layer.setDescription(shapePath);
        return layer;
    }

    public static List getFeaLayerFromShapeSync(String shapePath, String keyWords) {
        List list = new ArrayList();
        if (shapePath == null || !shapePath.toLowerCase().endsWith(".shp")) return null;
        String layerName = new File(shapePath).getName();
        if (!layerName.contains(keyWords)) {
            return list;
        }
        ShapefileFeatureTable shapefileFeatureTable = new ShapefileFeatureTable(shapePath);
        Layer layer = new FeatureLayer(shapefileFeatureTable);
        String name = ObjectUtil.Files.getFileName(shapePath);
        layer.setId(shapePath);
        layer.setName(name);
        layer.setDescription(shapePath);
        list.add(layer);
        return list;
    }


    public static Layer getRasterLayerFromPath(String imgPath) {
        if (imgPath == null) return null;
        if (imgPath.endsWith(".tif")) return getRasterLayerFromTifSync(imgPath);
        if (imgPath.endsWith(".img")) return getRasterLayerFromImgSync(imgPath);
        if (imgPath.endsWith(".tpk")) return getRasterLayerFromTpkSync(imgPath);
        return null;
    }

    private static Layer getRasterLayerFromTifSync(String tifPath) {
        if (tifPath == null || !tifPath.toLowerCase().endsWith(".tif")) return null;
        Raster raster = new Raster(tifPath);
        RasterLayer rasterLayer = new RasterLayer(raster);
        String name = ObjectUtil.Files.getFileName(tifPath);
        rasterLayer.setId(tifPath);
        rasterLayer.setName(name);
        rasterLayer.setDescription(tifPath);
        return rasterLayer;
    }

    private static Layer getRasterLayerFromTpkSync(String tpkPath) {
        if (tpkPath == null || !tpkPath.toLowerCase().endsWith(".tpk")) return null;
        ArcGISTiledLayer tiledLayer = new ArcGISTiledLayer(tpkPath);
        String name = ObjectUtil.Files.getFileName(tpkPath);
        tiledLayer.setId(tpkPath);
        tiledLayer.setName(name);
        tiledLayer.setDescription(tpkPath);
        return tiledLayer;
    }

    private static Layer getRasterLayerFromImgSync(String imgPath) {
        if (imgPath == null || !imgPath.toLowerCase().endsWith(".img")) return null;
        Raster raster = new Raster(imgPath);
        RasterLayer rasterLayer = new RasterLayer(raster);
        String name = ObjectUtil.Files.getFileName(imgPath);
        rasterLayer.setId(imgPath);
        rasterLayer.setName(name);
        rasterLayer.setDescription(imgPath);
        return rasterLayer;
    }

    /**
     * 通过url获取FeatureLayer
     * 可以是本地 也可以是服务器的
     *
     * @param url
     * @return
     */
    public static Layer getLayerFromUrl(String url, String alias) {
        if (url.toLowerCase().endsWith(".img")) {
            Layer layer = getRasterLayerFromImgSync(url);
            return layer;
        }
        if (url.toLowerCase().endsWith(".tif")) {
            Layer layer = getRasterLayerFromTifSync(url);
            return layer;
        }
        if (url.toLowerCase().endsWith(".shp")) {
            Layer layer = getFeaLayerFromShapeSync(url);
            return layer;
        }
        if (url.toLowerCase().startsWith("http")) {
            ServiceFeatureTable sFTable = new ServiceFeatureTable(url);
            FeatureLayer fLayer = new FeatureLayer(sFTable);
            fLayer.setId(url);
            fLayer.setName(alias);
            fLayer.setDescription(url);
            return fLayer;
        }
        if (url.toLowerCase().contains(".geodatabase")) {
            int index = url.lastIndexOf(".geodatabase/");
            if (index == -1) return null;
            String tableName = url.substring(index + ".geodatabase/".length());
            String path = url.substring(0, index + ".geodatabase".length());
            List<Layer> layers = getFeaLayerFromGdbSync(path, new String[]{tableName});
            if (layers != null && layers.size() > 0) return layers.get(0);
        }
        return null;
    }

    public static String getPathByUrl(String url) {
        int index = url.lastIndexOf(".geodatabase/");
        if (index == -1) return null;
        String tableName = url.substring(index + ".geodatabase/".length());
        String path = url.substring(0, index + ".geodatabase".length());
        return path;
    }


    public static FeatureLayer loadFeaLayerSync(FeatureLayer feaLayer) {
        LoadStatus loadStatus = feaLayer.getLoadStatus();
        //if (loadStatus == LoadStatus.NOT_LOADED) {
        if (loadStatus != LoadStatus.LOADED) {
            FutureUtil.WaitingForYou<FeatureLayer> future = new FutureUtil.WaitingForYou<>();
            new Thread() {
                @Override
                public void run() {
                    feaLayer.addDoneLoadingListener(new Runnable() {
                        @Override
                        public void run() {
                            feaLayer.removeDoneLoadingListener(this);
                            future.setResult(feaLayer);
                        }
                    });
                    feaLayer.loadAsync();
                }
            }.start();
            return future.get(5, TimeUnit.SECONDS);
        } else return feaLayer;
    }

    public static FeatureTable loadFeaTbSync(FeatureTable featureTable) {
        LoadStatus loadStatus = featureTable.getLoadStatus();
        if (loadStatus != LoadStatus.LOADED) {
            FutureUtil.WaitingForYou<FeatureTable> future = new FutureUtil.WaitingForYou<>();
            new Thread() {
                @Override
                public void run() {
                    featureTable.addDoneLoadingListener(new Runnable() {
                        @Override
                        public void run() {
                            featureTable.removeDoneLoadingListener(this);
                            future.setResult(featureTable);
                        }
                    });
                    featureTable.loadAsync();
                }
            }.start();
            return future.get(5, TimeUnit.SECONDS);
        } else return featureTable;
    }


    public static Layer loadLayer(Layer layer) {
        LoadStatus loadStatus = layer.getLoadStatus();
        if (loadStatus == LoadStatus.NOT_LOADED) {
            FutureUtil.WaitingForYou<Layer> future = new FutureUtil.WaitingForYou<>();
            new Thread() {
                @Override
                public void run() {
                    layer.addDoneLoadingListener(new Runnable() {
                        @Override
                        public void run() {
                            layer.removeDoneLoadingListener(this);
                            future.setResult(layer);
                        }
                    });
                    layer.loadAsync();
                }
            }.start();
            return future.get(5, TimeUnit.SECONDS);
        } else return layer;
    }


    public static RasterLayer loadRasterLayerSync(RasterLayer rasterLayer) {
        LoadStatus loadStatus = rasterLayer.getLoadStatus();
        if (loadStatus == LoadStatus.NOT_LOADED) {
            FutureUtil.WaitingForYou<RasterLayer> future = new FutureUtil.WaitingForYou<>();
            new Thread() {
                @Override
                public void run() {
                    rasterLayer.addDoneLoadingListener(new Runnable() {
                        @Override
                        public void run() {
                            rasterLayer.removeDoneLoadingListener(this);
                            future.setResult(rasterLayer);
                        }
                    });
                    rasterLayer.loadAsync();
                }
            }.start();
            return future.get(5, TimeUnit.SECONDS);
        } else return rasterLayer;
    }

    public static ArcGISMapImageSublayer loadImgSubLayerSync(ArcGISMapImageSublayer sublayer) {
        LoadStatus loadStatus = sublayer.getLoadStatus();
        if (loadStatus == LoadStatus.NOT_LOADED) {
            FutureUtil.WaitingForYou<ArcGISMapImageSublayer> future = new FutureUtil.WaitingForYou<>();
            new Thread() {
                @Override
                public void run() {
                    sublayer.addDoneLoadingListener(new Runnable() {
                        @Override
                        public void run() {
                            sublayer.removeDoneLoadingListener(this);
                            future.setResult(sublayer);
                        }
                    });
                    sublayer.loadAsync();
                }
            }.start();

            return future.get(5, TimeUnit.SECONDS);
        } else return sublayer;
    }

    public static ServiceFeatureTable loadSerFeaTableSync(ServiceFeatureTable serFeaTable) {
        LoadStatus loadStatus = serFeaTable.getLoadStatus();
        if (loadStatus == LoadStatus.NOT_LOADED) {
            FutureUtil.WaitingForYou<ServiceFeatureTable> future = new FutureUtil.WaitingForYou<>();
            new Thread() {
                @Override
                public void run() {
                    serFeaTable.addDoneLoadingListener(new Runnable() {
                        @Override
                        public void run() {
                            serFeaTable.removeDoneLoadingListener(this);
                            future.setResult(serFeaTable);
                        }
                    });
                    serFeaTable.loadAsync();
                }
            }.start();

            return future.get(5, TimeUnit.SECONDS);
        } else return serFeaTable;
    }


    public interface IListener<T> {
        public void layersCallback(T t);
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    /**
     * 加载本地文件
     *
     * @param listenBack
     * @param list
     * @param paths
     */
    public static void readLocalFile(IListen listenBack, LayerList list, List<String> paths) {
        String path;
        for (int i = 0; i < paths.size(); i++) {
            path = paths.get(i);
            if (path.toLowerCase().endsWith(".shp")) {
                readShapeFile(listenBack, list, path);
            }
            if (path.toLowerCase().endsWith(".geodatabase")) {
                readGeoDatabase(listenBack, list, path);
            }
        }
    }

    /**
     * 加载文件的父目录
     *
     * @param list
     * @param tree
     */
    public static void readLocalFile(IListen listenBack, LayerList list, Map<String, List<File>> tree) {
        List<File> files;
        for (String path : tree.keySet()) {
            files = tree.get(path);
            File file;
            for (int i = 0; i < files.size(); i++) {
                file = files.get(i);
                if (file.getName().toLowerCase().endsWith(".shp")) {
                    readShapeFile(listenBack, list, file.getPath());
                }
                if (file.getName().toLowerCase().endsWith(".geodatabase")) {
                    readGeoDatabase(listenBack, list, file.getPath());
                }
            }
        }
    }

    public interface IListen {
        public void listen(Layer layer);
    }

    /**
     * @param list
     * @param path
     */
    private static void readGeoDatabase(IListen listenBack, LayerList list, String path) {
        Geodatabase geodatabase = new Geodatabase(path);
        Runnable listen = new Runnable() {
            @Override
            public void run() {
                List<GeodatabaseFeatureTable> tables = geodatabase.getGeodatabaseFeatureTables();
                for (GeodatabaseFeatureTable gtb : tables) {
                    FeatureLayer layer = new FeatureLayer(gtb);
                    String id = path + File.separator + layer.getName();
                    layer.setId(id);
                    layer.setDescription(id);
                    Runnable listen = new Runnable() {
                        @Override
                        public void run() {
                            layer.removeDoneLoadingListener(this);
                        }
                    };
                    layer.loadAsync();
                    layer.addDoneLoadingListener(listen);
                    list.add(layer);
                    if (listenBack != null) listenBack.listen(layer);
                }
                geodatabase.removeDoneLoadingListener(this);
            }
        };
        geodatabase.loadAsync();
        geodatabase.addDoneLoadingListener(listen);
    }


    private static void readShapeFile(IListen listenBack, LayerList list, String path) {
        ShapefileFeatureTable sft = new ShapefileFeatureTable(path);
        sft.loadAsync();
        Runnable listen = new Runnable() {
            @Override
            public void run() {
                FeatureLayer layer = sft.getFeatureLayer();
                layer.setId(path);
                layer.setDescription(path);
                list.add(layer);
                sft.removeDoneLoadingListener(this);
                if (listenBack != null) listenBack.listen(layer);
            }
        };
        sft.addDoneLoadingListener(listen);
    }
}
