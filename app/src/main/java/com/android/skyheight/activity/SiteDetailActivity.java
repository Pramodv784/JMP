package com.android.skyheight.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.skyheight.R;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.ImageModel;
import com.android.skyheight.model.SiteListModel;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteDetailActivity extends AppCompatActivity {
    TextView owner1, area, price, location,llocation,site_name,owner_contact,site_description;
    FloatingActionButton fab;
    String site_id;
    String mobile;
    ImageView imagesite;
    ImageSlider imageSlider;
    ProgressBar progressBar;
    ImageView.ScaleType type;
    Prefrence yourprefrence;
    ArrayList<ImageModel> images =new ArrayList<ImageModel>();
    ListView listView;
    ArrayList<String> partner = new ArrayList<String>();
    SiteListModel siteListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_detail);
        owner1 = findViewById(R.id.owner1);
        area = findViewById(R.id.pplot_area);
        listView=findViewById(R.id.partnerlist);
        price = findViewById(R.id.price);
        imageSlider = findViewById(R.id.imageslider);
        location = findViewById(R.id.location);
        yourprefrence= Prefrence.getInstance(this);
        llocation = findViewById(R.id.llocation);
        site_name=findViewById(R.id.site_name);
        progressBar=findViewById(R.id.progressbar);
        owner_contact=findViewById(R.id.mobile);
        site_description=findViewById(R.id.site_description);
        progressBar.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
         siteListModel = (SiteListModel) args.getSerializable("ARRAYLIST");
        site_id= siteListModel.getid();
        getImages();
        if (!siteListModel.getPartner().isEmpty()){
             for(int i=0;i<siteListModel.getPartner().size();i++)
             {
                 partner.add(siteListModel.getPartner().get(i).getUsername());
                 ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,partner);
                 listView.setAdapter(adapter);

             }

        }
        else {
            Toast.makeText(getApplicationContext(),"No partner in this site",Toast.LENGTH_SHORT).show();
        }
           if (siteListModel.getOwner()!=null){
               mobile=siteListModel.getOwner().getMobile_number();
               owner1.setText(siteListModel.getOwner().getUsername());
           }
           else {
               mobile="";
               owner1.setText("");
           }


        price.setText("₹  "+siteListModel.getPrice() + " sq/ft");
        if (siteListModel.getSite_location()!=null){
            llocation.setText(siteListModel.site_location.getAddress());

        }
        else {
            llocation.setText("null");

        }
        // owner_contact.setText(siteListModel.getOwner().mobile_number);
        // site_description.setText(siteListModel.getDescription());
        if (siteListModel.getOwner()!=null)
        {
            if (siteListModel.getOwner().getMobile_number()!=null){
                owner_contact.setText(siteListModel.getOwner().mobile_number);
            }
        }
        else {
            owner_contact.setText("Null");
        }
        if (siteListModel.getDescription()!=null){
            site_description.setText(siteListModel.getDescription());
        }
        else {
            site_description.setText("Null");
        }
        if (siteListModel.getArea()!=null){
            area.setText(siteListModel.getArea());
        }
        else {
            area.setText("Null");
        }
        if (siteListModel.getName()!=null){
            site_name.setText(siteListModel.getName());
        }
        else {
            site_name.setText("Null");
        }
    }
    public void siteplot(View view) {
        Intent intent = new Intent(SiteDetailActivity.this,PlotListActivity.class);
        intent.putExtra("id",site_id);
        startActivity(intent);

    }
    public void getImages(){
        Call<ArrayList<ImageModel>> userResponse = ApiClient.
                getUserService().allimages("Bearer "+yourprefrence.getData(ConstantClass.TOKEN)
                ,site_id);
        userResponse.enqueue(new Callback<ArrayList<ImageModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ImageModel>> call, Response<ArrayList<ImageModel>> response) {
                    List<SlideModel> slideModels = new ArrayList<>();
                    if (response.code()==200){
                        if (!response.body().isEmpty())
                        {
                            progressBar.setVisibility(View.GONE);
                            images=response.body();
                            for (int i=0;i<images.size();i++){

                                if (images.get(i).getImage()==null || images.get(i).getImage().equals("")) {
                                    progressBar.setVisibility(View.GONE);
                                    slideModels.add(new SlideModel(R.drawable.plotimage2,siteListModel.getName(), ScaleTypes.FIT));
                                }
                                else {
                                    progressBar.setVisibility(View.GONE);
                                    slideModels.add(new SlideModel(images.get(i).getImage(), "" +siteListModel.getName(), ScaleTypes.FIT));

                                }

                            }
                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            slideModels.add(new SlideModel(R.drawable.plotimage2, siteListModel.getName(), ScaleTypes.FIT));

                        }
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        slideModels.add(new SlideModel(R.drawable.plotimage2, siteListModel.getName(), ScaleTypes.FIT));
                    }
                    imageSlider.setImageList(slideModels, ScaleTypes.FIT);
                }

                @Override
            public void onFailure(Call<ArrayList<ImageModel>> call, Throwable t) {


            }
        });
    }

    public void updatesite(View view) {
        Intent intent=new Intent(SiteDetailActivity.this,SiteUpdateActivity.class);
        intent.putExtra("site id",site_id);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sitedetail, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.site_setting:
                Intent intent = new Intent(SiteDetailActivity.this,SiteUpdateActivity.class);
                intent.putExtra("site_id",site_id);
                startActivity(intent);


        }
       return  (super.onOptionsItemSelected(item));
    }
}