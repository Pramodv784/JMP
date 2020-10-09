package com.android.skyheight.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.skyheight.R;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.UserDetail;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserActivity extends AppCompatActivity {
    TextInputLayout username,password,cpassword,mobile,address;
    ProgressBar progressBar;
    Prefrence yourprefrence;
    TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        mobile = findViewById(R.id.mobile_number);
        address = findViewById(R.id.address);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);
        register = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressbar);
        yourprefrence= Prefrence.getInstance(UpdateUserActivity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public void update(View view) {
        String un = username.getEditText().getText().toString();
        String p = password.getEditText().getText().toString();
        String cp = cpassword.getEditText().getText().toString();
        String m = mobile.getEditText().getText().toString();
        String ad = address.getEditText().getText().toString();
        ButtonActivated();
        UserDetail userDetail = new UserDetail(un,p,cp,m,null,ad,null);
        updateuser(userDetail);
    }

    private void updateuser(UserDetail userDetail) {
        Call<UserDetail> userResponse = ApiClient.getUserService()
                .update("Bearer "+yourprefrence.getData(ConstantClass.TOKEN)
                        ,yourprefrence.getData(ConstantClass.ID),userDetail);
        userResponse.enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                if (response.code()==200){
                    ButtonFinished();
                    Toast.makeText(getApplicationContext(),"User Updated ",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UpdateUserActivity.this,HomeActivity.class));
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
        });

    }

    private void showCustomDialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.my_dialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setContentView(R.layout.my_dialog);
        alertDialog.show();

    }
    public void ButtonActivated(){
        progressBar.setVisibility(View.VISIBLE);
        register.setText("Please Wait.....");
    }
    public void ButtonFinished(){
        progressBar.setVisibility(View.GONE);
        register.setText("Updated");
    }
    public void ok(View view) {
        startActivity(new Intent(UpdateUserActivity.this,UserLoginActivity.class));
    }
}