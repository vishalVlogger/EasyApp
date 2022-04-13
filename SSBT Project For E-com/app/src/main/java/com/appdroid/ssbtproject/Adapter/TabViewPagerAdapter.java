package com.appdroid.ssbtproject.Adapter;

import com.appdroid.ssbtproject.Fragment.ViewPager1Fragment;
import com.appdroid.ssbtproject.Fragment.ViewPager2Fragment;
import com.appdroid.ssbtproject.Fragment.ViewPager3Fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabViewPagerAdapter extends FragmentStatePagerAdapter {

    public TabViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ViewPager1Fragment tab1 = new ViewPager1Fragment();
                return tab1;

            case 1:
                ViewPager2Fragment tab2 = new ViewPager2Fragment();
                return tab2;

            case 2:
                ViewPager3Fragment tab3 = new ViewPager3Fragment();
                return tab3;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
