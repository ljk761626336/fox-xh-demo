package com.titan.mobile.arcruntime.tools;

import android.view.View;

import com.titan.mobile.arcruntime.R;
import com.titan.mobile.arcruntime.core.ArcMap;
import com.titan.mobile.arcruntime.tools.core.BaseTool;
import com.titan.mobile.arcruntime.tools.core.ToolView;

public class ZoomLoc extends BaseTool implements ToolView.ILongClick {

    public ZoomLoc() {
        id = getClass().getSimpleName();
        name = "当前位置";
        resId = R.mipmap.tool_map_zoom_loc_normal;
        checkedResId = R.mipmap.tool_map_zoom_loc_pressed;
    }

    @Override
    public void create(ArcMap arcMap) {
        super.create(arcMap);
        view.setILongClick(this);
    }

    @Override
    public void viewClick(View view) {
        arcMap.getMapControl().initDefaultLocation().useDefaultLocation(100);
    }

    @Override
    public void viewLongClick(View view) {

    }
}

