package com.sk.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.sk.permission.interf.IPermission;
import com.sk.permission.utils.PUtil;

/**
 * Created by smark on 2020/3/28.
 * 邮箱：smarkwzp@163.com
 * 构造一个透明的activity，用于权限申请相关操作，实现与使用端解耦
 */
public class PermissionRequestActivity extends Activity {

    private static final String PERMISSION = "permission";
    private static final String REQUEST_CODE = "request_code";
    private static IPermission mIPermission;


    public static void startPermissionRequest(Context context, String[] permissions,
                                              int requestCode, IPermission iPermission) {
        mIPermission = iPermission;
        Intent intent = new Intent(context, PermissionRequestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putStringArray(PERMISSION, permissions);
        bundle.putInt(REQUEST_CODE, requestCode);
        intent.putExtras(bundle);

        context.startActivity(intent);

        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置状态栏透明
        Window window = PermissionRequestActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        Bundle bundle = getIntent().getExtras();
        String[] permissions = bundle.getStringArray(PERMISSION);
        int requestCode = bundle.getInt(REQUEST_CODE);

        requestPermission(permissions, requestCode);
    }

    private void requestPermission(String[] permissions, int requestCode) {

        if (PUtil.hasSelfPermissions(this, permissions)) {
            // 权限已经有了
            mIPermission.onPermissionGranted();
            finish();
        } else {
            // 申请权限
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PUtil.verifyPermissions(grantResults)) {
            // 权限已经有了
            mIPermission.onPermissionGranted();
        } else {
            if (PUtil.shouldShowRequestPermissionRationale(this, permissions)) {
                //取消权限
                mIPermission.onPermissionCanceled(requestCode);
            } else {
                //拒绝权限
                mIPermission.onPermissionRefused(requestCode);
            }
        }
        finish();
        overridePendingTransition(0, 0);
    }
}
