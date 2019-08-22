package com.example.weatherforcast.ViewModels

import androidx.lifecycle.ViewModel;
import com.example.weatherforcast.Externals.lazyDeferred
import com.example.weatherforcast.Models.Providers.UnitsystemProvider
import com.example.weatherforcast.Models.Repositories.WeatherRepository
import org.threeten.bp.LocalDate

class WeatherDetailViewModel(
    private val dateDetail: LocalDate,
    private val weatherRepository: WeatherRepository ,
    unitsystemProvider: UnitsystemProvider) : BaseWeatherViewModel(weatherRepository , unitsystemProvider) {

    val weatherDeatail by lazyDeferred {
        weatherRepository.getDetailFutureWeather(dateDetail , isMetric)
    }

}
