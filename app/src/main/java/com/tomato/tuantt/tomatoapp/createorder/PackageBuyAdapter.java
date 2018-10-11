package com.tomato.tuantt.tomatoapp.createorder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tomato.tuantt.tomatoapp.R;
import com.tomato.tuantt.tomatoapp.model.Package;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Anh Nguyen on 10/11/2018.
 */
public class PackageBuyAdapter extends RecyclerView.Adapter<PackageBuyAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Package> packages;
    private OnPackageRemove onPackageRemove;
    public PackageBuyAdapter(Context context,OnPackageRemove onPackageRemove) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        packages = new ArrayList<>();
        this.onPackageRemove = onPackageRemove;
    }

    public void addPackage(Package p){
        packages.add(0,p);
        notifyDataSetChanged();
    }

    public boolean hasMoreView(){
        return packages !=null && packages.size() > 3;
    }

    public void updatePackage(Package p){
        ListIterator<Package> iterator = packages.listIterator();
        while(iterator.hasNext())
        {
            Package tmp = iterator.next();
            if (tmp.getId() == p.getId()) {
                if (p.number <=0) {
                    iterator.remove();
                    if (onPackageRemove !=null) {
                        onPackageRemove.onPackageRemove(p.getId());
                    }
                }else {
                    tmp.number = p.number;
                }
                break;
            }
        }
      notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.view_package_buy,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
         Package p = packages.get(position);
        holder.tvName.setText(p.getName());
        holder.tvCount.setText("x " + p.number);
        String price = p.getPrice();
        if (price.contains(",")) {
            price = price.replace(",","");
        }
        if (price.contains(".")) {
            price = price.replace(".","");
        }
        Integer i = Integer.valueOf(price);
        int sum = i * p.number;

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        price = formatter.format(sum);
        price = price.replace(",",".");
        holder.tvPrice.setText(price +" VNĐ");
        holder.imgCancel.setTag(position);
        holder.imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                if (pos < packages.size()) {
                    Package p = packages.get(pos);
                    packages.remove(p);
                    if (onPackageRemove !=null) {
                        onPackageRemove.onPackageRemove(p.getId());
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return packages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvCount;
        TextView tvPrice;
        View imgCancel;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imgCancel = itemView.findViewById(R.id.imgCancel);
        }
    }

    public interface OnPackageRemove{
        void onPackageRemove(int id);
    }
}
