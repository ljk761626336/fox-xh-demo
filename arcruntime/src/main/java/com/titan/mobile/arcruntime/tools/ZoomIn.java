package com.titan.mobile.arcruntime.tools;

import android.view.View;

import com.titan.mobile.arcruntime.R;
import com.titan.mobile.arcruntime.tools.core.BaseTool;

/**
 * Created by zy on 2019/5/28.
 */

public class ZoomIn extends BaseTool{

    public ZoomIn() {
        id = getClass().getSimpleName();
        name = "放大";
        resId = R.mipmap.tool_map_zoom_in_normal;
        checkedResId = R.mipmap.tool_map_zoom_in_pressed;
    }

    @Override
    public void viewClick(View view) {
        arcMap.getMapControl().zoomIn();
    }

}
