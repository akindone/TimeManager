package com.timer.jike.timemanager.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.timer.jike.timemanager.activity.MainActivity;
import com.timer.jike.timemanager.utils.UtilLog;

public class AutoLockService extends Service {

	private static String TAG = "AutoLockService";
	private Intent zdLockIntent = null ;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void onCreate(){
		super.onCreate();
		zdLockIntent = new Intent(AutoLockService.this , MainActivity.class);
		zdLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		/*注册广播*/
		IntentFilter mScreenChangeFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		mScreenChangeFilter.addAction(Intent.ACTION_SCREEN_OFF);
		AutoLockService.this.registerReceiver(mScreenChangeReceiver, mScreenChangeFilter);

	}

	public int onStartCommand(Intent intent , int flags , int startId){
		return super.onStartCommand(intent, flags, startId);
		//return Service.START_STICKY;
		
	}
	
	public void onDestroy(){
		super.onDestroy();
		AutoLockService.this.unregisterReceiver(mScreenChangeReceiver);
		//在此重新启动
		startService(new Intent(AutoLockService.this, AutoLockService.class));
	}


	//屏幕变暗/变亮的广播 ， 我们要调用KeyguardManager类相应方法去解除屏幕锁定
	private BroadcastReceiver mScreenChangeReceiver = new BroadcastReceiver(){
		@SuppressWarnings("deprecation")
		@Override
		public void onReceive(Context context , Intent intent) {
			String action = intent.getAction() ;
		    UtilLog.d(TAG, intent.toString());
		    
			if(action.equals(Intent.ACTION_SCREEN_OFF) || action.equals(Intent.ACTION_SCREEN_ON) ){
				KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
				KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("zdLock 1");
				keyguardLock.disableKeyguard();
				startActivity(zdLockIntent);
			}
		}
		
	};
	
}
