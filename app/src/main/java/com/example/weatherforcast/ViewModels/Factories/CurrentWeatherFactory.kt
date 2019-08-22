package com.example.weatherforcast.ViewModels.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforcast.Models.Providers.UnitsystemProvider
import com.example.weatherforcast.Models.Repositories.WeatherRepository
import com.example.weatherforcast.ViewModels.CurrentWeatherViewModel

class CurrentWeatherFactory (
    private val weatherRepository: WeatherRepository,
    private val unitsystemProvider: UnitsystemProvider) : ViewModelProvider.NewInstanceFactory() {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(weatherRepository , unitsystemProvider) as T
    }

}