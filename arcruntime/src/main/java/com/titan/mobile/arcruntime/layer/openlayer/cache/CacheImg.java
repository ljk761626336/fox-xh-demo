package com.titan.mobile.arcruntime.layer.openlayer.cache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zy on 2018/2/20.
 */

public class CacheImg {

    private String cachePath;

    public CacheImg(String cachePath) {
        this.cachePath = cachePath;
    }

    // 将图片保存到本地 目录结构可以随便定义 只要你找得到对应的图片
    public byte[] addOfflineCacheFile(int level, int col, int row, byte[] bytes) {
        FileOutputStream out = null;
        File file = new File(cachePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File levelFile = new File(cachePath + "/" + level);
        if (!levelFile.exists()) {
            levelFile.mkdirs();
        }
        File colFile = new File(cachePath + "/" + level + "/" + col);
        if (!colFile.exists()) {
            colFile.mkdirs();
        }
        File rowFile = new File(cachePath + "/" + level + "/" + col + "/" + row + ".dat");
        if (!rowFile.exists()) {
            try {
                out = new FileOutputStream(rowFile);
                out.write(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }

    // 从本地获取图片
    public byte[] getOfflineCacheFile(int level, int col, int row) {
        byte[] bytes = null;
        File rowFile = new File(cachePath + "/" + level + "/" + col + "/" + row + ".dat");
        if (rowFile.exists()) {
            try {
                bytes = copySdcardBytes(rowFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bytes = null;
        }
        return bytes;
    }

    // 读取本地图片流
    public byte[] copySdcardBytes(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        byte[] temp = new byte[1024];
        int size = 0;
        while ((size = in.read(temp)) != -1) {
            out.write(temp, 0, size);
        }
        byte[] bytes = out.toByteArray();
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
        return bytes;
    }


}
