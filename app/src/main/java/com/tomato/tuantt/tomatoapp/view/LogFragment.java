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
import com.tomato.tuantt.tomatoapp.adapter.RecyclerViewLogAdapter;
import com.tomato.tuantt.tomatoapp.model.Log;
import com.tomato.tuantt.tomatoapp.model.Package;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LogFragment  extends Fragment {

    List<Log> lstLog;
    RecyclerView mrc;
    RecyclerViewLogAdapter myAdapter;

    public LogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        lstLog = new ArrayList<>();
        mrc = (RecyclerView) view.findViewById(R.id.recyclerview_id);
        myAdapter = new RecyclerViewLogAdapter(getContext(), lstLog);
        mrc.setLayoutManager(new GridLayoutManager(getContext(), 2));

        String arrayJsonString = getArguments().getString("jsonDataSub");

        try {
            JSONArray jsonArray = new JSONArray(arrayJsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                int id = jo.getInt("id");
                String name = jo.getString("name");
                int price = jo.getInt("price");
                String urlImage = jo.getString("image");
                lstLog.add(new Log());
            }
            mrc.setAdapter(myAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}