package com.tomato.tuantt.tomatoapp.createorder;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.adapter.RecyclerViewPackageAdapter;
import com.tomato.tuantt.tomatoapp.helper.GridSpacingItemDecoration;
import com.tomato.tuantt.tomatoapp.model.Package;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anh Nguyen on 10/11/2018.
 */
public class ViewTwo extends LinearLayout {
    public ViewTwo(Context context) {
        super(context);
        init();
    }

    public ViewTwo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    List<Package> lstPackage;
    RecyclerView mrc;
    RecyclerViewPackageAdapter myAdapter;
    private String title;
    private ChangePackageListener listener;
    public ViewTwo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_detail,this);
        lstPackage = new ArrayList<>();
        mrc = (RecyclerView) view.findViewById(R.id.recyclerview_id);
        mrc.setLayoutManager(new GridLayoutManager(getContext(), 2));
        int spacing = 40; // 50px
        boolean includeEdge = false;
        mrc.addItemDecoration(new GridSpacingItemDecoration(2, spacing, includeEdge));
    }

    public void setData(ChangePackageListener listener,String title,String arrayJsonString) {
        this.title = title;
        try {
            myAdapter = new RecyclerViewPackageAdapter(getContext(), lstPackage,listener);
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
    }

    public String getTitle() {
        return title;
    }
}
