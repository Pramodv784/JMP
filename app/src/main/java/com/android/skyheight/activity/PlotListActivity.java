package com.android.skyheight.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.skyheight.R;
import com.android.skyheight.adaptor.PlotAdaptor;
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

public class PlotListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<PlotListModel> siteGrid;
    Prefrence yourprefrence;
    ProgressBar progressBar;
    String id;
    FloatingActionButton addbtn;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<PlotListModel> plotList = new ArrayList<PlotListModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_list);
        recyclerView = findViewById(R.id.recycle_view);
        yourprefrence=Prefrence.getInstance(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar= findViewById(R.id.progressbar);
        swipeRefreshLayout=findViewById(R.id.refresh);



        plotlist();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                plotlist();
            }
        });
    }
    private void plotlist() {
        final Call<ArrayList<PlotListModel>> userResponse = ApiClient.getUserService().siteplot("Bearer "
                +yourprefrence.getData(ConstantClass.TOKEN),yourprefrence.getData(SiteUtils.ID));
        userResponse.enqueue(new Callback<ArrayList<PlotListModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PlotListModel>> call, Response<ArrayList<PlotListModel>> response) {
                if (response.code()==200){
                    if (!response.body().isEmpty()) {
                        swipeRefreshLayout.setRefreshing(false);
                            progressBar.setVisibility(View.GONE);
                            plotList = response.body();
                            PlotAdaptor plotAdaptor = new PlotAdaptor(PlotListActivity.this, response.body(), plotList);
                            recyclerView.setLayoutManager(new GridLayoutManager(PlotListActivity.this, 6));
                            recyclerView.setAdapter(plotAdaptor);
                        }


                    else {
                        swipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setBackgroundResource(R.drawable.we);
                    }
                }
                else {  swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<PlotListModel>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Please Check your Internet turn On.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addplot(View view) {
        Intent intent= new Intent(PlotListActivity.this,AddPlotActivity.class);
        intent.putExtra("site_id",id);
        startActivity(intent);

    }
}