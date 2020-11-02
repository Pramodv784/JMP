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
import android.view.Menu;
import android.view.MenuItem;
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
import com.android.skyheight.model.BookingListModel;
import com.android.skyheight.model.PlotListModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BookedPrintActivity extends AppCompatActivity {
TextView sitename,remaining,total,username,user_address,user_mobile,paid_amount;
BookingListModel bookingModel;
    LinearLayout llScroll;
    private Bitmap bitmap;
    ListView plotlist;
    ArrayList<Integer> list= new ArrayList<>() ;
    String targetPdf = "/sdcard/pdfapp.pdf";

    RelativeLayout relativeLayout;
    ListView listView;
    ArrayList<String> plotnumbers =new ArrayList<>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_print);
        llScroll=findViewById(R.id.llScroll);
        remaining=findViewById(R.id.remaining_amount);
        total=findViewById(R.id.total_amount);
        listView=findViewById(R.id.plotlist);
        sitename=findViewById(R.id.site_name);
        username=findViewById(R.id.username);
        paid_amount=findViewById(R.id.paid_amount);
        relativeLayout=findViewById(R.id.relative_layout2);
        user_address=findViewById(R.id.user_address);
        user_mobile=findViewById(R.id.user_mobile);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        bookingModel = (BookingListModel) args.getSerializable("ARRAYLIST");

        String upperString = bookingModel.getBuyer().getUsername().substring(0, 1).toUpperCase()
                + bookingModel.getBuyer().getUsername().substring(1).toLowerCase();
        username.setText(upperString);
        user_address.setText(bookingModel.getBuyer().address);
        user_mobile.setText(bookingModel.getBuyer().getMobile_number());
        String upperString1 = bookingModel.getSite().getName().substring(0, 1).toUpperCase()
                + bookingModel.getSite().getName().substring(1).toLowerCase();
        sitename.setText(upperString1);
        paid_amount.setText("₹  "+String.valueOf(bookingModel.getPaid_amount()));
        remaining.setText("₹   "+String.valueOf(bookingModel.getRemaining_amount()));
        total.setText("₹   "+String.valueOf(bookingModel.getTotal_amount()));
        for (int i=0;i<bookingModel.getPlot().size();i++)
        {
            plotnumbers.add("Plot Number  "+bookingModel.getPlot().get(i).getPlot_number());

        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,plotnumbers);
        listView.setAdapter(adapter);
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