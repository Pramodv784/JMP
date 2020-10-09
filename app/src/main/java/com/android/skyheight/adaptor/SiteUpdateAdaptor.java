package com.android.skyheight.adaptor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.skyheight.R;
import com.android.skyheight.activity.AddPartnerSiteActivity;
import com.android.skyheight.model.AddressModel;
import com.android.skyheight.model.SiteListModel;
import com.android.skyheight.utils.Prefrence;
import com.android.skyheight.utils.SiteUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SiteUpdateAdaptor extends RecyclerView.Adapter<SiteUpdateAdaptor.ViewHolder>{

    private ArrayList<SiteListModel> siteListModels;
    private Context context;
    List<SiteListModel> list;
    String useName;
    Prefrence youprefrence;
    ArrayList<AddressModel> address = new ArrayList<AddressModel>();
public SiteUpdateAdaptor(Context context,ArrayList<SiteListModel> siteListModels,List<SiteListModel> list){
    this.context = context;
    this.siteListModels=siteListModels;
    this.list=list;
}
    @NonNull
    @Override
    public SiteUpdateAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.sitelistdelete_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SiteUpdateAdaptor.ViewHolder holder, int position) {
        final SiteListModel siteModel=list.get(position);
        youprefrence=Prefrence.getInstance(context);
        holder.site_size.setText(siteModel.getArea());
        holder.site_owner.setText(useName);
        holder.site_name.setText( siteModel.getName());
        if(siteModel.getSite_location()!=null){
            holder.site_address.setText(siteModel.site_location.getAddress());
        }
        else {
            holder.site_address.setText("");
        }
        //holder.site_image.setImageResource(Integer.parseInt(mylist.getImage()));
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youprefrence.saveData(SiteUtils.FILEPATH,siteModel.getFile());
                Intent intent = new Intent(context, AddPartnerSiteActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST", (Serializable) siteModel);
                intent.putExtra("BUNDLE", args);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView site_image;
        public TextView site_name;
        public TextView site_size;
        public TextView site_owner;
        public TextView site_address;
        public LinearLayout linear;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.site_image = (ImageView) itemView.findViewById(R.id.image);
            this.site_name = (TextView) itemView.findViewById(R.id.name);
            this.site_address = (TextView) itemView.findViewById(R.id.site_address);
            this.site_owner = (TextView) itemView.findViewById(R.id.owner1);
            this.site_size = (TextView) itemView.findViewById(R.id.size);
            this.linear=(LinearLayout)itemView.findViewById(R.id.linear);
        }
    }
}
