package com.android.skyheight.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.skyheight.R;
import com.android.skyheight.adaptor.PlotListAdaptor;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.PlotListModel;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;
import com.android.skyheight.utils.SiteUtils;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class UserPlotListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<PlotListModel> siteGrid;
    Prefrence yourprefrence;
    ProgressBar progressBar;
    String site_id;
    FloatingActionButton addbtn;
    ArrayList<PlotListModel> plotList = new ArrayList<PlotListModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userplot_list);
        recyclerView = findViewById(R.id.recycle_view);
        yourprefrence = Prefrence.getInstance(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.progressbar);
        addbtn = findViewById(R.id.addbtn);
        getSupportActionBar().setTitle("Plot Status List ");

        Intent intent = getIntent();
        site_id = intent.getStringExtra("id");


        yourprefrence.saveData(SiteUtils.ID, site_id);
        Log.e(TAG, "pramod>>>>>: " + site_id);
        //plotlist(site_id);


    }
}
  /*  private void plotlist(String id) {
        final Call<ArrayList<PlotListModel>> userResponse = ApiClient.getUserService().
                usersiteplot(id);
        userResponse.enqueue(new Callback<ArrayList<PlotListModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PlotListModel>> call, Response<ArrayList<PlotListModel>> response) {
                if (response.code()==200){
                    if (!response.body().isEmpty()){
                    progressBar.setVisibility(View.GONE);
                    plotList=response.body();

                   // PlotListAdaptor plotListAdaptor = new PlotListAdaptor(UserPlotListActivity.this,response.body(),plotList);
                    recyclerView.setLayoutManager(new GridLayoutManager(UserPlotListActivity.this,6));
                  //  recyclerView.setAdapter(plotListAdaptor);}
                  *//*  else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Empty List",Toast.LENGTH_SHORT).show();
                    }*//*
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<PlotListModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addplot(View view) {
        Intent intent = new Intent(UserPlotListActivity.this,AddPlotActivity.class);
        intent.putExtra("site_id",site_id);
        startActivity(intent);
    }
}*/