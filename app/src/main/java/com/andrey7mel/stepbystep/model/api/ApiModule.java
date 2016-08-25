package com.andrey7mel.stepbystep.model.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

public final class ApiModule {

    private static final boolean ENABLE_LOG = true;

    private static final boolean ENABLE_AUTH = false;
    private static final String AUTH_64 = "***";

    private ApiModule() {
    }

    public static ApiInterface getApiInterface(String url) {

        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();

        if (ENABLE_AUTH) {
            httpBuilder.addInterceptor(chain -> {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Authorization", "Basic " + AUTH_64)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            });
        }

        if (ENABLE_LOG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpBuilder.addInterceptor(interceptor);
        }

        Retrofit.Builder builder = new Retrofit.Builder().
                baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        OkHttpClient httpClient = httpBuilder.build();
        builder.client(httpClient);

        return builder.build().create(ApiInterface.class);
    }

}
