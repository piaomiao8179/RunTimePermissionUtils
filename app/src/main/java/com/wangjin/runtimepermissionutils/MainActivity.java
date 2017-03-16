package com.wangjin.runtimepermissionutils;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends BaseActivity {

    private String[] permissions = {Manifest.permission.SEND_SMS,
            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseActivity.requestRuntimePermission(permissions, new IPermissionListener() {
            @Override
            public void OnGranted() {
                // 成功时候的回调
                Toast.makeText(MainActivity.this, "全部授权成功了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnDenided(List<String> deniedPermission) {
                // 失败时候的回调
                for (String s : deniedPermission) {
                    Toast.makeText(MainActivity.this, "未授权的权限：" + s, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
