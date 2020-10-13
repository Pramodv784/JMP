package com.android.skyheight.activity;

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
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.skyheight.BuildConfig;
import com.android.skyheight.R;
import com.android.skyheight.model.BookingModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BookedPrintActivity extends AppCompatActivity {
TextView sitename,no_plot,remaining,total;
BookingModel bookingModel;
    LinearLayout llScroll;
    private Bitmap bitmap;
    ListView plotlist;
    ArrayList<Integer> list= new ArrayList<>() ;
    String targetPdf = "/sdcard/pdfapp.pdf";

    RelativeLayout relativeLayout;
    ListView listView;
    ArrayList<Integer> plotnumbers = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_print);
        llScroll=findViewById(R.id.llScroll);
        remaining=findViewById(R.id.remaining_amount);
        total=findViewById(R.id.total_amount);
        listView=findViewById(R.id.plotlist);

        relativeLayout=findViewById(R.id.relative_layout2);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        bookingModel = (BookingModel) args.getSerializable("ARRAYLIST");
        total.setText(" ₹  "+String.valueOf(bookingModel.getPaid_amount()));
        remaining.setText(" ₹  "+String.valueOf(bookingModel.getRemaining_amount()));



        for (int i=0;i<bookingModel.getPlot().size();i++)
        {
            plotnumbers.add(bookingModel.getPlot().get(i).getPlot_number());
        }
        ArrayAdapter adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_list_item_1,plotnumbers);
        listView.setAdapter(adapter);

    }



    public void pdf(View view) {
        bitmap = loadBitmapFromView(llScroll, llScroll.getWidth(), llScroll.getHeight());
        createPdf();
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
            Uri uri1= FileProvider.getUriForFile(BookedPrintActivity.this, BuildConfig.APPLICATION_ID +".provider",file);
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
                Toast.makeText(BookedPrintActivity.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
    }



}