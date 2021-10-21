package com.titan.mobile.arcruntime.util;

import java.util.UUID;

/**
 * Created by zy on 2018/1/30.
 */

public final class UUIDTool {
    public UUIDTool() {
    }

    /**
     * 自动生成32位的UUid，对应数据库的主键id进行插入用。
     *
     * @return
     */
    public static String get32UUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    public static String get36UUID() {
        return UUID.randomUUID().toString();
    }

}

