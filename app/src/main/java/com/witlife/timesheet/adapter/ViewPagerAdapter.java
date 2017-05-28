package com.witlife.timesheet.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.witlife.timesheet.activity.WeeklyJobFragment;
import com.witlife.timesheet.activity.WeeklyReportFragment;
import com.witlife.timesheet.activity.YearlyJobFragment;

/**
 * Created by yitian on 24/05/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position){
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new WeeklyJobFragment();
                break;
            case 1:
                fragment = new YearlyJobFragment();
                break;
            case 2:
                fragment = new Fragment();
                break;
            case 3:
                fragment = new WeeklyReportFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount(){
        return 4;
    }
}
