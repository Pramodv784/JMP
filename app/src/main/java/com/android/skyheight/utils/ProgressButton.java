package com.android.skyheight.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.skyheight.R;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ProgressButton {
    private CardView cardView;
    private ProgressBar progressBar;
    private ConstraintLayout constraintLayout;
    private TextView login;
    Animation fade_in;
    public ProgressButton(View view, Context ct){
        cardView=view.findViewById(R.id.cardview);
        constraintLayout=view.findViewById(R.id.constraint);
        progressBar=view.findViewById(R.id.progressbar);
        login=view.findViewById(R.id.login);
    }
    public void ButtonActivated(){
        progressBar.setVisibility(View.VISIBLE);
        login.setText("Please Wait...");
    }
    public void ButtonFinished(){
        progressBar.setVisibility(View.GONE);
        login.setText("Login Done");
    }
}
