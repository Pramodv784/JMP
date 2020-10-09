package com.android.skyheight.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.skyheight.BuildConfig;
import com.android.skyheight.R;
import com.android.skyheight.model.PlotSummaryModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlotSumaryActivity extends AppCompatActivity {
    TextView sitename,constcharge,labourcharge,electriccharge,other,sitedate,total,btn,created_date,plot_number;
    LinearLayout llScroll;
    private Bitmap bitmap;
    String targetPdf = "/sdcard/pdfapp.pdf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_sumaary);
        sitename=findViewById(R.id.sitename);
        constcharge=findViewById(R.id.construction_materials);
        labourcharge=findViewById(R.id.labour_charge);
        electriccharge=findViewById(R.id.electric_charge);
        other=findViewById(R.id.other_charges);
        btn=findViewById(R.id.pdf);
        llScroll=findViewById(R.id.llScroll);
        total=findViewById(R.id.total_charges);
        created_date=findViewById(R.id.created_date);
        plot_number=findViewById(R.id.plotnumber3);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("BUNDLE");
        PlotSummaryModel plotSummaryModel= (PlotSummaryModel) bundle.getSerializable("ARRAYLIST");
        String date=plotSummaryModel.getCreated_at();
        SimpleDateFormat inputformat= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        try {
            Date parsedDate = inputformat.parse(date);
            String formattedDate = outputFormat.format(parsedDate);
            created_date.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        plot_number.setText(String.valueOf(plotSummaryModel.getPlot().getPlot_number()));
        constcharge.setText("₹ "+String.valueOf(plotSummaryModel.getConstruction_material()));
        labourcharge.setText("₹ "+String.valueOf(plotSummaryModel.getLabour_charges()));
        electriccharge.setText("₹ "+String.valueOf(plotSummaryModel.getElectricity()));
        other.setText("₹ "+String.valueOf(plotSummaryModel.getOthers()));
        total.setText("₹ "+String.valueOf(plotSummaryModel.getTotal_plot_expense()));
        total.setTextColor(Color.parseColor("#00C853"));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pdfmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.pdf:
                bitmap = loadBitmapFromView(llScroll, llScroll.getWidth(), llScroll.getHeight());
                createPdf();
        }
        return  super.onOptionsItemSelected(item);
    }

    public void pdf(MenuItem item) {
        Log.d("size"," "+llScroll.getWidth() +"  "+llScroll.getWidth());

    }
    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }
    private void createPdf(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            document = new PdfDocument();
        }
        PdfDocument.PageInfo pageInfo = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        }
        PdfDocument.Page page = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            page = document.startPage(pageInfo);
        }

        Canvas canvas = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            canvas = page.getCanvas();
        }

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            document.finishPage(page);
        }

        // write the document content

        File filePath;
        filePath = new File(targetPdf);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                document.writeTo(new FileOutputStream(filePath));
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            document.close();
        }
        Toast.makeText(this, "PDF of Scroll is created!!!", Toast.LENGTH_SHORT).show();

        openGeneratedPDF();

    }
    private void openGeneratedPDF(){
        File file = new File(targetPdf);
        if (file.exists())
        {

            Intent intent=new Intent(Intent.ACTION_VIEW);
            Uri uri1= FileProvider.getUriForFile(PlotSumaryActivity.this, BuildConfig.APPLICATION_ID +".provider",file);
            // Uri uri = Uri.fromFile(file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri1,"application/pdf");
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(PlotSumaryActivity.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
    }


}