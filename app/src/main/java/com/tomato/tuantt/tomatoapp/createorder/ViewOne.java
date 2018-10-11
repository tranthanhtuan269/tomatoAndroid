package com.tomato.tuantt.tomatoapp.createorder;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tomato.tuantt.tomatoapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anh Nguyen on 10/11/2018.
 */
public class ViewOne extends ConstraintLayout {
    public ViewOne(Context context) {
        super(context);
        init();
    }

    public ViewOne(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewOne(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewTwoAdapter adapter;
    private String title;
    private void init(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_one,this);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
    }

    public void setData(String arrayJsonString,String title,ChangePackageListener listener){
        this.title = title;
        adapter = new ViewTwoAdapter();
        try {
            JSONObject jsonObject = new JSONObject(arrayJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                int id = jo.getInt("id");
                String name = jo.getString("name");
                ViewTwo two = new ViewTwo(getContext());
                two.setData(listener,name, jo.getJSONArray("packages").toString());
                adapter.addView(two);
            }
            viewPager.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tabLayout.setupWithViewPager(viewPager);

        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.separator));
            drawable.setSize(2, 1);
            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }
    }

    public void resetData(int id) {
        for (ViewTwo t : adapter.getViewTwos()) {
            t.resetData(id);
        }
    }

    public String getName() {
        return title;
    }
}
