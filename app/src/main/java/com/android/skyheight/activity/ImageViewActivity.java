package com.android.skyheight.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.skyheight.R;
import com.android.skyheight.adaptor.ImageAdaptor;
import com.android.skyheight.model.ImageModel;
import com.android.skyheight.model.SiteListModel;
import com.denzcoskun.imageslider.adapters.ViewPagerAdapter;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;
import ozaydin.serkan.com.image_zoom_view.ImageViewZoomConfig;
import ozaydin.serkan.com.image_zoom_view.SaveFileListener;

public class ImageViewActivity extends AppCompatActivity {
String i;
ImageViewZoom photoView;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    Toolbar toolbar;
    ImageAdaptor imageAdaptor;
    ViewPager viewPager;
    ArrayList<ImageModel> images=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        toolbar = findViewById(R.id.toolbar);
        photoView=(ImageViewZoom) findViewById(R.id.photoview);
        toolbar=findViewById(R.id.toolbar);
        photoView.getBase64();
        Intent intent = getIntent();
        i=intent.getStringExtra("i");
        Picasso.get().load(i).into(photoView);
        //photoView.setImageResource(R.drawable.plotimage2);
        ImageViewZoomConfig imageViewZoomConfig =new ImageViewZoomConfig.Builder().saveProperty(true).saveMethod(ImageViewZoomConfig.ImageViewZoomConfigSaveMethod.onlyOnDialog).build();
        photoView.setConfig(imageViewZoomConfig);
        photoView.saveImage(ImageViewActivity.this, "ImageViewZoom", "test", Bitmap.CompressFormat.JPEG, 1, imageViewZoomConfig, new SaveFileListener() {
            @Override
            public void onSuccess(File file) {
                Toast.makeText(ImageViewActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(Exception excepti) {
                Toast.makeText(ImageViewActivity.this, "Error Save", Toast.LENGTH_SHORT).show();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_icon));

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

}