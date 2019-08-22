package com.example.weatherforcast.Models

import android.content.Context
import androidx.core.app.CoreComponentFactory
import com.example.weatherforcast.Models.Entities.CurrentWeatherResponse
import com.example.weatherforcast.Models.Entities.FutureWeather.FutureWeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query
import retrofit2.http.GET


const val API_KEY = "c50e498237244747b56223646192108"

interface ApixuApiService {

    // It returns Deferred because it is network request and it may delay sometimes
    // so that we want to wait in our main thread to update the ui with the coming data
    @GET("current.json")
    fun getCurrentWeather(
        @Query("q") location: String) : Deferred<CurrentWeatherResponse>

    @GET("forecast.json")
    fun getFutureWeather(
        @Query("q") location: String , @Query("days") days: Int) : Deferred<FutureWeatherResponse>

    companion object{

        // invoke is a kotlin function that takes place when this class is called
        // it takes place without calling the function name ( ApixuApiService() ) you could say it is like a constructor function but to the static class
        operator fun invoke(connectivityInterceptor: ConnectivityInterceptor) : ApixuApiService {

            // the role of this interceptor is to intercept the request and modify it
            // so that we could add api key in each request without needing to pass it through each retrofit function
            // this is best practise
            val interceptor = Interceptor {chain ->
                val url = chain.request().url().newBuilder().addQueryParameter("key" , API_KEY).build()
                val request = chain.request().newBuilder().url(url).build()
                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(connectivityInterceptor).build()

            // addCallAdapterFactory() we put CoroutineCallAdapterFactory() because we are getting Deferred as an response
            return Retrofit.Builder().client(okHttpClient).baseUrl("http://api.apixu.com/v1/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create()).build().create(ApixuApiService::class.java)
        }
    }

}