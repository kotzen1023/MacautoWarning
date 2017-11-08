package com.macauto.macautowarning.Service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class WarnInstanceIDService extends FirebaseInstanceIdService{
    private static final String TAG = WarnInstanceIDService.class.getName();

    public WarnInstanceIDService() {
        Log.d(TAG, "WarnInstanceIDService");
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        //sendRegistrationToServer(refreshedToken);
    }
}
