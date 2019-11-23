package com.example.comicmap;



import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TokenProcess {
    String LOGIN_URL = "https://auth2.circle.ms/";
    String BASE_URL = "https://auth1-sandbox.circle.ms/";
    String API_URL = "https://api1.circle.ms/";

    @FormUrlEncoded
    @Headers({"Accept-Language: ko,en-US;q=0.9,en;q=0.8,ja;q=0.7",
              "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36",
    })
    @POST("/")
    Call<ResponseBody> LoginData(
            @FieldMap HashMap<String, Object> param);

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
    @Headers({"Content-Type: application/x-www-form-urlencoded",
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3",
            "Accept-Language: ko,en-US;q=0.9,en;q=0.8,ja;q=0.7",
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36",
            "Referer: https://auth1-sandbox.circle.ms/OAuth2/?response_type=code&client_id=comicmapgZwp98BPmh5rj35zfnFNcZA5mxrpyCUQ&state=test&scope=user_info%20circle_read%20circle_write%20favorite_read%20favorite_write",
    })
    @POST("OAuth2/AuthorizeRequest/")
    Call<ResponseBody> postData(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/OAuth2/Token/")
    Call<ResponseBody> accessToken(@FieldMap HashMap<String, Object> param);
}