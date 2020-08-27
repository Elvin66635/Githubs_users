package com.example.githubs_users.controller;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.githubs_users.model.CustomItemClickListener;
import com.example.githubs_users.R;
import com.example.githubs_users.adapter.ItemAdapter;
import com.example.githubs_users.api.Client;
import com.example.githubs_users.api.Service;
import com.example.githubs_users.model.Item;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private   ProgressDialog pd;
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private ArrayList<Item> userList;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        setContentView(R.layout.activity_main);

        initViews();
        getUserListFromRestApi();
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserListFromRestApi();
                Toast.makeText(MainActivity.this, "Пользователи обновлены", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        pd = new ProgressDialog(this);
        pd.setMessage("Загрузка пользователей...");
        pd.setCancelable(false);
        pd.show();
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getUserListFromRestApi();

    }

    private void getUserListFromRestApi() {
        pd.show();
        Service apiInterface = Client.getClient().create(Service.class);

        Call<List<Item>> call = apiInterface.getItems("application/vnd.github.v3+json");
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful()) {
                    swipeContainer.setRefreshing(false);
                    pd.dismiss();
                    userList = new ArrayList<>(response.body());
                    adapter = new ItemAdapter(getApplicationContext(), userList, new CustomItemClickListener() {
                        @Override
                        public void onItemClick(Item user, int pos) {
                            if (pos != RecyclerView.NO_POSITION) {
                                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                                intent.putExtra("login", userList.get(pos).getLogin());
                                intent.putExtra("html_url", userList.get(pos).getHtml_url());
                                intent.putExtra("avatar_url", userList.get(pos).getAvatar_url());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(intent);
                            }
                        }
                    });
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();

        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


}
