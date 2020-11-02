package com.android.skyheight.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.skyheight.R;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.BookingModel;
import com.android.skyheight.model.PlotListModel;
import com.android.skyheight.model.UserDetail;
import com.android.skyheight.model.UserList;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookSinglePlotActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
Spinner userspinner;
EditText amount;
TextView remain,total_amount,plot_numner;
String userselected,user_id;
ProgressDialog progressDialog;
Prefrence yourprefrence;
ArrayList<UserList> userlist=new ArrayList<>();
    ArrayList<String> allcustomer = new ArrayList<String>();
    PlotListModel plotListModel;
    ProgressBar progressBar;
    private List<Integer> plot_ids = new ArrayList<>();
    String usertype="Customer";
    int total=0;
    int remaining_amount=0;
    int paid,id;
    String site;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_single_plot);
        userspinner=findViewById(R.id.userspinner);
        amount=findViewById(R.id.paid_amount);
        plot_numner=findViewById(R.id.plot_number);
        remain=findViewById(R.id.remain);
        progressBar=findViewById(R.id.progressbar2);
        total_amount=findViewById(R.id.total_amount);
        yourprefrence=Prefrence.getInstance(this);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        plotListModel = (PlotListModel) args.getSerializable("ARRAYLIST");
        plot_numner.setText(plotListModel.getPlot_number());
        total_amount.setText(String.valueOf(plotListModel.getTotal_plot_amount()));
        site=plotListModel.getSite();
        userlist(usertype);
          id= Integer.parseInt(plotListModel.getId());
        Log.i("","plot id>>"+id);

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                remain.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()>0) {

                    plotdata();
                    remain.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



    private void userlist(String usertype) {
        Call<ArrayList<UserList>> userResponse= ApiClient.getUserService().allcustomer("Bearer "+yourprefrence
                .getData(ConstantClass.TOKEN),usertype);
        userResponse.enqueue(new Callback<ArrayList<UserList>>() {
            @Override
            public void onResponse(Call<ArrayList<UserList>> call, Response<ArrayList<UserList>> response) {
                if (response.code()==200)
                {

                    userlist=response.body();
                    for (int i=0;i<userlist.size();i++)
                    {
                        allcustomer.add(userlist.get(i).getUser_name().substring(0,1).toUpperCase()+userlist.get(i)
                                .getUser_name().substring(1).toLowerCase());
                       // user_id=userlist.get(i).getId();
                    }
                    ArrayAdapter<String> aa1 = new
                            ArrayAdapter<String>(getApplicationContext()
                            ,R.layout.spinner_dropdown_layout,allcustomer);
                    aa1.setDropDownViewResource(R.layout.spinner_layout);
                    userspinner.setAdapter(aa1);
                }
                else {

                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserList>> call, Throwable t) {

                Toast.makeText(getApplicationContext(),"User List Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
     userselected= (String) parent.getItemAtPosition(position);

        Log.i("","user id >>"+user_id);
    }

    private void plotdata() {
        total=plotListModel.getTotal_plot_amount();
         paid= Integer.parseInt(amount.getText().toString());
        String t = userspinner.getSelectedItem().toString().trim();
        Log.i("","user id >>"+user_id);
        int postion=userspinner.getSelectedItemPosition();
        user_id=userlist.get(postion).getId();
        // Log.i("data","lis>>>"+plotOwner);
        if (paid==total)
        {
            remain.setText("Remaining Amount  ₹  0");
        }
        else if (paid>total){
            remain.setText("Extra Amount Paid  ₹ "+(-(total-paid)));
            remaining_amount=-total-paid;
        }
        else {
            remain.setText("Remaining Amount  ₹ "+(total-paid));
            //remaining_amount= Integer.parseInt(remain.getText().toString());
            remaining_amount=total-paid;
        }

    }
    public void booksingle(View view) {
        plot_ids.clear();
        plot_ids.add(id);
        if (amount.toString().isEmpty())
        {
            amount.setError("Enter Amount ");
            amount.requestFocus();
        }
        else {
            Log.i("","user id >>"+user_id);
            Toast.makeText(getApplicationContext(),"ID is "+user_id,Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.VISIBLE);
            BookingModel bookingModel= new BookingModel(plot_ids,user_id,paid,remaining_amount,total,site);
            Log.i("data","dataaaa>>>"+bookingModel);
            plotstatus(bookingModel);
        }






    }

        private void plotstatus(BookingModel bookingModel) {
            Call<PlotListModel> userResponse=ApiClient.getUserService()
                    .plotbook("Bearer  "+yourprefrence.getData(ConstantClass.TOKEN),bookingModel);
            userResponse.enqueue(new Callback<PlotListModel>() {
                @Override
                public void onResponse(Call<PlotListModel> call, Response<PlotListModel> response) {
                    if (response.code()==200)
                    {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"PLot is Booked",Toast.LENGTH_SHORT).show();
                        showCustomDialog();
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"PLot is Booked Failed",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<PlotListModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext()," Failed",Toast.LENGTH_SHORT).show();
                }
            });
        }
    private void showCustomDialog() {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.bookeddialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setContentView(R.layout.bookeddialog);
        alertDialog.show();
    }
    public void ok(View view) {
        startActivity(new Intent(BookSinglePlotActivity.this,PlotListActivity.class));
    }
}