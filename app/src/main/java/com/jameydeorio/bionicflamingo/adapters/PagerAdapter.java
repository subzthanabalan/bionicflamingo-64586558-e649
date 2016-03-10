package com.jameydeorio.bionicflamingo.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jameydeorio.bionicflamingo.ui.fragments.LibraryFragment;
import com.jameydeorio.bionicflamingo.ui.fragments.YourQueueFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumTabs;

    public PagerAdapter(FragmentManager fragmentManager, int numTabs) {
        super(fragmentManager);
        mNumTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new YourQueueFragment();
            case 1:
                return new LibraryFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mNumTabs;
    }
}
