package com.titan.mobile.arcruntime.tools;

import android.view.View;

import com.titan.mobile.arcruntime.R;
import com.titan.mobile.arcruntime.tools.core.BaseTool;

/**
 * Created by zy on 2019/5/28.
 */

public class ZoomOut extends BaseTool{

    public ZoomOut() {
        id = getClass().getSimpleName();
        name = "缩小";
        resId = R.mipmap.tool_map_zoom_out_normal;
        checkedResId = R.mipmap.tool_map_zoom_out_pressed;
    }

    @Override
    public void viewClick(View view) {
        arcMap.getMapControl().zoomOut();
    }
}
