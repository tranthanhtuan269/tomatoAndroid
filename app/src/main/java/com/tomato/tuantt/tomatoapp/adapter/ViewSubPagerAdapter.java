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
    private JSONArray mFragmentJSONObjectSub;

    private Map<Integer, DetailFragment> mapFragment  = new HashMap<>( );

    public ViewSubPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("jsonDataSub", mFragmentJSONObjectSub.toString());
        Fragment fragment = mFragmentListSub.get(position);
        if (mapFragment.get( position) == null){
            mapFragment.put(position, DetailFragment.newInstance(position, mFragmentJSONObjectSub,  position != 0));
        }
        return mapFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentListSub.size();
    }

    public void addFrag(Fragment fragment, String title, JSONArray jsonObject) {
        mFragmentListSub.add(fragment);
        mFragmentTitleListSub.add(title);
        mFragmentJSONObjectSub = jsonObject;
    }

    public void reloadDataDetails(int position){
        if (mapFragment.get(position) != null){
            //
            mapFragment.get(position).reloadData(position);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleListSub.get(position);
    }
}