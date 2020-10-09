package com.android.skyheight.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.skyheight.R;
import com.android.skyheight.adaptor.PlotSummaryAdaptor;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.PlotSummaryModel;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlotSummaryListActivity extends AppCompatActivity {
RecyclerView recyclerView;
SwipeRefreshLayout swipeRefreshLayout;
ProgressBar progressBar;
String plot_id;
Prefrence youprefrence;
Toolbar toolbar;
RelativeLayout relative;
ArrayList<PlotSummaryModel> plotsummary;
PlotSummaryAdaptor plotSummaryAdaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_summary_list);
        recyclerView=findViewById(R.id.recycle4);
        swipeRefreshLayout=findViewById(R.id.refresh);
        progressBar=findViewById(R.id.progressbar);
        youprefrence =Prefrence.getInstance(this);
        toolbar=findViewById(R.id.toolbar);
        relative=findViewById(R.id.relative);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        Intent intent=getIntent();
        plot_id=intent.getStringExtra("plot");
        plotsummary(plot_id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_icon));

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                      onBackPressed();
                }
            });
        }
    }
    private void plotsummary(String plot_id) {
        Call<ArrayList<PlotSummaryModel>> userResponse= ApiClient.getUserService()
                .plotsummary("Bearer "+youprefrence.getData(ConstantClass.TOKEN),plot_id);
        userResponse.enqueue(new Callback<ArrayList<PlotSummaryModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PlotSummaryModel>> call, Response<ArrayList<PlotSummaryModel>> response) {
                if (response.code()==200)
                {
                    if (!response.body().isEmpty()){
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                      plotsummary=response.body();
                      plotSummaryAdaptor=new PlotSummaryAdaptor(PlotSummaryListActivity.this,response.body(),plotsummary);
                      recyclerView.setAdapter(plotSummaryAdaptor);
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        relative.setBackgroundResource(R.drawable.emptyitem);
                        Toast.makeText(getApplicationContext(),"Failed to load sumaary ",Toast.LENGTH_SHORT).show();

                    }
                }

            }
            @Override
            public void onFailure(Call<ArrayList<PlotSummaryModel>> call, Throwable t) {
                  progressBar.setVisibility(View.GONE);
                  swipeRefreshLayout.setRefreshing(false);
                Log.e( "onFailure: ","error>>>>>>>"+t);
                Toast.makeText(getApplicationContext(),"Check Your Internet Connection "+t,Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}