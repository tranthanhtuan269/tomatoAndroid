package com.tomato.tuantt.tomatoapp.createorder;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anh Nguyen on 10/11/2018.
 */
public class ViewTwoAdapter extends PagerAdapter {

    List<ViewTwo> viewOnes = new ArrayList<>();

    public List<ViewTwo> getViewTwos(){
        return this.viewOnes;
    }

    @Override
    public int getCount() {
        return viewOnes.size();
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
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void addView(ViewTwo viewOne){
        viewOnes.add(viewOne);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return viewOnes.get(position).getTitle();
    }
}
