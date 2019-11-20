package com.example.comicmap;



import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TokenProcess {
    String LOGIN_URL = "https://auth2.circle.ms/";
    String BASE_URL = "https://auth1-sandbox.circle.ms/";

    /*
    GET for OAuth permission check
     */
    @GET("OAuth2/")
    Call<ResponseBody> addParameters(
                @Query("response_type") String owner,
                @Query("client_id") String repo,
                @Query("state") String state,
                @Query("scope") String scope);

    /*
    POST After check permission
     */
    @FormUrlEncoded
    @POST("OAuth2/AuthorizeRequest")
    Call<ResponseBody> postData(@FieldMap HashMap<String, Object> param);
}