package com.example.weatherforcast.Models.Repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.weatherforcast.Models.Dao.CurrentWeatherDao
import com.example.weatherforcast.Models.Dao.FutureWeatherDao
import com.example.weatherforcast.Models.Dao.WeatherLocationDao
import com.example.weatherforcast.Models.Entities.CurrentWeatherResponse
import com.example.weatherforcast.Models.Entities.FutureWeather.FutureWeatherResponse
import com.example.weatherforcast.Models.Entities.WeatherLocation
import com.example.weatherforcast.Models.Providers.LocationProvider
import com.example.weatherforcast.Models.UnitSpecific.Future.Detail.SpecificDetailFutureWeatherEntry
import com.example.weatherforcast.Models.UnitSpecific.Future.List.SpecificSimpleFutureWeatherEntry
import com.example.weatherforcast.Models.UnitSpecific.UnitSpecificWeatherEntry
import com.example.weatherforcast.Models.WeatherNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime

const val DAYS_COUNT = 7

class WeatherRepositoryImpl(private val currentWeatherDao: CurrentWeatherDao,
                            private val weatherLocationDao: WeatherLocationDao,
                            private val futureWeatherDao: FutureWeatherDao,
                            private val locationProvider: LocationProvider,
                            private val weatherNetworkDataSource: WeatherNetworkDataSource) : WeatherRepository {

    init {
        weatherNetworkDataSource.apply {
            downloadedWeatherData.observeForever(Observer {
                    data -> presistWeatherData(data)
            })

            downloadedFutureWeatherData.observeForever{
                data -> persistFetchedFutureWeather(data)
            }
        }
    }

    // ( out ) this is something called genircs in kotlin out here tells the compiler that
    // it is allowed to return a class implementing UnitSpecificWeatherEntry interface , it does not have to return the interface itself
    override suspend fun getCurrentWeather(metric:Boolean): LiveData<out UnitSpecificWeatherEntry> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext if (metric) currentWeatherDao.getWeatherMetric()
            else currentWeatherDao.getWeatherImprical()
        }
    }

    override suspend fun getFutureWeatherList(startDate : LocalDate, isMetric:Boolean): LiveData<out List<SpecificSimpleFutureWeatherEntry>> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext if (isMetric) futureWeatherDao.getSimpleWeatherForecastsMetric(startDate)
            else futureWeatherDao.getSimpleWeatherForecastsImperial(startDate)
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO){
            return@withContext weatherLocationDao.getLocation()
        }
    }

    override suspend fun getDetailFutureWeather(date: LocalDate , isMetric: Boolean): LiveData<out SpecificDetailFutureWeatherEntry> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext if (isMetric) futureWeatherDao.getMetricDetailWeather(date)
            else futureWeatherDao.getImpricalDetailWeather(date)
        }
    }

    private fun presistWeatherData (currentWeatherResponse: CurrentWeatherResponse){
        GlobalScope.launch {
            currentWeatherDao.upsert(currentWeatherResponse.currentWeatherEntry)
            weatherLocationDao.upsert(currentWeatherResponse.location)
        }
    }

    private fun persistFetchedFutureWeather(fetchedWeather: FutureWeatherResponse) {

        fun deleteOldForecastData() {
            val today = LocalDate.now()
            futureWeatherDao.deleteOldEntries(today)
        }

        GlobalScope.launch(Dispatchers.IO) {
            deleteOldForecastData()
            val futureWeatherList = fetchedWeather.forecast.futureWeatherEntry
            futureWeatherDao.insert(futureWeatherList)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    suspend fun initWeatherData(){
        var weatherLocation = weatherLocationDao.getLocation().value

        if (weatherLocation == null || locationProvider.isLocationChanged(weatherLocation)){
            fetchCurrentWeather()
            fetchFutureWeather()
            return
        }

        if (isFetchCurrentNeeded(weatherLocation.zonedDateTime))
            fetchCurrentWeather()

        if (isFetchFutureWeatherNeeded())
            fetchFutureWeather()

    }

    private suspend fun fetchCurrentWeather() {
        Log.e("locf" , locationProvider.getPreferredLocationString())
        weatherNetworkDataSource.fetchWeatherData(
            locationProvider.getPreferredLocationString()
        )
    }

    private suspend fun fetchFutureWeather(){
        weatherNetworkDataSource.fetchFutureWeatherData(
            locationProvider.getPreferredLocationString(),
            DAYS_COUNT
        )
    }

    private fun isFetchFutureWeatherNeeded():Boolean{
        val today = LocalDate.now()
        val futureWeatherCount = futureWeatherDao.countFutureWeather(today)
        return futureWeatherCount < DAYS_COUNT
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

}