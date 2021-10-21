package com.titan.mobile.arcruntime.layer.openlayer.enums;

public enum TypeSource {

    UN_KNOW("UN_KNOW", -1, "未知"),
    GOG("GOG", 0, "谷歌"),
    TDT("TDT", 1, "天地图"),
    ARCGIS("ARCGIS",2,"ESRI");

    private final String name;
    private final int source;
    private final String desc;


    private TypeSource(String name, int source, String desc) {
        this.name = name;
        this.source = source;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public int getSource() {
        return source;
    }

    public String getDesc() {
        return desc;
    }

    public static TypeSource getEnumByUrl(Integer status) {
        if (status == null) return null;
        for (int i = 0; i < TypeSource.values().length; i++) {
            if (TypeSource.values()[i].getSource() == status.intValue()) {
                return TypeSource.values()[i];
            }
        }
        return UN_KNOW;
    }

    public static TypeSource getEnumByName(String name) {
        for (int i = 0; i < TypeSource.values().length; i++) {
            if (TypeSource.values()[i].getName().equalsIgnoreCase(name.trim())) {
                return TypeSource.values()[i];
            }
        }
        return UN_KNOW;
    }

    public static TypeSource getEnumByDesc(String desc) {
        for (int i = 0; i < TypeSource.values().length; i++) {
            if (TypeSource.values()[i].getDesc().equalsIgnoreCase(desc.trim())) {
                return TypeSource.values()[i];
            }
        }
        return UN_KNOW;
    }
}
