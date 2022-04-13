package com.appdroid.ssbt_delivery_boy.OTP;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;


public class MyApplication extends Application {//, NotificationWebViewListener {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate() {
        super.onCreate();

        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
        appSignatureHelper.getAppSignatures();


    }
}
