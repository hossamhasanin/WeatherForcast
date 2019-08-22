package com.example.weatherforcast.Models.Entities.FutureWeather


import com.example.weatherforcast.Models.Entities.WeatherLocation

data class FutureWeatherResponse(
    val forecast: ForecastWeatherDays,
    val location: WeatherLocation
)