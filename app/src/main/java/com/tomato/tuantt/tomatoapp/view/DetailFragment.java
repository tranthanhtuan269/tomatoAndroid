package com.tomato.tuantt.tomatoapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.adapter.RecyclerViewPackageAdapter;
import com.tomato.tuantt.tomatoapp.model.Services.DataNoteTwo;
import com.tomato.tuantt.tomatoapp.model.Services.Package;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {

    private List<Package> lstPackage = new ArrayList<>();
    private RecyclerView mrc;
    private RecyclerViewPackageAdapter myAdapter;

    public DetailFragment() {
        // Required empty public constructor
    }

    private boolean isReload = false;
    private Package mPackages = new Package();

    private DataNoteTwo dataNoteTwo;
    private View view;

    public static DetailFragment newInstance(DataNoteTwo dataNoteTwo, boolean isReload) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("KEY_DATA_TWO_PAGE", dataNoteTwo);
        bundle.putBoolean("KEY_RELOAD_DATA", isReload);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isReload = getArguments().getBoolean("KEY_POSITION_PAGE");

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail, container, false);
        setupRecyclerView();
        if (!isReload) {
            getData();
        }
        return view;
    }

    private void getData() {
        dataNoteTwo = (DataNoteTwo) getArguments().getSerializable("KEY_DATA_TWO_PAGE");
        lstPackage.clear();
        lstPackage.addAll(dataNoteTwo.listPackage);
        myAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {
        mrc = (RecyclerView) view.findViewById(R.id.recyclerview_id);
        myAdapter = new RecyclerViewPackageAdapter(getContext(), lstPackage);
        mrc.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mrc.setAdapter(myAdapter);
    }

    public void reloadData(DataNoteTwo dataNoteTwo) {
        lstPackage.clear();
        lstPackage.addAll(dataNoteTwo.listPackage);
        myAdapter.notifyDataSetChanged();
    }
}