package com.pab.android_client;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("insert_user.php")
    Call<Void> insertUser(@Body com.pab.android_client.User user);

    @GET("get_users.php")
    Call<List<com.pab.android_client.User>> getUsers();
    @PUT("update_user.php")
    Call<Void> updateUser(@Body com.pab.android_client.User user);

    @DELETE("delete_user.php/{id}")
    Call<Void> deleteUser(@Path("id") int id);




}