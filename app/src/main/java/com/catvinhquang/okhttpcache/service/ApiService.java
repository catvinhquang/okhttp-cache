package com.catvinhquang.okhttpcache.service;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by QuangCV on 13-Jul-2020
 * Reference: https://reqres.in
 **/

public interface ApiService {

    @GET("users/{id}")
    Observable<GetUserResponse> getUser(@Path("id") int id);

    @POST("users")
    Observable<CreateUserResponse> createUser(@Body CreateUserRequest request);

}