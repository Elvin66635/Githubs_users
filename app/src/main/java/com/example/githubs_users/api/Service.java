package com.example.githubs_users.api;

import com.example.githubs_users.model.DetailRepos;
import com.example.githubs_users.model.DetailUser;
import com.example.githubs_users.model.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    @GET("users")
    Call<List<Item>> getItems(@Header("Accept") String accept);

    @GET("users")
    Call<List<Item>> getItemsPage(@Query("since") int page);

    @GET("users/{username}")
    Call<DetailUser> getUser(@Path("username") String user);

    @GET("users/{username}/repos")
    Call<List<DetailRepos>> getUserRepos(@Path("username") String user);

}
