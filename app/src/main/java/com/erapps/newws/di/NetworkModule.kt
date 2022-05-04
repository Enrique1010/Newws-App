package com.erapps.newws.di

import com.erapps.newws.BuildConfig
import com.erapps.newws.api.NetworkResponseAdapterFactory
import com.erapps.newws.api.services.NewsApiService
import com.erapps.newws.utils.ExcludeDeserializationStrategy
import com.erapps.newws.utils.ExcludeSerializationStrategy
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpLogginInterceptor(): HttpLoggingInterceptor{
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(okHttpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient{
        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG){
            builder.addInterceptor(okHttpLoggingInterceptor)
        }

        builder.apply {
            addInterceptor(Interceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader("x-api-key", BuildConfig.API_KEY)
                    .build()
                chain.proceed(request)
            })
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        val gson = GsonBuilder()
            .addSerializationExclusionStrategy(ExcludeSerializationStrategy())
            .addDeserializationExclusionStrategy(ExcludeDeserializationStrategy())
            .create()
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    @NewsApiServiceAnnotation
    fun provideRetrofitNewsApiInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.News_Api_Base_URL)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsApiService(@NewsApiServiceAnnotation retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }
}