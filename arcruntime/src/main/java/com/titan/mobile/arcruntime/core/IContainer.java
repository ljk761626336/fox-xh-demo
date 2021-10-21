package com.titan.mobile.arcruntime.core;


/**
 * Created by zy on 2019/5/9.
 */

public interface IContainer {

    public void create(ArcMap arcMap);

    //public void ready(List<Layer> layers);
    public void ready();

    public void destroy();
}
