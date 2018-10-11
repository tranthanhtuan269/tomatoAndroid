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

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.createorder.ChangePackageListener;
import com.tomato.tuantt.tomatoapp.model.Package;

import java.text.DecimalFormat;
import java.util.List;

public class RecyclerViewPackageAdapter extends RecyclerView.Adapter<RecyclerViewPackageAdapter.MyViewPackageHolder>{

    private Context mContext;
    private List<Package> mData;
    private String defaultUrlImage = "http://api.timtruyen.online/public/images/";
    private ChangePackageListener listener;
    public RecyclerViewPackageAdapter(Context mContext, List<Package> mData, ChangePackageListener listener) {
        this.mContext = mContext;
        this.mData = mData;
        this.listener = listener;
    }

    @Override
    public MyViewPackageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_package, parent, false);
        return new MyViewPackageHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewPackageHolder holder, final int position) {
        Package pac = mData.get(position);
        holder.tv_package_name.setText(pac.getName());
        String price =new String(pac.getPrice());
        if (!price.contains(",")) {
            if (price.contains(".")) {
                price = price.replace(".",",");
            }else {
                DecimalFormat formatter = new DecimalFormat("#,###,###");
                try {
                    long number = Long.valueOf(price);
                    price = formatter.format(number);
                } catch (Exception e) {
                    throw new IllegalStateException("loiii :" + pac.getId() + " " + pac.getName() + " " + price + "; " + pac.getPrice());
                }

            }
        }
        price = price.replace(",",".");
        holder.tv_package_price.setText(price +" VND");
        //Glide.with(mContext).load(defaultUrlImage + mData.get(position).getIcon()).into(holder.img_pageage_thumbnail);
        Picasso.with(mContext).load(defaultUrlImage + pac.getIcon()).into(holder.img_pageage_thumbnail);

        holder.tvNumber.setText(String.valueOf(pac.number));
        holder.icMinus.setTag(position);
        holder.icPlus.setTag(position);
        holder.icPlus.setVisibility(View.VISIBLE);
        if (pac.number <=0) {
            holder.icMinus.setVisibility(View.GONE);
        }else {
            holder.icMinus.setVisibility(View.VISIBLE);
        }
        holder.icMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                Package p = mData.get(pos);
                if (p.number <=0) {
                    return;
                }
                p.number--;
                holder.tvNumber.setText(String.valueOf(p.number));
                if (p.number <=0) {
                    v.setVisibility(View.GONE);
                }else {
                    v.setVisibility(View.VISIBLE);
                }
                if (listener !=null) {
                    listener.onChange(p);
                }
            }
        });
        holder.icPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                Package p = mData.get(pos);
                p.number++;
                holder.tvNumber.setText(String.valueOf(p.number));
                holder.icMinus.setVisibility(View.VISIBLE);
                if (listener !=null) {
                    listener.onChange(p);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewPackageHolder extends RecyclerView.ViewHolder{

        TextView tv_package_name;
        TextView tv_package_price;
        ImageView img_pageage_thumbnail;
        View icMinus;
        View icPlus;
        TextView tvNumber;
        CardView cardView;

        public MyViewPackageHolder(View itemView) {
            super(itemView);

            tv_package_name = (TextView) itemView.findViewById(R.id.package_name_id);
            tv_package_price = (TextView) itemView.findViewById(R.id.package_price);
            img_pageage_thumbnail = (ImageView) itemView.findViewById(R.id.package_img_id);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
            icMinus = itemView.findViewById(R.id.ivMinus);
            icPlus = itemView.findViewById(R.id.ivPlus);
            tvNumber = itemView.findViewById(R.id.tvNumber);
        }
    }


}
