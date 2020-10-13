package com.android.skyheight.adaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.skyheight.R;
import com.android.skyheight.Interface.RecycleViewInterface;
import com.android.skyheight.model.PlotListModel;

import java.util.ArrayList;
import java.util.List;

public class PlotListAdaptor extends RecyclerView.Adapter<PlotListAdaptor.ViewHolder> {
    private Context context;
    private ArrayList<PlotListModel> plotlist;
    private List<PlotListModel> sitelist;
    private RecycleViewInterface recycleViewInterface;
    public
    int row_index=-1;
    PlotListModel[] lists;
   public PlotListAdaptor(Context context,List<PlotListModel> sitelist,
                          ArrayList<PlotListModel> plotlist,RecycleViewInterface recycleViewInterface){
       this.context=context;
       this.recycleViewInterface=recycleViewInterface;
       this.sitelist =sitelist;
       this.plotlist=plotlist;
   }

    @NonNull
    @Override
    public PlotListAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.plotlistview, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }


    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final PlotListAdaptor.ViewHolder holder, final int position) {
        final PlotListModel mylist=plotlist.get(position);

            holder.plot_number.setText(mylist.getPlot_number());



        holder.plot_number.setText(mylist.getPlot_number());
        if (mylist.getStatus().equals(false)){

            holder.linear.setBackgroundColor(Color.parseColor("#BEBCBC"));
            holder.plot_number.setTextColor(Color.parseColor("#ffffff"));

        }
        else {
            holder.linear.setBackgroundColor(Color.parseColor("#ffffff"));
        }

         if (mylist.getStatus().equals(true)){
             holder.cardView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                      row_index=position;
                     notifyDataSetChanged();
                    recycleViewInterface.onclickitem(position);
                 }
             });

             if(row_index==position){
                 holder.linear.setBackgroundColor(Color.parseColor("#3DDC84"));
                 holder.plot_number.setTextColor(Color.parseColor("#FFFFFF"));
             }
             else
             {
                 holder.linear.setBackgroundColor(Color.parseColor("#ffffff"));
                 holder.plot_number.setTextColor(Color.parseColor("#3DDC84"));
             }
         }
         else {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Plot is Booked",Toast.LENGTH_SHORT).show();
                }
            });
         }
    }
    @Override
    public int getItemCount() {
        return sitelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            data(getAdapterPosition());
        }
    }

    private void data(int adapterPosition) {
    }
}
