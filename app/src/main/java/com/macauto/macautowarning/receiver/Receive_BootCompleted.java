package com.macauto.macautowarning.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.util.Log;

import com.macauto.macautowarning.data.Constants;
import com.macauto.macautowarning.service.WarnFirebaseMessagingService;
import com.macauto.macautowarning.service.WarnInstanceIDService;


public class Receive_BootCompleted extends BroadcastReceiver {
    private static final String TAG = Receive_BootCompleted.class.getName();

    //static SharedPreferences pref ;
    //static SharedPreferences.Editor editor;
    //private static final String FILE_NAME = "Preference";

    @Override
    public void onReceive(Context context, Intent intent) {
        //pref = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        //String account = pref.getString("ACCOUNT", "");
        //String password = pref.getString("PASSWORD", "");

        if (intent.getAction() != null) {

            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {

                Log.e(TAG, "receive ACTION_BOOT_COMPLETED");
                //Intent serviceIntent = new Intent(context, AlarmService.class);
                //context.startService(serviceIntent);

                Log.e(TAG, "start instanceIDService");
                //alarm service
                Intent instanceIDService = new Intent(context, WarnInstanceIDService.class);
                context.startService(instanceIDService);

                Log.e(TAG, "start firebaseMessagingService");
                //alarm service
                Intent firebaseMessagingService = new Intent(context, WarnFirebaseMessagingService.class);
                context.startService(firebaseMessagingService);


            } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_START_SCM_INSTANCE_ID_ACTION)) {
                Log.e(TAG, "receive GET_START_SCM_INSTANCE_ID_ACTION");
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    if (!isMyServiceRunning(WarnInstanceIDService.class, context)) {
                        Log.e(TAG, "start ScmInstanceIDService");
                        Intent serviceIntent = new Intent(context, WarnInstanceIDService.class);
                        context.startService(serviceIntent);
                    } else {
                        Log.e(TAG, "ScmInstanceIDService is running");
                    }
                } else {
                    Log.e(TAG, "start ScmInstanceIDService");
                    Intent serviceIntent = new Intent(context, WarnInstanceIDService.class);
                    context.startService(serviceIntent);
                }
            } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_START_SCM_FIREBASE_MESSAGE_ACTION)) {
                Log.e(TAG, "receive GET_START_SCM_FIREBASE_MESSAGE_ACTION");
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    if (!isMyServiceRunning(WarnFirebaseMessagingService.class, context)) {
                        Log.e(TAG, "start ScmFirebaseMessagingService");
                        Intent serviceIntent = new Intent(context, WarnFirebaseMessagingService.class);
                        context.startService(serviceIntent);
                    } else {
                        Log.e(TAG, "ScmFirebaseMessagingService is running");
                    }
                } else {
                    Log.e(TAG, "start ScmFirebaseMessagingService");
                    Intent serviceIntent = new Intent(context, WarnFirebaseMessagingService.class);
                    context.startService(serviceIntent);
                }
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass, Context mContext) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }



        return false;
    }
}
