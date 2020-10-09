package com.android.skyheight.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileUploadActivity extends AppCompatActivity {
TextView file,pickfile;
    String selectedpdf;
    Prefrence yourprefrence;
    ProgressBar progressBar;
    String site;
    private static final String IMAGE_DIRECTORY = "/demonuts_upload_gallery";
    private static final int BUFFER_SIZE = 1024 * 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);
        file = findViewById(R.id.file);
        pickfile =findViewById(R.id.pickfile);
        yourprefrence =Prefrence.getInstance(this);
        progressBar=findViewById(R.id.progressbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        site =intent.getStringExtra("site");

        pickfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select a PDF "), 100);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100){
            if (resultCode ==RESULT_OK){

                Uri selectedUri_PDF = data.getData();
                selectedpdf = getFilePathFromURI(this,selectedUri_PDF);
                file.setText(selectedpdf);
            }
        }
    }
    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(wallpaperDirectory + File.separator + fileName);
            // create folder if not exists

            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }
    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            copystream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int copystream(InputStream input, OutputStream output) throws Exception, IOException {
        byte[] buffer = new byte[BUFFER_SIZE];

        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
            try {
                in.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
        }
        return count;
    }

    public void uploadfile(View view) {
        if (selectedpdf!=null) {
            progressBar.setVisibility(View.VISIBLE);
            File file1 = new File(selectedpdf);
            RequestBody reqBody = RequestBody.create(MediaType.parse("/"), file1);
            MultipartBody.Part partfile = MultipartBody.Part.createFormData("file", file1.getName(), reqBody);
            RequestBody sitedata = RequestBody.create(MediaType.parse("Site"), yourprefrence.getData(SiteUtils.ID));

            Call<ImageModel> UserResponse = ApiClient.getUserService().fileupload("Bearer " + yourprefrence.getData(ConstantClass.TOKEN),
                    partfile, sitedata);
            UserResponse.enqueue(new Callback<ImageModel>() {
                @Override
                public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {
                    if (response.code() == 201) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "File Upload Sucessfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),SiteUpdateActivity.class));
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "File Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ImageModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), " Failed", Toast.LENGTH_SHORT).show();

                }
            });
        }

        else{
                Toast.makeText(getApplicationContext(), "Please Pick a File from Gallery", Toast.LENGTH_SHORT).show();
            }
        }

        }

