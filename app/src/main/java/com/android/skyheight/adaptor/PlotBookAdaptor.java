package com.android.skyheight.adaptor;

import android.content.Context;
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
import com.android.skyheight.activity.BookingActivity;
import com.android.skyheight.model.PlotListModel;

import java.util.ArrayList;
import java.util.List;

public class PlotBookAdaptor extends RecyclerView.Adapter<PlotBookAdaptor.ViewHolder> {
    private Context context;
    private ArrayList<PlotListModel> plotlist;
    private List<PlotListModel> sitelist;
    PlotListModel[] lists;
    private static int lastClickedPosition = -1;
    private int selectedItem =-1;
    BookingActivity bookingActivity;

    public PlotBookAdaptor(Context context, List<PlotListModel> sitelist,
                           ArrayList<PlotListModel> plotlist){
        this.sitelist =sitelist;
        this.context=context;
        this.plotlist=plotlist;


    }

    @NonNull
    @Override
    public PlotBookAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.plotlistview, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem,bookingActivity);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PlotBookAdaptor.ViewHolder holder, final int position) {
        final PlotListModel mylist=plotlist.get(position);
        holder.itemView.setTag(mylist.getPlot_number());
        holder.plot_number.setText(mylist.getPlot_number());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

    @Override
    public int getItemCount() {
        return    sitelist == null ? 0:  sitelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView plot_number;
        public TextView status;
        public FrameLayout cardView;
        public LinearLayout linear;
        public TextView description;
        public TextView size;
        public CardView cardView2;
        public ViewHolder(@NonNull View itemView,BookingActivity bookingActivity) {
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


