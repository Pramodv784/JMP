package com.android.skyheight.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.skyheight.R;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.PlotListModel;
import com.android.skyheight.model.PlotUpdateModel;
import com.android.skyheight.model.UserList;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;
import com.android.skyheight.utils.SiteUtils;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlotDetailActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{
    EditText  plot_size1,plot_size2,plot_description,plot_owner;
    RadioButton btn_book,btn_unbook;
    TextView plot_number,plot_size;
    Prefrence yourprefrence;
    RadioButton booking,unbook;
    String plot_id;
    Boolean status;
    int plotSize;
    String brokerselected;
    String broker_id;
    Spinner spinner;
    ProgressBar progressBar;
    RadioGroup radiogrp;
    ConstraintLayout constraint;
    ArrayList<UserList> brokerlist = new ArrayList<>();
    ArrayList<String> allbroker = new ArrayList<String>();
    TextView numberDisplay, operationsDisplay;
    HorizontalScrollView scrollerDisplayNumber, scrollerDisplayOperations;
    Button button1, button2, button3, button4, button5, button6, button7, button8,
            button9, button0, buttonCE, buttonRoot, buttonBracketsOpen, buttonBracketsClose,
            buttonPI, buttonExponentation, buttonSum, buttonSubtraction, buttonMultiplication, buttonDivision, buttonEqual;
    String stringNumber, stringSpecial;
    Expression expression;
    double value=0;
    boolean numberClicked=false;
    ClipboardManager clipboard;
    ClipData clip;
    int charBracketOpenCount=0, charBracketCloseCount=0, charInExceed=0, dotCount=0;
    char bracketOpen='(';
    char bracketClose=')';
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_detail);
        plot_number=findViewById(R.id.plot_number);
        plot_size=findViewById(R.id.plot_size);
        plot_size1=findViewById(R.id.plot_size1);
        plot_size2=findViewById(R.id.plot_size2);
        plot_description=findViewById(R.id.plot_description);
        //btn_book = findViewById(R.id.btn_book);
        yourprefrence=Prefrence.getInstance(this);
        plot_owner=findViewById(R.id.plot_owner);
       // btn_unbook =findViewById(R.id.btn_unbook);
        progressBar=findViewById(R.id.progressbar);
        constraint=findViewById(R.id.constraint);
        //radiogrp = findViewById(R.id.radiogrp);
        spinner= findViewById(R.id.spinner);
      //  booking=findViewById(R.id.btn_book);
       // unbook=findViewById(R.id.btn_unbook);
          getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner.setOnItemSelectedListener(this);
        String type1="Broker";
        brokerlist(type1);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        PlotListModel plotListModel = (PlotListModel) args.getSerializable("ARRAYLIST");
        plot_number.setText(plotListModel.getPlot_number());
  plot_size2.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      plot_size.setText("");
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
          if (s.toString().length()>0) {
              sum();


          }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
  });
       /* if (plotListModel.getSize()>0){
            plot_size.setText(String.valueOf(plotListModel.getSize()));
        }
        else {
            plot_size.setText("");
        }*/
        plot_description.setText(plotListModel.getDescription());

        plot_id=plotListModel.getId();

        constraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (plot_size.getText().toString().isEmpty()){
                    plot_size.setError("Enter the plot size");
                    plot_size.requestFocus();
                }
                 else {
                    //booking.isChecked(false)
                    //  booking.getId();

                  //  booking = findViewById(R.id.btn_book);
                   // int selectedId = radiogrp.getCheckedRadioButtonId();
                    //booking = (RadioButton) findViewById(selectedId);
/*
                    if (booking.isChecked()) {
                        status = false;
                        updateplot(status);

                    } else if (unbook.isChecked()) {
                        status =true;
                        updateplot(status);
                    } else {
                        unbook.isChecked();
                        status = true;
                        updateplot(status);
                    }*/
                  updateplot();

                }

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sum() {
        int len,breadth;
        len= Integer.parseInt(plot_size1.getText().toString());
        breadth= Integer.parseInt(plot_size2.getText().toString());
        plotSize=len*breadth;
        plot_size.setText(String.valueOf(plotSize));
    }

    private void brokerlist(String type) {
        Call<ArrayList<UserList>> userResponse = ApiClient.getUserService()
                .allbroker("Bearer "+yourprefrence.getData(ConstantClass.TOKEN),type);
        userResponse.enqueue(new Callback<ArrayList<UserList>>() {
            @Override
            public void onResponse(Call<ArrayList<UserList>> call, Response<ArrayList<UserList>> response) {
                if (response.code()==200){
                    brokerlist=response.body();


                    for(int i=0;i<brokerlist.size();i++){

                        allbroker.add(brokerlist.get(i).getUser_name().toString());
                        broker_id=brokerlist.get(i).getId();

                    }
                    ArrayAdapter<String> aa = new
                            ArrayAdapter<String>(getApplicationContext()
                            ,R.layout.spinner_dropdown_layout,allbroker);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.plotdetail_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete:
                showAlertDialog();
                break;
            case R.id.summary:
              Intent intent= new Intent(PlotDetailActivity.this,PlotSummaryListActivity.class);
              intent.putExtra("plot",plot_id);
              startActivity(intent);

        }
        return(super.onOptionsItemSelected(item));
    }


    public void updateplot() {
        progressBar.setVisibility(View.VISIBLE);
        String plot_no = plot_number.getText().toString().trim();
        String description=plot_description.getText().toString().trim();
        int site= Integer.parseInt(yourprefrence.getData(SiteUtils.ID));
        String p_id=plot_id;
        String owner=plot_owner.getText().toString().trim();
        PlotUpdateModel plotUpdateModel= new PlotUpdateModel(plot_no,
                description,plotSize,p_id,site,null,broker_id,owner);
        newupdate(plotUpdateModel,p_id);

    }
    private void newupdate(PlotUpdateModel plotUpdateModel,String id) {
        Call<PlotUpdateModel> userResponse = ApiClient.getUserService()
                .update_plot("Bearer "+yourprefrence
                        .getData(ConstantClass.TOKEN),id,plotUpdateModel);
        userResponse.enqueue(new Callback<PlotUpdateModel>() {
            @Override
            public void onResponse(Call<PlotUpdateModel> call, Response<PlotUpdateModel> response) {
                if (response.code()==200){
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(PlotDetailActivity.this,SiteListActivity.class));
                    Toast.makeText(getApplicationContext(),"Plot Updated",Toast.LENGTH_SHORT).show();

                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Failed to Update plot",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlotUpdateModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Failed some thing went wrong",Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void showAlertDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PlotDetailActivity.this);
        alertDialog.setTitle("Delete Plot");
        alertDialog.setMessage("Are you Sure want to delete this plot ?");
        alertDialog.setIcon(R.drawable.delete_icon);
        alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteplot(plot_id);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
    private void deleteplot(String id) {
        Call<PlotUpdateModel> userResponse =ApiClient.getUserService()
                .deleteplot("Bearer "+yourprefrence
                        .getData(ConstantClass.TOKEN),id);
        userResponse.enqueue(new Callback<PlotUpdateModel>() {
            @Override
            public void onResponse(Call<PlotUpdateModel> call, Response<PlotUpdateModel> response) {
                if (response.code()==204){

                    Toast.makeText(getApplicationContext(),"Sucessfulle Deleted Plot",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PlotDetailActivity.this,SiteListActivity.class));
                }
                else {
                    Toast.makeText(getApplicationContext(),"Failed to delete",Toast.LENGTH_SHORT).show();
                }
                 if(response.code()==500){
                    Toast.makeText(getApplicationContext(),"Plot not found",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlotUpdateModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Some thing went wrong",Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        brokerselected= (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*public void  {
        startActivity(new Intent(PlotDetailActivity.this,CalculatorActivity.class));
    }*/
    public void calculator(MenuItem item){

        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_calculator, null);

        numberDisplay = (TextView)dialogView.findViewById(R.id.displayNumber);
        operationsDisplay = (TextView) dialogView.findViewById(R.id.displayOperationNumber);
        scrollerDisplayNumber = (HorizontalScrollView)dialogView. findViewById(R.id.displayNumberScroller);
        scrollerDisplayOperations = (HorizontalScrollView)dialogView. findViewById(R.id.displayOperationNumberScroller);
        button1 = (Button) dialogView.findViewById(R.id.button1);
        button2 = (Button) dialogView.findViewById(R.id.button2);
        button3 = (Button) dialogView.findViewById(R.id.button3);
        button4 = (Button) dialogView.findViewById(R.id.button4);
        button5 = (Button) dialogView.findViewById(R.id.button5);
        button6 = (Button) dialogView.findViewById(R.id.button6);
        button7 = (Button) dialogView.findViewById(R.id.button7);
        button8 = (Button) dialogView.findViewById(R.id.button8);
        button9 = (Button) dialogView.findViewById(R.id.button9);
        button0 = (Button) dialogView.findViewById(R.id.button0);
        buttonCE = (Button) dialogView.findViewById(R.id.buttonCE);
        buttonRoot = (Button) dialogView.findViewById(R.id.buttonRoot);

        buttonPI = (Button) dialogView.findViewById(R.id.buttonPI);
        buttonExponentation = (Button) dialogView.findViewById(R.id.buttonExponentation);
        buttonSum = (Button) dialogView.findViewById(R.id.buttonSum);
        buttonSubtraction = (Button) dialogView.findViewById(R.id.buttonSubtraction);
        buttonMultiplication = (Button)dialogView. findViewById(R.id.buttonMultiplication);
        buttonDivision = (Button) dialogView.findViewById(R.id.buttonDivision);
        buttonEqual = (Button) dialogView.findViewById(R.id.buttonEqaul);
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
        Window window =dialogBuilder.getWindow();
        window.setLayout(1000,1400);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogBuilder.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogBuilder.getWindow().setAttributes(lp);
    }
    public void clickButton1(View v) {

        numberDisplay.setText(numberDisplay.getText()+"1");
        operationsDisplay.setText(operationsDisplay.getText()+"1");
        numberClicked=true;
    }

    public void clickButton2(View v) {

        numberDisplay.setText(numberDisplay.getText()+"2");
        operationsDisplay.setText(operationsDisplay.getText()+"2");
        numberClicked=true;
    }

    public void clickButton3(View v) {

        numberDisplay.setText(numberDisplay.getText()+"3");
        operationsDisplay.setText(operationsDisplay.getText()+"3");
        numberClicked=true;
    }

    public void clickButton4(View v) {

        numberDisplay.setText(numberDisplay.getText()+"4");
        operationsDisplay.setText(operationsDisplay.getText()+"4");
        numberClicked=true;
    }

    public void clickButton5(View v) {

        numberDisplay.setText(numberDisplay.getText()+"5");
        operationsDisplay.setText(operationsDisplay.getText()+"5");
        numberClicked=true;
    }

    public void clickButton6(View v) {

        numberDisplay.setText(numberDisplay.getText()+"6");
        operationsDisplay.setText(operationsDisplay.getText()+"6");
        numberClicked=true;
    }

    public void clickButton7(View v) {

        numberDisplay.setText(numberDisplay.getText()+"7");
        operationsDisplay.setText(operationsDisplay.getText()+"7");
        numberClicked=true;
    }

    public void clickButton8(View v) {

        numberDisplay.setText(numberDisplay.getText()+"8");
        operationsDisplay.setText(operationsDisplay.getText()+"8");
        numberClicked=true;
    }

    public void clickButton9(View v) {

        numberDisplay.setText(numberDisplay.getText()+"9");
        operationsDisplay.setText(operationsDisplay.getText()+"9");
        numberClicked=true;
    }

    public void clickButton0(View v) {

        numberDisplay.setText(numberDisplay.getText()+"0");
        operationsDisplay.setText(operationsDisplay.getText()+"0");
        numberClicked=true;
    }

    public void clickButtonDot(View v) {

        stringSpecial = operationsDisplay.getText().toString();

        if (numberClicked == false) {

        }

        else if(stringSpecial.endsWith("(") || stringSpecial.endsWith("+") || stringSpecial.endsWith("-") || stringSpecial.endsWith("*") || stringSpecial.endsWith("/")) {

        }

        else {
            if (dotCount == 1) {

            } else {
                dotCount++;
                operationsDisplay.setText(operationsDisplay.getText() + ".");
                stringSpecial = numberDisplay.getText().toString();
                if(!stringSpecial.contains(".")) {
                    numberDisplay.setText(numberDisplay.getText() + "."); }
            }
        }
    }
    public void clickButtonExponentation(View view) {

        stringSpecial = numberDisplay.getText().toString();
        if(numberClicked == false) {

        }

        else if(stringSpecial.endsWith("^")) {

        }

        else {
            numberDisplay.setText(numberDisplay.getText() + "^");
            operationsDisplay.setText(operationsDisplay.getText() + "^");

        }

    }

    public void clickButtonSquareRoot(View v) {

        numberDisplay.setText(numberDisplay.getText()+"√(");
        operationsDisplay.setText(operationsDisplay.getText()+"sqrt(");
        numberClicked=false;
        charBracketOpenCount=0;
        charBracketCloseCount=0;
        charInExceed=0;
        dotCount=0;

    }





    public void clickButtonPI(View v) {

        numberDisplay.setText(numberDisplay.getText() + "π");
        operationsDisplay.setText(operationsDisplay.getText() + "π");
        numberClicked = true;

    }

    public void clickButtonAddition(View v) {

        checkNumberDisplay();

        stringSpecial = operationsDisplay.getText().toString();

        if(stringSpecial.isEmpty()) {

        }

        else if(stringSpecial.charAt(stringSpecial.length()-1)=='+' || stringSpecial.charAt(stringSpecial.length()-1)=='-' || stringSpecial.charAt(stringSpecial.length()-1)=='*' || stringSpecial.charAt(stringSpecial.length()-1)=='/') {

        }

        else {
            buttonCE.setText("DEL");
            operationsDisplay.setText(operationsDisplay.getText() + "+");
            numberDisplay.setText(null);
            numberClicked=false;
            charBracketCloseCount=0;
            charBracketCloseCount=0;
            charBracketOpenCount=0;
            dotCount=0;

        }


    }

    public void clickButtonSubtraction(View v) {

        checkNumberDisplay();

        stringSpecial = operationsDisplay.getText().toString();

        if(stringSpecial.endsWith("sqrt(")) {

        }

        else {
            buttonCE.setText("DEL");
            operationsDisplay.setText(operationsDisplay.getText() + "-");
            numberDisplay.setText("-");
            numberClicked = false;
            charBracketCloseCount = 0;
            charBracketCloseCount = 0;
            charBracketOpenCount = 0;
            dotCount = 0;
        }

    }

    public void clickButtonMultiplication(View v) {

        checkNumberDisplay();


        stringSpecial = operationsDisplay.getText().toString();

        if(stringSpecial.isEmpty()) {

        }

        else if(stringSpecial.charAt(stringSpecial.length()-1)=='(') {

        }

        else if(stringSpecial.charAt(stringSpecial.length()-1)=='+' || stringSpecial.charAt(stringSpecial.length()-1)=='-' || stringSpecial.charAt(stringSpecial.length()-1)=='*' || stringSpecial.charAt(stringSpecial.length()-1)=='/') {

        }

        else {
            buttonCE.setText("DEL");
            operationsDisplay.setText(operationsDisplay.getText() + "*");
            numberDisplay.setText(null);
            numberClicked=false;
            charBracketCloseCount=0;
            charBracketCloseCount=0;
            charBracketOpenCount=0;
            dotCount=0;
        }
    }

    public void clickButtonDivision(View v) {

        checkNumberDisplay();

        stringSpecial = operationsDisplay.getText().toString();

        if(stringSpecial.isEmpty()) {

        }

        else if(stringSpecial.charAt(stringSpecial.length()-1)=='(') {

        }

        else if(stringSpecial.charAt(stringSpecial.length()-1)=='+' || stringSpecial.charAt(stringSpecial.length()-1)=='-' || stringSpecial.charAt(stringSpecial.length()-1)=='*' || stringSpecial.charAt(stringSpecial.length()-1)=='/') {

        }

        else {
            numberClicked=false;
            buttonCE.setText("DEL");
            operationsDisplay.setText(operationsDisplay.getText() + "/");
            numberDisplay.setText(null);
            numberClicked=false;
            charBracketCloseCount=0;
            charBracketCloseCount=0;
            charBracketOpenCount=0;
            dotCount=0;
        }
    }

    public void checkNumberDisplay() {
        stringSpecial = numberDisplay.getText().toString();
        if(Double.toString(value).equals(stringSpecial)) {
            operationsDisplay.setText(stringSpecial);
        }
    }

    public void checkBracketsNumber() {

        charBracketCloseCount=0;
        charBracketOpenCount=0;
        charInExceed=0;

        for (int i = 0; i < stringNumber.length(); i++) {
            if (stringNumber.charAt(i) == bracketOpen)
                charBracketOpenCount++;

            else if (stringNumber.charAt(i) == bracketClose)
                charBracketCloseCount++;
        }

        if (charBracketOpenCount == charBracketCloseCount) {

        }

        else if (charBracketOpenCount > charBracketCloseCount) {
            charInExceed = charBracketOpenCount - charBracketCloseCount;

            for (int j = 0; j < charInExceed; j++) {
                stringNumber = stringNumber + ")";
            }
        }
    }

    public void clickButtonEqual(View v) {

        if(numberClicked==false) {

        }

        else {

            stringNumber = operationsDisplay.getText().toString();

            checkBracketsNumber();

            if(charBracketCloseCount > charBracketOpenCount) {
                numberDisplay.setText("Invalid expression");
            }

            else if(stringNumber.contains("Infinity")) {
                numberDisplay.setText("Infinity");
            }

            else {

                expression = new ExpressionBuilder(stringNumber).build();

                try {
                    value = expression.evaluate();
                    numberDisplay.setText(Double.toString(value));
                }

                catch (ArithmeticException e) {
                    numberDisplay.setText("Can't divide by 0");
                }

                buttonCE.setText("CLR");
            }
        }

    }

    public void clickNumberDisplay(View v) {

        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clip = ClipData.newPlainText("number", numberDisplay.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(PlotDetailActivity.this, "Copied result to clipboard", Toast.LENGTH_SHORT).show();
    }

    public void clickOperationsDisplay(View v) {

        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clip = ClipData.newPlainText("operations", operationsDisplay.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(PlotDetailActivity.this, "Copied operations to clipboard", Toast.LENGTH_SHORT).show();
    }

    public void clickButtonCE() {

        buttonCE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stringSpecial = operationsDisplay.getText().toString();

                if(!stringSpecial.isEmpty()) {

                    if (Double.toString(value).equals(numberDisplay.getText().toString())) {

                    }

                    else if(stringSpecial.endsWith(".")) {
                        dotCount=0;
                        stringSpecial = stringSpecial.substring(0, stringSpecial.length()-1);
                        operationsDisplay.setText(stringSpecial);

                    }

                    else {
                        stringSpecial = stringSpecial.substring(0, stringSpecial.length() - 1);
                        operationsDisplay.setText(stringSpecial);
                        numberClicked=false;
                        if(stringSpecial.endsWith("1") || stringSpecial.endsWith("2") || stringSpecial.endsWith("3") || stringSpecial.endsWith("4") || stringSpecial.endsWith("5") || stringSpecial.endsWith("6") || stringSpecial.endsWith("7") || stringSpecial.endsWith("8") || stringSpecial.endsWith("9") || stringSpecial.endsWith("0") || stringSpecial.endsWith(")")) {
                            numberClicked=true;
                        }
                    }
                }

                stringSpecial = numberDisplay.getText().toString();

                if(!stringSpecial.isEmpty()) {

                    if (Double.toString(value).equals(numberDisplay.getText().toString())) {

                    }

                    else if(stringSpecial.equals("Invalid expression") || stringSpecial.equals("Can't divide by 0")) {

                    }

                    else if (stringSpecial.endsWith("sin(") || stringSpecial.endsWith("cos(") || stringSpecial.endsWith("tan(")) {
                        stringSpecial = stringSpecial.substring(0, stringSpecial.length() - 4);
                        numberDisplay.setText(stringSpecial);
                    }

                    else if(stringSpecial.endsWith("√(")) {
                        stringSpecial = stringSpecial.substring(0, stringSpecial.length()-2);
                        numberDisplay.setText(stringSpecial);
                    }

                    else {

                        stringSpecial = stringSpecial.substring(0, stringSpecial.length() - 1);
                        numberDisplay.setText(stringSpecial);
                    }
                }

            }
        });


        buttonCE.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                numberDisplay.setText(null);
                operationsDisplay.setText(null);
                stringNumber="";
                stringSpecial="";
                value=0;
                numberClicked=false;
                charBracketCloseCount=0;
                charBracketOpenCount=0;
                charInExceed=0;
                dotCount=0;
                buttonCE.setText("DEL");
                return true;
            }
        });
    }

    public void plotexpense(MenuItem item) {
       Intent intent = new Intent(PlotDetailActivity.this,PlotExpenseCalActivtiy.class);
       intent.putExtra("id",plot_id);
       startActivity(intent);
    }
}