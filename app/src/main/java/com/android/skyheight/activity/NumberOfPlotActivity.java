package com.android.skyheight.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.skyheight.R;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.PlotsModel;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;
import com.android.skyheight.utils.SiteUtils;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NumberOfPlotActivity extends AppCompatActivity {
TextInputLayout plot_number;
Prefrence yourprefrence;
ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_of_plot);
        plot_number= findViewById(R.id.plot_number);
        yourprefrence=Prefrence.getInstance(this);
        progressBar=findViewById(R.id.progressbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void plot(View view) {
        progressBar.setVisibility(View.VISIBLE);
        String plot =plot_number.getEditText().getText().toString();
        //PlotsModel plotsModel = new PlotsModel(plot,yourprefrence.getData(SiteUtils.ID));
        plotCount(plot);
    }

    private void plotCount(String plot_count) {
        Call<PlotsModel> userResponse = ApiClient.getUserService().
                plots("Bearer "+yourprefrence.getData(ConstantClass.TOKEN)
                ,yourprefrence.getData(SiteUtils.ID),plot_count);
        userResponse.enqueue(new Callback<PlotsModel>() {
            @Override
            public void onResponse(Call<PlotsModel> call, Response<PlotsModel> response) {
                if (response.code()==201){
                    progressBar.setVisibility(View.GONE);
                    Log.i("response>>>","done>>");
                    startActivity(new Intent(NumberOfPlotActivity.this,AdminViewActivity.class));
                    Toast.makeText(getApplicationContext(),"Plot Added",Toast.LENGTH_SHORT).show();
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Plot Added Failed",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<PlotsModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Plot Added",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(NumberOfPlotActivity.this,AdminViewActivity.class));
            }
        });
    }
    public void adminhome(View view) {
        startActivity(new Intent(NumberOfPlotActivity.this,AdminViewActivity.class));
    }
}