package com.titan.mobile.arcruntime.layer.openlayer;

import com.esri.arcgisruntime.arcgisservices.TileInfo;
import com.esri.arcgisruntime.geometry.Envelope;
import com.titan.mobile.arcruntime.layer.openlayer.enums.Type;
import com.titan.mobile.arcruntime.layer.openlayer.util.ImgTileUtil;

public class TImgLayer extends BaseImgLayer {

    Type type;

    public static TImgLayer create(Type type, TileInterceptor tileInterceptor) {
        TileInfo tileInfo = ImgTileUtil.TDT.getTileInfo(type);
        Envelope fullExtent = ImgTileUtil.TDT.getEnvelop(type);
        TImgLayer tImgLayer = new TImgLayer(tileInfo, fullExtent, type, tileInterceptor);
        return tImgLayer;
    }

    protected TImgLayer(TileInfo tileInfo, Envelope fullExtent, Type type, TileInterceptor interceptor) {
        super(tileInfo, fullExtent, type.getName(), interceptor);
        this.type = type;
    }

    @Override
    protected String getReqUrl(int level, int col, int row) {
        String url = ImgTileUtil.TDT.getRequestUrl(level, col, row, type);
        System.out.println(url);
        return url;
    }
}
