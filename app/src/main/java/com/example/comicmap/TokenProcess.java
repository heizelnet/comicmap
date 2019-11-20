package com.example.comicmap;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface TokenProcess {
    String BASE_URL = "https://auth1-sandbox.circle.ms/";

    @GET("OAuth2/")
    Call<ResponseBody> addParameters(
                @Query("response_type") String owner,
                @Query("client_id") String repo,
                @Query("state") String state,
                @Query("scope") String scope);
}