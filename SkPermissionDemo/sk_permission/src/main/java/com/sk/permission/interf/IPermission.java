package com.sk.permission.interf;

/**
 * Created by smark on 2020/1/28.
 * 邮箱：smarkwzp@163.com
 * 操作回调接口
 */
public interface IPermission {

    void onPermissionGranted();

    void onPermissionCanceled(int requestCode);

    void onPermissionRefused(int requestCode);

}
