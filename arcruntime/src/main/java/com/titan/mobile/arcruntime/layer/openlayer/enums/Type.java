package com.titan.mobile.arcruntime.layer.openlayer.enums;

public enum Type {

    UN_KNOW(
            TypeSource.TDT,
            "UN_KNOW",
            "UN_KNOW",
            "未知"
    ),
    ARCGIS_VECTOR_MERCATOR(
            TypeSource.ARCGIS,
            "ARCGIS_VECTOR_MERCATOR",
            "http://services.arcgisonline.com/arcgis/rest/services/World_Street_Map/MapServer",
            "ESRI矢量3857墨卡托坐标系地图服务"
    ),

    ARCGIS_IMAGE_MERCATOR(
            TypeSource.ARCGIS,
            "ARCGIS_IMAGE_MERCATOR",
            "http://services.arcgisonline.com/arcgis/rest/services/World_Imagery/MapServer",
            "ESRI影像3857墨卡托坐标系地图服务"
    ),

    TDT_URL_VECTOR_2000(
            TypeSource.TDT,
            "TDT_URL_VECTOR_2000",
            "http://t0.tianditu.gov.cn/vec_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX={z}&TILECOL={x}&TILEROW={y}&tk={key}",
            "天地图矢量国家2000坐标系地图服务"
    ),
    TDT_URL_VECTOR_ANNOTATION_CHINESE_2000(
            TypeSource.TDT,
            "TDT_URL_VECTOR_ANNOTATION_CHINESE_2000",
            "http://t0.tianditu.gov.cn/cva_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX={z}&TILECOL={x}&TILEROW={y}&tk={key}",
            "天地图矢量国家2000坐标系中文标注"
    ),
    TDT_URL_VECTOR_ANNOTATION_ENGLISH_2000(
            TypeSource.TDT,
            "TDT_URL_VECTOR_ANNOTATION_ENGLISH_2000",
            "http://t0.tianditu.gov.cn/eva_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=eva&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX={z}&TILECOL={x}&TILEROW={y}&tk={key}",
            "天地图矢量国家2000坐标系英文标注"
    ),
    TDT_URL_IMAGE_2000(
            TypeSource.TDT,
            "TDT_URL_IMAGE_2000",
            "http://t0.tianditu.gov.cn/img_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX={z}&TILECOL={x}&TILEROW={y}&tk={key}",
            "天地图影像国家2000坐标系中文标注"
    ),
    TDT_URL_IMAGE_ANNOTATION_CHINESE_2000(
            TypeSource.TDT,
            "TDT_URL_IMAGE_ANNOTATION_CHINESE_2000",
            "http://t0.tianditu.gov.cn/cia_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX={z}&TILECOL={x}&TILEROW={y}&tk={key}",
            "天地图影像国家2000坐标系中文标注"
    ),

    URL_IMAGE_2000(
            TypeSource.TDT,
            "URL_IMAGE_2000",
            "http://t0.tianditu.gov.cn/img_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX={z}&TILECOL={x}&TILEROW={y}&tk=1d63495140638de0bfd2273f64b67371",
            "天地图影像国家2000坐标系"
    ),
    URL_IMAGE_ANNOTATION_CHINESE_2000(
            TypeSource.TDT,
            "URL_IMAGE_ANNOTATION_CHINESE_2000",
            "http://t0.tianditu.gov.cn/cia_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX={z}&TILECOL={x}&TILEROW={y}&tk=1d63495140638de0bfd2273f64b67371",
            "天地图影像国家2000坐标系中文标注"
    ),
    TDT_URL_IMAGE_ANNOTATION_ENGLISH_2000(
            TypeSource.TDT,
            "TDT_URL_IMAGE_ANNOTATION_ENGLISH_2000",
            "http://t0.tianditu.gov.cn/eia_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=eia&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX={z}&TILECOL={x}&TILEROW={y}&tk={key}",
            "天地图影像国家2000坐标系英文标注"
    ),
    TDT_URL_TERRAIN_2000(
            TypeSource.TDT,
            "TDT_URL_TERRAIN_2000",
            "http://t0.tianditu.gov.cn/ter_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=ter&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX={z}&TILECOL={x}&TILEROW={y}&tk={key}",
            "天地图地形国家2000坐标系地图服务"
    ),
    TDT_URL_TERRAIN_ANNOTATION_CHINESE_2000(
            TypeSource.TDT,
            "TDT_URL_TERRAIN_ANNOTATION_CHINESE_2000",
            "http://t0.tianditu.gov.cn/cta_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cta&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX={z}&TILECOL={x}&TILEROW={y}&tk={key}",
            "天地图地形国家2000坐标系中文标注"
    ),
    TDT_URL_VECTOR_MERCATOR(
            TypeSource.TDT,
            "TDT_URL_VECTOR_MERCATOR",
            "http://t{random}.tianditu.gov.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILECOL={x}&TILEROW={y}&tk={key}",
            "天地图矢量墨卡托投影地图服务"
    ),
    TDT_URL_VECTOR_ANNOTATION_CHINESE_MERCATOR(
            TypeSource.TDT,
            "TDT_URL_VECTOR_ANNOTATION_CHINESE_MERCATOR",
            "http://t0.tianditu.gov.cn/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILECOL={x}&TILEROW={y}&tk={key}",
            "天地图矢量墨卡托中文标注"
    ),
    TDT_URL_VECTOR_ANNOTATION_ENGLISH_MERCATOR(
            TypeSource.TDT,
            "TDT_URL_VECTOR_ANNOTATION_ENGLISH_MERCATOR",
            "http://t0.tianditu.gov.cn/eva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=eva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILECOL={x}&TILEROW={y}&tk={key}",
            "天地图矢量墨卡托英文标注"
    ),
    TDT_URL_IMAGE_MERCATOR(
            TypeSource.TDT,
            "TDT_URL_IMAGE_MERCATOR",
            "http://t{random}.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILECOL={x}&TILEROW={y}&tk={key}",
            "天地图影像墨卡托投影地图服务"
    ),
    TDT_URL_IMAGE_ANNOTATION_CHINESE_MERCATOR(
            TypeSource.TDT,
            "TDT_URL_IMAGE_ANNOTATION_CHINESE_MERCATOR",
            "http://t0.tianditu.gov.cn/cia_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILECOL={x}&TILEROW={y}&tk={key}",
            "天地图影像墨卡托投影中文标注"
    ),
    TDT_URL_IMAGE_ANNOTATION_ENGLISH_MERCATOR(
            TypeSource.TDT,
            "TDT_URL_IMAGE_ANNOTATION_ENGLISH_MERCATOR",
            "http://t0.tianditu.gov.cn/eia_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=eia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILECOL={x}&TILEROW={y}&tk={key}",
            "天地图影像墨卡托投影英文标注"
    ),
    TDT_URL_TERRAIN_MERCATOR(
            TypeSource.TDT,
            "TDT_URL_TERRAIN_MERCATOR",
            "http://t0.tianditu.gov.cn/ter_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=ter&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILECOL={x}&TILEROW={y}&tk={key}",
            "天地图地形墨卡托投影地图服务"
    ),
    TDT_URL_TERRAIN_ANNOTATION_CHINESE_MERCATOR(
            TypeSource.TDT,
            "TDT_URL_TERRAIN_ANNOTATION_CHINESE_MERCATOR",
            "http://t0.tianditu.gov.cn/cta_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cta&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILECOL={x}&TILEROW={y}&tk={key}",
            "天地图地形墨卡托投影中文标注"
    ),
    GOOGLE_URL_IMAGE_84(
            TypeSource.GOG,
            "GOOGLE_URL_IMAGE_84",
            "http://map.google.cn/vt/lyrs=s@113&hl=nl&x={x}&y={y}&z={z}&s=",
            "谷歌影像"
    ),
    GOOGLE_URL_VECTOR_84(
            TypeSource.GOG,
            "GOOGLE_URL_VECTOR_84",
            "http://mt0.google.cn/vt/lyrs=m@113&hl=n1&x={x}&y={y}&z={z}&s=",
            "谷歌矢量"
    ),
    GOOGLE_URL_ROAD_84(
            TypeSource.GOG,
            "GOOGLE_URL_ROAD_84",
            "http://mt0.google.cn/vt/imgtp=png32&lyrs=h@169000000&hl=zh-CN&gl=cn&x={x}&y={y}&z={z}&s=",
            "谷歌道路"
    );

    private final String name;
    private final String url;
    private final TypeSource source;
    private final String desc;


    private Type(TypeSource source, String name, String url, String desc) {
        this.source = source;
        this.name = name;
        this.url = url;
        this.desc = desc;
    }

    public TypeSource getSource() {
        return source;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getDesc() {
        return desc;
    }

    public static Type getEnumByUrl(String url) {
        if (url == null) return null;
        for (int i = 0; i < Type.values().length; i++) {
            if (Type.values()[i].getUrl().equalsIgnoreCase(url)) {
                return Type.values()[i];
            }
        }
        return UN_KNOW;
    }

    public static Type getEnumByName(String name) {
        for (int i = 0; i < Type.values().length; i++) {
            if (Type.values()[i].getName().equalsIgnoreCase(name.trim())) {
                return Type.values()[i];
            }
        }
        return UN_KNOW;
    }

    public static Type getEnumByDesc(String desc) {
        for (int i = 0; i < Type.values().length; i++) {
            if (Type.values()[i].getDesc().equalsIgnoreCase(desc.trim())) {
                return Type.values()[i];
            }
        }
        return UN_KNOW;
    }

}
