package com.example.weatherforcast.ViewModels

import androidx.lifecycle.ViewModel;
import com.example.weatherforcast.Externals.UnitSystem
import com.example.weatherforcast.Externals.lazyDeferred
import com.example.weatherforcast.Models.Providers.UnitsystemProvider
import com.example.weatherforcast.Models.Repositories.WeatherRepository

class CurrentWeatherViewModel(
    private val weatherRepository: WeatherRepository,
    unitsystemProvider: UnitsystemProvider) : BaseWeatherViewModel(weatherRepository , unitsystemProvider) {

    val weather by lazyDeferred {
        weatherRepository.getCurrentWeather(isMetric)
    }

}
