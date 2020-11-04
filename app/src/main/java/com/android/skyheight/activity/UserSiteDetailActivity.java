package com.android.skyheight.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.skyheight.R;
import com.android.skyheight.Interface.RecycleViewInterface;
import com.android.skyheight.adaptor.PlotListAdaptor;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.ImageModel;
import com.android.skyheight.model.PlotListModel;
import com.android.skyheight.model.SiteListModel;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;
import com.android.skyheight.utils.SiteUtils;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class UserSiteDetailActivity extends AppCompatActivity implements RecycleViewInterface {
    TextView owner1, area, price, location, llocation, owner_contact,area1,street;
    FloatingActionButton fab;
    String site_id;
    String mobile;
    ImageView imagesite;
    ImageSlider imageSlider;
    ImageView.ScaleType type;
    Prefrence yourprefrence;
    ProgressBar progressBar;
    SiteListModel siteListModel;
    RecyclerView recyclerView;
    List<PlotListModel> siteGrid;
    RelativeLayout relativeLayout;
    ExtendedFloatingActionButton callbtn;
    ArrayList<PlotListModel> plotList = new ArrayList<PlotListModel>();
    ProgressBar progressBar2;
    ArrayList<ImageModel> images = new ArrayList<ImageModel>();
    PlotListModel plotListModel;
    Button btnfile;
    LinearLayout linerlayout;
    List<SlideModel> slideModels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_site_detail);
        owner1 = findViewById(R.id.owner1);
        area = findViewById(R.id.pplot_area);
        price = findViewById(R.id.price);
        callbtn=findViewById(R.id.callbtn);
        relativeLayout = findViewById(R.id.relative);
        imageSlider = findViewById(R.id.imageslider);
        location = findViewById(R.id.location);
        recyclerView = findViewById(R.id.recycle_view);
        yourprefrence = Prefrence.getInstance(this);
        progressBar2 = findViewById(R.id.progressbar2);
        llocation = findViewById(R.id.llocation);
        btnfile = findViewById(R.id.btnfile);
        linerlayout = findViewById(R.id.linerlayout);
        owner_contact = findViewById(R.id.mobile);
        progressBar = findViewById(R.id.progressbar);
        area1=findViewById(R.id.area1);
        street=findViewById(R.id.street);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar.setVisibility(View.VISIBLE);
        final Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        siteListModel = (SiteListModel) args.getSerializable("ARRAYLIST");
           if (siteListModel.getOwner()==null)
           {
                mobile="000000";
           }
           else {
               if (!siteListModel.getOwner().getMobile_number().isEmpty())
               {
                   mobile = siteListModel.getOwner().getMobile_number();

               }
               else {
                   mobile="00000";
               }

           }
        String upperString = siteListModel.getOwner().getUsername().substring(0, 1).toUpperCase() + siteListModel.getOwner().getUsername().substring(1).toLowerCase();
        owner1.setText(upperString);
        site_id = siteListModel.getid();
        getImages();
        plotlist(site_id);
        price.setText("₹  " + siteListModel.getPrice() + " /Sqft");
        if (siteListModel.getSite_location() != null) {
            String upperString1 = siteListModel.getSite_location().getCity().substring(0, 1).toUpperCase()
                    + siteListModel.getSite_location().getCity().substring(1).toLowerCase();
            llocation.setText(upperString1);
            String upperarea=siteListModel.getSite_location().getAddress().substring(0,1).toUpperCase()
                    +siteListModel.getSite_location().getAddress().substring(1).toLowerCase();
            String upperstreet=siteListModel.getSite_location().getStreet().substring(0,1).toUpperCase()
                    +siteListModel.getSite_location().getStreet().substring(1).toLowerCase();
            area1.setText(upperarea);
            street.setText(upperstreet);
        } else {
            llocation.setText("null");
        }
        // owner_contact.setText(siteListModel.getOwner().mobile_number);
        // site_description.setText(siteListModel.getDescription());
        if (siteListModel.getArea() != null) {
            area.setText(siteListModel.getArea()+" Sq.ft");
        } else {
            area.setText("Null");
        }
        if (siteListModel.getFile() != null) {
            btnfile.setVisibility(View.VISIBLE);
        } else {
            btnfile.setVisibility(View.GONE);
        }
        btnfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadpdf();
            }
        });
        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhoneNumber();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhoneNumber();
            }
        }
    }
    private void callPhoneNumber() {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UserSiteDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mobile));
                startActivity(callIntent);
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mobile));
                startActivity(callIntent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void siteplot(View view) {
        Intent intent = new Intent(UserSiteDetailActivity.this, UserPlotListActivity.class);
        intent.putExtra("id", site_id);
        startActivity(intent);
    }

    public void getImages() {
        Call<ArrayList<ImageModel>> userResponse = ApiClient.
                getUserService().allimages(site_id);
        userResponse.enqueue(new Callback<ArrayList<ImageModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ImageModel>> call, Response<ArrayList<ImageModel>> response) {

                if (response.code() == 200) {
                    if (!response.body().isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        images = response.body();
                        for (int i = 0; i < images.size(); i++) {
                            if (images.get(i).getImage() == null || images.get(i).getImage().equals("")) {
                                progressBar.setVisibility(View.GONE);
                                slideModels.add(new SlideModel(R.drawable.plotimage2, siteListModel.getName(), ScaleTypes.FIT));


                            } else {
                                progressBar.setVisibility(View.GONE);
                                slideModels.add(new SlideModel(images.get(i).getImage(), "" + siteListModel.getName(), ScaleTypes.FIT));
                            }
                        }
                    }
                        else if (siteListModel.getImage()!=null){
                            progressBar.setVisibility(View.GONE);
                            slideModels.add(new SlideModel(siteListModel.getImage(),siteListModel.getName(),ScaleTypes.FIT));


                        }
                     else {
                        progressBar.setVisibility(View.GONE);
                        slideModels.add(new SlideModel(R.drawable.plotimage2, siteListModel.getName(), ScaleTypes.FIT));
                    }
                    imageSlider.setImageList(slideModels, ScaleTypes.FIT);
                     imageSlider.setItemClickListener(new ItemClickListener() {
                         @Override
                         public void onItemSelected(int i) {
                             if (!images.isEmpty())
                             {
                                 Intent intent1=new Intent(UserSiteDetailActivity.this,ImageViewActivity.class);
                                 intent1.putExtra("i",images.get(i).getImage());
                                 startActivity(intent1);
                             }
                             Intent intent1=new Intent(UserSiteDetailActivity.this,ImageViewActivity.class);
                             intent1.putExtra("i",siteListModel.getImage());
                             startActivity(intent1);

                         }

                     });
                } else {
                    progressBar.setVisibility(View.GONE);
                    slideModels.add(new SlideModel(R.drawable.plotimage2, siteListModel.getName(), ScaleTypes.FIT));
                    imageSlider.setImageList(slideModels, ScaleTypes.FIT);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<ImageModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                List<SlideModel> slideModels = new ArrayList<>();
                slideModels.add(new SlideModel(R.drawable.plotimage2, "site", ScaleTypes.FIT));
                imageSlider.setImageList(slideModels, ScaleTypes.FIT);
                Toast.makeText(getApplicationContext(), "Image Load Failed", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void downloadpdf() {
        if (siteListModel.getFile() != null) {
            String path = yourprefrence.getData(SiteUtils.FILEPATH);
            String file ="file";
            String fileextension= MimeTypeMap.getFileExtensionFromUrl(path);
            file +="."+fileextension;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // this will request for permission when user has not granted permission for the app
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                //Download Script
                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(path);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setVisibleInDownloadsUi(true);
                request.setTitle("JMP");
                request.setDescription("Downloading"+file);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "file.pdf");
               // Log.e("bhagwan", "download" + uri.getPath());
                downloadManager.enqueue(request);
                Toast.makeText(getApplicationContext(), "Starting Downloading... ", Toast.LENGTH_SHORT).show();


            }
        } else {
            Toast.makeText(getApplicationContext(), "File Not Present", Toast.LENGTH_SHORT).show();
        }
    }


    private void plotlist(String id) {
        final Call<ArrayList<PlotListModel>> userResponse = ApiClient.getUserService().
                usersiteplot(id);
        userResponse.enqueue(new Callback<ArrayList<PlotListModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PlotListModel>> call, Response<ArrayList<PlotListModel>> response) {
                if (response.code() == 200) {
                    if (!response.body().isEmpty()) {
                        progressBar2.setVisibility(View.GONE);
                        plotList = response.body();
                        PlotListAdaptor plotListAdaptor = new PlotListAdaptor(UserSiteDetailActivity.this, response.body(), plotList, UserSiteDetailActivity.this);
                        recyclerView.setLayoutManager(new GridLayoutManager(UserSiteDetailActivity.this, 5));
                        recyclerView.setAdapter(plotListAdaptor);
                    } else {
                        progressBar2.setVisibility(View.GONE);
                        linerlayout.setVisibility(View.GONE);
                        relativeLayout.setBackgroundResource(R.drawable.we);
                        Toast.makeText(getApplicationContext(), "Empty List", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    progressBar2.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PlotListModel>> call, Throwable t) {
                progressBar2.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onclickitem(int position) {
        TextView plot_no, plot_size, plot_status;
        plot_no = findViewById(R.id.plotnumber1);
        plot_size = findViewById(R.id.plot_size);
        plot_no.setText(String.valueOf(plotList.get(position).getPlot_number()));
        plot_no.setTextColor(Color.parseColor("#3DDC84"));
        if (plotList.get(position).getSize() > 0) {
            plot_size.setText(String.valueOf(plotList.get(position).getSize()) + " Sq/ft");
            plot_size.setTextColor(Color.parseColor("#3DDC84"));
        } else {
            plot_size.setText("0");
        }


    }

}
