package com.timer.jike.timemanager.utils;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.timer.jike.timemanager.App;
import com.timer.jike.timemanager.bean.MyUser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * Created by jike on 2017/5/7.
 */

public class UtilBmob {

    private static final String APPLICTION_ID = "9808f66ad92e98c3973ae1248ef6515d";
    private static final String TAG = "UtilBmob";
    private static Application mApp;

    public static void init(Application app) {
        mApp = app;
        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config =new BmobConfig.Builder(app)
                //设置appkey
                .setApplicationId(APPLICTION_ID)
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024*1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
    }

    public static void initAppVersion(){
//        initAppVersion方法适合开发者调试自动更新功能时使用，一旦AppVersion表在后台创建成功，建议屏蔽或删除此方法，否则会生成多行记录。
//        BmobUpdateAgent.initAppVersion();
    }

    public static void update(Context context){
//        BmobUpdateAgent.setUpdateOnlyWifi(false);
        BmobUpdateAgent.update(context);
    }

//    它和自动更新的主要区别是：在这种手动更新的情况下，无论网络状况是否Wifi，无论用户是否忽略过该版本的更新，都可以像下面的示例一样在按钮的回调中发起更新检查
    public static void updateForce(Context context){
        BmobUpdateAgent.forceUpdate(context);
    }

//    静默下载
    public static void updateSilent(Context context){
        BmobUpdateAgent.silentUpdate(context);
    }


    public static void signUp(Context context,BmobUser bu){
        //注意：不能用save方法进行注册
        bu.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser s, BmobException e) {
                if(e==null){
                    toast("注册成功:" +s.toString());
                }else{
                    UtilLog.e(TAG,e);
                }
            }
        });
    }

    private static void toast(String s) {
        Toast.makeText(App.getInstance(), s, Toast.LENGTH_SHORT).show();
    }

    public static void login(Context context,BmobUser bu){
        bu.login(new SaveListener<BmobUser>() {

            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if(e==null){
                    toast("登录成功:");
                    //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                    //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                }else{
                    UtilLog.e(TAG,e);
                }
            }
        });

        BmobUser.loginByAccount("username", "用户密码", new LogInListener<MyUser>() {

            @Override
            public void done(MyUser user, BmobException e) {
                if(user!=null){
                    UtilLog.i("smile","用户登陆成功");
                }
            }
        });
    }

    public static String getUserServerUid(){
        MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
        if(bmobUser != null){
            return bmobUser.getObjectId();
        }
        return null;//UtilDevice.getDeviceId(mApp);
    }

    public static boolean isLoggedin(){
        MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
        if(bmobUser != null){
            // 允许用户使用应用

            //BmobUser中的特定属性
            String username = (String) BmobUser.getObjectByKey("username");
//MyUser中的扩展属性
            Integer age = (Integer) BmobUser.getObjectByKey("age");
            Boolean sex = (Boolean) BmobUser.getObjectByKey("sex");

            return true;
        }else{
            //缓存用户对象为空时， 可打开用户注册界面…
            return false;
        }
    }

    public static void updateUser(BmobUser newUser){
        BmobUser bmobUser = BmobUser.getCurrentUser();
        newUser.update(bmobUser.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    toast("更新用户信息成功");
                }else{
                    toast("更新用户信息失败:" + e.getMessage());
                }
            }
        });
    }

    public static void logOut(){
        BmobUser.logOut();   //清除缓存用户对象
        BmobUser currentUser = BmobUser.getCurrentUser(); // 现在的currentUser是null了
    }

    public static void updateCurrentUserPassword(){
        BmobUser.updateCurrentUserPassword("旧密码", "新密码", new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    toast("密码修改成功，可以用新密码进行登录啦");
                }else{
                    toast("失败:" + e.getMessage());
                }
            }

        });
    }
}
