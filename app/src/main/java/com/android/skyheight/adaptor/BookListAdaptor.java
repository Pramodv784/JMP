package com.android.skyheight.adaptor;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.skyheight.R;
import com.android.skyheight.activity.BookedPrintActivity;
import com.android.skyheight.activity.PlotSumaryActivity;
import com.android.skyheight.model.AddressModel;
import com.android.skyheight.model.BookingListModel;
import com.android.skyheight.model.BookingModel;
import com.android.skyheight.model.PlotDataModel;
import com.android.skyheight.model.PlotSummaryModel;
import com.android.skyheight.model.SiteListModel;
import com.android.skyheight.model.UserDetail;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

public class BookListAdaptor extends RecyclerView.Adapter<BookListAdaptor.ViewHolder> {
    private ArrayList<BookingListModel> bookingmodel;
    private Context context;
    List<BookingListModel> list;

    ArrayList<SiteListModel> sitlist= new ArrayList<SiteListModel>();
    private SiteListModel siteListModel;
    String useName;
    private String owner;
   UserDetail owner3;
    public BookListAdaptor(Context context, ArrayList<BookingListModel> bookingmodel, List<BookingListModel> list) {
        this.context = context;
        this.bookingmodel=bookingmodel;
        this.list=list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.customerlist, parent, false);
        BookListAdaptor.ViewHolder viewHolder = new BookListAdaptor.ViewHolder(listItem);
        return viewHolder;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final BookingListModel bookinglist=list.get(position);


            holder.user_name.setText(bookinglist.getBuyer().getUsername());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookedPrintActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST", (Serializable) bookinglist);
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
        public ImageView user_image;
        public TextView user_name;
        public RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.user_image=(ImageView)itemView.findViewById(R.id.user_image);
            this.user_name=(TextView)itemView.findViewById(R.id.username2);
            this.relativeLayout=(RelativeLayout)itemView.findViewById(R.id.relative2);

        }
    }
}