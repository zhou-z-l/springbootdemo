package com.example.springbootdemo.untils;

import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * @author zhouzuolin
 * @date 2023/4/12
 */
public class OkHttpClientUntil {

    public static final OkHttpClient getClient(){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.SECONDS)
                .build();
        return client;
    }

}
