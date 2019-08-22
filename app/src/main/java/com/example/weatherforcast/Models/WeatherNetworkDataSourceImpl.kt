package com.example.weatherforcast.Models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherforcast.Externals.NoConnectivityException
import com.example.weatherforcast.Models.Entities.CurrentWeatherResponse
import com.example.weatherforcast.Models.Entities.FutureWeather.FutureWeatherResponse
import retrofit2.HttpException

class WeatherNetworkDataSourceImpl(private val apixuApiService: ApixuApiService) : WeatherNetworkDataSource {
    private val _downloadedWeatherData : MutableLiveData<CurrentWeatherResponse>  = MutableLiveData()
    override val downloadedWeatherData: LiveData<CurrentWeatherResponse>
        get() = _downloadedWeatherData

    private val _downloadedFutureWeatherData : MutableLiveData<FutureWeatherResponse>  = MutableLiveData()
    override val downloadedFutureWeatherData: LiveData<FutureWeatherResponse>
        get() = _downloadedFutureWeatherData

    override suspend fun fetchWeatherData(location: String) {
        try {

            val fetchedData = apixuApiService.getCurrentWeather(location).await()
            _downloadedWeatherData.postValue(fetchedData)

        } catch (e: HttpException){
            Log.e("Connectivity" , "No connection. ${e.response().toString()}")
        }
    }

    override suspend fun fetchFutureWeatherData(location: String, days: Int) {
        try {

            val fetchedData = apixuApiService.getFutureWeather(location , days).await()
            _downloadedFutureWeatherData.postValue(fetchedData)

        } catch (e: HttpException){
            Log.e("Connectivity" , "No connection. ${e.response().toString()}")
        }
    }

}