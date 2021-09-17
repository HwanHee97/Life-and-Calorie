package com.example.life_and_calorie.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodRetrofitInterface {

    @GET("select/")
    Call<String> selectFood(@Query("FOOD") String food);

    @GET("insert/")
    Call<String> insertFood(
            @Query("FOOD") String food,
            @Query("CALORIE") String calorie
    );

    @GET("report/")
    Call<String> reportFood(
            @Query("FOOD") String food,
            @Query("CALORIE") String calorie
    );
}
