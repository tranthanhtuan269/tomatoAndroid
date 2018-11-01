package com.tomato.tuantt.tomatoapp.createorder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anh Nguyen on 10/11/2018.
 */
public class ViewOneAdapter extends PagerAdapter {

    List<ViewOne> viewOnes = new ArrayList<>();

    public List<ViewOne> getViewOnes(){
        return this.viewOnes;
    }

    @Override
    public int getCount() {
        return viewOnes.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = viewOnes.get(position);
        if (view.getParent() !=null) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            viewGroup.removeView(view);
        }
        container.addView(viewOnes.get(position));
        return view;
    }

    public void addView(ViewOne viewOne){
        viewOnes.add(viewOne);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return viewOnes.get(position).getName();
    }
}
