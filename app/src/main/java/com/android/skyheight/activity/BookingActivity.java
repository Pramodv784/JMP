package com.android.skyheight.activity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.skyheight.R;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.BookingModel;
import com.android.skyheight.model.PlotListModel;
import com.android.skyheight.model.SiteListModel;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;
import com.sayantan.advancedspinner.MultiSpinner;
import com.sayantan.advancedspinner.MultiSpinnerListener;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener  {
    Spinner spinner;
    String plotOwner;
    int amount;
    String site_id,plot_id;
    String siteselected;
    Prefrence yourprefrence;
    LinearLayout plottext;
    TextView total_amount,remain;
    int no_plot=0;
    String site_price;
    List<Integer> plot_size1= new ArrayList<>();
    MultiSpinner multi_two;
    ProgressBar progressBar;
    String[] listItems = new String[]{};
    EditText paid_amount,plot_owner;
    boolean[] checkedItems;
    ArrayList<SiteListModel> sitelist = new ArrayList<>();
    ArrayList<String> allsite = new ArrayList<String>();
    ArrayList<PlotListModel> plotlist = new ArrayList<>();
    ArrayList<Integer> allplots = new ArrayList<Integer>();
    ArrayList<PlotListModel> mUserItems = new ArrayList<>();
   ArrayList<String> spinnerlist =new ArrayList<>();
    List<String> choice =new ArrayList<>();
    List<Integer> plotsno=new ArrayList<>();
    ProgressDialog progressDialog;
    int plot_total_amount=0;
    int remaining_amount;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        spinner= findViewById(R.id.spinner);
        multi_two= findViewById(R.id.multi_two);
        yourprefrence=Prefrence.getInstance(this);
        progressBar=findViewById(R.id.progressbar2);
        spinner.setOnItemSelectedListener(this);
        paid_amount=findViewById(R.id.paid_amount);
        plot_owner=findViewById(R.id.plot_owner);
        total_amount=findViewById(R.id.total_amount);
        remain=findViewById(R.id.remain);



       paid_amount.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               remain.setText("");
           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (s.toString().length()>0) {

               plotdata();
               }
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sitelist();

        progressDialog =new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

    }



    private void sitelist() {
        Call<ArrayList<SiteListModel>> userResponseCall = ApiClient.getUserService()
                .usersitelist();
        userResponseCall.enqueue(new Callback<ArrayList<SiteListModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SiteListModel>> call,
                                   Response<ArrayList<SiteListModel>> response) {
                if (response.code()==200){
                    if (!response.body().isEmpty()) {
                        sitelist = response.body();
                        for(int i=0;i<sitelist.size();i++){
                            allsite.add(sitelist.get(i).getName().toString());
                            site_id=sitelist.get(i).getid();
                            site_price=sitelist.get(i).getPrice();

                        }
                        ArrayAdapter<String> aa = new
                                ArrayAdapter<String>(getApplicationContext()
                                ,R.layout.spinner_dropdown_layout,allsite);
                        aa.setDropDownViewResource(R.layout.spinner_layout);
                        spinner.setAdapter(aa);
                    }
                    else {
                    }
                }
                else if (response.code()==401){

                }
                else {

                    Toast.makeText(getApplicationContext(),"List Failed ",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<SiteListModel>> call, Throwable t) {

                Log.e( "onResponse: ","failed"+t);
                Toast.makeText(getApplicationContext(),"Please check your Internet connection ",Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        siteselected= (String) parent.getItemAtPosition(position);
        site_id =sitelist.get(position).getid();
        plotset();
    }
    private void plotset() {
 progressDialog.show();
        plot(site_id);
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void plot(String site_id) {
        Call<ArrayList<PlotListModel>> userResponse =ApiClient.getUserService()
                .getplot("Bearer "+yourprefrence.getData(ConstantClass.TOKEN),site_id);
     userResponse.enqueue(new Callback<ArrayList<PlotListModel>>() {
         @Override
         public void onResponse(Call<ArrayList<PlotListModel>> call, Response<ArrayList<PlotListModel>> response) {
             if (response.code()==200) {
                 progressDialog.dismiss();
                 spinnerlist.clear();
                 plotlist = response.body();
                 int i;
                 for (i = 0; i < plotlist.size(); i++)
                    if (plotlist.isEmpty())
                    {
                       Toast.makeText(getApplicationContext(),"Plotlist",Toast.LENGTH_SHORT).show();
                    }
                 else {
                        spinnerlist.add(plotlist.get(i).getPlot_number());
                        //plot_size1.add(plotlist.get(i).getTotal_plot_amount());
                    }



             }
                 multi_two.setSpinnerList(spinnerlist);
                 multi_two.addOnItemsSelectedListener(new MultiSpinnerListener() {
                     @Override
                     public void onItemsSelected(List<String> choices, boolean[] selected) {
                         int i;
                         for (i = 0; i < choices.size(); i++)
                             choice.add(plotlist.get(i).getId());
                         plot_size1.add(plotlist.get(i).getTotal_plot_amount());
                         no_plotadd();
                         // plotbook();
                         // Toast.makeText(BookingActivity.this, choices.get(i), Toast.LENGTH_SHORT).show();
                     }
                 });
                 multi_two.setLayout(R.layout.spinner_dropdown_layout);

                   //Toast.makeText(getApplicationContext(),"Plot"+plotlist,Toast.LENGTH_SHORT).show();


             for (int i=0;i<plot_size1.size();i++)
             {
                 plot_total_amount+=plot_size1.get(i);
             }
             total_amount.setText("Total Amount  ₹ "+String.valueOf(plot_total_amount));
             Log.e("dayta", "total>>>"+plot_total_amount);

            /* for (int i=0;i<demolist.size();i++)
             {
                 plot_total_amount+=demolist.get(i);
             }
             total_amount.setText("Total Amount  ₹ "+String.valueOf(plot_total_amount));

             Log.e("dayta", "total>>>"+plot_total_amount);
*/


         }


         @Override
         public void onFailure(Call<ArrayList<PlotListModel>> call, Throwable t) {
             progressDialog.dismiss();

         }
     });
    }

    private void no_plotadd() {
        for (int i=0;i<choice.size();i++){
            if (choice.get(i)!=null){

                //plotstatus(choice.get(i));
                plotsno.add(Integer.valueOf(choice.get(i)));

            }
            else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Please Update The size of Plot",Toast.LENGTH_LONG).show();
            }


        }


    }

    public void plotdata()
{

     amount= Integer.parseInt(paid_amount.getText().toString());
    Log.i("data","lis>>>"+plotOwner);
    if (amount==plot_total_amount)
    {
        remain.setText("Remaining Amount  ₹  0");
    }
    else if (amount>plot_total_amount){
        remain.setText("Extra Amount Paid  ₹ "+(-(plot_total_amount-amount)));
        remaining_amount=-plot_total_amount-amount;

    }
    else {
        remain.setText("Remaining Amount  ₹ "+(plot_total_amount-amount));
        //remaining_amount= Integer.parseInt(remain.getText().toString());
        remaining_amount=plot_total_amount-amount;

    }

}
public void plotsetId(View view)
{
    plotOwner=plot_owner.getText().toString().trim();
   if (plot_owner.getText().toString().isEmpty())
   {
       plot_owner.setError("Please Enter PLot Owner ");
       plot_owner.requestFocus();
   }
   else {
       progressBar.setVisibility(View.VISIBLE);
       BookingModel bookingModel= new BookingModel(plotsno,plotOwner,amount,remaining_amount,plot_total_amount);
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
        startActivity(new Intent(BookingActivity.this,AdminViewActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.booking_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.calculator:
                calculator();
        }

        return super.onOptionsItemSelected(item);
    }

    private void calculator() {



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
         clickButtonCE();
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
        Toast.makeText(BookingActivity.this, "Copied result to clipboard", Toast.LENGTH_SHORT).show();
    }

    public void clickOperationsDisplay(View v) {

        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clip = ClipData.newPlainText("operations", operationsDisplay.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(BookingActivity.this, "Copied operations to clipboard", Toast.LENGTH_SHORT).show();
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
}