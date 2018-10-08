package com.tomato.tuantt.tomatoapp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tomato.tuantt.tomatoapp.model.Services.DataNoteOne;
import com.tomato.tuantt.tomatoapp.model.Services.DataNoteTwo;
import com.tomato.tuantt.tomatoapp.view.DetailFragment;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewSubPagerAdapter extends FragmentPagerAdapter {
    private Map<Integer, DetailFragment> mapFragment  = new HashMap<>( );
    private List<DataNoteTwo> listServiceData = new ArrayList<>();

    public ViewSubPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    public void addListServiceChildData(List<DataNoteTwo> listServiceData){
        this.listServiceData.clear();
        this.listServiceData.addAll(listServiceData);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        if (mapFragment.get( position) == null){
            mapFragment.put(position, DetailFragment.newInstance(listServiceData.get(position), position != 0));
        }
        return mapFragment.get(position);
    }

    @Override
    public int getCount() {
        return listServiceData.size();
    }


    public void reloadDataDetails(int position){
        if (mapFragment.get(position) != null){
            mapFragment.get(position).reloadData(listServiceData.get(position));
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listServiceData.get(position).nameService;
    }
}