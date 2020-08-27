package com.example.githubs_users.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.githubs_users.R;
import com.example.githubs_users.api.Client;
import com.example.githubs_users.api.Service;
import com.example.githubs_users.model.DetailRepos;
import com.example.githubs_users.model.DetailUser;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";
    TextView link, username, profileName, createDate, location, detailReposTxt;
    List<DetailRepos> detailReposList;
    Toolbar mActionToolbar;
    ImageView imageView;
    Spinner spinner;
    private boolean isSpinnerTouched = false;
    ArrayAdapter<DetailRepos> adapter;
    String usernameString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();

        usernameString = getIntent().getExtras().getString("login");
        String avatarUrlString = getIntent().getExtras().getString("avatar_url");
        String linkString = getIntent().getExtras().getString("html_url");

        username.setText(usernameString);
        link.setText(linkString);
        Linkify.addLinks(link, Linkify.WEB_URLS);

        detailReposList = new ArrayList<>();
        adapter = new ArrayAdapter<DetailRepos>(this,
                android.R.layout.simple_expandable_list_item_1, detailReposList);
        spinner.setPrompt("Choose Repo");

        Picasso.with(this)
                .load(avatarUrlString)
                .placeholder(R.drawable.loading)
                .into(imageView);


        Service service = Client.getClient().create(Service.class);
        Call<DetailUser> call = service.getUser(usernameString);
        call.enqueue(new Callback<DetailUser>() {
            @Override
            public void onResponse(Call<DetailUser> call, Response<DetailUser> response) {
                if (response.isSuccessful()) {
                    //   Log.d(TAG, response.body().getName());
                    profileName.setText(response.body().getName());
                    createDate.setText(response.body().getCreated_at().toString());
                    location.setText(response.body().getLocation());
                } else {
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<DetailUser> call, Throwable t) {
                Log.d(TAG, "onFailure:" + t.getMessage());
            }
        });
        getUserRepo();
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.user_image_header);
        link = (TextView) findViewById(R.id.link);
        username = (TextView) findViewById(R.id.username);
        profileName = (TextView) findViewById(R.id.profileName);
        location = (TextView) findViewById(R.id.location);
        createDate = (TextView) findViewById(R.id.createDate);
        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isSpinnerTouched = true;
                return false;
            }
        });
    }

    private void getUserRepo() {
        Service service = Client.getClient().create(Service.class);
        Call<List<DetailRepos>> callRepos = service.getUserRepos(usernameString);
        callRepos.enqueue(new Callback<List<DetailRepos>>() {
            @Override
            public void onResponse(Call<List<DetailRepos>> call, final Response<List<DetailRepos>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "is successful ");
                    for (final DetailRepos detailRepos : response.body()) {
                        final String name = detailRepos.getName();
                        final int rating = detailRepos.getStargazers_count();
                        String language = detailRepos.getLanguage();
                        if (language == null){
                            return;
                        }
                        String formatData = detailRepos.getUpdated_at().toString();
                        DetailRepos dR = new DetailRepos(name, language, rating);
                        detailReposList.add(dR);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        Log.d(TAG, "onResponse: " + name);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (!isSpinnerTouched)
                                    return;
                                Toast.makeText(DetailActivity.this, name, Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }
                } else {
                    Log.d(TAG, "fail " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<DetailRepos>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });
    }
}
