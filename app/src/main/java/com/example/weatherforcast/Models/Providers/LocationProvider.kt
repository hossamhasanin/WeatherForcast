package com.example.weatherforcast.Models.Providers

import com.example.weatherforcast.Models.Entities.WeatherLocation

interface LocationProvider {
    suspend fun isLocationChanged(lastWeatherLocation: WeatherLocation):Boolean
    suspend fun getPreferredLocationString():String
}