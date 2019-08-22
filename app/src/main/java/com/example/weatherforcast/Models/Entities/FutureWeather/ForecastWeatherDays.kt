package com.example.weatherforcast.Models.Entities.FutureWeather


import com.google.gson.annotations.SerializedName

data class ForecastWeatherDays(
    @SerializedName("forecastday")
    val futureWeatherEntry: List<FutureWeatherEntry>
)