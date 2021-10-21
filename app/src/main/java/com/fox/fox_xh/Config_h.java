package com.fox.fox_xh;

import android.content.Context;
import android.os.Environment;

import com.titan.mobile.arcruntime.util.FileUtil;

import java.io.File;

/**
 * 正式环境
 */
public final class Config_h extends Config_home {

    //二调
    public static String APP_ED_DIR = "二调";
    //二调
    public static String APP_GYL_DIR = "公益林";
    //二调
    public static String APP_YZL_DIR = "营造林";
    //地图影像存储位置
    public static String APP_IMAGE_DIR = "卫片";
    //base.tpk文件存储位置
    public static String APP_TPK_DIR = "base";
    //崩溃文件存储位置
    public static String APP_CRASH_DIR = "crash";
    //空间数据表存储位置
    public static String APP_SPATIAL_DIR = "spatial";
    //缓存数据存储位置
    public static String APP_CACHE_DIR = "cache";
    //调查数据备份文件夹
    public static String APP_SPARE_DIR = "备份";
    //字典数据库存储位置
    public static String APP_DIC_DIR = "dic";
    //照片存储位置
    public static String APP_PHOTO_DIR = "photo";

    //android 存储目录
    public static String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    //app 所在目录
    public static String APP_PATH = ROOT_PATH.concat(File.separator).concat(APP_PATH_DIR);
    //备份数据目录
    public static String APP_PATH_SPARE = APP_PATH.concat(File.separator).concat(APP_SPARE_DIR);
    //营造林数据目录
    public static String APP_YZL_PATH = APP_PATH.concat(File.separator).concat(APP_YZL_DIR);
    //二调数据目录
    public static String APP_ED_PATH = APP_PATH.concat(File.separator).concat(APP_ED_DIR);
    //公益林数据目录
    public static String APP_GYL_PATH = APP_PATH.concat(File.separator).concat(APP_GYL_DIR);
    //客户卫片 影像文件
    public static String APP_IMAGE_PATH = APP_PATH.concat(File.separator).concat(APP_IMAGE_DIR);
    //客户卫片 影像文件
    public static String APP_PATH_TPK = APP_PATH.concat(File.separator).concat(APP_TPK_DIR);
    //app空间数据库目录
    public static String APP_SDB_PATH = APP_PATH.concat(File.separator).concat(APP_SPATIAL_DIR);
    //app本地崩溃日志目录
    public static String APP_PATH_CRASH = APP_PATH.concat(File.separator).concat(APP_CRASH_DIR);
    //app系统字典文件夹
    public static String APP_PATH_DIC = APP_PATH.concat(File.separator).concat(APP_DIC_DIR);
    //app地图缓存目录
    public static String APP_MAP_CACHE = APP_PATH.concat(File.separator).concat(APP_CACHE_DIR);
    //app地图影像缓存  基础底图影像缓存
    public static String APP_BASE_MAP_IMG_CACHE = APP_PATH.concat(File.separator).concat(APP_CACHE_DIR).concat(File.separator).concat("BASE_MAP_IMG.img");
    //app图片目录
    public static String APP_PHOTO_PATH = APP_PATH.concat(File.separator).concat(APP_PHOTO_DIR);
    //app数据库所在路径
    public static String APP_DB_PATH = APP_PATH.concat(File.separator).concat(APP_DB_NAME);
    //app字典数据库路径


    private Config_h() {
    }

    public static void creatDir(Context context) {
        //创建文集目录
        FileUtil.createFileSmart(
                Config_h.APP_PATH,
                Config_h.APP_ED_PATH,
                Config_h.APP_GYL_PATH,
                Config_h.APP_YZL_PATH,
                Config_h.APP_DB_PATH,
                Config_h.APP_IMAGE_PATH,
                Config_h.APP_SDB_PATH,
                Config_h.APP_MAP_CACHE,
                Config_h.APP_PATH_CRASH,
                Config_h.APP_PHOTO_DIR,
                Config_h.APP_PATH_DIC,
                Config_h.APP_PHOTO_PATH,
                Config_h.APP_PATH_TPK,
                Config_h.APP_PATH_SPARE,
                Config_h.APP_BASE_MAP_IMG_CACHE
        );


        FileUtil.copyAssets(context,"base.tpk",Config_h.APP_PATH_TPK.concat(File.separator)+"base.tpk");
    }
}
