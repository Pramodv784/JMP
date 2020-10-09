package com.android.skyheight.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.skyheight.R;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.PlotExpenseModel;
import com.android.skyheight.model.PlotListModel;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlotExpenseCalActivtiy extends AppCompatActivity  {
    EditText labour_charge, construction_materials, electric_charge,
            maintenance_charge, other_charges, remark;
    Button start_date, end_date;
    TextView start_date_set, end_date_set;
    private int mYear, mMonth, mDay;
    Spinner spinner;
    Prefrence yourprefrence;
    String plotselected;
    String site_id, plot_id;
    ProgressBar progressBar;
    ArrayList<PlotListModel> plotlist = new ArrayList<>();
    ArrayList<String> allplot = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_expense_cal_activtiy);
        labour_charge = findViewById(R.id.labour_charge);
        construction_materials = findViewById(R.id.construction_materials);
        electric_charge = findViewById(R.id.electric_charge);
        maintenance_charge = findViewById(R.id.maintenance_charge);
        other_charges = findViewById(R.id.other_charges);
        remark = findViewById(R.id.remark);
        progressBar=findViewById(R.id.progressbar);
        yourprefrence = Prefrence.getInstance(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        plot_id=intent.getStringExtra("id");
        labour_charge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()>0) {

                    labour_charge.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rupees_icon,0,0,0);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()==0){
                    labour_charge.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                }
            }
        });
        construction_materials.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()>0) {

                    construction_materials.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rupees_icon,0,0,0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()==0){
                    construction_materials.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                }
            }
        });
        electric_charge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()>0) {

                    electric_charge.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rupees_icon,0,0,0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()==0){
                    electric_charge.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                }
            }
        });
        maintenance_charge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()>0) {

                    maintenance_charge.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rupees_icon,0,0,0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()==0){
                    maintenance_charge.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                }
            }
        });
        other_charges.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()>0) {

                    other_charges.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rupees_icon,0,0,0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()==0){
                    other_charges.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                }
            }
        });
      /*  start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(PlotExpenseCalActivtiy.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                start_date_set.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }


        });
        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(PlotExpenseCalActivtiy.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                end_date_set.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }


        });
*/

    }

    public void addexpense(View view) {

        if (labour_charge.getText().toString().isEmpty()){
            labour_charge.setError("Please Enter the Labour Charge ");
            labour_charge.requestFocus();
        }
        else if (maintenance_charge.getText().toString().isEmpty()){
            maintenance_charge.setError("Please Enter the Material Charge ");
            maintenance_charge.requestFocus();
        }
        else if (electric_charge.getText().toString().isEmpty()){
            electric_charge.setError("Please Enter the Electricity Charge ");
            electric_charge.requestFocus();
        }
        else if (maintenance_charge.getText().toString().isEmpty()){
            maintenance_charge.setError("Please Enter the Maintenance Charge ");
            maintenance_charge.requestFocus();
        }

        else {
            progressBar.setVisibility(View.VISIBLE);
            float labour= Float.parseFloat(labour_charge.getText().toString());
            float material= Float.parseFloat(construction_materials.getText().toString().trim());
            float electric= Float.parseFloat(electric_charge.getText().toString().trim());
            float maintence= Float.parseFloat(maintenance_charge.getText().toString().trim());
            float other= Float.parseFloat(other_charges.getText().toString().trim());
            String remar=remark.getText().toString().trim();

            PlotExpenseModel plotExpenseModel= new PlotExpenseModel(plot_id,remar,material,
                    labour,electric,maintence,other);
            expense(plotExpenseModel);
        }

    }

    private void expense(PlotExpenseModel plotExpenseModel) {

        Call<PlotExpenseModel> userResponse = ApiClient.getUserService()
                .plotexpense("Bearer "+yourprefrence.getData(ConstantClass.TOKEN)
                ,plotExpenseModel);
        userResponse.enqueue(new Callback<PlotExpenseModel>() {
            @Override
            public void onResponse(Call<PlotExpenseModel> call, Response<PlotExpenseModel> response) {
                if (response.code()==201){
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(PlotExpenseCalActivtiy.this,PlotListActivity.class));
                    Toast.makeText(getApplicationContext(),"Expense Added",Toast.LENGTH_SHORT).show();
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Expense Added Fail",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlotExpenseModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Expense Added Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }
}