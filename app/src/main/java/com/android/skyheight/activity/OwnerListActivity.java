package com.android.skyheight.activity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.skyheight.R;
import com.android.skyheight.adaptor.UserAdaptor;
import com.android.skyheight.api.ApiClient;
import com.android.skyheight.model.DeleteModel;
import com.android.skyheight.model.UserList;
import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    UserAdaptor userAdaptor;
    Prefrence yourprefrence;
    ProgressBar progressBar;
    ArrayList<UserList> userlist = new ArrayList<UserList>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_list);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view3);
        relativeLayout=findViewById(R.id.relative_layout3);
        progressBar = findViewById(R.id.progressbar);
        yourprefrence = Prefrence.getInstance(OwnerListActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        String owner="Owner";
        userlist(owner);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void userlist(String owner) {
        final Call<ArrayList<UserList>> userResponseCall = ApiClient.getUserService()
                .allowner("Bearer "+yourprefrence.getData(ConstantClass.TOKEN),owner);
        userResponseCall.enqueue(new Callback<ArrayList<UserList>>() {
            @Override
            public void onResponse(Call<ArrayList<UserList>> call, Response<ArrayList<UserList>> response) {
                //Type listType = new TypeToken<ArrayList<UserList>>() {}.getType();
                //ArrayList<UserList> allUserses = new GsonBuilder().create().fromJson(String.valueOf(response.body()), listType);
                //Log.d("Bhagawam", "Responce: " + allUserses.size());
                if (response.code() == 200) {
                    if (!response.body().isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        userlist = response.body();
                        userAdaptor = new UserAdaptor(OwnerListActivity.this, response.body(), userlist);
                        recyclerView.setAdapter(userAdaptor);
                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                        itemTouchHelper.attachToRecyclerView(recyclerView);
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        relativeLayout.setBackgroundResource(R.drawable.emptyitem);

                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<UserList>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
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
            Snackbar.make(relativeLayout,"Are you Sure want to delete",Snackbar.LENGTH_LONG)
                    .setAction("delete", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String id = userlist.get(position).getId();
                            Log.e("done","site_id"+id );
                            userlist.remove(position);
                            userAdaptor.notifyItemRemoved(position);
                            deleteuser(id);
                        }
                    }).setActionTextColor(getResources().getColor(R.color.red)).show();
            userAdaptor.notifyDataSetChanged();

        }
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(OwnerListActivity.this,R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.delete_icon)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
        private void deleteuser(String id) {
            Call<DeleteModel> userResponseCall = ApiClient.getUserService()
                    .delete("Bearer "+yourprefrence.getData(ConstantClass.TOKEN),id);
            userResponseCall.enqueue(new Callback<DeleteModel>() {
                @Override
                public void onResponse(Call<DeleteModel> call, Response<DeleteModel> response) {
                    if (response.code()==204){
                        // startActivity(new Intent(CustomerListActivity.this,CustomerListActivity.class));
                        Toast.makeText(getApplicationContext(),"Owner Deleted",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"faild to delete",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<DeleteModel> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"Check Your Internet Connection",Toast.LENGTH_SHORT).show();
                }
            });
        }
}