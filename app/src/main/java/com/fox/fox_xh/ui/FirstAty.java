package com.fox.fox_xh.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ToastUtils;
import com.fox.fox_xh.Config_h;
import com.fox.fox_xh.R;
import com.fox.xpermit.OnPermissionCallback;
import com.fox.xpermit.Permission;
import com.fox.xpermit.XXPermissions;

import java.util.List;

public class FirstAty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        checkPermission();

    }

    String[] MANIFEST = new String[]{
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_BACKGROUND_LOCATION",
            "android.permission.ACCESS_WIFI_STATE",
            "android.permission.CAMERA",
            "android.permission.RECORD_AUDIO",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    String[] MANIFEST_Q = new String[]{
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_BACKGROUND_LOCATION",
            "android.permission.ACCESS_WIFI_STATE",
            "android.permission.CAMERA",
            "android.permission.RECORD_AUDIO",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.MANAGE_EXTERNAL_STORAGE"
    };

    @SuppressLint("CheckResult")
    private void checkPermission() {
        if (isAndroid11()) {
            XXPermissions.with(FirstAty.this)
                    // 不适配 Android 11 可以这样写
                    //.permission(Permission.Group.STORAGE)
                    // 适配 Android 11 需要这样写，这里无需再写 Permission.Group.STORAGE
                    .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                    .request((permissions, all) -> {
                        if (all) {
                            XXPermissions.with(FirstAty.this)
                                    .permission(Permission.Group.COMMON_Q)
                                    .request((permissions1, all1) -> {
                                        if (all) {
                                            //LocalManager.getInstance(FirstAty.this.getBaseContext()).grantingPermission();
                                            startActivity(new Intent(FirstAty.this, MainActivity.class));

                                            //创建APP所用得目录文件夹
                                            Config_h.creatDir(FirstAty.this);
                                            FirstAty.this.finish();
                                        } else {
                                            ToastUtils.showLong("请授予相应权限！");
                                        }
                                    });

                        } else {
                            ToastUtils.showLong("请授予相应权限！");
                        }
                    });
        } else {
            XXPermissions.with(FirstAty.this)
                    // 不适配 Android 11 可以这样写
                    //.permission(Permission.Group.STORAGE)
                    // 适配 Android 11 需要这样写，这里无需再写 Permission.Group.STORAGE
                    .permission(Permission.Group.COMMON)
                    .request(new OnPermissionCallback() {

                        @Override
                        public void onGranted(List<String> permissions, boolean all) {
                            if (all) {
                                //LocalManager.getInstance(FirstAty.this.getBaseContext()).grantingPermission();
                                startActivity(new Intent(FirstAty.this, MainActivity.class));

                                //创建APP所用得目录文件夹
                                Config_h.creatDir(FirstAty.this);
                                FirstAty.this.finish();
                            } else {
                                ToastUtils.showLong("请授予相应权限！");
                            }
                        }
                    });
        }




        /*RxPermissionFactory.getRxPermissions(this)
                .requestEachCombined(permis)
                .subscribe(new RxConsumer(this) {
                    @Override
                    public void accept(Permission permission) {
                        super.accept(permission);
                        if (permission.granted) {
                            LocalManager.getInstance(FirstAty.this.getBaseContext()).grantingPermission();
                            startActivity(new Intent(FirstAty.this, LoginAty.class));

                            //创建APP所用得目录文件夹
                            Config_h.creatDir(FirstAty.this);
                        } else {
                            ToastUtils.showLong("请授予相应权限！");
                        }
                    }
                });*/
    }


    /**
     * 是否是 Android 11 及以上版本
     */
    static boolean isAndroid11() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.Q;
    }
}
