package com.appdroid.ssbtproject.OTP;

import android.app.Application;
import android.os.Build;


import androidx.annotation.RequiresApi;

//import com.izooto.iZooto;

public class MyApplication extends Application {//, NotificationWebViewListener {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate() {
        super.onCreate();
       // iZooto.initialize(this).build();
        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
        appSignatureHelper.getAppSignatures();


    }
}
