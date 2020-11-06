package com.android.skyheight.activity;

import android.content.Intent;
import android.os.Bundle;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.skyheight.R;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.AddressModel;
import com.android.skyheight.model.SiteListModel;
import com.android.skyheight.model.SiteModel;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;
import com.android.skyheight.utils.SiteUtils;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateSiteActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{
    Intent myfile;

    String site_id;
  int spinnervalue;
       Spinner spinner;
    ConstraintLayout constraint;
    TextView pick, file, pick_image, image;
    public static final int PICK_IMAGE = 1;
    Prefrence yourprefrence;
    String picture, anyfile, address, street, city;
    RequestBody requestFile, requestfile2;
    String name1, location,area,price,description;
    EditText site_name, site_location, site_price, site_description, site_area;
    MultipartBody.Part body1, body2;
    ProgressBar progressBar;

    String site_street,site_address,site_city;
    String site_street1,site_address1,site_city1;
    String[] ciy = {"Select City","Agar Malwa", "Alirajpur", "Anuppur", "Ashoknagar", "Balaghat", "Barwani", "Betul",
            "Bhind", "Bhopal", "Burhanpur", "Chhatarpur", "Chhindwara", "Damoh", "Datia", "Dewas", "Dhar", "Dindori",
            "Guna", "Gwalior", "Harda", "Hoshangabad", "Indore", "Jabalpur", "Jhabua", "Katni", "Khandwa", "Khargone",
            "Mandla", "Mandsaur", "Morena", "Narsinghpur", "Neemuch", "Panna", "Raisen", "Rajgarh", "Ratlam", "Rewa",
            "Sagar", "Satna", "Sehore", "Seoni", "Shahdol", "Shajapur", "Sheopur", "Shivpuri", "Sidhi", "Singrauli",
            "Tikamgarh", "Ujjain", "Umaria", "Vidisha"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_site);
        site_name = findViewById(R.id.site_name);
        site_price = findViewById(R.id.site_price);
        site_area = findViewById(R.id.site_area);
        site_description = findViewById(R.id.site_description);
        pick = findViewById(R.id.pick_file);
        constraint=findViewById(R.id.constraint);
        file = findViewById(R.id.file);
        progressBar = findViewById(R.id.progressbar);
        yourprefrence = Prefrence.getInstance(UpdateSiteActivity.this);
        pick_image = findViewById(R.id.pick_image);
        image = findViewById(R.id.image1);

        site_id=yourprefrence.getData(SiteUtils.ID);
        getsite(site_id);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private void getsite(String site_id) {
        Call<SiteListModel> userResponse =ApiClient.getUserService()
                .getsinglesite("Bearer"+yourprefrence.getData(ConstantClass.TOKEN),site_id);
        userResponse.enqueue(new Callback<SiteListModel>() {
            @Override
            public void onResponse(Call<SiteListModel> call, Response<SiteListModel> response) {
                if (response.code()==200){
                    Log.e("data",">>>>>>>>"+response.body().getName() );
                    site_name.setText(response.body().getName());
                    site_price.setText(response.body().getPrice());
                    site_description.setText(response.body().getDescription());
                    site_area.setText(response.body().getArea());
                    site_street1=response.body().getSite_location().getStreet();
                    site_address1=response.body().getSite_location().getAddress();
                    site_city1=response.body().getSite_location().getCity();

                }
            }

            @Override
            public void onFailure(Call<SiteListModel> call, Throwable t) {

            }
        });
    }



    public void addlocation(View view) {

        //startActivity(new Intent(AddSiteActivity.this, AddSiteLocationActivity.class));
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.address_layout, null);
        final EditText address = (EditText) dialogView.findViewById(R.id.site_address);
        final   EditText street = (EditText) dialogView.findViewById(R.id.site_street);
        Button add_address = (Button) dialogView.findViewById(R.id.add_address);
        address.setText(site_address1);
        street.setText(site_street1);
        spinner =(Spinner)dialogView.findViewById(R.id.spinner);
        ArrayAdapter aa = new ArrayAdapter(this,R.layout.spinner_dropdown_layout,ciy);
        spinnervalue=aa.getPosition(site_city1);
        aa.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(aa);
        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                site_address = address.getText().toString();
                site_street= street.getText().toString();
                site_city= spinner.getSelectedItem().toString().trim();
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        site_city = (String) parent.getItemAtPosition(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void updatesite(View view) {
        name1 = site_name.getText().toString().trim();
        area = site_area.getText().toString().trim();
        price = site_price.getText().toString().trim();
        description = site_description.getText().toString();
        site_city= spinner.getSelectedItem().toString().trim();
        if (name1.isEmpty()) {
            site_name.setError("Enter the Site Name");
            site_name.requestFocus();
        } else if(site_city.equals("Select User")) {
            Toast.makeText(getApplicationContext(),"Please Select City ",Toast.LENGTH_SHORT).show();
        }


        else{
            progressBar.setVisibility(View.VISIBLE);
            AddressModel addressModel = new AddressModel(site_address, site_street, site_city);
            //SiteModel siteModel = new SiteModel(name1, area, price, description, picture, anyfile);
            addressapi(addressModel);
        }

    }
    private void addressapi(AddressModel addressModel) {
        Call<SiteModel> userResponse = ApiClient.getUserService().addaddress("Bearer " + yourprefrence.getData(ConstantClass.TOKEN), addressModel);
        userResponse.enqueue(new Callback<SiteModel>() {
            @Override
            public void onResponse(Call<SiteModel> call, Response<SiteModel> response) {
                if (response.code() == 201) {
                    progressBar.setVisibility(View.GONE);
                    String address_id = response.body().getId();
                    Toast.makeText(getApplicationContext(), "Address added", Toast.LENGTH_SHORT).show();
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


                    site(name, address_id1,area1,price1,description1,yourprefrence.getData(SiteUtils.ID));
                    Log.e( "onResponse: ","id"+site_location );
                } else {
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
            ,RequestBody price,RequestBody description
            ,String id) {
        Call<SiteModel> userResponse = ApiClient.getUserService()
                .updatesite("Bearer " + yourprefrence.getData(ConstantClass.TOKEN)
                        , name, address_id1,area,price,description,id);
        userResponse.enqueue(new Callback<SiteModel>() {
            @Override
            public void onResponse(Call<SiteModel> call, Response<SiteModel> response) {
                Log.e("Tag", "response" + response.code());
                if (response.code() == 200) {
                    progressBar.setVisibility(View.GONE);
                    yourprefrence.saveData(SiteUtils.ID,response.body().getId());
                    startActivity(new Intent(UpdateSiteActivity.this,SiteUpdateActivity.class));
                    Toast.makeText(getApplicationContext(), "Site Updated Sucessfully"+response.body().getId(), Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Site Updated Failed", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<SiteModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("Tag", "response" + t);
                Toast.makeText(getApplicationContext(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }


}