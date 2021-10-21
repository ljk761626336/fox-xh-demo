package com.titan.mobile.arcruntime.layer.openlayer;


import com.esri.arcgisruntime.arcgisservices.TileInfo;
import com.esri.arcgisruntime.data.TileKey;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.internal.jni.CoreImageTiledLayer;
import com.esri.arcgisruntime.layers.ImageTiledLayer;
import com.titan.mobile.arcruntime.layer.arcgis.ArcImgLayer;
import com.titan.mobile.arcruntime.layer.openlayer.enums.Type;
import com.titan.mobile.arcruntime.layer.openlayer.enums.TypeSource;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class BaseImgLayer extends ImageTiledLayer {

    public static BaseImgLayer createSmart(Type type, TileInterceptor interceptor) {
        if (type.getSource() == TypeSource.GOG) {
            return GImgLayer.create(type, interceptor);
        }
        if (type.getSource() == TypeSource.TDT) {
            return TImgLayer.create(type, interceptor);
        }

        if(type.getSource() == TypeSource.ARCGIS){
            return ArcImgLayer.create(type,interceptor);
        }
        return null;
    }



    protected String flag;
    protected TileInterceptor tileInterceptor;

    protected BaseImgLayer(TileInfo tileInfo, Envelope fullExtent, String flag, TileInterceptor interceptor) {
        super(tileInfo, fullExtent);
        this.flag = flag;
        this.tileInterceptor = interceptor;
    }

    protected BaseImgLayer(CoreImageTiledLayer coreTiledLayer, boolean addToCache, String flag, TileInterceptor interceptor) {
        super(coreTiledLayer, addToCache);
        this.flag = flag;
        this.tileInterceptor = interceptor;
    }

    protected abstract String getReqUrl(int level, int col, int row);

    @Override
    protected byte[] getTile(TileKey tileKey) {
        int level = tileKey.getLevel();
        int col = tileKey.getColumn();
        int row = tileKey.getRow();

        byte[] tile = null;
        if (tileInterceptor != null){
            tile = tileInterceptor.before(flag, level, col, row);
        }
        if (tile != null && tile.length != 0) return tile;
        String url = getReqUrl(level, col, row);
        tile = queryByteFromService(url);
        if (tileInterceptor != null && tile != null && tile.length != 0)
            tileInterceptor.after(flag, level, col, row, tile);

        return tile;
    }

    protected byte[] queryByteFromService(String url) {
        HttpURLConnection iHttpURLConnection;
        BufferedInputStream iBufferedInputStream;
        ByteArrayOutputStream iByteArrayOutputStream;
        try {
            URL iURL = new URL(url);
            iHttpURLConnection = (HttpURLConnection) iURL.openConnection();
            iHttpURLConnection.connect();
            iBufferedInputStream = new BufferedInputStream(iHttpURLConnection.getInputStream());
            iByteArrayOutputStream = new ByteArrayOutputStream();
            byte[] iBuffer = new byte[1024];
            while (true) {
                int iLength = iBufferedInputStream.read(iBuffer);
                if (iLength > 0) {
                    iByteArrayOutputStream.write(iBuffer, 0, iLength);
                } else {
                    break;
                }
            }
            iBufferedInputStream.close();
            iHttpURLConnection.disconnect();
            byte[] iResult = iByteArrayOutputStream.toByteArray();
            return iResult;
        } catch (Exception e) {
            return null;
        }
    }
}
