package com.titan.mobile.arcruntime.util;

import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.ArcGISSublayer;
import com.esri.arcgisruntime.layers.SublayerList;
import com.esri.arcgisruntime.util.ListenableList;

/**
 * Created by zy on 2019/5/24.
 */

public final class LayerIteration {

    public static void iteration(ArcGISMapImageLayer layers) {
        SublayerList list = layers.getSublayers();
        ArcGISSublayer gisSublayer;
        for (int i = 0; i < list.size(); i++) {
            gisSublayer = list.get(i);
           // System.out.println("---------" + gisSublayer.getName() + "---------");
            iteration(gisSublayer);
        }
    }


    public static void iteration(ArcGISSublayer gisSublayer) {
        ListenableList<ArcGISSublayer> list = gisSublayer.getSublayers();
        for (int i = 0; i < list.size(); i++) {
            gisSublayer = list.get(i);
            //System.out.println(gisSublayer.getName());
            iteration(gisSublayer);
        }
    }
}
