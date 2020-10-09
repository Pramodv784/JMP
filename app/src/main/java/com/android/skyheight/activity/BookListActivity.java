package com.android.skyheight.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.skyheight.R;
import com.android.skyheight.adaptor.BookListAdaptor;
import com.android.skyheight.adaptor.PlotSummaryAdaptor;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.BookingModel;
import com.android.skyheight.model.PlotSummaryModel;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
    String plot_id;
    Prefrence youprefrence;
    Toolbar toolbar;
    RelativeLayout relative;
    ArrayList<BookingModel> bookingsumaary;
    BookListAdaptor bookListAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        recyclerView=findViewById(R.id.recycle4);
        swipeRefreshLayout=findViewById(R.id.refresh);
        progressBar=findViewById(R.id.progressbar);
        youprefrence =Prefrence.getInstance(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                booking();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        booking();
    }
    public void booking()
    {
        Call<ArrayList<BookingModel>> userResponse = ApiClient.getUserService()
                .bookinglist("Bearer "+youprefrence.getData(ConstantClass.TOKEN));
        userResponse.enqueue(new Callback<ArrayList<BookingModel>>() {
            @Override
            public void onResponse(Call<ArrayList<BookingModel>> call, Response<ArrayList<BookingModel>> response) {
                if (response.code()==200)
                {
                    if (!response.body().isEmpty())
                    {
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        bookingsumaary=response.body();
                        bookListAdaptor =new BookListAdaptor(BookListActivity.this,response.body(),bookingsumaary);
                        recyclerView.setAdapter(bookListAdaptor);
                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                        itemTouchHelper.attachToRecyclerView(recyclerView);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BookingModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.i("data","error>>>"+t);
                Toast.makeText(getApplicationContext(),"List failed"+t,Toast.LENGTH_SHORT).show();
            }
        });
    }
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            bookListAdaptor.notifyItemRemoved(position);
            bookListAdaptor.notifyDataSetChanged();
            String id =bookingsumaary.get(position).getId();
           /* sitelist.remove(position);
            deleteListAdaptor.notifyDataSetChanged();*/
            showCustomDialog(id,position);
        }
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(BookListActivity.this,R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.delete_icon)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
    private void showCustomDialog(final String id,final int position) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.delete_dialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        Button okbtn= dialogView.findViewById(R.id.buttonOk);
        Button cancel =dialogView.findViewById(R.id.cancel);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setContentView(R.layout.delete_dialog);
        alertDialog.show();
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletesite(id);
                bookingsumaary.remove(position);
                bookListAdaptor.notifyDataSetChanged();
                alertDialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                bookListAdaptor.notifyDataSetChanged();
            }
        });
    }

    private void deletesite(String id) {

        Call<BookingModel> userResponse =ApiClient.getUserService()
                .delete_book_summary("Bearer "+youprefrence.getData(ConstantClass.TOKEN),id);
        userResponse.enqueue(new Callback<BookingModel>() {
            @Override
            public void onResponse(Call<BookingModel> call, Response<BookingModel> response) {
                if (response.code()==204)
                {
                    Toast.makeText(getApplicationContext()," delete Sucessfully",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Failed to delete",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookingModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Failed to delete",Toast.LENGTH_SHORT).show();

            }
        });
    }
}