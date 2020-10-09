package com.android.skyheight.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.skyheight.R;
import com.android.skyheight.adaptor.SiteSummaryAdaptor;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.SiteSummaryModel;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class SiteSummaryListActivity extends AppCompatActivity {
ProgressBar progressBar;
RecyclerView recyclerView;
String site;
Prefrence youprefrence;
RelativeLayout relativeLayout;
ArrayList<SiteSummaryModel> siteSummaryModel;
SiteSummaryAdaptor siteSummaryAdaptor;
SwipeRefreshLayout swipeRefreshLayout;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_summary_list);
        progressBar=findViewById(R.id.progressbar);
        recyclerView =findViewById(R.id.recycle4);
        relativeLayout=findViewById(R.id.relative);
        youprefrence=Prefrence.getInstance(this);
        recyclerView.hasFixedSize();
        swipeRefreshLayout=findViewById(R.id.refresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.
                VERTICAL,false));
        Intent intent = getIntent();
        site=intent.getStringExtra("site");
        sitesummary(site);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               sitesummary(site);

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void sitesummary(String site) {
        Call<ArrayList<SiteSummaryModel>> userResponse = ApiClient.
                getUserService().sitesummary("Bearer "+youprefrence.getData(ConstantClass.TOKEN),site);

        userResponse.enqueue(new Callback<ArrayList<SiteSummaryModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SiteSummaryModel>> call, Response<ArrayList<SiteSummaryModel>> response) {
                if (response.code()==200) {
                    if (!response.body().isEmpty())
                    {
                        progressBar.setVisibility(View.GONE);
                        siteSummaryModel = response.body();
                        siteSummaryAdaptor = new SiteSummaryAdaptor(SiteSummaryListActivity.this, response.body(), siteSummaryModel);
                        recyclerView.setAdapter(siteSummaryAdaptor);
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        relativeLayout.setBackgroundResource(R.drawable.emptyitem);
                    }

                    } else {
                        progressBar.setVisibility(View.GONE);
                       // relativeLayout.setBackgroundResource(R.drawable.emptyitem);

                        Toast.makeText(getApplicationContext(), " Site Summary Failed", Toast.LENGTH_LONG).show();
                    }

            }

            @Override
            public void onFailure(Call<ArrayList<SiteSummaryModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e( "onResponse: ","error>>>>"+t );
                Toast.makeText(getApplicationContext(),"Check your internet Connection"+t,Toast.LENGTH_SHORT).show();
            }
        });

    }
}