
package com.timer.jike.timemanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.timer.jike.timemanager.activity.MainActivity;

public class BootBroadcastReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent lockPage = new Intent(context, MainActivity.class);
            lockPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(lockPage);
        }
    }
}


