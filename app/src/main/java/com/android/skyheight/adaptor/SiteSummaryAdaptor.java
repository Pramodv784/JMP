package com.android.skyheight.adaptor;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.skyheight.R;
import com.android.skyheight.activity.SiteSumaaryActivity;
import com.android.skyheight.model.AddressModel;
import com.android.skyheight.model.SiteListModel;
import com.android.skyheight.model.SiteSummaryModel;
import com.android.skyheight.model.UserDetail;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class SiteSummaryAdaptor extends RecyclerView.Adapter<SiteSummaryAdaptor.ViewHolder> {
    private ArrayList<SiteSummaryModel> siteSummaryModels;
    private Context context;
    List<SiteSummaryModel> list;
    ArrayList<AddressModel> address = new ArrayList<AddressModel>();
    //ArrayList<UserDetail> owner = new ArrayList<UserDetail>();
    ArrayList<SiteListModel> sitlist= new ArrayList<SiteListModel>();
    private SiteListModel siteListModel;
    String useName;
    private String owner;
   UserDetail owner3;
    public SiteSummaryAdaptor(Context context, ArrayList<SiteSummaryModel> siteSummaryModels, List<SiteSummaryModel> list) {
        this.context = context;
        this.siteSummaryModels=siteSummaryModels;
        this.list=list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.summarylist, parent, false);
        SiteSummaryAdaptor.ViewHolder viewHolder = new SiteSummaryAdaptor.ViewHolder(listItem);
        return viewHolder;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SiteSummaryModel sitesummarylist=list.get(position);
        String date=sitesummarylist.getCreatedAt();
        SimpleDateFormat inputformat= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        try {
            Date parsedDate = inputformat.parse(date);
            String formattedDate = outputFormat.format(parsedDate);
            holder.time.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SiteSumaaryActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST", (Serializable) sitesummarylist);
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
        public TextView time;
        public RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.time = (TextView) itemView.findViewById(R.id.textview);
           this.relativeLayout=(RelativeLayout)itemView.findViewById(R.id.relative);

        }
    }
}