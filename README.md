# sk_permission
aop代理模式Android权限请求工具库，无需添加任何业务代码，只需添加依赖，使用三个注解便可完成权限申请，权限取消，权限拒绝三种类型的处理，使用超级简单方便。
# 使用方式app model下build.gradle 文件中加入如下依赖：

   implementation 'com.sk.permission:sk_permission:1.0.1'
   
   
   
   
   点击同步，如同步不成功则在项目build.gradle 文件中加入
    maven {
            url ="https://dl.bintray.com/smarkorg/maven"
        }
	如下：
	
	allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url ="https://dl.bintray.com/smarkorg/maven"
        }
        
    }
}


同步完成后则可使用


在需要申请权限的方法上加入 注解：@ApplyPermission(permission = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA},requestCode = 1)，
可随意添加你需要申请的权限，填入requestCode 
并在处理权限取消的方法上加入注解 @CancerPermission，在处理权限拒绝的方法上加入注解  @RefusePermission ，
便可根据requestCode进行相应的处理，且本库包含用户拒绝权限后跳手机权限设置工具类，
可跳经过适配的不同手机权限设置： PermissionSettingUtil.goFitSetting(MainActivity.this);
也可跳默认手机设置页面 PermissionSettingUtil.goDefaultSetting(MainActivity.this);
也可以自定义intent ： PermissionSettingUtil.goCustomSetting(MainActivity.this,new Intent());
不懂的可下载demo查看使用

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

