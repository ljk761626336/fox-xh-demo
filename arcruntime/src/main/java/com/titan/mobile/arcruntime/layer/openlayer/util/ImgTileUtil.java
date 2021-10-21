package com.titan.mobile.arcruntime.layer.openlayer.util;

import com.esri.arcgisruntime.arcgisservices.LevelOfDetail;
import com.esri.arcgisruntime.arcgisservices.TileInfo;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.titan.mobile.arcruntime.layer.openlayer.enums.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class ImgTileUtil {

    public final static class ARCGIS{

        private static final SpatialReference spatialReference = SpatialReference.create(3857); //空间参考
        public static final Envelope fullExtent = new Envelope(-20037507.229594, -19971868.880409, 20037507.229594, 19971868.880409, spatialReference);//全服范围
        public static final Envelope initExtent = new Envelope(-20037507.229594, -19971868.880409, 20037507.229594, 19971868.880409, spatialReference);//初始范围
        private static final double originX = -2.0037508342787E7;
        private static final double originY= 2.0037508342787E7;
        private static final int dpi = 96;
        private static final int rows = 256;
        private static final int cols = 256;

        public static double[] iScale = {
                5.91657527591555E8, 2.95828763795777E8, 1.47914381897889E8, 7.3957190948944E7,
                3.6978595474472E7, 1.8489297737236E7, 9244648.868618, 4622324.434309,
                2311162.217155, 1155581.108577, 577790.554289, 288895.277144,
                144447.638572, 72223.819286, 36111.909643, 18055.954822,
                9027.977411, 4513.988705, 2256.994353, 1128.497176,
                564.248588,282.124294,141.062147,70.5310735
        };
        public static double[] iRes = {
                156543.03392800014, 78271.51696399994, 39135.75848200009,19567.87924099992,
                9783.93962049996,4891.96981024998,2445.98490512499,1222.992452562495,
                611.4962262813797,305.74811314055756,152.87405657041106,76.43702828507324,
                38.21851414253662,19.10925707126831,9.554628535634155,4.77731426794937,
                2.388657133974685,1.1943285668550503,0.5971642835598172,0.29858214164761665,
                0.14929107082380833,0.07464553541190416,0.03732276770595208,0.01866138385297604
        };

        public static TileInfo getTileInfo() {
            /*原点坐标*/
            Point localPoint = new Point(originX, originY,spatialReference);
            List<LevelOfDetail> levelOfDetails = new ArrayList<>();
            for (int i = 0; i < iRes.length; i++) {
                LevelOfDetail levelOfDetail = new LevelOfDetail(i, iRes[i], iScale[i]);
                levelOfDetails.add(levelOfDetail);
            }
            return new TileInfo(dpi, TileInfo.ImageFormat.UNKNOWN, levelOfDetails, localPoint, spatialReference, rows, cols);
        }

        public static String getRequestUrl(int level, int col, int row, Type type) {
            String url = type.getUrl() + "/tile" + "/" + level + "/" + row + "/" + col;
            return url;
        }

    }

    public final static class TDT {

        /*通过天地图官网开发者申请Android平台key*/
        private static final String tk="7bcb29b5b8508777f9fca1989b7ac134";
        private static final int tileHeight = 256;
        private static final int tileWidth = 256;
        private static final int DPI = 90;

        private static final int SRID_2000 = 4490;
        private static SpatialReference SID_2000 = SpatialReference.create(SRID_2000);
        private static final Point origin_2000 = new Point(-180, 90, SID_2000);
        public static final Envelope fullExtent_2000 = new Envelope(-180, -90, 180, 90, SID_2000);
        private static double[] RESOLUTIONS;
        private static Point origin;

        //MERCATOR
        private static final int SRID_MERCATOR = 102100;
        private static SpatialReference SID_MERCATOR = SpatialReference.create(SRID_MERCATOR);
        private static final Point origin_MERCATOR = new Point(-20037508.3427892, 20037508.3427892, SID_MERCATOR);
        public static final Envelope fullExtent_MERCATOR = new Envelope(-20037508.3427892, -20037508.3427892, 20037508.3427892, 20037508.3427892, SID_MERCATOR);

        // 墨卡托坐标系下的分辨率
        private static final double[] RESOLUTIONS_MERCATOR = {78271.51696402048,
                39135.75848201024, 19567.87924100512, 9783.93962050256,
                4891.96981025128, 2445.98490512564, 1222.99245256282,
                611.49622628141, 305.748113140705, 152.8740565703525,
                76.43702828517625, 38.21851414258813, 19.109257071294063,
                9.554628535647032, 4.777314267823516, 2.388657133911758,
                1.194328566955879, 0.5971642834779395};

        // 国家2000坐标系下的分辨率
        private static final double[] RESOLUTIONS_2000 = {0.7031249999891485,
                0.35156249999999994, 0.17578124999999997, 0.08789062500000014,
                0.04394531250000007, 0.021972656250000007, 0.01098632812500002,
                0.00549316406250001, 0.0027465820312500017, 0.0013732910156250009,
                0.000686645507812499, 0.0003433227539062495,
                0.00017166137695312503, 0.00008583068847656251,
                0.000042915344238281406, 0.000021457672119140645,
                0.000010728836059570307, 0.000005364418029785169};

        /**
         * 比例尺    // 两种坐标系下的比例尺一致
         */
        private static final double[] SCALES = {2.958293554545656E8,
                1.479146777272828E8, 7.39573388636414E7, 3.69786694318207E7,
                1.848933471591035E7, 9244667.357955175, 4622333.678977588,
                2311166.839488794, 1155583.419744397, 577791.7098721985,
                288895.85493609926, 144447.92746804963, 72223.96373402482,
                36111.98186701241, 18055.990933506204, 9027.995466753102,
                4513.997733376551, 2256.998866688275};


        public static TileInfo getTileInfo(Type type) {
            if (type == Type.TDT_URL_IMAGE_2000 ||
                    type == Type.TDT_URL_IMAGE_ANNOTATION_CHINESE_2000 ||
                    type == Type.TDT_URL_IMAGE_ANNOTATION_ENGLISH_2000 ||
                    type == Type.TDT_URL_VECTOR_2000 ||
                    type == Type.TDT_URL_VECTOR_ANNOTATION_CHINESE_2000 ||
                    type == Type.TDT_URL_VECTOR_ANNOTATION_ENGLISH_2000 ||
                    type == Type.TDT_URL_TERRAIN_2000 ||
                    type == Type.TDT_URL_TERRAIN_ANNOTATION_CHINESE_2000) {

                RESOLUTIONS = RESOLUTIONS_2000;
                origin = origin_2000;
            } else {
                RESOLUTIONS = RESOLUTIONS_MERCATOR;
                origin = origin_MERCATOR;
            }
            List<LevelOfDetail> levelOfDetails = new ArrayList<>();
            for (int i = 0; i < RESOLUTIONS.length; i++) {
                LevelOfDetail levelOfDetail = new LevelOfDetail(i, RESOLUTIONS[i], SCALES[i]);
                levelOfDetails.add(levelOfDetail);
            }
            return new TileInfo(DPI, TileInfo.ImageFormat.UNKNOWN, levelOfDetails, origin, origin.getSpatialReference(), tileHeight, tileWidth);
        }


        public static Envelope getEnvelop(Type type) {
            if (type == Type.TDT_URL_IMAGE_2000 ||
                    type == Type.TDT_URL_IMAGE_ANNOTATION_CHINESE_2000 ||
                    type == Type.TDT_URL_IMAGE_ANNOTATION_ENGLISH_2000 ||
                    type == Type.TDT_URL_VECTOR_2000 ||
                    type == Type.TDT_URL_VECTOR_ANNOTATION_CHINESE_2000 ||
                    type == Type.TDT_URL_VECTOR_ANNOTATION_ENGLISH_2000 ||
                    type == Type.TDT_URL_TERRAIN_2000 ||
                    type == Type.TDT_URL_TERRAIN_ANNOTATION_CHINESE_2000) {
                return fullExtent_2000;
            } else {
                return fullExtent_MERCATOR;
            }
        }

        public static String getRequestUrl(int level, int col, int row, Type type) {
            int random = new Random().nextInt(6);
            String url = type.getUrl();
            url = url
                    .replace("{random}", String.valueOf(random))
                    .replace("{z}", String.valueOf(level + 1))
                    //.replace("{z}", String.valueOf(level))
                    .replace("{x}", String.valueOf(col))
                    .replace("{y}", String.valueOf(row))
                    .replace("{key}", tk);
            return url;
        }


    }

    public final static class Gog {

        public static double[] iScale = {
                591657527.591555, 295828763.795777, 147914381.897889, 73957190.948944,
                36978595.474472, 18489297.737236, 9244648.868618, 4622324.434309,
                2311162.217155, 1155581.108577, 577790.554289, 288895.277144,
                144447.638572, 72223.819286, 36111.909643, 18055.954822,
                9027.977411, 4513.988705, 2256.994353, 1128.497176,
        };
        public static double[] iRes = {
                156543.033928, 78271.5169639999, 39135.7584820001, 19567.8792409999,
                9783.93962049996, 4891.96981024998, 2445.98490512499, 1222.99245256249,
                611.49622628138, 305.748113140558, 152.874056570411, 76.4370282850732,
                38.2185141425366, 19.1092570712683, 9.55462853563415, 4.77731426794937,
                2.38865713397468, 1.19432856685505, 0.597164283559817, 0.298582141647617,
        };

        private static int dpi = 160;
        private static int tileWidth = 256;
        private static int tileHeight = 256;
        private static Point origin = new Point(-20037508.342787, 20037508.342787, SpatialReference.create(102113));

        public static TileInfo getTileInfo() {
            List<LevelOfDetail> levelOfDetails = new ArrayList<>();
            for (int i = 0; i < iRes.length; i++) {
                LevelOfDetail levelOfDetail = new LevelOfDetail(i, iRes[i], iScale[i]);
                levelOfDetails.add(levelOfDetail);
            }
            return new TileInfo(dpi, TileInfo.ImageFormat.UNKNOWN, levelOfDetails, origin, SpatialReference.create(102113), tileHeight, tileWidth);
        }

        public static Envelope getFullEnvelope() {
            return new Envelope(-22041257.773878, -32673939.6727517, 22041257.773878, 20851350.0432886, SpatialReference.create(102113));
        }

        public static String getRequestUrl(int level, int col, int row, Type type) {
            int random = new Random().nextInt(4);
            String url = type.getUrl();
            url = url
                    .replace("{random}", String.valueOf(random))
                    .replace("{z}", String.valueOf(level))
                    .replace("{x}", String.valueOf(col))
                    .replace("{y}", String.valueOf(row))
                    .replace("{key}", "");
            return url;
        }

        //影像的叠加层 lyrs=h
        //"http://mt1.google.cn/vt/imgtp=png32&lyrs=h@210000000&hl=en-US&gl=US&src=app&s=G";
        //"http://mt2.google.cn/vt/imgtp=png32&lyrs=h@210000000&hl=en-US&gl=US&src=app&s=G";
        //"http://mt3.google.cn/vt/imgtp=png32&lyrs=h@210000000&hl=en-US&gl=US&src=app&s=G";

        //矢量底图 lyrs=m  lyrs=是指瓦片类型 有标注  在国内但有偏移，国外暂无测试
        //"http://mt1.google.cn/vt/lyrs=m@209712068&hl=en-US&gl=US&src=app&s=G";
        //"http://mt2.google.cn/vt/lyrs=m@209712068&hl=en-US&gl=US&src=app&s=G";
        //"http://mt3.google.cn/vt/lyrs=m@209712068&hl=en-US&gl=US&src=app&s=G";

        //影像底图 lyrs=y  有标注  在国内但有偏移，国外暂无测试
        //"http://mt0.google.cn/vt/lyrs=y@126&hl=zh-CN&gl=cn&src=app&s=G";
        //"http://mt1.google.cn/vt/lyrs=y@126&hl=zh-CN&gl=cn&src=app&s=G";
        //"http://mt2.google.cn/vt/lyrs=y@126&hl=zh-CN&gl=cn&src=app&s=G";
        //"http://mt3.google.cn/vt/lyrs=y@126&hl=zh-CN&gl=cn&src=app&s=G";

        //影像底图 lyrs=s  没有标注
        //"http://mt0.google.cn/vt/lyrs=s@126&hl=en-US&gl=US&src=app&s=G",
        //"http://mt1.google.cn/vt/lyrs=s@126&hl=en-US&gl=US&src=app&s=G",
        //"http://mt2.google.cn/vt/lyrs=s@126&hl=en-US&gl=US&src=app&s=G",
        //"http://mt3.google.cn/vt/lyrs=s@126&hl=en-US&gl=US&src=app&s=G"
    }


}
