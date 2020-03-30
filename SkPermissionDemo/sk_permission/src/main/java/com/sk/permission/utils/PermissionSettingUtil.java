package com.sk.permission.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by smark on 2020/1/28.
 * 邮箱：smarkwzp@163.com
 * 权限拒绝后引导用户设置跳转
 */
public class PermissionSettingUtil {

    //市面主流手机设置适配
    private static final String MANUFACTURER_HUAWEI = "huawei";//华为
    private static final String MANUFACTURER_MEIZU = "meizu";//魅族
    private static final String MANUFACTURER_XIAOMI = "xiaomi";//小米
    private static final String MANUFACTURER_REDMI = "redmi";//红米
    private static final String MANUFACTURER_OPPO = "oppo";
    private static final String MANUFACTURER_LG = "lg";
    private static final String MANUFACTURER_VIVO = "vivo";
    private static final String MANUFACTURER_SAMSUNG = "samsung";//三星
    private static final String MANUFACTURER_LETV = "letv";//乐视
    private static final String MANUFACTURER_ZTE = "zte";//中兴
    private static final String MANUFACTURER_YULONG = "yulong";//酷派
    private static final String MANUFACTURER_LENOVO = "lenovo";//联想


    /**
     * 跳转默认设置界面
     *
     * @param context
     */
    public static void goDefaultSetting(Context context) {
        context.startActivity(getDefaultSetting(context));
    }

    /**
     * 跳转自定义设置界面
     *
     * @param context
     */
    public static void goCustomSetting(Context context, Intent intent) {
        context.startActivity(intent);
    }

    /**
     * 跳转框架适配权限设置界面
     *
     * @param context
     */
    public static void goFitSetting(Context context) {
        Log.e("PermissionSettingUtil", "BRAND: " + Build.MANUFACTURER);
        Intent intent = null;
        switch (Build.MANUFACTURER.toLowerCase()) {
            case MANUFACTURER_HUAWEI:
                intent = getHuaweiSetting(context);
                break;
            case MANUFACTURER_MEIZU:
                intent = getMeizuSetting(context);
                break;
            case MANUFACTURER_XIAOMI:
            case MANUFACTURER_REDMI:
                intent = getMiSetting(context);
                break;
            case MANUFACTURER_OPPO:
                intent = getOPPOSetting(context);
                break;
            case MANUFACTURER_LG:
                intent = getLGSetting(context);
                break;
            case MANUFACTURER_VIVO:
                intent = getViVOSetting(context);
                break;
            case MANUFACTURER_LETV:
                intent = getLetvSetting(context);
                break;
            case MANUFACTURER_LENOVO:
            case MANUFACTURER_YULONG:
            case MANUFACTURER_ZTE:
            case MANUFACTURER_SAMSUNG:
            default:
                intent = getDefaultSetting(context);
                break;
        }

        context.startActivity(intent);
    }

    private static Intent getDefaultSetting(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return intent;
    }


    /**
     * ViVo
     *
     * @param context
     * @return
     */
    private static Intent getViVOSetting(Context context) {
        try {
            Intent appIntent = context.getPackageManager().getLaunchIntentForPackage("com.iqoo.secure");
            if (appIntent != null && Build.VERSION.SDK_INT < 23) {
                context.startActivity(appIntent);
                return null;
            }
            Intent vIntent = new Intent();
            vIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            vIntent.setAction(Settings.ACTION_SETTINGS);
            return vIntent;
        } catch (Exception e) {
            e.printStackTrace();
            return getDefaultSetting(context);
        }
    }

    /**
     * oppo
     *
     * @param context
     * @return
     */
    private static Intent getOPPOSetting(Context context) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", context.getPackageName());
            ComponentName comp = new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity");
            intent.setComponent(comp);
            return intent;
        } catch (Exception e) {
            e.printStackTrace();
            return getDefaultSetting(context);
        }
    }


    /**
     * LG
     *
     * @param context
     * @return
     */
    private static Intent getLGSetting(Context context) {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", context.getPackageName());
            ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
            intent.setComponent(comp);
            return intent;
        } catch (Exception e) {
            e.printStackTrace();
            return getDefaultSetting(context);
        }
    }


    /**
     * 乐视
     *
     * @param context
     * @return
     */
    private static Intent getLetvSetting(Context context) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", context.getPackageName());
            ComponentName comp = new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps");
            intent.setComponent(comp);
            return intent;
        } catch (Exception e) {
            e.printStackTrace();
            return getDefaultSetting(context);
        }
    }


    /**
     * 小米与红米
     */
    private static Intent getMiSetting(Context context) {
        try { // MIUI 8
            Intent miIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            miIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            miIntent.putExtra("extra_pkgname", context.getPackageName());
            return miIntent;
        } catch (Exception e) {
            try { // MIUI 5/6/7
                Intent miIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                miIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                miIntent.putExtra("extra_pkgname", context.getPackageName());
                return miIntent;
            } catch (Exception e1) { // 否则跳转到应用详情
                return getDefaultSetting(context);
            }
        }
    }

    /**
     * 魅族
     */
    private static Intent getMeizuSetting(Context context) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", context.getPackageName());
            return intent;
        } catch (Exception e) {
            e.printStackTrace();
            return getDefaultSetting(context);
        }
    }

    /**
     * 华为的权限管理页面
     */
    private static Intent getHuaweiSetting(Context context) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            intent.setComponent(comp);
            return intent;
        } catch (Exception e) {
            e.printStackTrace();
            return getDefaultSetting(context);
        }

    }

}
