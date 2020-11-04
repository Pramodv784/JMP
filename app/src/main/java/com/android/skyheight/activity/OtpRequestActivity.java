package com.android.skyheight.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.skyheight.R;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.common.base.Verify;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
public class OtpRequestActivity extends AppCompatActivity {
Prefrence yourprefrence;
ProgressBar progressBar;
    String phone_number;
String verificationCodeBySystem;
Button btn;
OtpTextView otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_request);
        yourprefrence=Prefrence.getInstance(this);
        progressBar=findViewById(R.id.progressbar);
        otp=findViewById(R.id.otp);
        Intent intent =getIntent();
        btn=findViewById(R.id.btn);
        phone_number=intent.getStringExtra("mobile");
        Log.i("","mobile>>"+phone_number);
             sendVerificationCodeToUser(phone_number);
        progressBar.setVisibility(View.GONE);
otp.setOtpListener(new OTPListener() {
    @Override
    public void onInteractionListener() {
        String code1=otp.getOTP();
        if (code1.isEmpty() ||code1.length()<6)
        {
            otp.showError();
            otp.requestFocusOTP();
            progressBar.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public void onOTPComplete(String otp) {
        verifyCode(otp);
        progressBar.setVisibility(View.VISIBLE);
    }
});
    }
    private void sendVerificationCodeToUser(String phone_number) {
        PhoneAuthProvider.getInstance().
                verifyPhoneNumber("+91"+phone_number,
                        60,TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallbacks);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks= new
            PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);

                    verificationCodeBySystem=s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code=phoneAuthCredential.getSmsCode();
                    if (code!=null)
                    {
                      progressBar.setVisibility(View.VISIBLE);
                      verifyCode(code);
                      otp.setOTP(code);
                    }
                }
                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    otp.showError();
                    otp.requestFocusOTP();
                    Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            };
    private void verifyCode(String code) {
        PhoneAuthCredential phoneAuthCredential= PhoneAuthProvider.getCredential(verificationCodeBySystem,code);
        signInUserByCredential(phoneAuthCredential);
    }
    private void signInUserByCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(OtpRequestActivity.this, new OnCompleteListener<AuthResult>(){
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                               if (task.isSuccessful())
                               {
                                  Intent intent = new Intent(OtpRequestActivity.this,HomeActivity.class);
                                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                  startActivity(intent);
                               }
                               else {
                                   otp.showError();
                                   otp.requestFocusOTP();
                                   Toast.makeText(getApplicationContext()," "+task.getException().getMessage() ,Toast.LENGTH_SHORT).show();
                               }
                            }
                        });
    }
}