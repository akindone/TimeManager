package com.timer.jike.timemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.timer.jike.timemanager.R;
import com.timer.jike.timemanager.bean.DaoSession;
import com.timer.jike.timemanager.bean.Event;
import com.timer.jike.timemanager.bean.EventDao;
import com.timer.jike.timemanager.service.AutoLockService;
import com.timer.jike.timemanager.utils.UtilDB;
import com.timer.jike.timemanager.utils.UtilDialog;
import com.timer.jike.timemanager.utils.UtilLog;
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
    private static final String TITLE = "标签";


    private static final String LOCK_TAG = null;
    @BindView(R.id.tv_ma_title) TextView mTvTitle;
    @BindView(R.id.tv_ma_duration) TextView mTvDuration;
    @BindView(R.id.btn_ma_start) Button mBtnStart;
    @BindView(R.id.btn_ma_stop) Button mBtnStop;
    @BindView(R.id.btn_ma_history) Button mBtnHistory;
    @BindView(R.id.btn_ma_settings) Button mBtnSettings;

    private Date mStartTime;
    private EventDao mEventDao;
    private boolean isStop;
    private Subscription mSubscription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getLocalClassName();

//        WindowManager.LayoutParams
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
//                WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock(LOCK_TAG);
//        keyguardLock.disableKeyguard();
        startService(new Intent(this, AutoLockService.class));

        // get the note DAO
        DaoSession daoSession = UtilDB.getDaoSession();
        mEventDao = UtilDB.getEventDao();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        UtilLog.d(TAG, "onWindowFocusChanged", hasFocus);
        if (!hasFocus) {
            Intent intent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            intent.putExtra("reason", "globalactions");//可避免关机对话框被关闭
            sendBroadcast(intent);
        }
    }

    @OnClick(R.id.tv_ma_title)
    void onClickTitle() {
        UtilDialog.inputDialog(this, TITLE, null, null, null, (dialog, input) -> {
            if (!TextUtils.isEmpty(input))
                mTvTitle.setText(input);
        });
    }

    @OnClick(R.id.btn_ma_start)
    void onClickStart() {
        isStop = false;
        mTvDuration.setText(UtilString.getTimeSpan(0));
        mStartTime = new Date();
        mBtnStart.setEnabled(false);
        mBtnStop.setEnabled(true);
        mSubscription = Observable
                .interval(1, TimeUnit.SECONDS)
                .takeUntil(aLong -> isStop)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    UtilLog.d(TAG, "interval", aLong);
                    mTvDuration.setText(UtilString.getTimeSpan(aLong+1));
                });


    }

    @OnClick(R.id.btn_ma_stop)
    void onClickStop() {
        isStop = true;
        Date stopTime = new Date();
        stopTime.setTime(new Date().getTime()+1000*300);
        Event event = new Event();
        event.setBegin_time(mStartTime);
        event.setTitle(mTvTitle.getText().toString());
        event.setEnd_time(stopTime);
        event.setDuration(stopTime.getTime() - mStartTime.getTime());

        mEventDao.insert(event);
        UtilLog.d(TAG, "insert", event.getId());
        mBtnStart.setEnabled(true);
        mBtnStop.setEnabled(false);

        mTvDuration.setText("0");
        mSubscription.unsubscribe();

    }

    @OnClick(R.id.btn_ma_history)
    void onClickHistory(){
        startActivity(new Intent(this, HistoryActivity.class));
    }


    @OnClick(R.id.btn_ma_settings)
    void onClickSettings(){
        startActivity(new Intent(this, HistoryActivity.class));
    }
}
