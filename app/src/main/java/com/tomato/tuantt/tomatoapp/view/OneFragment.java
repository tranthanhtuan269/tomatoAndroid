package com.tomato.tuantt.tomatoapp.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.adapter.ViewSubPagerAdapter;
import com.tomato.tuantt.tomatoapp.model.Package;
import com.tomato.tuantt.tomatoapp.model.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OneFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewSubPagerAdapter adapter;
    private List<Service> lstService;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        String arrayJsonString = getArguments().getString("jsonData");

        adapter = new ViewSubPagerAdapter(getChildFragmentManager());
        try {
            JSONObject jsonObject = new JSONObject(arrayJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                int id = jo.getInt("id");
                String name = jo.getString("name");
                adapter.addFrag(new DetailFragment(), name, jo.getJSONArray("packages"));
            }
            viewPager.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setActionForView();

        return view;
    }

    private void setActionForView(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // todo  get object .
//                adapter.reloadDataDetails(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
