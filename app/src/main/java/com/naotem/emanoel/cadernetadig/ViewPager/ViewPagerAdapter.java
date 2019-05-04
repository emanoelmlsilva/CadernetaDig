package com.naotem.emanoel.cadernetadig.ViewPager;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> listFragment;
    private final List<String> listTitle;


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.listFragment = new ArrayList<>();
        this.listTitle = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {

       return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return this.listTitle.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return this.listTitle.get(position);
    }

    public void addFragment(Fragment newFragment,String nameTab){

        this.listFragment.add(newFragment);
        this.listTitle.add(nameTab);

    }


}

