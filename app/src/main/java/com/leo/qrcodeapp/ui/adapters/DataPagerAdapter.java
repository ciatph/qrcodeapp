package com.leo.qrcodeapp.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.leo.qrcodeapp.ui.AboutFragment;
import com.leo.qrcodeapp.ui.DataMngtFragment;
import com.leo.qrcodeapp.ui.EventListFragment;

public class DataPagerAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;

    public DataPagerAdapter(FragmentManager fm, int numOfTabs, int type){
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0: {
                return new EventListFragment();
            }

            case 1: {
                return new DataMngtFragment();
            }

            case 2: {
                return new AboutFragment();
            }

            default:
                return null;
        }
    }

    @Override
    public int getCount(){
        return numOfTabs;
    }
}
