package com.tomato.tuantt.tomatoapp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tomato.tuantt.tomatoapp.view.DetailFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewSubPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentListSub = new ArrayList<>();
    private final List<String> mFragmentTitleListSub = new ArrayList<>();
    private List<JSONArray> jsonObjects = new ArrayList<>();

    private Map<Integer, DetailFragment> mapFragment  = new HashMap<>( );

    public ViewSubPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("jsonDataSub", jsonObjects.get(position).toString());
        Fragment fragment = mFragmentListSub.get(position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mFragmentListSub.size();
    }

    public void addFrag(Fragment fragment, String title, JSONArray jsonObject) {
        mFragmentListSub.add(fragment);
        mFragmentTitleListSub.add(title);
        jsonObjects.add(jsonObject);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleListSub.get(position);
    }
}