package com.android.skyheight.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.skyheight.R;
import com.android.skyheight.model.PlotListModel;

public class UserPlotDetailActivity extends AppCompatActivity {
TextView plot_number,plot_size,plot_description,plot_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_plot_detail);
        plot_number =findViewById(R.id.plot_number);
        plot_size =findViewById(R.id.plot_size);
        plot_description =findViewById(R.id.plot_description);
        plot_status =findViewById(R.id.plot_status);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        PlotListModel plotListModel = (PlotListModel) args.getSerializable("ARRAYLIST");
        plot_number.setText(plotListModel.getPlot_number());

        if (plotListModel.getSize()>0){
            plot_size.setText(String.valueOf(plotListModel.getSize()));
        }
        else {
            plot_size.setText("0");
        }
        plot_description.setText(plotListModel.getDescription());
        if (plotListModel.getStatus()==true){
            plot_status.setText("Available");
        }
        else {
            plot_status.setText("Book");
        }


    }
}