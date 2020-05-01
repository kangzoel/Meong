package com.example.meong.interfaces;

import com.example.meong.models.ImageResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ImageAPIInterface {
    @Headers("Authorization:563492ad6f917000010000018efb110eba6c467895836db5a5caa1bb")
    @GET("v1/search")
    Call<ImageResult> getImages(
            @Query("query") String query,
            @Query("per_page") int per_page,
            @Query("page") int page
    );
}
