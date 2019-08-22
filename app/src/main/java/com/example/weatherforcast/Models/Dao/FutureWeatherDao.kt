package com.example.weatherforcast.Models.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforcast.Models.Entities.FutureWeather.FutureWeatherEntry
import com.example.weatherforcast.Models.UnitSpecific.Future.Detail.ImpricalDetailFutureWeatherEntry
import com.example.weatherforcast.Models.UnitSpecific.Future.Detail.MetricDetailFutureWeatherEntry
import com.example.weatherforcast.Models.UnitSpecific.Future.List.ImpricalFutureWeatherEntry
import com.example.weatherforcast.Models.UnitSpecific.Future.List.MetricFutureWeatherEntry
import org.threeten.bp.LocalDate

@Dao
interface FutureWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(forca: List<FutureWeatherEntry>)

    @Query("select * from weather_future where date(date) >= date(:startDate)")
    fun getSimpleWeatherForecastsMetric(startDate: LocalDate): LiveData<List<MetricFutureWeatherEntry>>

    @Query("select * from weather_future where date(date) >= date(:startDate)")
    fun getSimpleWeatherForecastsImperial(startDate: LocalDate): LiveData<List<ImpricalFutureWeatherEntry>>

    @Query("select count(id) from weather_future where date(date) >= date(:startDate)")
    fun countFutureWeather(startDate: LocalDate): Int

    @Query("delete from weather_future where date(date) < date(:firstDateToKeep)")
    fun deleteOldEntries(firstDateToKeep: LocalDate)

    @Query("select * from weather_future where date(date) = date(:startDate)")
    fun getMetricDetailWeather(startDate: LocalDate): LiveData<MetricDetailFutureWeatherEntry>

    @Query("select * from weather_future where date(date) = date(:startDate)")
    fun getImpricalDetailWeather(startDate: LocalDate): LiveData<ImpricalDetailFutureWeatherEntry>


}