package com.tomato.tuantt.tomatoapp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.view.ListHistoryFragment;

public class HistoryPagerAdapter extends FragmentStatePagerAdapter {

    private static final int COUNT = 2;

    public static final int TAB_TODO = 0;

    public static final int TAB_DONE = 1;

    private Context mContext;

    public HistoryPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return ListHistoryFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mContext != null) {
            switch (position) {
                case TAB_TODO:
                    return mContext.getString(R.string.title_waite);
                case TAB_DONE:
                    return mContext.getString(R.string.title_done);
                default:
                    break;
            }
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return COUNT;
    }
}
