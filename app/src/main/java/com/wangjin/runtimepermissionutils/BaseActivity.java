package com.wangjin.runtimepermissionutils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/***
 * 作用：写一个基类，进行Android 6.0权限的处理
 */
public class BaseActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 0;// 权限请求返回标识
    private static IPermissionListener mListener; // 回调接口，返回授权结果信息
    private static Activity sTopActivity; // 获取的栈顶Activity

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivityToTask(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.removeActivityToTask(this);
    }

    /**
     *  该方法用于请求必要的权限
     * @param permissions 调用者传过来需要申请的权限
     */
    public static void requestRuntimePermission(String[] permissions, IPermissionListener permissionListener) {
        mListener = permissionListener;
        sTopActivity = ActivityManager.getTaskTopActivity();
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            // 检查自身是否已经授权该权限
            if (ActivityCompat.checkSelfPermission(sTopActivity, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        // 去请求未授权的权限
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(sTopActivity,
                    permissionList.toArray(new String[permissionList.size()]), REQUEST_CODE);
        } else {
            // 集合为空，说明请求的权限全部已授权
            mListener.OnGranted();
        }

    }

    /**
     * 权限请求结果的回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE :
                List<String> deniedPermissions = new ArrayList<>(); // 用户未授权的权限
                for (int i = 0; i < permissions.length; i++) {
                    String permission  = permissions[i];
                    int grant = grantResults[i];
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        // 用户未授权该权限，将该权限添加进集合通过接口回调返回出去
                        deniedPermissions.add(permission);
                    }
                }
                if (deniedPermissions.isEmpty()) {
                    // 为空  说明全部授权
                    mListener.OnGranted();
                } else {
                    // 不为空则有权限未授权，根据业务逻辑，可以再次申请提示用户授权,此处不再处理
                    mListener.OnDenided(deniedPermissions);
                }
                break;
            default:

                break;
        }
    }

    /**
     * 定义接口，进行授权结果的监听回调
     */
    public interface IPermissionListener {
        // 授权成功的回调
        void OnGranted();
        // 授权失败的回调，返回未授权的一些权限
        void OnDenided(List<String> deniedPermission);
    }

}
