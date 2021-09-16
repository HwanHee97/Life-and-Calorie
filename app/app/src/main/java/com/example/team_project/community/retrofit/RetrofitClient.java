package com.example.team_project.community.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    public Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.0.29:3000/community/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
