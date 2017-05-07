package com.timer.jike.timemanager.utils;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * Created by jike on 2017/5/7.
 */

public class UtilBmob {

    private static final String APPLICTION_ID = "9808f66ad92e98c3973ae1248ef6515d";
    public static void init(Application app) {
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
    public static void forceUpdate(Context context){
        BmobUpdateAgent.forceUpdate(context);
    }

//    静默下载
    public static void silentUpdate(Context context){
        BmobUpdateAgent.silentUpdate(context);
    }
}
