package com.fox.fox_xh.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.blankj.utilcode.util.TimeUtils;
import com.esri.arcgisruntime.geometry.Point;
import com.fox.fox_xh.R;
import com.fox.fox_xh.util.LogcatHelper;
import com.fox.fox_xh.util.PositionUtil;
import com.fox.fox_xh.util.TrakManager;
import com.titan.foxgaode.dao.GdListener;
import com.titan.foxgaode.view.IGdListener;
import com.titan.mobile.arcruntime.core.ArcMap;
import com.titan.mobile.arcruntime.layer.openlayer.BaseImgLayer;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements IGdListener {

    private ArcMap arcMap;
    private TrakManager trakManager;
    private TextView tvTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTxt = findViewById(R.id.tvTxt);
        arcMap = findViewById(R.id.arcMap);
        arcMap.mapLoad(new ArcMap.IMapReady() {
            @Override
            public void onMapReady() {
                arcMap.getMapControl().initDefaultLocation().useDefaultLocation(200);
            }
        }, null);

        GdListener.getInstance(this).init(this, true);

        trakManager = new TrakManager(arcMap.getMapView());
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) return;
        if (aMapLocation.getErrorCode() == 6) return;
        double lon = aMapLocation.getLongitude();
        double lat = aMapLocation.getLatitude();
        float accuracy = aMapLocation.getAccuracy();
        int errorcode = aMapLocation.getErrorCode();
        String txt = TimeUtils.date2String(new Date())+":" + errorcode + "," + lon + "," + lat + "," + accuracy;
        Log.e("TTT", txt);
        tvTxt.setText(txt);
        LogcatHelper.getInstance(this).write(txt);
        PositionUtil.Gps gps = PositionUtil.gcj02_To_Gps84(lon, lat);
        trakManager.showLine(new Point(gps.getWgLon(), gps.getWgLat()), accuracy);
    }
}
