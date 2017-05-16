package com.timer.jike.timemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.timer.jike.timemanager.R;
import com.timer.jike.timemanager.bean.Event;
import com.timer.jike.timemanager.service.AutoLockService;
import com.timer.jike.timemanager.utils.UtilBmob;
import com.timer.jike.timemanager.utils.UtilDB;
import com.timer.jike.timemanager.utils.UtilDialog;
import com.timer.jike.timemanager.utils.UtilLog;
import com.timer.jike.timemanager.utils.UtilSP;
import com.timer.jike.timemanager.utils.UtilString;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {
    private static String TAG;
    private static final String TITLE_DEFAULT = "标签";


    private static final String LOCK_TAG = null;

    @BindView(R.id.tv_ma_title) TextView mTvTitle;
    @BindView(R.id.tv_ma_duration) TextView mTvDuration;
    @BindView(R.id.btn_ma_start) Button mBtnStart;
    @BindView(R.id.btn_ma_stop) Button mBtnStop;
    @BindView(R.id.btn_ma_history) Button mBtnHistory;
    @BindView(R.id.btn_ma_settings) Button mBtnSettings;

    private Date mStartTime;
    private Subscription mSubscription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UtilLog.d(TAG,"------onCreate");
        TAG = getLocalClassName();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        startService(new Intent(this, AutoLockService.class));

        restoreUnfinishedEvent();

        //检查自动更新
        UtilBmob.update(this);
    }

    private void restoreUnfinishedEvent() {
        long lastStartTime = UtilSP.getSPSetting(this).getLong(UtilSP.EVENT_START_TIME, -1);
        String title = UtilSP.getSPSetting(this).getString(UtilSP.EVENT_TITLE, TITLE_DEFAULT);
        mTvTitle.setText(title);
        if (lastStartTime != -1){
            mStartTime = new Date(lastStartTime);
            startCount(mStartTime);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        UtilLog.d(TAG, "------onWindowFocusChanged", hasFocus);
        if (!hasFocus) {
            Intent intent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            intent.putExtra("reason", "globalactions");//可避免关机对话框被关闭
            sendBroadcast(intent);
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        UtilLog.d(TAG,"------onSaveInstanceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UtilLog.d(TAG,"------onDestroy");
        if (mSubscription != null) mSubscription.unsubscribe();
        saveUnFinishedEvent();
    }

    private void saveUnFinishedEvent() {
        if (mStartTime != null){
           UtilSP.getSPSetting(this).edit().putLong(UtilSP.EVENT_START_TIME, mStartTime.getTime()).apply();
           UtilSP.getSPSetting(this).edit().putString(UtilSP.EVENT_TITLE, mTvTitle.getText().toString()).apply();
        }
    }

    @OnClick(R.id.tv_ma_title)
    void onClickTitle() {
        UtilDialog.inputDialog(this, TITLE_DEFAULT, null, null, null, (dialog, input) -> {
            if (!TextUtils.isEmpty(input))
                mTvTitle.setText(input);
        });
    }

    @OnClick(R.id.btn_ma_start)
    void onClickStart() {
        mStartTime = new Date();
        saveUnFinishedEvent();
        startCount(mStartTime);
    }

    private void startCount(Date startTime) {
        long lastCount = (new Date().getTime() - startTime.getTime())/1000;
        mBtnStart.setEnabled(false);
        mBtnStop.setEnabled(true);
        mSubscription = Observable
                .interval(1, TimeUnit.SECONDS)
                .takeUntil(aLong -> mStartTime == null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    UtilLog.d(TAG, "interval",lastCount, aLong);
                    mTvDuration.setText(UtilString.getTimeSpan(aLong+lastCount+1));
                });
    }

    @OnClick(R.id.btn_ma_stop)
    void onClickStop() {
        Date stopTime = new Date();
        Event event = new Event();
        event.setBegin_time(mStartTime);
        event.setTitle(mTvTitle.getText().toString());
        event.setEnd_time(stopTime);
        event.setDuration(stopTime.getTime() - mStartTime.getTime());
        UtilDB.insertEvent(event);
        UtilLog.d(TAG, "insert", event.getId());

        mBtnStart.setEnabled(true);
        mBtnStop.setEnabled(false);
        mSubscription.unsubscribe();
        UtilSP.getSPSetting(this).edit().putLong(UtilSP.EVENT_START_TIME,-1).apply();
        UtilSP.getSPSetting(this).edit().putString(UtilSP.EVENT_TITLE,TITLE_DEFAULT).apply();
        mStartTime = null;
    }

    @OnClick(R.id.btn_ma_history)
    void onClickHistory(){
        startActivity(new Intent(this, HistoryActivity.class));
    }


    @OnClick(R.id.btn_ma_settings)
    void onClickSettings(){
        startActivity(new Intent(this, UIExpandableLvActivity.class));
    }

    @OnClick(R.id.btn_ma_login)
    void onClickLogin(){
        startActivity(new Intent(this, SyncActivity.class));
    }

    @OnClick(R.id.btn_ma_static)
    void onClickStatic(){
        startActivity(new Intent(this, StaticActivity.class));
    }

    @OnClick(R.id.btn_ma_my_static)
    void onClickMyStatic(){
        startActivity(new Intent(this, MyStaticActivity.class));
    }
}
