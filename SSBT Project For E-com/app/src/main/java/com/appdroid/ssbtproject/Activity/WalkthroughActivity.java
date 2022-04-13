package com.appdroid.ssbtproject.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.appdroid.ssbtproject.Adapter.TabViewPagerAdapter;
import com.appdroid.ssbtproject.R;

public class WalkthroughActivity extends AppCompatActivity {

    LinearLayout li_line1, li_line2, li_line3;
    ViewPager viewPager;
    TabViewPagerAdapter tabViewPagerAdapter;
    int CURRENTPAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
        li_line1 = findViewById(R.id.li_line1);
        li_line2 = findViewById(R.id.li_line2);
        li_line3 = findViewById(R.id.li_line3);
        viewPager = findViewById(R.id.viewPager);

        li_line1.setBackgroundResource(R.drawable.green_line);
        /*next page click listner */

        if (CURRENTPAGE < 2) {
            CURRENTPAGE++;
            viewPager.setCurrentItem(CURRENTPAGE);
            setcompletedStates(CURRENTPAGE);
            Log.e("CURRENTPAGE", CURRENTPAGE + "");

        }

        tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabViewPagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                CURRENTPAGE = position;
                setcompletedStates(CURRENTPAGE);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setcompletedStates(int CURRENTPAGE) {
        if (CURRENTPAGE == 0) {
            li_line1.setBackgroundResource(R.drawable.green_line);
            li_line2.setBackgroundResource(R.drawable.circle_unselect);
            li_line3.setBackgroundResource(R.drawable.circle_unselect);
        }
        if (CURRENTPAGE == 1) {
            li_line1.setBackgroundResource(R.drawable.circle_unselect);
            li_line2.setBackgroundResource(R.drawable.green_line);
            li_line3.setBackgroundResource(R.drawable.circle_unselect);
        }
        if (CURRENTPAGE == 2) {
            li_line1.setBackgroundResource(R.drawable.circle_unselect);
            li_line2.setBackgroundResource(R.drawable.circle_unselect);
            li_line3.setBackgroundResource(R.drawable.green_line);
        }
    }

}