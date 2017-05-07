package com.timer.jike.timemanager.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.timer.jike.timemanager.R;
import com.timer.jike.timemanager.bean.MyUser;
import com.timer.jike.timemanager.utils.UtilLog;
import com.timer.jike.timemanager.utils.UtilSP;
import com.timer.jike.timemanager.utils.UtilString;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;


public class SyncActivity extends AppCompatActivity {


    private static final String TAG = "SyncActivity";
    @BindView(R.id.et_username) EditText mEtUsername;
//    @BindView(R.id.et_phone) EditText mEtPhone;
    @BindView(R.id.et_pwd) EditText mEtPwd;
    @BindView(R.id.btn_la_sign_up) Button mBtnSignUp;
    @BindView(R.id.btn_la_login_by_pwd) Button mBtnLogin;
    @BindView(R.id.btn_la_logout) Button mBtnLogOut;
    @BindView(R.id.btn_la_sync) Button mBtnSync;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sync);
        ButterKnife.bind(this);

        MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
        if(bmobUser != null){
            mBtnLogin.setEnabled(false);
            mBtnLogOut.setEnabled(true);
            String username = (String) BmobUser.getObjectByKey("username");
            String password = (String) BmobUser.getObjectByKey("password");//貌似拿不到
            mEtUsername.setText(username);
            mEtPwd.setText(password);
            mEtUsername.setEnabled(false);
            mEtPwd.setEnabled(false);
            mBtnSignUp.setEnabled(false);
        } else {
            mBtnLogOut.setEnabled(false);
            mBtnSync.setEnabled(false);
        }

    }

    @OnClick(R.id.btn_la_sign_up)
    void onClickSignUp(){
        if(validateFormInput()){
            MyUser user = new MyUser();
            user.setUsername(mEtUsername.getText().toString());
            user.setPassword(mEtPwd.getText().toString());

            user.signUp(new SaveListener<MyUser>() {
                @Override
                public void done(MyUser s, BmobException e) {
                    if(e==null){
                        Toast.makeText(SyncActivity.this, "注册成功:" +s.toString(), Toast.LENGTH_SHORT).show();
                        UtilSP.getSPSetting(SyncActivity.this).edit().putString(UtilSP.USER_NAME,user.getUsername()).apply();
                        UtilSP.getSPSetting(SyncActivity.this).edit().putString(UtilSP.USER_PWD,mEtPwd.getText().toString()).apply();
                    }else {
                        Toast.makeText(SyncActivity.this, "注册失败:" +e.toString(), Toast.LENGTH_SHORT).show();
                        UtilLog.e(TAG,e);
                    }
                }
            });
        }

    }

    @OnClick(R.id.btn_la_login_by_pwd)
    void onClickLogin(){
        if(validateFormInput()){
            BmobUser.loginByAccount(mEtUsername.getText().toString(),mEtPwd.getText().toString(), new LogInListener<MyUser>() {

                @Override
                public void done(MyUser user, BmobException e) {
                    if(user!=null){
                        Toast.makeText(SyncActivity.this, "用户登陆成功", Toast.LENGTH_SHORT).show();
                        mBtnLogin.setEnabled(false);
                        mEtUsername.setEnabled(false);
                        mEtPwd.setEnabled(false);
                    } else {
                        Toast.makeText(SyncActivity.this, "用户登陆失败:" +e.toString(), Toast.LENGTH_SHORT).show();
                        UtilLog.e(TAG,e);
                    }
                }
            });
        }

    }

    @OnClick(R.id.btn_la_logout)
    void onClickLogOut(){
        BmobUser.logOut();   //清除缓存用户对象
        mBtnLogin.setEnabled(true);
        mEtUsername.setEnabled(true);
        mEtPwd.setEnabled(true);
        mBtnLogOut.setEnabled(false);
    }

    @OnClick(R.id.btn_la_sync)
    void onClickSync(){
        Toast.makeText(this, "上传或者下载该帐户的数据到本地，敬请期待！", Toast.LENGTH_SHORT).show();
    }

    private boolean validateFormInput() {
        if (!UtilString.isValidUserName(mEtUsername.getText().toString())){
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!UtilString.isValidPwd(mEtPwd.getText().toString())){
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

//
//
//    @OnClick(R.id.btn_la_login_send_pin)
//    void onClickSendPin(){
//        BmobSMS.requestSMSCode(mEtPhone.getText().toString(),"模板名称", new QueryListener<Integer>() {
//
//            @Override
//            public void done(Integer smsId,BmobException ex) {
//                if(ex==null){//验证码发送成功
//                    Toast.makeText(SyncActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
//                    Log.i("smile", "短信id："+smsId);//用于查询本次短信发送详情
//                }
//            }
//        });
//    }
//
//    @OnClick(R.id.btn_la_login_by_pin)
//    void onClickLoginByPin(){
//        MyUser user = new MyUser();
//        user.setMobilePhoneNumber(mEtPhone.getText().toString());//设置手机号码（必填）
//        user.setUsername(mEtUsername.getText().toString());                  //设置用户名，如果没有传用户名，则默认为手机号码
//        user.setPassword(mEtPwd.getText().toString());                  //设置用户密码
//        user.signOrLogin("验证码", new SaveListener<MyUser>() {
//
//            @Override
//            public void done(MyUser user,BmobException e) {
//                if(e==null){
//                    Toast.makeText(SyncActivity.this, "注册或登录成功", Toast.LENGTH_SHORT).show();
//                    Log.i("smile", ""+user.getUsername()+"-"+user.getAge()+"-"+user.getObjectId());
//                }else{
//                    Toast.makeText(SyncActivity.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//        });
//    }
}
