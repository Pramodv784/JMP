package com.android.skyheight.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.skyheight.R;
import com.android.skyheight.adaptor.ActivateAdaptor;
import com.android.skyheight.adaptor.DeactivateAdaptor;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.UserList;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeactivateListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    DeactivateAdaptor deactivateAdaptor;
    Prefrence yourprefrence;
    ProgressBar progressBar;
    String deactivate="1";
    ArrayList<UserList> userlist = new ArrayList<UserList>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivate_list);
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        relativeLayout=findViewById(R.id.relative_layout4);
        progressBar = findViewById(R.id.progressbar);
        yourprefrence = Prefrence.getInstance(DeactivateListActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        userlist(deactivate);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void userlist(String deactivate) {
        Call<ArrayList<UserList>> userResponseCall = ApiClient.getUserService()
                .deactive("Bearer "+yourprefrence.getData(ConstantClass.TOKEN),deactivate);
        userResponseCall.enqueue(new Callback<ArrayList<UserList>>() {
            @Override
            public void onResponse(Call<ArrayList<UserList>> call, Response<ArrayList<UserList>> response) {

                if (response.code() == 200) {
                    if (!response.body().isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        userlist = response.body();
                        deactivateAdaptor = new DeactivateAdaptor(DeactivateListActivity.this, response.body(),userlist);
                        recyclerView.setAdapter(deactivateAdaptor);
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        relativeLayout.setBackgroundResource(R.drawable.emptyitem);
                    }
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<UserList>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

}