package com.example.weatherforcast.Models.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforcast.Models.Entities.CURRENT_WEATHER_ID
import com.example.weatherforcast.Models.Entities.CurrentWeatherEntry
import com.example.weatherforcast.Models.UnitSpecific.ImpericalCurrentWeatherEntry
import com.example.weatherforcast.Models.UnitSpecific.MetricCurrentWeather

@Dao
interface CurrentWeatherDao {

    // (onConflict = OnConflictStrategy.REPLACE) this whole block is for preventing the conflicting when we add a field has the same id (primaryKey) that inside the database
    // it is not gonna crash but instead gonna update that column that has this id
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry: CurrentWeatherEntry)

    @Query("SELECT * FROM current_weather WHERE id = $CURRENT_WEATHER_ID")
    fun getWeatherImprical(): LiveData<ImpericalCurrentWeatherEntry>

    @Query("SELECT * FROM current_weather WHERE id = $CURRENT_WEATHER_ID")
    fun getWeatherMetric(): LiveData<MetricCurrentWeather>

}