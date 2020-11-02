package com.android.skyheight.adaptor;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.skyheight.R;
import com.android.skyheight.activity.UserSiteDetailActivity;
import com.android.skyheight.model.AddressModel;
import com.android.skyheight.model.SiteListModel;
import com.android.skyheight.model.UserDetail;
import com.android.skyheight.utils.Prefrence;
import com.android.skyheight.utils.SiteUtils;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dagger.multibindings.ElementsIntoSet;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class SiteListAdaptor extends RecyclerView.Adapter<SiteListAdaptor.ViewHolder> {
    private ArrayList<SiteListModel> siteListModels;
    private Context context;
    List<SiteListModel> list;
    ArrayList<AddressModel> address = new ArrayList<AddressModel>();
    //ArrayList<UserDetail> owner = new ArrayList<UserDetail>();
    ArrayList<SiteListModel> sitlist= new ArrayList<SiteListModel>();
    private SiteListModel siteListModel;
    String useName;
    Prefrence yourprefrence;
    private String owner;
   UserDetail owner3;
    public SiteListAdaptor( Context context,ArrayList<SiteListModel> siteListModels,List<SiteListModel> list) {
        this.context = context;
        this.siteListModels=siteListModels;
        this.list=list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.site_design_list, parent, false);
        SiteListAdaptor.ViewHolder viewHolder = new SiteListAdaptor.ViewHolder(listItem);
        return viewHolder;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SiteListModel siteModel=list.get(position);
       yourprefrence=Prefrence.getInstance(context);


        Log.e(TAG, "data>>>>>: "+useName );
        if (siteModel.getImage()==null || siteModel.getImage().equals("")){
            holder.site_image.setImageResource(R.drawable.plotimage2);

        }
        else {
            Picasso.get().load(siteModel.getImage()).into(holder.site_image);

        }
        holder.site_size.setText(siteModel.getArea()+" sq/ft");
        if (siteModel.getOwner()==null)
        {

        }
        else {
            String upperownername = siteModel.getOwner().getUsername().substring(0, 1).toUpperCase() + siteModel.getOwner().getUsername().substring(1).toLowerCase();
            holder.site_owner.setText(upperownername);
        }


        String upperString = siteModel.getName().substring(0, 1).toUpperCase() + siteModel.getName().substring(1).toLowerCase();
        holder.site_name.setText(upperString);
      if(siteModel.getSite_location()!=null){
          holder.site_address.setText(siteModel.site_location.getAddress());
      }
      else {
          holder.site_address.setText("");
      }

        //holder.site_image.setImageResource(Integer.parseInt(mylist.getImage()));

        holder.site_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (siteModel.getFile()!=null)
                {
                    yourprefrence.saveData(SiteUtils.FILEPATH,siteModel.getFile());
                }
                yourprefrence.saveData(SiteUtils.NAME,siteModel.getName());
                yourprefrence.saveData(SiteUtils.ID,siteModel.getid());
                Intent intent = new Intent(context, UserSiteDetailActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST", (Serializable) siteModel);
                intent.putExtra("BUNDLE", args);
                context.startActivity(intent);
            }
        });
      holder.siteview.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (siteModel.getFile()!=null)
              {
                  yourprefrence.saveData(SiteUtils.FILEPATH,siteModel.getFile());
              }
              Intent intent = new Intent(context, UserSiteDetailActivity.class);
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
         public RelativeLayout relativeLayout;
         public Button siteview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.site_image = (ImageView) itemView.findViewById(R.id.image);
            this.site_name = (TextView) itemView.findViewById(R.id.name);
            this.site_address = (TextView) itemView.findViewById(R.id.site_address);
            this.site_owner = (TextView) itemView.findViewById(R.id.owner2);
            this.site_size = (TextView) itemView.findViewById(R.id.size);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative);
            this.siteview=(Button)itemView.findViewById(R.id.siteview);

        }
    }
}