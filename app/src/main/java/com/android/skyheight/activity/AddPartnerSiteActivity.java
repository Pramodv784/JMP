package com.android.skyheight.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.skyheight.R;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.ErrorModel;
import com.android.skyheight.model.SiteListModel;
import com.android.skyheight.model.UserList;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;
import com.android.skyheight.utils.SiteUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPartnerSiteActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    Spinner spinner;
    String partner_id;
    String patnerselected;
    Prefrence yourprefrence;
    String sit;
    TextView site_name;
    String add ="1";
    ProgressBar progressBar;
    ArrayList<UserList> partnerlist = new ArrayList<>();
    ArrayList<String> allpartner = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_partner_site);
        spinner= findViewById(R.id.spinner);
        progressBar=findViewById(R.id.progressbar);
        yourprefrence=Prefrence.getInstance(this);
        site_name=findViewById(R.id.site_name);
        site_name.setText(yourprefrence.getData(SiteUtils.NAME));
        String type1="Co-owner";
        partnerlist(type1);
        spinner.setOnItemSelectedListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        sit=intent.getStringExtra("site");
       // site_name.setText(siteListModel.getName());




    }
    private void partnerlist(String type) {
        Call<ArrayList<UserList>> userResponse = ApiClient.getUserService()
                .allpartner("Bearer "+yourprefrence.getData(ConstantClass.TOKEN),type);
        userResponse.enqueue(new Callback<ArrayList<UserList>>() {
            @Override
            public void onResponse(Call<ArrayList<UserList>> call, Response<ArrayList<UserList>> response) {
                if (response.code()==200){
                    partnerlist=response.body();
                    for(int i=0;i<partnerlist.size();i++){

                        allpartner.add(partnerlist.get(i).getUser_name().substring(0,1).toUpperCase()+partnerlist.get(i)
                                .getUser_name().substring(1).toLowerCase());


                    }
                    ArrayAdapter<String> aa = new
                            ArrayAdapter<String>(getApplicationContext()
                            ,R.layout.spinner_dropdown_layout,allpartner);
                    aa.setDropDownViewResource(R.layout.spinner_layout);
                    spinner.setAdapter(aa);

                }
                else {
                    Toast.makeText(getApplicationContext(),"Failed broker list",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserList>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        patnerselected= (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void addpartner_in_site(View view) {

            String partnerselected=spinner.getSelectedItem().toString();
            int position=spinner.getSelectedItemPosition();
            partner_id=partnerlist.get(position).getId();

        if (partnerselected.equals("Select Partner")) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"Please Select Partner",Toast.LENGTH_SHORT).show();
        }
        else {
            sitepartner(partner_id, yourprefrence.getData(SiteUtils.ID));
            progressBar.setVisibility(View.VISIBLE);
        }
        }
        private void sitepartner(String user_id, String site_id) {
            Call<SiteListModel> userResponse =ApiClient.getUserService()
                    .addpartner_in_site("Bearer "+yourprefrence.getData(ConstantClass.TOKEN)
                    ,user_id,yourprefrence.getData(SiteUtils.ID),add);
            userResponse.enqueue(new Callback<SiteListModel>() {
                @Override
                public void onResponse(Call<SiteListModel> call, Response<SiteListModel> response) {
                    if (response.code()==200){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Partner Added Sucessfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddPartnerSiteActivity.this,SiteUpdateActivity.class));
                    }
                    else if (response.code()==403){

                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"User not allowed to add Partner",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<SiteListModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Failed ",Toast.LENGTH_SHORT).show();
                }
            });

        }

}