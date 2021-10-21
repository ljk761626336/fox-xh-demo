package com.titan.mobile.arcruntime.util;

import java.math.BigDecimal;

public final class CustomUtil {

    /**
     * 将小数度数转换为度分秒格式
     *
     * @param latlng
     * @return
     */
    public static String _10To60(String latlng) {
        double num = Double.parseDouble(latlng);
        int du = (int) Math.floor(Math.abs(num));    //获取整数部分
        double temp = getdPoint(Math.abs(num)) * 60;
        int fen = (int) Math.floor(temp); //获取整数部分
        int miao = (int) (getdPoint(temp) * 60);
        if (num < 0)
            return "-" + du + "°" + fen + "′" + miao + "″";
        return du + "°" + fen + "′" + miao + "″";
    }

    public static String _10To60_len2(String latlng) {
        double num = Double.parseDouble(latlng);
        int du = (int) Math.floor(Math.abs(num));    //获取整数部分
        double temp = getdPoint(Math.abs(num)) * 60;
        int fen = (int) Math.floor(temp); //获取整数部分
        double miao = getdPoint(temp) * 60;
        String s = DecimalFormats.getFormat("00.00").format(miao);
        if (num < 0)
            return "-" + du + "°" + fen + "′" + s + "″";
        return du + "°" + fen + "′" + s + "″";
    }

    public static String _10To60_short(String latlng) {
        double num = Double.parseDouble(latlng);
        int du = (int) Math.floor(Math.abs(num));    //获取整数部分
        double temp = getdPoint(Math.abs(num)) * 60;
        int fen = (int) Math.floor(temp); //获取整数部分
        if (num < 0)
            return "-" + du + "°" + fen + "′";
        return du + "°" + fen + "′";
    }

    /**
     * 60进制转10进制
     *
     * @param latlng
     * @return
     */
    public static double _60To10(String latlng) {
        double du = Double.parseDouble(latlng.substring(0, latlng.indexOf("°")));
        double fen = Double.parseDouble(latlng.substring(latlng.indexOf("°") + 1, latlng.indexOf("′")));
        double miao = Double.parseDouble(latlng.substring(latlng.indexOf("′") + 1, latlng.indexOf("″")));
        if (du < 0)
            return -(Math.abs(du) + (fen + (miao / 60)) / 60);
        return du + (fen + (miao / 60)) / 60;
    }

    //获取小数部分
    private static double getdPoint(double num) {
        double d = num;
        int fInt = (int) d;
        BigDecimal b1 = new BigDecimal(Double.toString(d));
        BigDecimal b2 = new BigDecimal(Integer.toString(fInt));
        double dPoint = b1.subtract(b2).floatValue();
        return dPoint;
    }

}
