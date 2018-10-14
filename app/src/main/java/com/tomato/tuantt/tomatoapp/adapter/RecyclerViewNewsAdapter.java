package com.tomato.tuantt.tomatoapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.activity.NewsDetailActivity;
import com.tomato.tuantt.tomatoapp.activity.ServiceActivity;
import com.tomato.tuantt.tomatoapp.createorder.OrderWorking;
import com.tomato.tuantt.tomatoapp.model.News;
import com.tomato.tuantt.tomatoapp.model.Service;

import java.util.List;

public class RecyclerViewNewsAdapter extends RecyclerView.Adapter<RecyclerViewNewsAdapter.MyViewNewsHolder>{

    private Context mContext;
    private List<News> mData;
    private String defaultUrlImage = "http://api.timtruyen.online/public/images/";

    public RecyclerViewNewsAdapter(Context mContext, List<News> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewNewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_news, parent, false);
        return new MyViewNewsHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewNewsHolder holder, final int position) {

        holder.title_txt.setText(mData.get(position).getTitle());
        holder.year_txt.setText(mData.get(position).getYear());

        holder.cardview_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("NewsID", mData.get(position).getId() + "");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewNewsHolder extends RecyclerView.ViewHolder{

        TextView title_txt;
        TextView year_txt;
        ConstraintLayout cardview_layout;

        public MyViewNewsHolder(View itemView) {
            super(itemView);

            title_txt = (TextView) itemView.findViewById(R.id.title);
            year_txt = (TextView) itemView.findViewById(R.id.year);
            cardview_layout = (ConstraintLayout) itemView.findViewById(R.id.cardview_id);
        }
    }


}
