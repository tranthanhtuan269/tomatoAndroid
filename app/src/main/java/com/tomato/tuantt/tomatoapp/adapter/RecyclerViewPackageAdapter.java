package com.tomato.tuantt.tomatoapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.model.Services.Package;

import java.util.List;

public class RecyclerViewPackageAdapter extends RecyclerView.Adapter<RecyclerViewPackageAdapter.MyViewPackageHolder>{

    private Context mContext;
    private List<Package> mData;
    private String defaultUrlImage = "http://api.timtruyen.online/public/images/";

    public RecyclerViewPackageAdapter(Context mContext, List<Package> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewPackageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_package, parent, false);
        return new MyViewPackageHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewPackageHolder holder, final int position) {

        holder.tv_package_name.setText(mData.get(position).getName());
        holder.tv_package_price.setText(mData.get(position).getPrice());
        Picasso.with(mContext).load(defaultUrlImage + mData.get(position).getIcon()).fit().centerInside().into(holder.img_pageage_thumbnail);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewPackageHolder extends RecyclerView.ViewHolder{

        TextView tv_package_name;
        TextView tv_package_price;
        ImageView img_pageage_thumbnail;
        CardView cardView;

        public MyViewPackageHolder(View itemView) {
            super(itemView);

            tv_package_name = (TextView) itemView.findViewById(R.id.package_name_id);
            tv_package_price = (TextView) itemView.findViewById(R.id.package_price);
            img_pageage_thumbnail = (ImageView) itemView.findViewById(R.id.package_img_id);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
        }
    }


}
