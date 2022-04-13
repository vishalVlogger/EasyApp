package com.appdroid.ssbt_delivery_boy.OTP;

public interface OtpReceivedInterface {
    void onOtpReceived(String otp);
    void onOtpTimeout();
}
