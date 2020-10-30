package com.android.skyheight.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.skyheight.R;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.UserDetail;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {
TextView user_name,username1,user_mobile,user_address,user_staus,user_type;
Prefrence yourprefrence;
    EditText editusername,editaddress,editmobile;
    ProgressBar progressBar;
    ConstraintLayout btn;
    TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        yourprefrence= Prefrence.getInstance(UserProfileActivity.this);
        user_name =findViewById(R.id.username);
        username1 =findViewById(R.id.username1);
        user_mobile =findViewById(R.id.mob_no);
        user_address =findViewById(R.id.address);
        user_staus =findViewById(R.id.user_status);
        user_type =findViewById(R.id.user_type);
        progressBar=findViewById(R.id.progressbar);
        editusername=findViewById(R.id.editusername);
        register = findViewById(R.id.register);
        editaddress=findViewById(R.id.editaddress);
        editmobile=findViewById(R.id.editmobile);
        btn=findViewById(R.id.btn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        yourprefrence = Prefrence.getInstance(UserProfileActivity.this);
        user_name.setText(yourprefrence.getData(ConstantClass.USERNAME));
        username1.setText(yourprefrence.getData(ConstantClass.USERNAME));
        user_mobile.setText(yourprefrence.getData(ConstantClass.MOBILE_NUMBER));
        user_address.setText(yourprefrence.getData(ConstantClass.ADDRESS));
        editusername.setText(yourprefrence.getData(ConstantClass.USERNAME));
        editmobile.setText(yourprefrence.getData(ConstantClass.MOBILE_NUMBER));
        editaddress.setText(yourprefrence.getData(ConstantClass.ADDRESS));
        if (yourprefrence.getData(ConstantClass.STATUS).equals("true"))
        {
            user_staus.setText("Active");
        }
        else {
            user_staus.setText("Deactive");
        }
        user_type.setText(yourprefrence.getData(ConstantClass.TYPE));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void updateprofile(View view) {
        String un = editusername.getText().toString();
        String m = editmobile.getText().toString();
        String add = editaddress.getText().toString();
        UserDetail userDetail = new UserDetail(un,null,null,m,null,add,null);
        updateuser(userDetail);
        //startActivity(new Intent(UserProfileActivity.this,UpdateUserActivity.class));
    }
    private void updateuser(UserDetail userDetail) {
        ButtonActivated();
        Call<UserDetail> userResponse = ApiClient.getUserService()
                .update("Bearer "+yourprefrence.getData(ConstantClass.TOKEN)
                        ,yourprefrence.getData(ConstantClass.ID),userDetail);
        userResponse.enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                if (response.code()==200){
                    ButtonFinished();
                    Toast.makeText(getApplicationContext(),"User Updated ",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UserProfileActivity.this,HomeActivity.class));
                }
                else {
                    ButtonFinished();
                    Toast.makeText(getApplicationContext(),"User Updated Failed ",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UserDetail> call, Throwable t) {
                ButtonFinished();
                Toast.makeText(getApplicationContext()," Failed ",Toast.LENGTH_SHORT).show();
            }
        }); }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.userprofile,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.profile:
                btn.setVisibility(View.VISIBLE);
                editusername.setVisibility(View.VISIBLE);
                editmobile.setVisibility(View.VISIBLE);
                editaddress.setVisibility(View.VISIBLE);
                user_name.setVisibility(View.GONE);
                user_mobile.setVisibility(View.GONE);
                user_address.setVisibility(View.GONE);
                editusername.requestFocus();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    public void ButtonActivated(){
        progressBar.setVisibility(View.VISIBLE);
        register.setText("Please Wait.....");
    }
    public void ButtonFinished(){
        progressBar.setVisibility(View.GONE);
        register.setText("Updated");
    }
}