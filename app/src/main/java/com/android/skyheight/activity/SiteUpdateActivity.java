package com.android.skyheight.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.skyheight.R;

public class SiteUpdateActivity extends AppCompatActivity {
String site;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_update);

        Intent intent = getIntent();
        site=intent.getStringExtra("site_id");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addpartner_in_site(View view) {
        Intent intent = new Intent(SiteUpdateActivity.this,AddPartnerSiteActivity.class);
        intent.putExtra("site",site);
        startActivity(intent);
    }

    public void site_expenses(View view) {
        Intent intent = new Intent(SiteUpdateActivity.this,SiteExpensesActivity.class);
        intent.putExtra("site",site);
        startActivity(intent);
    }

    public void imagesitelist(View view) {
        Intent intent = new Intent(SiteUpdateActivity.this,ImageUploadActivity.class);
        intent.putExtra("site",site);
        startActivity(intent);
    }

    public void plotexpense(View view) {
        Intent intent = new Intent(SiteUpdateActivity.this,PlotExpenseCalActivtiy.class);
        intent.putExtra("site",site);
        startActivity(intent);
    }

    public void sitedetailupdate(View view) {
        Intent intent = new Intent(SiteUpdateActivity.this,UpdateSiteActivity.class);
        intent.putExtra("site",site);
        startActivity(intent);
    }

    public void uploadfile_in_site(View view) {
        Intent intent = new Intent(SiteUpdateActivity.this,FileUploadActivity.class);
        intent.putExtra("site",site);
        startActivity(intent);
    }

    public void sitesummary(View view) {
        Intent intent = new Intent(SiteUpdateActivity.this,SiteSummaryListActivity.class);
        intent.putExtra("site",site);
        startActivity(intent);

    }
}