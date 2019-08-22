package com.example.weatherforcast.Models.Entities.FutureWeather


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather_future" , indices = [Index(value = ["date"] , unique = true)])
data class FutureWeatherEntry(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null ,
    val date: String,
    @Embedded
    val day: Day
)