package com.tomato.tuantt.tomatoapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tomato.tuantt.tomatoapp.model.Services.DataNoteOne;
import com.tomato.tuantt.tomatoapp.view.OneFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private JSONObject mFragmentJSONObject;
    private List<DataNoteOne> listCategory = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager, List<DataNoteOne> listCategory) {
        super(manager);
        this.listCategory = listCategory;
    }

    public void addListCategory(List<DataNoteOne> listCategory){
        this.listCategory.clear();
        this.listCategory.addAll(listCategory);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return OneFragment.newInstance(listCategory.get(position));
    }

    @Override
    public int getCount() {
        return listCategory.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listCategory.get(position).nameService;
    }
}