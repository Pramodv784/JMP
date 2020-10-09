package com.android.skyheight.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.skyheight.R;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.ActiveUserModel;
import com.android.skyheight.model.UserList;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeactivateActivity extends AppCompatActivity {
    TextView username,mobile,address,type,status;
    Prefrence yourprefrence;
    ProgressBar progressBar;
    ConstraintLayout constraint;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivate);
        username=findViewById(R.id.username);
        mobile=findViewById(R.id.mobile_number);
        address=findViewById(R.id.address);
        status=findViewById(R.id.user_status);
        type=findViewById(R.id.user_type);
        constraint=findViewById(R.id.constraint);
        progressBar=findViewById(R.id.progressbar);
        yourprefrence=Prefrence.getInstance(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        UserList userList = (UserList) args.getSerializable("ARRAYLIST");
        id=userList.getId();
        username.setText(userList.getUser_name());
        mobile.setText(userList.getMobile());
        address.setText(userList.getAddress());
        type.setText(userList.getType());
        if (userList.getIs_active()==true){
            status.setText("Active");

        }
        else {
            status.setText("Deactive");

        }
    }

    public void deactivate(View view) {
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
                    yourprefrence.saveData(ConstantClass.STATUS,response.body().getStatus());
                    startActivity(new Intent(DeactivateActivity.this,DeactivateListActivity.class));
                    Toast.makeText(getApplicationContext(),"Deactivated User",Toast.LENGTH_SHORT).show();

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
}