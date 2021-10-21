package com.titan.mobile.arcruntime.core.operate;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;


public class Operate implements Serializable {

    private Map<Object, Operator> cache = new LinkedHashMap<>();

    public static Operate create() {
        return new Operate();
    }

    private Operate() {
    }

    public Operate addOperate(Operator operator, Object data) {
        cache.put(data, operator);
        return this;
    }

    public Map<Object, Operator> getCache() {
        return cache;
    }
}