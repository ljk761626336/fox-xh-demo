package com.titan.mobile.arcruntime.core;

import com.titan.mobile.arcruntime.core.operate.Operate;

import java.util.ArrayList;
import java.util.List;

public class OperateSet extends BaseContainer {

    /**
     * 缓存数量
     */
    private int size = 10;

    private List<Operate> cacheSet;

    public OperateSet() {
        cacheSet = new ArrayList<>();
    }

    public OperateSet addOperate(Operate operate) {
        if (cacheSet != null) cacheSet.add(operate);
        if (cacheSet.size() > size) cacheSet.remove(0);
        return this;
    }

    public List<Operate> getCacheSet() {
        return cacheSet;
    }

    /**
     * 获取操作符
     *
     * @return
     */
    public Operate pollOperate() {
        int len = cacheSet.size();
        if (len == 0) return null;
        else {
            Operate operate = cacheSet.get(len - 1);
            cacheSet.remove(len - 1);
            return operate;
        }
    }

    public boolean isEmpty() {
        if (cacheSet == null || cacheSet.size() == 0) return true;
        else return false;
    }
}
