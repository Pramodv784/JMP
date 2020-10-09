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

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoginActivity extends AppCompatActivity {
    EditText mobile, password,mobile_number,otpverify;
    Prefrence yourprefrence;
    RelativeLayout relativeLayout;
    ProgressBar progressBar;
    TextView login, skip;
    ConstraintLayout constraintLayout;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    TextView resend;
    Button send;
    FirebaseAuth mAuth;
    private String mVerificationId;
    View view;
 String id;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        relativeLayout = findViewById(R.id.relative);
        progressBar = findViewById(R.id.progressbar);
        login = findViewById(R.id.login);
        constraintLayout = findViewById(R.id.constraint);
        //view =findViewById(R.id.myprogressbutton);
        yourprefrence = Prefrence.getInstance(UserLoginActivity.this);
            mAuth=FirebaseAuth.getInstance();
        // password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        //password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
       // sendVerificationCode();

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.toString().length() == 0) {
                    constraintLayout.setBackgroundResource(R.drawable.layout);
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
        resend = dialogView.findViewById(R.id.resend);
        send = dialogView.findViewById(R.id.send);
        mobile_number=dialogView.findViewById(R.id.mobile_number);
        otpverify=dialogView.findViewById(R.id.otpverify);
        alertDialog.show();
      /*  send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mobile_number.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Enter mobile Number",Toast.LENGTH_LONG).show();
                }else
                if (otpverify.getText().toString().replace(" ","").length()!=6){
                    Toast.makeText(getApplicationContext(),"Enter OTP ",Toast.LENGTH_LONG).show();
                }
                else{
                    //loader.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id,otpverify.getText().toString().replace(" ","") );
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });*/

    }

   /* private void sendVerificationCode() {
        new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long l) {
                resend.setText("" + l / 1000);
                resend.setEnabled(false);
            }

            @Override
            public void onFinish() {
                resend.setText("Resend");
                resend.setEnabled(true);
            }
        }.start();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(String.valueOf(mobile_number), 60, TimeUnit.SECONDS,
                this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        UserLoginActivity.this.id=id;
                    }
                });



    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //loader.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(UserLoginActivity.this, HomeActivity.class);
                            intent.putExtra("mobile", getIntent().getStringExtra("mobile"));
                            startActivity(intent);
                            finish();
                            FirebaseUser user = task.getResult().getUser();

                        } else {
                            Toast.makeText(UserLoginActivity.this, "Verification Failed", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }*/
}