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
import com.tomato.tuantt.tomatoapp.createorder.ChangePackageListener;
import com.tomato.tuantt.tomatoapp.helper.GridSpacingItemDecoration;
import com.tomato.tuantt.tomatoapp.model.Package;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment implements ChangePackageListener {

    List<Package> lstPackage;
    RecyclerView mrc;
    RecyclerViewPackageAdapter myAdapter;

    public DetailFragment() {
        // Required empty public constructor
    }

    private boolean isReload = false;
    private Package mPackages = new Package();

    public static DetailFragment newInstance(int positionPage, boolean isReload){
        DetailFragment fragment = new DetailFragment() ;
        Bundle bundle = new Bundle();
        bundle.putInt("KEY_POSITION_PAGE", positionPage);
        bundle.putBoolean("KEY_RELOAD_DATA", isReload);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        lstPackage = new ArrayList<>();
        mrc = (RecyclerView) view.findViewById(R.id.recyclerview_id);
        myAdapter = new RecyclerViewPackageAdapter(getContext(), lstPackage,this);
        mrc.setLayoutManager(new GridLayoutManager(getContext(), 2));
        int spacing = 40; // 50px
        boolean includeEdge = false;
        mrc.addItemDecoration(new GridSpacingItemDecoration(2, spacing, includeEdge));

        String arrayJsonString = getArguments().getString("jsonDataSub");

        try {
            JSONArray jsonArray = new JSONArray(arrayJsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                int id = jo.getInt("id");
                String name = jo.getString("name");
                //int price = jo.getInt("price");
                String price = jo.getString("price");
                String urlImage = jo.getString("image");
                lstPackage.add(new Package(id, name, price, urlImage));
            }
            mrc.setAdapter(myAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onChange(Package p) {

    }
}