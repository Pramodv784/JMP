package com.android.skyheight.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.skyheight.R;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.ActiveUserModel;
import com.android.skyheight.model.UserList;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileDetailActivity extends AppCompatActivity {
TextView username,mobile,address,type,user_status,username1;
Prefrence yourprefrence;
ProgressBar progressBar;
TextView text,status;
Button active,deactive;
String id;
ScrollView scrollView;
    UserList userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        username=findViewById(R.id.username);
        mobile=findViewById(R.id.mob_no);
        address=findViewById(R.id.address);
        type=findViewById(R.id.user_type);
        user_status=findViewById(R.id.user_status);
        username1=findViewById(R.id.username1);
        progressBar=findViewById(R.id.progressbar);
        active=findViewById(R.id.active);
        deactive=findViewById(R.id.deactive);
        status=findViewById(R.id.status);
        scrollView=findViewById(R.id.scroll);
        yourprefrence=Prefrence.getInstance(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
         userList = (UserList) args.getSerializable("ARRAYLIST");
        id=userList.getId();
        username.setText(userList.getUser_name());
        username1.setText(userList.getUser_name());
        mobile.setText(userList.getMobile());
        address.setText(userList.getAddress());

        type.setText(userList.getType());
        if (userList.getIs_active())
        {
         user_status.setText("Active");
         status.setText("Active");

        }else {
            user_status.setText("Deactive");
            status.setText("Deactive");

        }
        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Activate();
            }
        });
        deactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                deactive();
            }
        });
    }
    private void deactive() {
        progressBar.setVisibility(View.VISIBLE);
        Call<ActiveUserModel> userResponese= ApiClient.getUserService()
                .deactivate("Bearer "+yourprefrence
                                .getData(ConstantClass.TOKEN)
                        ,id);
        userResponese.enqueue(new Callback<ActiveUserModel>() {
            @Override
            public void onResponse(Call<ActiveUserModel> call, Response<ActiveUserModel> response) {
                if (response.code()==200){
                    progressBar.setVisibility(View.GONE);
                    user_status.setText("Deactive");
                    status.setText("Deactive");
                    yourprefrence.saveData(ConstantClass.STATUS,response.body().getType());
                   // startActivity(new Intent(ProfileDetailActivity.this,DeactivateListActivity.class));
                    //Toast.makeText(getApplicationContext(),"Deactivated User",Toast.LENGTH_SHORT).show();
                    Snackbar.make(scrollView,"User Deactivated",Snackbar.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(getApplicationContext(),"Failed to Deactivate",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ActiveUserModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Failed ",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void Activate()
{
    progressBar.setVisibility(View.VISIBLE);
    Call<ActiveUserModel> userResponese= ApiClient.getUserService()
            .active("Bearer "+yourprefrence



                                .getData(ConstantClass.TOKEN)
                    ,id);
    userResponese.enqueue(new Callback<ActiveUserModel>() {
        @Override
        public void onResponse(Call<ActiveUserModel> call, Response<ActiveUserModel> response) {
            if (response.code()==200){
                progressBar.setVisibility(View.GONE);
                yourprefrence.saveData(ConstantClass.STATUS,response.body().getType());
                user_status.setText("Active");
                status.setText("Active");
                // startActivity(new Intent(ProfileDetailActivity.this,ActivateUserActivity.class));
               // Toast.makeText(getApplicationContext(),"Activated User",Toast.LENGTH_SHORT).show();
                Snackbar.make(scrollView,"User Activated",Snackbar.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"Failed to Activate",Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onFailure(Call<ActiveUserModel> call, Throwable t) {
            Toast.makeText(getApplicationContext(),"Failed ",Toast.LENGTH_SHORT).show();
        }
    });
}
}