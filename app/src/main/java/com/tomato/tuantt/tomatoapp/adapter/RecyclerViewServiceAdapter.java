package com.tomato.tuantt.tomatoapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.activity.ServiceActivity;
import com.tomato.tuantt.tomatoapp.createorder.OrderWorking;
import com.tomato.tuantt.tomatoapp.model.Service;

import java.util.List;

public class RecyclerViewServiceAdapter extends RecyclerView.Adapter<RecyclerViewServiceAdapter.MyViewServiceHolder>{

    private Context mContext;
    private List<Service> mData;
    private String defaultUrlImage = "http://api.timtruyen.online/public/images/";

    public RecyclerViewServiceAdapter(Context mContext, List<Service> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewServiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_service, parent, false);
        return new MyViewServiceHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewServiceHolder holder, final int position) {

        holder.tv_service_name.setText(mData.get(position).getName());
        Picasso.with(mContext).load(defaultUrlImage + mData.get(position).getIcon()).fit().centerInside().into(holder.img_service_thumbnail);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, ServiceActivity.class);
//                intent.putExtra("ServiceId", mData.get(position).getId());
//                intent.putExtra("ServiceName", mData.get(position).getName());
//                intent.putExtra("ServiceThumbnail", mData.get(position).getIcon());
                OrderWorking.currentService = mData.get(position).getName();
                Intent intent = ServiceActivity.createIntent(mContext, mData.get(position).getId(),mData.get(position).getName(),true);
                mContext.startActivity(intent);
            }
        });
        // set onclick
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewServiceHolder extends RecyclerView.ViewHolder{

        TextView tv_service_name;
        ImageView img_service_thumbnail;
        CardView cardView;

        public MyViewServiceHolder(View itemView) {
            super(itemView);

            tv_service_name = (TextView) itemView.findViewById(R.id.service_name_id);
            img_service_thumbnail = (ImageView) itemView.findViewById(R.id.service_img_id);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
        }
    }


}
