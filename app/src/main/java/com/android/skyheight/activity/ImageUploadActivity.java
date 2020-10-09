package com.android.skyheight.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.skyheight.R;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.ImageModel;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;
import com.android.skyheight.utils.SiteUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageUploadActivity extends AppCompatActivity {
TextView pick;
ImageView imageView;
    final int REQUEST_GALLERY = 9544;
    String mediaPath;
    Bitmap bitmap;
    Prefrence yourprefrence;
    ProgressBar progressBar;
    String site;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        pick=findViewById(R.id.pickimage);
        imageView=findViewById(R.id.image);
        progressBar =findViewById(R.id.progressbar);
        yourprefrence=Prefrence.getInstance(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        site=intent.getStringExtra("site");
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_GALLERY);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

                if (requestCode==REQUEST_GALLERY)
                {
                    if (requestCode == REQUEST_GALLERY) {

                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        try {
                             bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        assert cursor != null;
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        mediaPath = cursor.getString(columnIndex);
                        imageView.setImageBitmap(bitmap);
                        // Set the Image in ImageView for Previewing the Media
                        // imgView.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                        cursor.close();
                    } else {
                        Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();

                    }
                }

    }


    public void uploadimage(View view) {
        if (mediaPath!=null){
           progressBar.setVisibility(View.VISIBLE);
            File file = new File(mediaPath);
            RequestBody reqBody = RequestBody.create(MediaType.parse("/"),file);
            MultipartBody.Part partImage = MultipartBody.Part.createFormData("Image", file.getName(),reqBody);
            RequestBody sitedata = RequestBody.create(MediaType.parse("Site"),yourprefrence.getData(SiteUtils.ID));

            Call<ImageModel> UserResponse= ApiClient.getUserService().upload("Bearer "+yourprefrence.getData(ConstantClass.TOKEN),
                    partImage,sitedata);
            UserResponse.enqueue(new Callback<ImageModel>() {
                @Override
                public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {
                    if (response.code()==201)
                    {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Image Upload Sucessfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),SiteUpdateActivity.class));
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Image Upload Failed",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ImageModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.i( "data: ","data>>>>"+t);
                    Toast.makeText(getApplicationContext(),"Check Your Internet Connection",Toast.LENGTH_SHORT).show();

                }
            });

        }
        else {
            Toast.makeText(getApplicationContext(),"Please Pick a Image from Gallery",Toast.LENGTH_SHORT).show();
        }

    }


}