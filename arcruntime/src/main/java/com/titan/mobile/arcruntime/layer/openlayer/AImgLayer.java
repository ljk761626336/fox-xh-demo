package com.titan.mobile.arcruntime.layer.openlayer;

import com.esri.arcgisruntime.arcgisservices.LevelOfDetail;
import com.esri.arcgisruntime.arcgisservices.TileInfo;
import com.esri.arcgisruntime.data.TileKey;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;

import com.esri.arcgisruntime.layers.ServiceImageTiledLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AImgLayer extends ServiceImageTiledLayer {


    public final static AImgLayer create() {
        List<LevelOfDetail> levelOfDetails = new ArrayList<>();
        LevelOfDetail levelOfDetail = new LevelOfDetail(0, 156543.03392800014, 5.91657527591555E8);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(1, 78271.51696399994,  2.95828763795777E8);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(2, 39135.75848200009, 1.47914381897889E8);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(3, 19567.87924099992,  7.3957190948944E7);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(4, 9783.93962049996, 3.6978595474472E7);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(5, 4891.96981024998, 1.8489297737236E7);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(6, 2445.98490512499,  9244648.868618);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(7, 1222.992452562495, 4622324.434309);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(8, 611.4962262813797, 2311162.217155);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(9, 305.74811314055756,  1155581.108577);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(10, 152.87405657041106,  577790.554289);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(11, 76.43702828507324,  288895.277144);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(12, 38.21851414253662, 144447.638572);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(13, 19.10925707126831,  72223.819286);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(14, 9.554628535634155,   36111.909643);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(15, 4.77731426794937, 18055.954822);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(16, 2.388657133974685,  9027.977411);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(17, 1.1943285668550503,  4513.988705);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(18, 0.5971642835598172, 2256.994353);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(19, 0.29858214164761665, 1128.497176);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(20, 0.14929107082380833,  564.248588);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(21, 0.07464553541190416,  282.124294);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(22, 0.03732276770595208, 141.062147);
        levelOfDetails.add(levelOfDetail);
        levelOfDetail = new LevelOfDetail(23, 0.01866138385297604, 70.5310735);
        levelOfDetails.add(levelOfDetail);

        Point origin = new Point(-2.0037508342787E7 ,-2.0037508342787E7 ,SpatialReference.create(3857));
        TileInfo tileInfo = new TileInfo(96, TileInfo.ImageFormat.JPG, levelOfDetails, origin, SpatialReference.create(3857), 256, 256);

        Geometry fullExtent = Envelope.fromJson("{\"xmin\":-2.003718760706861E7,\"ymin\":-7679113.797548823,\"xmax\":2.0036105498462453E7,\"ymax\":1.8037800594432846E7,\"spatialReference\":{\"wkid\":3857}}");

        return new AImgLayer(tileInfo, (Envelope) fullExtent);
    }


    private final String baseUrl = "http://elevation3d.arcgis.com/arcgis/rest/services/World_Imagery/MapServer/0";

    protected AImgLayer(TileInfo tileInfo, Envelope fullExtent) {
        super(tileInfo, fullExtent);
    }

    @Override
    protected String getTileUrl(TileKey tileKey) {
        return null;
    }


    @Override
    protected byte[] getTile(TileKey tileKey) {
        String iResult = null;
        Random iRandom = null;
        int level = tileKey.getLevel();
        int col = tileKey.getColumn();
        int row = tileKey.getRow();
        System.out.println(level + "|" + col + "|" + row);
        return new byte[0];
    }

    @Override
    public String getUri() {
        return null;
    }
}
