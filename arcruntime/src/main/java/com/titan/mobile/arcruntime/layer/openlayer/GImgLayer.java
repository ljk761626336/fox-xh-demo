package com.titan.mobile.arcruntime.layer.openlayer;

import com.esri.arcgisruntime.arcgisservices.TileInfo;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.internal.jni.CoreImageTiledLayer;
import com.titan.mobile.arcruntime.layer.openlayer.enums.Type;
import com.titan.mobile.arcruntime.layer.openlayer.util.ImgTileUtil;

/**
 * Created by zy on 2018/2/19.
 */

public class GImgLayer extends BaseImgLayer {

    private Type type;

    public static GImgLayer create(Type type, TileInterceptor interceptor) {
        TileInfo tileInfo = ImgTileUtil.Gog.getTileInfo();
        Envelope fullExtent = ImgTileUtil.Gog.getFullEnvelope();
        return new GImgLayer(tileInfo, fullExtent, type, interceptor);
    }

    protected GImgLayer(TileInfo tileInfo, Envelope fullExtent,Type type, TileInterceptor interceptor) {
        super(tileInfo, fullExtent, type.getName(), interceptor);
        this.type = type;
    }

    protected GImgLayer(CoreImageTiledLayer coreTiledLayer, boolean addToCache, Type type, TileInterceptor interceptor) {
        super(coreTiledLayer, addToCache, type.getName(), interceptor);
        this.type = type;
    }

    @Override
    protected String getReqUrl(int level, int col, int row) {
        String url = ImgTileUtil.Gog.getRequestUrl(level, col, row, type);
        System.out.println(url);
        return url;
    }

}
