package com.catvinhquang.okhttpcache.service;

import com.google.gson.annotations.SerializedName;

/**
 * Created by QuangCV on 13-Jul-2020
 **/

public class GetUserResponse {

    @SerializedName("data")
    public User user;

    @SerializedName("ad")
    public Ad ad;

    public static class User {

        @SerializedName("id")
        public String id;

        @SerializedName("email")
        public String email;

        @SerializedName("first_name")
        public String firstName;

        @SerializedName("last_name")
        public String lastName;

        @SerializedName("avatar")
        public String avatar;

    }

    public static class Ad {

        @SerializedName("company")
        public String company;

        @SerializedName("url")
        public String url;

        @SerializedName("text")
        public String text;

    }

}