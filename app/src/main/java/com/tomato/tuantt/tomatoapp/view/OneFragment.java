package com.tomato.tuantt.tomatoapp.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.adapter.ViewSubPagerAdapter;
import com.tomato.tuantt.tomatoapp.model.Services.DataNoteOne;
import com.tomato.tuantt.tomatoapp.model.Services.ServiceNoteOne;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OneFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewSubPagerAdapter adapter;
    private View view;
    private DataNoteOne dataNoteOne;

    public OneFragment() {
        // Required empty public constructor
    }

    public static OneFragment newInstance(DataNoteOne dataNoteOne) {
        OneFragment fragment = new OneFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("KEY_POSITION_CATEGORY", (Serializable) dataNoteOne);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_one, container, false);
        setupChildViewPager();
        getDataFromBundle();
        setActionForView();
        return view;
    }

    private void setupChildViewPager() {
        adapter = new ViewSubPagerAdapter(getChildFragmentManager());
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void getDataFromBundle() {
        try {
            dataNoteOne = (DataNoteOne) getArguments().getSerializable("KEY_POSITION_CATEGORY");
            adapter.addListServiceChildData(dataNoteOne.serviceNoteTwo.listService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setActionForView() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // todo  get object .
                adapter.reloadDataDetails(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
