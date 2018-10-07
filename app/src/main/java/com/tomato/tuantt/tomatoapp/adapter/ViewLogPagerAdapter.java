package com.tomato.tuantt.tomatoapp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewLogPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentLogList = new ArrayList<>();
    private final List<String> mFragmentTitleLogList = new ArrayList<>();

    public ViewLogPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        Fragment fragment = mFragmentLogList.get(position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mFragmentLogList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentLogList.add(fragment);
        mFragmentTitleLogList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleLogList.get(position);
    }
}