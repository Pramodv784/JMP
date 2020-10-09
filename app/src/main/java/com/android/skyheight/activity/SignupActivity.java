package com.android.skyheight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.skyheight.R;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.ErrorModel;
import com.android.skyheight.model.UserDetail;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity  implements
        AdapterView.OnItemSelectedListener{
    TextInputLayout username,password,cpassword,mobile,address;
    Spinner type;
    Prefrence yourprefrence;
    String usertypeselected;
   UserDetail userDetail;
   TextView register;
   ProgressBar progressBar;
    String[] usertype = {"Select User","Customer","Broker","Owner","Co-owner","Super_Admin"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mobile = findViewById(R.id.mobile_number);
        address = findViewById(R.id.address);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);
        type = findViewById(R.id.type);
        register = findViewById(R.id.register);

        progressBar = findViewById(R.id.progressbar);
        yourprefrence= Prefrence.getInstance(SignupActivity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        type.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,R.layout.spinner_dropdown_layout,usertype);
        aa.setDropDownViewResource(R.layout.spinner_layout);
        type.setAdapter(aa);
    }
    public void userReg(View view) {
        String un = username.getEditText().getText().toString();
        String p = password.getEditText().getText().toString();
        String cp = cpassword.getEditText().getText().toString();
        String m = mobile.getEditText().getText().toString();
        String ad = address.getEditText().getText().toString();
        String t = type.getSelectedItem().toString().trim();

        if (un.isEmpty() && p.isEmpty() && cp.isEmpty() && m.isEmpty() && ad.isEmpty()) {
            username.setError("enter your name");
            password.setError("enter password");
            cpassword.setError("re enter your password");
            mobile.setError("Enter mobile");
            address.setError("Enter Address");
        } else if (p.isEmpty()) {
            password.setError("Enter password");
            password.requestFocus();
        } else if (cp.isEmpty()) {
            cpassword.setError("Re Enter password");
            cpassword.requestFocus();
        }
        else if (m.isEmpty()) {
            mobile.setError("enter mobile number");
            mobile.requestFocus();
        }
        else if (ad.isEmpty()) {
            mobile.setError("enter your address");
            mobile.requestFocus();
        }
        else if (t.equals("Select User")) {
           Toast.makeText(getApplicationContext(),"Please select Type User",Toast.LENGTH_SHORT).show();
        }
        else if (!(cp.equals(p))) {
            password.setError("Password not match ");
            password.requestFocus();
        }
        else if (!(m.length() > 9)) {
            mobile.setError("Enter 10 digit mobile");
            mobile.requestFocus();
        }

        else {
            ButtonActivated();
            UserDetail userDetail = new UserDetail(un,p,cp,m,usertypeselected,ad,null);
            saveuser(userDetail);
            //startActivity(new Intent(SignupActivity.this,UserLoginActivity.class));
        }
    }

    private void saveuser(UserDetail userDetail) {
        Call<UserDetail> userResponseCall = ApiClient.getUserService().createUser(userDetail);
        userResponseCall.enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                if (response.code() == 201) {


                    ButtonFinished();
                    showCustomDialog();

                }
                else if(response.code() == 400){

                    Gson gson = new GsonBuilder().create();

                    ErrorModel errorModel;

                    try {
                        errorModel=gson.fromJson(response.errorBody().string(),ErrorModel.class);
                                    if(errorModel.getMobile_number()!=null){
                                        mobile.setError("Mobile  Should be Unique.");
                                    }
                                    else if (errorModel.getUsername()!=null)
                                    {
                                        username.setError("User Number Should be Unique.");
                                    }
                  /*      username.setError(errorModel.getUsername().toString());
                        mobile.setError(errorModel.getUsername().toString());*/
                        for (int i=0;i<response.errorBody().string().length();i++){
                            if (errorModel.getUsername()!=null) {
                                username.setError(errorModel.getUsername().get(i).toString());
                            }
                            else if (errorModel.getMobile_number()!=null){
                                mobile.setError(errorModel.getMobile_number().get(i).toString());
                            }
                           else if (errorModel.getUsername()!=null && errorModel.getMobile_number()!=null){
                                username.setError(errorModel.getUsername().get(i).toString());
                                mobile.setError(errorModel.getUsername().get(i).toString());
                            }
                           else {
                                Toast.makeText(getApplicationContext()," else",Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext()," catch"+e,Toast.LENGTH_SHORT).show();
                    }
                    ButtonFinished();
                    //Toast.makeText(SignupActivity.this, "Mobile and User Name Should be Unique " , Toast.LENGTH_LONG).show();
                }
                else{
                    ButtonFinished();
                    Toast.makeText(SignupActivity.this, "Some thing went wrong " , Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onFailure(Call<UserDetail> call, Throwable t) {
                ButtonFinished();
                Toast.makeText(SignupActivity.this, "Request failed " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            usertypeselected = (String) parent.getItemAtPosition(position);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getApplicationContext(),"Please select user type",Toast.LENGTH_SHORT).show();

    }
    public void ButtonActivated(){
        progressBar.setVisibility(View.VISIBLE);
        register.setText("Please Wait.....");
    }
        public void ButtonFinished(){
            progressBar.setVisibility(View.GONE);
            register.setText("Registration");
        }
    public void ok(View view) {
        startActivity(new Intent(SignupActivity.this,HomeActivity.class));
    }
}
