package com.android.skyheight.adaptor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.skyheight.R;
import com.android.skyheight.activity.PlotDetailActivity;
import com.android.skyheight.model.PlotListModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlotAdaptor extends RecyclerView.Adapter<PlotAdaptor.ViewHolder> {
    private Context context;
    private ArrayList<PlotListModel> plotlist;
    private List<PlotListModel> sitelist;
    PlotListModel[] lists;

    public PlotAdaptor(Context context,List<PlotListModel> sitelist,
                       ArrayList<PlotListModel> plotlist){
        this.sitelist =sitelist;
        this.context=context;
        this.plotlist=plotlist;


    }

    @NonNull
    @Override
    public PlotAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.plotlistview, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlotAdaptor.ViewHolder holder, int position) {
        final PlotListModel mylist=plotlist.get(position);

        holder.plot_number.setText(mylist.getPlot_number());
        if (mylist.getStatus().equals(false)){
            holder.linear.setBackgroundColor(Color.parseColor("#BEBCBC"));
            holder.plot_number.setTextColor(Color.parseColor("#ffffff"));


        }
        else {
            holder.linear.setBackgroundColor(Color.parseColor("#ffffff"));
        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlotDetailActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST", (Serializable) mylist);
                intent.putExtra("BUNDLE", args);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sitelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView plot_number;
        public TextView status;
        public FrameLayout cardView;
        public LinearLayout linear;
        public TextView description;
        public TextView size;
        public CardView cardView2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.plot_number=(TextView)itemView.findViewById(R.id.plot_number);
            this.description=(TextView)itemView.findViewById(R.id.plot_description);
            this.size=(TextView)itemView.findViewById(R.id.plot_size);
            this.cardView =(FrameLayout) itemView.findViewById(R.id.cardview);
            this.linear =(LinearLayout) itemView.findViewById(R.id.linear);
            this.cardView2 =(CardView) itemView.findViewById(R.id.cardview2);
        }
    }
}
