package com.appdroid.ssbtproject.OTP;

public interface OtpReceivedInterface {
    void onOtpReceived(String otp);
    void onOtpTimeout();
}
