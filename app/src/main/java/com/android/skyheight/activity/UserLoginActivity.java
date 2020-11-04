package com.android.skyheight.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.skyheight.R;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.ErrorModel;
import com.android.skyheight.model.LoginModel;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;
import com.android.skyheight.utils.SiteUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoginActivity extends AppCompatActivity {
    EditText mobile,mobile_number,otpverify;
    Prefrence yourprefrence;
    RelativeLayout relativeLayout;
    ProgressBar progressBar;
    TextView login, skip;
    ConstraintLayout constraintLayout;
    String mob;
    TextView resend;
    Button send;
   ShowHidePasswordEditText password;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        mobile = findViewById(R.id.mobile);
        password = (ShowHidePasswordEditText) findViewById(R.id.password);
        relativeLayout = findViewById(R.id.relative);
        progressBar = findViewById(R.id.progressbar);
        login = findViewById(R.id.login);
        constraintLayout = findViewById(R.id.constraint);
        password.setTintColor(Color.parseColor("#3DDC84"));
        yourprefrence = Prefrence.getInstance(UserLoginActivity.this);
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.toString().length() == 0) {
                    constraintLayout.setBackgroundResource(R.drawable.btn_layout);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    constraintLayout.setBackgroundResource(R.drawable.btn);
                    login.setTextColor(Color.parseColor("#FFFFFF"));

                }
            }
        });

    }

    public void ulogin(View view) {
        String m = mobile.getText().toString();
        String p = password.getText().toString();


        if (m.isEmpty() && p.isEmpty()) {
            mobile.setError("Enter mobile");
            password.setError("Enter password");
        } else if (m.isEmpty()) {
            mobile.setError("Enter Mobile Number");
            mobile.requestFocus();
        } else if (p.isEmpty()) {
            password.setError("Enter Mobile Number");
            password.requestFocus();
        } else if (!(m.length() > 9)) {
            mobile.setError("Enter 10 digit mobile number");
            mobile.requestFocus();
        } else {
            ButtonActivated();
            loginuser(m, p);

        }
    }

    private void loginuser(String m, String p) {
        Call<LoginModel> userResponseCall = ApiClient.getUserService().user_login(m, p);
        userResponseCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {

                if (response.code() == 202) {
                    ButtonFinished();
                    yourprefrence.saveData(ConstantClass.TOKEN, response.body().getToken());
                    yourprefrence.saveData(ConstantClass.MOBILE_NUMBER, response.body().getMobile_number());
                    yourprefrence.saveData(ConstantClass.PASSWORD, response.body().getPassword());
                    yourprefrence.saveData(ConstantClass.TYPE, response.body().getType());
                    yourprefrence.saveData(ConstantClass.USERNAME, response.body().getUsername());
                    yourprefrence.saveData(ConstantClass.ADDRESS, response.body().getAddress());
                    yourprefrence.saveData(ConstantClass.ID, response.body().getId());
                    yourprefrence.saveData(SiteUtils.USERID,response.body().getId());
                    Log.i("data","login id>>>"+yourprefrence.getData(ConstantClass.ID));
                    ButtonFinished();
                   /* if (yourprefrence.getData(ConstantClass.TYPE).equals("Super_Admin")
                            && yourprefrence.getData(ConstantClass.TYPE).equals("Owner"))
                    {
                        startActivity(new Intent(UserLoginActivity.this,AdminViewActivity.class));
                    }
                    else {
                        startActivity(new Intent(UserLoginActivity.this,HomeActivity.class));
                    }*/
                    startActivity(new Intent(UserLoginActivity.this, HomeActivity.class));
                } else if (response.code() == 401) {

                    ButtonFinished();

                    Gson gson = new GsonBuilder().create();

                    ErrorModel errorModel;
                    try {

                        //Toast.makeText(getApplicationContext(),"Error is "+response.errorBody().string(),Toast.LENGTH_SHORT).show();
                        errorModel = gson.fromJson(response.errorBody().string(), ErrorModel.class);
                        for (int i = 0; i < errorModel.getNonFieldErrors().size(); i++) {
                            Toast.makeText(getApplicationContext(), "" + errorModel.getNonFieldErrors().get(i).toString(), Toast.LENGTH_LONG).show();
                        }

                    } catch (IOException e) { // handle failure at error parse }
                    }
                    // Toast.makeText(getApplicationContext(),"Wrong mobile number or password", Toast.LENGTH_SHORT).show();
                } else {
                    ButtonFinished();
                    Snackbar.make(relativeLayout, R.string.exit, Snackbar.LENGTH_LONG);
                    Toast.makeText(getApplicationContext(), "Some thing went Wrong ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                ButtonFinished();
                Snackbar.make(relativeLayout, R.string.exit, Snackbar.LENGTH_LONG);
                Toast.makeText(getApplicationContext(), " Check Internet Connection Some thing went wrong ", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void ButtonActivated() {
        progressBar.setVisibility(View.VISIBLE);
        login.setText("Please Wait.....");
    }

    public void ButtonFinished() {
        progressBar.setVisibility(View.GONE);
        login.setText("Login");
    }


    public void register(View view) {
        startActivity(new Intent(UserLoginActivity.this, SignupActivity.class));
    }

    public void forgetpass(View view) {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.forgetpassword_dialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setContentView(R.layout.forgetpassword_dialog);

        send = dialogView.findViewById(R.id.send);
        mobile_number=dialogView.findViewById(R.id.mobile);
        alertDialog.show();
          mob=mobile_number.getText().toString();
       mobile_number.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (s.toString().length() > 10) {
                  mobile_number.setError("Enter correct mobile number");
                  mobile_number.requestFocus();

               }
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
        Log.i("data","mobile>>"+mob);

           send.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (mobile_number.getText().toString().length()==10) {
                       if (TextUtils.isEmpty(mobile_number.getText().toString())) {
                           Toast.makeText(getApplicationContext(), "Enter mobile Number", Toast.LENGTH_LONG).show();
                       } else {
                           Intent intent = new Intent(UserLoginActivity.this, OtpRequestActivity.class);
                           intent.putExtra("mobile", mobile_number.getText().toString());
                           startActivity(intent);
                       }

                   }
                   else {
                       mobile_number.setError("Enter correct mobile number");
                       mobile_number.requestFocus();
                   }
               }
           });
       }



    }

