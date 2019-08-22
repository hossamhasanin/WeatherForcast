package com.example.weatherforcast.ViewModels

import androidx.lifecycle.ViewModel
import com.example.weatherforcast.Externals.UnitSystem
import com.example.weatherforcast.Externals.lazyDeferred
import com.example.weatherforcast.Models.Providers.UnitsystemProvider
import com.example.weatherforcast.Models.Repositories.WeatherRepository

abstract class BaseWeatherViewModel(
    private val weatherRepository: WeatherRepository,
    unitsystemProvider: UnitsystemProvider
) : ViewModel() {

    private val unitSystem = unitsystemProvider.getUnitSystem()

    val isMetric get() = unitSystem == UnitSystem.METRIC

    val weatherLocation by lazyDeferred {
        weatherRepository.getWeatherLocation()
    }

}