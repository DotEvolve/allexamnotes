package com.allexamnotes.libdroid.network;

import com.allexamnotes.libdroid.WordroidInit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static String BASE_URL = WordroidInit.getSiteUrl();
    public static Retrofit retrofit = null;
    public static String token;

    public static Retrofit getClient() {

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient getHttpClient(){
        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();
        httpClient.connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS);
        /*httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("app", "network")

                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        //.addHeader("Authorization", "Bearer " + token)
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });*/
        return httpClient.build();
    }
}
