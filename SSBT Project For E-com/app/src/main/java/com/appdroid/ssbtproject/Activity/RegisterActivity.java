package com.appdroid.ssbtproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.appdroid.ssbtproject.R;

public class RegisterActivity extends AppCompatActivity {

    EditText password_et,password_et1;
    ImageView img_eye1, img_eye2,img_eye3,img_eye4;
    FrameLayout frame_eye,frame_eye1;
    private int passwordNotVisible = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
        ImageView img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        password_et = findViewById(R.id.et_password);
        frame_eye =  findViewById(R.id.frame_eye);
        img_eye1 =  findViewById(R.id.img_eye1);
        img_eye2 =  findViewById(R.id.img_eye2);

        password_et1 = findViewById(R.id.et_password1);
        frame_eye1 = findViewById(R.id.frame_eye1);
        img_eye3 = findViewById(R.id.img_eye3);
        img_eye4 = findViewById(R.id.img_eye4);

        frame_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordNotVisible == 1) {
                    img_eye1.setVisibility(View.VISIBLE);
                    img_eye2.setVisibility(View.GONE);
                    password_et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordNotVisible = 0;
                } else {
                    img_eye1.setVisibility(View.GONE);
                    img_eye2.setVisibility(View.VISIBLE);
                    password_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordNotVisible = 1;
                }
                password_et.setSelection(password_et.length());
            }
        });
        frame_eye1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordNotVisible == 1) {
                    img_eye3.setVisibility(View.VISIBLE);
                    img_eye4.setVisibility(View.GONE);
                    password_et1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordNotVisible = 0;
                } else {
                    img_eye3.setVisibility(View.GONE);
                    img_eye4.setVisibility(View.VISIBLE);
                    password_et1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordNotVisible = 1;
                }
                password_et1.setSelection(password_et1.length());
            }
        });

    }
}