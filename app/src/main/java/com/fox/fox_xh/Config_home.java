package com.fox.fox_xh;

public class Config_home {
    //黑龙江数据采集项目文件路径
    public static String APP_PATH_DIR = "test";
    //全局db文件
    public static String APP_DB_NAME = "home.db";

    public interface BASE_URL {

        String BaseUrl_Locale = "http://127.0.0.1:8080/";
        //数据提交
        String BaseUrl_Remote = "http://218.9.73.245:8002/";
        //net  登录
        String BaseUrl_Login_net = "http://218.9.73.245:8003/";
        //java 登录
        String BaseUrl_Login_JAVA = "http://218.9.73.245:14001/";
        // 内网
        String BaseUrl_Login = "http://10.2.10.52:8003/";

    }
}
