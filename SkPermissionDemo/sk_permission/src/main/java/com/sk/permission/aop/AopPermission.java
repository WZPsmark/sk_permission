package com.sk.permission.aop;

import android.content.Context;
import android.util.Log;

import com.sk.permission.PermissionRequestActivity;
import com.sk.permission.annotation.ApplyPermission;
import com.sk.permission.annotation.CancerPermission;
import com.sk.permission.annotation.RefusePermission;
import com.sk.permission.interf.IPermission;
import com.sk.permission.utils.PUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by smark on 2020/1/28.
 * 邮箱：smarkwzp@163.com
 * 网络权限切面执行类
 */
@Aspect
public class AopPermission {


    @Pointcut("execution(@com.sk.permission.annotation.ApplyPermission * *(..)) && @annotation(permissionApply)")
    public void requestPermission(ApplyPermission permissionApply) {

    }


    @Around("requestPermission(permissionApply)")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, ApplyPermission permissionApply) {
        final Object object = joinPoint.getThis();
        Context mContext = (Context) object;
        Log.e("AopPermission","KKKKKKKKKKKKKKKKKKKKK");
        PermissionRequestActivity.startPermissionRequest(mContext, permissionApply.permission(), permissionApply.requestCode(), new IPermission() {
            @Override
            public void onPermissionGranted() {
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
            @Override
            public void onPermissionCanceled(int requestCode) {
                PUtil.invokAnnotation(object, RefusePermission.class, requestCode);
            }

            @Override
            public void onPermissionRefused(int requestCode) {
                PUtil.invokAnnotation(object, CancerPermission.class, requestCode);
            }
        });
    }

}
