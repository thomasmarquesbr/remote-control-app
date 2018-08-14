package com.lapic.thomas.remote_control_app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * Created by thomasmarquesbrandaoreis on 27/09/16.
 */

public class RCViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<RCViewFragment> fragments = new ArrayList<>();
    private RCViewFragment currentFragment;

    public RCViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.clear();
        fragments.add(RCViewFragment.newInstance(0));
        fragments.add(RCViewFragment.newInstance(1));
        fragments.add(RCViewFragment.newInstance(2));
        fragments.add(RCViewFragment.newInstance(3));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = ((RCViewFragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    public RCViewFragment getCurrentFragment() {
        return currentFragment;
    }
}
