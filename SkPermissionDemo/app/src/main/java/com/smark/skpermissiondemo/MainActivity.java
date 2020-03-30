package com.smark.skpermissiondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.sk.permission.annotation.CancerPermission;
import com.sk.permission.annotation.RefusePermission;
import com.sk.permission.annotation.ApplyPermission;
import com.sk.permission.utils.PermissionSettingUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
    }


    @ApplyPermission(permission = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA},requestCode = 1)
    private void requestPermission() {
        Toast.makeText(this,"获取权限成功",Toast.LENGTH_SHORT).show();
        System.out.println("do your Things");
    }


    @CancerPermission
    private void permissionCancer(int requestCode){
        Toast.makeText(this,"权限申请已被取消",Toast.LENGTH_SHORT).show();
    }


    @RefusePermission
    private void permissionRefused(int requestCode){
        Toast.makeText(this,"权限申请已被拒绝",Toast.LENGTH_SHORT).show();
        goSetting();
    }

    private void goSetting() {
        System.out.println("此处可以弹出让用户去设置打开权限的弹框");
        AlertDialog alertDialog= new AlertDialog.Builder(this).setTitle("权限设置")
                .setMessage("请先到设置界面允许相关权限，才能正常使用功能")
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionSettingUtil.goFitSetting(MainActivity.this);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
    }
}
