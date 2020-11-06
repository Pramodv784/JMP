package com.android.skyheight.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.skyheight.R;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.PlotModel;
import com.android.skyheight.model.UserList;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;
import com.android.skyheight.utils.SiteUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPlotActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
TextInputLayout plot_number,plot_size,plot_description;
Spinner spinner;
    String broker_id;
String brokerselected;
    Prefrence yourprefrence;
    UserList userList;
String sit;
ProgressBar progressBar;
    ArrayList<UserList> brokerlist = new ArrayList<>();
    ArrayList<String> allbroker = new ArrayList<String>();
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plot);
        plot_number = findViewById(R.id.plot_number);
        plot_size = findViewById(R.id.plot_size);
        yourprefrence=Prefrence.getInstance(this);
        plot_description = findViewById(R.id.description);
        spinner= findViewById(R.id.spinner);
        progressBar=findViewById(R.id.progressbar);
        String type1="Broker";
        brokerlist(type1);
        spinner.setOnItemSelectedListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        sit=intent.getStringExtra("site_id");
        allbroker.add("Select Broker");

    }

    private void brokerlist(String type) {
        Call<ArrayList<UserList>> userResponse = ApiClient.getUserService()
                .allbroker("Bearer "+yourprefrence.getData(ConstantClass.TOKEN),type);
        userResponse.enqueue(new Callback<ArrayList<UserList>>() {
            @Override
            public void onResponse(Call<ArrayList<UserList>> call, Response<ArrayList<UserList>> response) {
                if (response.code()==200){
                    if (!response.body().isEmpty())
                    {


                 brokerlist=response.body();
                for(int i=0;i<brokerlist.size();i++){

                     allbroker.add(brokerlist.get(i).getUser_name().toString());


                }
                    ArrayAdapter<String> aa = new
                            ArrayAdapter<String>(getApplicationContext()
                            ,R.layout.spinner_dropdown_layout,allbroker);
                    aa.setDropDownViewResource(R.layout.spinner_layout);
                    spinner.setAdapter(aa);

                }
                    else {
                        if (allbroker.isEmpty())
                        {

                            allbroker.add("No Brokers");
                            ArrayAdapter<String> aa = new
                                    ArrayAdapter<String>(getApplicationContext()
                                    ,R.layout.spinner_dropdown_layout,allbroker);
                            aa.setDropDownViewResource(R.layout.spinner_layout);
                            spinner.setAdapter(aa);
                        }

                    }
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
        brokerselected= (String) parent.getItemAtPosition(position);
        position=parent.getSelectedItemPosition();

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    public void plotadd(View view) {

        String plotno=plot_number.getEditText().getText().toString().trim();
        String size=plot_size.getEditText().getText().toString().trim();
        String descrip=plot_description.getEditText().getText().toString().trim();
        String broker=spinner.getSelectedItem().toString().trim();
        broker_id=brokerlist.get(position).getId();

        if (plotno.isEmpty())
        {
            plot_number.setError("Enter plot number");
        }
        else if (size.isEmpty())
        {
            plot_size.setError("Enter the plot size");
        }
        else if (broker.equals("Select Broker"))
        {
            broker=null;
            progressBar.setVisibility(View.VISIBLE);

            PlotModel plotModel= new PlotModel(plotno,size,descrip,broker,yourprefrence.getData(SiteUtils.ID));
            addsingleplot(plotModel);
        }
        else {
            broker=broker_id;
            progressBar.setVisibility(View.VISIBLE);
            PlotModel plotModel= new PlotModel(plotno,size,descrip,broker,yourprefrence.getData(SiteUtils.ID));
            addsingleplot(plotModel);
        }

    }

    private void addsingleplot(PlotModel plotModel) {
        Call<PlotModel> userResponse =ApiClient.getUserService()
                .addplot("Bearer "+yourprefrence
                                .getData(ConstantClass.TOKEN),plotModel);
        userResponse.enqueue(new Callback<PlotModel>() {
            @Override
            public void onResponse(Call<PlotModel> call, Response<PlotModel> response) {
                if (response.code()==201){
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(AddPlotActivity.this,PlotListActivity.class));
                    Toast.makeText(getApplicationContext(),"Plot Added Sucessfully..",Toast.LENGTH_SHORT).show();

                }
                else {
                    progressBar.setVisibility(View.GONE);
                   Toast.makeText(getApplicationContext(),"Failed to add plot",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlotModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Failed  some thing went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }


}