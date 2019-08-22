package com.example.weatherforcast.Models.Repositories

import androidx.lifecycle.LiveData
import com.example.weatherforcast.Models.Entities.WeatherLocation
import com.example.weatherforcast.Models.UnitSpecific.Future.Detail.SpecificDetailFutureWeatherEntry
import com.example.weatherforcast.Models.UnitSpecific.Future.List.SpecificSimpleFutureWeatherEntry
import com.example.weatherforcast.Models.UnitSpecific.UnitSpecificWeatherEntry
import org.threeten.bp.LocalDate

interface WeatherRepository {

    suspend fun getCurrentWeather(metric:Boolean) : LiveData<out UnitSpecificWeatherEntry>
    suspend fun getWeatherLocation() : LiveData<WeatherLocation>
    suspend fun getFutureWeatherList(startDate : LocalDate , isMetric : Boolean) : LiveData<out List<SpecificSimpleFutureWeatherEntry>>
    suspend fun getDetailFutureWeather(date : LocalDate , isMetric: Boolean) : LiveData<out SpecificDetailFutureWeatherEntry>

}