package com.catvinhquang.okhttpcache.service;

import com.google.gson.annotations.SerializedName;

/**
 * Created by QuangCV on 13-Jul-2020
 **/

public class CreateUserRequest {

    @SerializedName("name")
    public String name;

    @SerializedName("job")
    public String job;

}