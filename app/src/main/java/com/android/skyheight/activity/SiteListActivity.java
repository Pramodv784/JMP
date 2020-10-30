package com.android.skyheight.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.skyheight.R;
import com.android.skyheight.adaptor.DeleteListAdaptor;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.DeleteModel;
import com.android.skyheight.model.SiteListModel;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Prefrence yourprefrence;
    DeleteListAdaptor deleteListAdaptor;
    ProgressBar progressBar;
    String owner;
    SwipeRefreshLayout refreshLayout;
    ArrayList<SiteListModel> sitelist = new ArrayList<SiteListModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_list);
        yourprefrence = Prefrence.getInstance(SiteListActivity.this);
        recyclerView = findViewById(R.id.recycle4);
        progressBar=findViewById(R.id.progressbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        refreshLayout=findViewById(R.id.refresh);
        getSupportActionBar().setTitle("Site");
        getSupportActionBar().setSubtitle("Swipe left to delete");
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        siteList();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                siteList();
                refreshLayout.setRefreshing(false);
            }
        });
    }
    private void siteList() {
        Call<ArrayList<SiteListModel>> userResponseCall = ApiClient.getUserService()
                .sitelist("Bearer "+yourprefrence.getData(ConstantClass.TOKEN));
        userResponseCall.enqueue(new Callback<ArrayList<SiteListModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SiteListModel>> call, Response<ArrayList<SiteListModel>> response) {
                if (response.code()==200){
                     if (!response.body().isEmpty()) {
                         progressBar.setVisibility(View.GONE);
                         //Log.i( "onResponse: ",response.body().toString() );
                         sitelist = response.body();
                         deleteListAdaptor = new DeleteListAdaptor(SiteListActivity.this, response.body(), sitelist);
                         recyclerView.setAdapter(deleteListAdaptor);
                         ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                         itemTouchHelper.attachToRecyclerView(recyclerView);
                     }
                     else {
                         progressBar.setVisibility(View.GONE);
                         recyclerView.setBackgroundResource(R.drawable.we);
                     }
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"List Failed ",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<SiteListModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e( "onResponse: ","failed"+t);
                Toast.makeText(getApplicationContext(),"Check Your Internet Connection ",Toast.LENGTH_SHORT).show();
            }
        });
    }
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            deleteListAdaptor.notifyItemRemoved(position);
            deleteListAdaptor.notifyDataSetChanged();
            String id =sitelist.get(position).getid();
           /* sitelist.remove(position);
            deleteListAdaptor.notifyDataSetChanged();*/
            showCustomDialog(id,position);
        }
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(SiteListActivity.this,R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.delete_icon)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
    private void showCustomDialog(final String id,final int position) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.delete_dialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        Button okbtn= dialogView.findViewById(R.id.buttonOk);
        Button cancel =dialogView.findViewById(R.id.cancel);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setContentView(R.layout.delete_dialog);
        alertDialog.show();
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletesite(id);
                sitelist.remove(position);
                deleteListAdaptor.notifyDataSetChanged();
                alertDialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                deleteListAdaptor.notifyDataSetChanged();
            }
        });
    }
   private void deletesite(String id) {
        Call<DeleteModel> userResponse =ApiClient.getUserService()
                .deletesite("Bearer "+yourprefrence.getData(ConstantClass.TOKEN),id);
        userResponse.enqueue(new Callback<DeleteModel>() {
            @Override
            public void onResponse(Call<DeleteModel> call, Response<DeleteModel> response) {
                if (response.code()==204){
                    Toast.makeText(getApplicationContext(),"Delete Sucessfully",Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(SiteListActivity.this, SiteListActivity.class));
                }
                else {
                    Toast.makeText(getApplicationContext(),"Failed to delete",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<DeleteModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }
}