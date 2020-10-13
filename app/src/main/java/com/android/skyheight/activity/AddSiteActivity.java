package com.android.skyheight.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.skyheight.R;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.AddressModel;
import com.android.skyheight.model.ErrorModel;
import com.android.skyheight.model.SiteModel;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;
import com.android.skyheight.utils.SiteUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.core.Platform;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

import static com.android.skyheight.utils.ConstantClass.ID;

public class AddSiteActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    Intent myfile;
    Spinner spinner1;
    InputStream is;
    String mediaPath, mediapath2;
    String selectedpdf;
    private static final int PERMISSION_REQUEST_CODE = 100;
    ConstraintLayout constraint;
    TextView pick, file, pick_image, image;
    final int REQUEST_GALLERY = 9544;
    String[] mediaColumns = {MediaStore.Video.Media._ID};
    Prefrence yourprefrence;
    private static final String IMAGE_DIRECTORY = "/demonuts_upload_gallery";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String name1, location, area, price, description;
    TextInputLayout site_name, site_location, site_price, site_description, site_area;
    MultipartBody.Part body1, body2;
    ProgressBar progressBar;
    Bitmap bitmap;
    File image2;
    String userid;
    int owner;
    private static final int BUFFER_SIZE = 1024 * 2;
    String site_street, site_address, site_city;
    String[] ciy = {"Select City", "Agar Malwa", "Alirajpur", "Anuppur", "Ashoknagar", "Balaghat", "Barwani", "Betul",
            "Bhind", "Bhopal", "Burhanpur", "Chhatarpur", "Chhindwara", "Damoh", "Datia", "Dewas", "Dhar", "Dindori",
            "Guna", "Gwalior", "Harda", "Hoshangabad", "Indore", "Jabalpur", "Jhabua", "Katni", "Khandwa", "Khargone",
            "Mandla", "Mandsaur", "Morena", "Narsinghpur", "Neemuch", "Panna", "Raisen", "Rajgarh", "Ratlam", "Rewa",
            "Sagar", "Satna", "Sehore", "Seoni", "Shahdol", "Shajapur", "Sheopur", "Shivpuri", "Sidhi", "Singrauli",
            "Tikamgarh", "Ujjain", "Umaria", "Vidisha"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_site);
        site_name = findViewById(R.id.site_name);
        site_price = findViewById(R.id.site_price);
        site_area = findViewById(R.id.site_area);
        site_description = findViewById(R.id.description);
        pick = findViewById(R.id.pick_file);
        constraint = findViewById(R.id.constraint);
        file = findViewById(R.id.file);
        progressBar = findViewById(R.id.progressbar);
        yourprefrence = Prefrence.getInstance(this);
        pick_image = findViewById(R.id.pick_image);
        image = findViewById(R.id.image1);
        initview();
        requestPermission();
        checkPermission();
         userid= yourprefrence.getData(SiteUtils.USERID);
        Log.i("data","id>>>"+userid);

    }

    private void initview() {
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select a PDF "), 100);

            }
        });
        if (pick_image != null) {
            pick_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isStoragePermissionGranted();
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, REQUEST_GALLERY);
                }
            });
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void addlocation(View view) {

        //startActivity(new Intent(AddSiteActivity.this, AddSiteLocationActivity.class));
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.address_layout, null);
        final EditText address = (EditText) dialogView.findViewById(R.id.site_address);
        final EditText street = (EditText) dialogView.findViewById(R.id.site_street);
        Button add_address = (Button) dialogView.findViewById(R.id.add_address);
        spinner1 = (Spinner) dialogView.findViewById(R.id.spinner);

        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_dropdown_layout, ciy);
        aa.setDropDownViewResource(R.layout.spinner_layout);
        spinner1.setAdapter(aa);
        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site_address = address.getText().toString();
                site_street = street.getText().toString();
                site_city = spinner1.getSelectedItem().toString().trim();
                dialogBuilder.dismiss();
                addbuttonenable();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void addbuttonenable() {
        constraint.setVisibility(View.VISIBLE);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted");
                return true;
            } else {

                // Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            // Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 100:
                if (resultCode == RESULT_OK) {
                    if (requestCode == 100) {
                        Uri selectedUri_PDF = data.getData();
                        selectedpdf = getFilePathFromURI(this,selectedUri_PDF);
                        file.setText(selectedpdf);
                    }

                } else {
                    Toast.makeText(this, "You haven't picked file", Toast.LENGTH_LONG).show();
                }
                break;
            case REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    if (requestCode == REQUEST_GALLERY) {

                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        assert cursor != null;
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        mediaPath = cursor.getString(columnIndex);
                        image.setText(mediaPath);
                        // Set the Image in ImageView for Previewing the Media
                        // imgView.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                        cursor.close();
                    } else {
                        Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();

                    }

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        site_city = (String) parent.getItemAtPosition(position);
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    public void addsite(View view) {
        name1 = site_name.getEditText().getText().toString().trim();
        area = site_area.getEditText().getText().toString().trim();
        price = site_price.getEditText().getText().toString().trim();
        description = site_description.getEditText().getText().toString();
        site_city= spinner1.getSelectedItem().toString().trim();
        if (name1.isEmpty()) {
            site_name.setError("Enter the Site Name");
            site_name.requestFocus();
        } else if(site_city.equals("Select User")) {
            Toast.makeText(getApplicationContext(),"Please Select City ",Toast.LENGTH_SHORT).show();
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            AddressModel addressModel = new AddressModel(site_address, site_street, site_city);

            addressapi(addressModel);
        }
    }
    private void addressapi(AddressModel addressModel) {
        Call<SiteModel> userResponse = ApiClient.getUserService()
                .addaddress("Bearer " + yourprefrence.getData(ConstantClass.TOKEN), addressModel);
        userResponse.enqueue(new Callback<SiteModel>() {
            @Override
            public void onResponse(Call<SiteModel> call, Response<SiteModel> response) {
                if (response.code() == 201) {
                    progressBar.setVisibility(View.GONE);
                    String address_id = response.body().getId();
                    // Toast.makeText(getApplicationContext(), "Address added", Toast.LENGTH_SHORT).show();
                    RequestBody address_id1 =
                            RequestBody.create(MediaType.parse("multipart/form-data"), address_id);
                    RequestBody name =
                            RequestBody.create(MediaType.parse("multipart/form-data"), name1);
                    RequestBody area1 =
                            RequestBody.create(MediaType.parse("multipart/form-data"), area);
                    RequestBody price1 =
                            RequestBody.create(MediaType.parse("multipart/form-data"), price);
                    RequestBody description1 =
                            RequestBody.create(MediaType.parse("multipart/form-data"), description);
                    RequestBody owner =
                            RequestBody.create(MediaType.parse("multipart/form-data")
                                    , userid);
                    Log.i( "data","id>>>>"+yourprefrence.getData(ID)+yourprefrence.getData(ConstantClass.USERNAME));
                    File file = new File(mediaPath);
                   File anfile= new File(selectedpdf);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("/"),anfile);
                    MultipartBody.Part finalfile = MultipartBody.Part.createFormData("file", anfile.getName(),requestBody);
                    RequestBody reqBody = RequestBody.create(MediaType.parse("/"),file);
                    MultipartBody.Part partImage = MultipartBody.Part.createFormData("image", file.getName(),reqBody);
                    site(name, address_id1,area1,price1,description1,owner, partImage,finalfile);
                    //Log.e( "onResponse: ","id"+site_location );
                }
                else if (response.code()==500)
                {
                    Gson gson = new GsonBuilder().create();

                    ErrorModel errorModel;
                    try {

                        //Toast.makeText(getApplicationContext(),"Error is "+response.errorBody().string(),Toast.LENGTH_SHORT).show();
                        errorModel = gson.fromJson(response.errorBody().string(), ErrorModel.class);
                        for (int i = 0; i < errorModel.getNonFieldErrors().size(); i++) {

                            Toast.makeText(getApplicationContext(), "" + errorModel.getNonFieldErrors().get(i).toString(), Toast.LENGTH_LONG).show();
                        }
                        Log.i("data","error>>"+errorModel);

                    } catch (IOException e) { // handle failure at error parse }
                    }
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Faild to save Address", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<SiteModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

            }
        });
    }
    private void site(RequestBody name, RequestBody address_id1,RequestBody area
            ,RequestBody price,RequestBody description,RequestBody owner, MultipartBody.Part body1
            , MultipartBody.Part body2) {
        progressBar.setVisibility(View.VISIBLE);
        Call<SiteModel> userResponse = ApiClient.getUserService()
                .addsite("Bearer " + yourprefrence.getData(ConstantClass.TOKEN)
                        , name, address_id1,area,price,description,owner, body1, body2);
        userResponse.enqueue(new Callback<SiteModel>() {
            @Override
            public void onResponse(Call<SiteModel> call, Response<SiteModel> response) {
                Log.e("Tag", "response" + response.code());
                if (response.code() == 201) {
                    progressBar.setVisibility(View.GONE);
                    yourprefrence.saveData(SiteUtils.ID,response.body().getId());
                    startActivity(new Intent(AddSiteActivity.this,NumberOfPlotActivity.class));
                    Toast.makeText(getApplicationContext(), "Site Added Sucessfully", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Site Added Failed"+response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<SiteModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("Tag", "response" + t);
                Toast.makeText(getApplicationContext(), " Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void requestPermission() {


        if (ActivityCompat.shouldShowRequestPermissionRationale(AddSiteActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(AddSiteActivity.this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(AddSiteActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(AddSiteActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(result==PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            return true;
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                // Re-attempt file write
        }
    }

}