package com.titan.mobile.arcruntime.layer.openlayer;

public interface TileInterceptor {

    public byte[] before(String flag, long level, long col, long row);

    public void after(String flag, long level, long col, long row, byte[] tile);

}
