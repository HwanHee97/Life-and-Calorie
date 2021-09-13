package com.example.team_project.community.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CommunityRetrofitInterface {
    @GET("show/")
    Call<String> showCommunity(@Query("index") String index);

    @GET("insert/")
    Call<String> insertCommunity(
            @Query("TITLE") String index,
            @Query("TEXT") String text,
            @Query("WRITER") String writer_ID
    );

    @GET("comment/")
    Call<String> commentShow(@Query("ID") String _ID);

    @GET("commentinsert/")
    Call<String> commentInsert(
            @Query("ID") String _ID,
            @Query("TEXT") String text,
            @Query("DATE") String date,
            @Query("WRITER") String writer_ID
    );
}
