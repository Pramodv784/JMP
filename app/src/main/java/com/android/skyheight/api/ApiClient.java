package com.android.skyheight.api;
import android.content.Context;

import com.android.skyheight.utils.ConstantClass;
import com.android.skyheight.utils.Prefrence;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Context context;
    private static Retrofit getRetrofit(){
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                   Request newRequest = chain.request().newBuilder()
                          // .addHeader("Authorization","Bearer  "+Prefrence.getInstance(context).getData(ConstantClass.TOKEN))
                           .build();
                    return chain.proceed(newRequest);
                }
        })
                .addInterceptor(interceptor)
                .build();
        // OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantClass.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }
    public static UserService getUserService(){
        UserService userService = getRetrofit().create(UserService.class);
        return userService;
    }
}