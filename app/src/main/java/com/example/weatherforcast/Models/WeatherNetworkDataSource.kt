package com.example.weatherforcast.Models

import androidx.lifecycle.LiveData
import com.example.weatherforcast.Models.Entities.CurrentWeatherResponse
import com.example.weatherforcast.Models.Entities.FutureWeather.FutureWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedWeatherData : LiveData<CurrentWeatherResponse>
    val downloadedFutureWeatherData : LiveData<FutureWeatherResponse>

    suspend fun fetchWeatherData(location:String)
    suspend fun fetchFutureWeatherData(location:String , days:Int)
}