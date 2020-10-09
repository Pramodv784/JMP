package com.android.skyheight.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.skyheight.R;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.SiteExpenseModel;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;
import com.android.skyheight.utils.SiteUtils;
import com.basgeekball.awesomevalidation.AwesomeValidation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteExpensesActivity extends AppCompatActivity {
    EditText labour_charge,construction_materials,electric_charge,
            maintenance_charge,other_charges,remark;
    Button start_date,end_date;
    TextView start_date_set,end_date_set;
    private int mYear, mMonth, mDay;
    String site;
    Prefrence yourprefrence;
    ProgressBar progressBar;
    private AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_expenses);
        labour_charge=findViewById(R.id.labour_charge);
        construction_materials=findViewById(R.id.construction_materials);
        electric_charge=findViewById(R.id.electric_charge);
        maintenance_charge=findViewById(R.id.maintenance_charge);
        other_charges=findViewById(R.id.other_charges);
        yourprefrence =Prefrence.getInstance(this);
        remark=findViewById(R.id.remark);
        progressBar=findViewById(R.id.progressbar);
        Intent intent = getIntent();
        site=intent.getStringExtra("site");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
       /* start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(SiteExpensesActivity.this,
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


                DatePickerDialog datePickerDialog = new DatePickerDialog(SiteExpensesActivity.this,
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

    public void submit(View view) {


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
            SiteExpenseModel siteExpenseModel= new SiteExpenseModel(remar,material,
                    labour,electric,maintence,other,yourprefrence.getData(SiteUtils.ID));
            expense(siteExpenseModel);
        }

    }

    private void expense(SiteExpenseModel siteExpenseModel) {
        Call<SiteExpenseModel> userResponse = ApiClient.getUserService()
                .siteexpense("Bearer "+yourprefrence.getData(ConstantClass.TOKEN)
                ,siteExpenseModel);
        userResponse.enqueue(new Callback<SiteExpenseModel>() {
            @Override
            public void onResponse(Call<SiteExpenseModel> call, Response<SiteExpenseModel> response) {
                if (response.code()==201)

                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Expense Added",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SiteExpensesActivity.this,SiteUpdateActivity.class));
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Expense failed",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SiteExpenseModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }
}

