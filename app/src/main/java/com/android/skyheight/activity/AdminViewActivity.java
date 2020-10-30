package com.android.skyheight.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.skyheight.R;

public class AdminViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Admin Control");
        getSupportActionBar().setSubtitle("Admin View");


    }

    public void addsite(View view) {
        startActivity(new Intent(AdminViewActivity.this,AddSiteActivity.class));
    }

    public void userlist(View view) {
        startActivity(new Intent(AdminViewActivity.this,CustomerListActivity.class));
    }



    public void adduser(View view) {
        startActivity(new Intent(AdminViewActivity.this,SignupActivity.class));
    }

    public void sitelist(View view) {
        startActivity(new Intent(AdminViewActivity.this, SiteListActivity.class));
    }





    public void summary(View view) {
        Toast.makeText(getApplicationContext(),"Feature In Progress... ",Toast.LENGTH_SHORT).show();
    }







    public void removesite(View view) {
        startActivity(new Intent(AdminViewActivity.this, SiteListActivity.class));
    }

    public void history(View view) {
        Toast.makeText(getApplicationContext(),"Feature In Progress... ",Toast.LENGTH_SHORT).show();
    }

    public void alluserlist(View view) {
        startActivity(new Intent(AdminViewActivity.this,CustomerListActivity.class));
    }

    public void sitelistplot(View view) {
        //startActivity(new Intent(AdminViewActivity.this,SiteListPlotActivity.class));
    }





    public void sitesummary(View view) {
        Toast.makeText(getApplicationContext(),"Feature In Progress...",Toast.LENGTH_SHORT).show();
    }


    public void book(View view) {
        startActivity(new Intent(AdminViewActivity.this,BookingActivity.class));
    }

    public void booklist(View view) {
        startActivity(new Intent(AdminViewActivity.this,BookListActivity.class));
    }
}
