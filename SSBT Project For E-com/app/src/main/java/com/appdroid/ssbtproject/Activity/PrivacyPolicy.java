package com.appdroid.ssbtproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.appdroid.ssbtproject.R;

public class PrivacyPolicy extends AppCompatActivity {

    private WebView policyWebView;
    ProgressBar pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        policyWebView = (WebView)findViewById(R.id.policyWebView);
        pd = findViewById(R.id.pro);

        policyWebView.setVisibility(View.GONE);
        pd.setVisibility(View.VISIBLE);
        WebSettings webSettings=policyWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        policyWebView.loadUrl("https://www.websitepolicies.com/policies/view/eFHRdaOo");

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                policyWebView.setVisibility(View.VISIBLE);
                pd.setVisibility(View.GONE);
            }
        },1500);

    }
}