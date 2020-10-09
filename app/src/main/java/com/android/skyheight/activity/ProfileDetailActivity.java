package com.android.skyheight.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.skyheight.R;
import com.android.skyheight.model.UserList;

public class ProfileDetailActivity extends AppCompatActivity {
TextView username,mobile,address,type,user_status,username1;


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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        UserList userList = (UserList) args.getSerializable("ARRAYLIST");
        username.setText(userList.getUser_name());
        username1.setText(userList.getUser_name());
        mobile.setText(userList.getMobile());
        address.setText(userList.getAddress());

        type.setText(userList.getType());
        if (userList.getIs_active())
        {
         user_status.setText("Active");
        }else {
            user_status.setText("DeActive");
        }
    }

}