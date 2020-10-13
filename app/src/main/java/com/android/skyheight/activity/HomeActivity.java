package com.android.skyheight.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.skyheight.R;
import com.android.skyheight.adaptor.SiteListAdaptor;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.SiteListModel;
import com.android.skyheight.network.NetworkChangeReceiver;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.jackandphantom.circularimageview.CircleImage;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.nio.channels.NetworkChannel;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity  extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,NetworkChangeReceiver.ConnectionChangeCallback
       {
    private Toolbar toolbar;
    private DrawerLayout drawer;
    NavigationView navigationView;
    CircleImage circularImageView;
    RecyclerView recyclerView;
    ToggleButton toggleButton;
    Prefrence yourprefrence;
    TextView userName,typeuser;
    Button loginbtn,signup;
    SiteListAdaptor siteListAdaptor;
    ProgressBar progressBar;
    NetworkChangeReceiver.ConnectionChangeCallback connectionChangeCallback;
    SwipeRefreshLayout refresh;
    boolean doubleBackToExitPressedOnce = false;
    ArrayList<SiteListModel> sitelist = new ArrayList<SiteListModel>();
    ShimmerFrameLayout shimmer;
    NetworkChangeReceiver networkChangeReceiver;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer);
        yourprefrence = Prefrence.getInstance(HomeActivity.this);
        recyclerView = findViewById(R.id.recycle);
        refresh =findViewById(R.id.refresh);
        shimmer=findViewById(R.id.shimmer_view_container);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View headerview = (View) navigationView.getHeaderView(0);
        circularImageView = (CircleImage) headerview.findViewById(R.id.user_image);
        userName = (TextView) headerview.findViewById(R.id.userName);
        typeuser=(TextView) headerview.findViewById(R.id.typeuser);
        refresh.setColorSchemeColors(R.color.colorPrimary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                siteList();
            }
        });
        Log.i("data","userid"+yourprefrence.getData(ConstantClass.ID));
        IntentFilter intentFilter= new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        NetworkChangeReceiver networkChangeReceiver= new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver,intentFilter);
        networkChangeReceiver.setConnectionChangeCallback(this);

        if (yourprefrence.getData(ConstantClass.TOKEN).length()>10){
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.userregister).setVisible(false);
        }
        else {
            userName.setText("Guest User");
            hidedrawermenu();
        }

       userName.setText("  Hi  "+yourprefrence.getData(ConstantClass.USERNAME));
       userName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.smiley_icon,0,0,0);
       if (yourprefrence.getData(ConstantClass.TYPE).equals("Super_Admin"))
       {
           typeuser.setText("Super Admin");
       }
       else {
           typeuser.setText(yourprefrence.getData(ConstantClass.TYPE));
       }
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) HomeActivity.this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        siteList();
        toolbar.inflateMenu(R.menu.homemenu);

    }
    @Override
    public void onResume() {
        super.onResume();
        shimmer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        shimmer.stopShimmerAnimation();
        super.onPause();
    }

           @Override
           public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homemenu,menu);


        return  true;
           }

           @Override
           public boolean onOptionsItemSelected(@NonNull MenuItem item) {
               if (item.getItemId()==R.id.refresh)
               {
                   siteList();
               }
        return super.onOptionsItemSelected(item);
           }

           private void hidedrawermenu(){
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.admin_control).setVisible(false);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.profile).setVisible(false);
    }
    private void siteList() {

        Call<ArrayList<SiteListModel>> userResponseCall = ApiClient.getUserService()
                .usersitelist();
        userResponseCall.enqueue(new Callback<ArrayList<SiteListModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SiteListModel>> call,
                                   Response<ArrayList<SiteListModel>> response) {
                if (response.code()==200){
                    if (!response.body().isEmpty()){
                        refresh.setRefreshing(false);
                        shimmer.stopShimmerAnimation();
                        shimmer.setVisibility(View.GONE);
                    Log.i( "onResponse: ",response.body().toString() );
                    sitelist=response.body();
                    siteListAdaptor = new SiteListAdaptor(HomeActivity.this,response.body(),sitelist);
                    recyclerView.setAdapter(siteListAdaptor);}
                    else {
                        shimmer.setVisibility(View.GONE);
                        shimmer.stopShimmerAnimation();

                      recyclerView.setBackgroundResource(R.drawable.emptyitem);
                    }
                }
                else if (response.code()==401){
                    refresh.setRefreshing(false);

                }
                else {
                    shimmer.stopShimmerAnimation();
                    shimmer.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(),"List Failed ",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<SiteListModel>> call, Throwable t) {
                refresh.setRefreshing(false);
                shimmer.setVisibility(View.GONE);
                shimmer.stopShimmerAnimation();
                Log.e( "onResponse: ","failed"+t);
                Toast.makeText(getApplicationContext(),"Please check your Internet connection ",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id) {
            case R.id.admin_control:
                checkuser();

                break;
            case R.id.profile:
                startActivity(new Intent(HomeActivity.this, UserProfileActivity.class));
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.share:
                share();
                break;
            case R.id.userregister:
                startActivity(new Intent(HomeActivity.this,SignupActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void share() {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share App link ");
        String app_url = " https://play.google.com/store";
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
        startActivity(Intent.createChooser(shareIntent, "Share via"));

    }
    private void checkuser()  {
        if (yourprefrence.getData(ConstantClass.TYPE).equals("Super_Admin") || yourprefrence.getData(ConstantClass.TYPE).equals("Owner")) {
            startActivity(new Intent(HomeActivity.this, AdminViewActivity.class));
        } else {
            Snackbar.make(drawer, "You Don't have Access of Admin Panel", Snackbar.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(), "You Don't have Access of Admin Panel", Toast.LENGTH_LONG).show();
        }
    }
    private void logout() {
        yourprefrence.clear();
        Log.i("logout","logdata>>"+yourprefrence.getData(ConstantClass.ID));
        startActivity(new Intent(HomeActivity.this, HomeActivity.class));
        // Toast.makeText(getApplicationContext(),"Email"+yourprefrence.getData(ConstantClass.EMAIL),Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(drawer, "Double press back to exit", Snackbar.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


    public void login(MenuItem item) {
        startActivity(new Intent(HomeActivity.this,UserLoginActivity.class));
    }
           @Override
           public void onConnectionChange(boolean isConnected) {
               if(isConnected){
                  // Snackbar.make(drawer,"Connected", Snackbar.LENGTH_LONG).show();
                   shimmer.startShimmerAnimation();
                   shimmer.setVisibility(View.VISIBLE);
                   siteList();
               }
               else{
                   Snackbar.make(drawer, "Connection Lost", Snackbar.LENGTH_LONG).show();
               }

           }
       }


