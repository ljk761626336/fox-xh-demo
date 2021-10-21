package com.titan.mobile.arcruntime.layer.arcgis;

import com.esri.arcgisruntime.arcgisservices.TileInfo;
import com.esri.arcgisruntime.geometry.Envelope;
import com.titan.mobile.arcruntime.layer.openlayer.BaseImgLayer;
import com.titan.mobile.arcruntime.layer.openlayer.TileInterceptor;
import com.titan.mobile.arcruntime.layer.openlayer.enums.Type;
import com.titan.mobile.arcruntime.layer.openlayer.util.ImgTileUtil;

/**
 * @Description: 重写Arcgis切片服务类
 * @Author: ljk
 * @CreateDate: 2021/1/20 10:36
 */
public final class ArcImgLayer extends BaseImgLayer {

    private Type type;
    public static ArcImgLayer create(Type type, TileInterceptor tileInterceptor){
        TileInfo tileInfo = ImgTileUtil.ARCGIS.getTileInfo();
        Envelope fullExtent = ImgTileUtil.ARCGIS.fullExtent;
        return new ArcImgLayer(type,tileInfo,fullExtent,tileInterceptor);
    }

    protected ArcImgLayer(Type type,TileInfo tileInfo, Envelope fullExtent, TileInterceptor interceptor) {
        super(tileInfo, fullExtent, type.getName(), interceptor);
        this.type = type;
    }

    @Override
    protected String getReqUrl(int level, int col, int row) {
        return ImgTileUtil.ARCGIS.getRequestUrl(level,col,row,type);
    }

}
