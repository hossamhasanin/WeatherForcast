package com.example.weatherforcast.Models.UnitSpecific

import androidx.room.ColumnInfo

// Using ColumnInfo so that we could tell room which field we want from the database
// this class is made to get specific data from the database so that we don't get all the fields from the database , we get just what we need so this is also best practise

class ImpericalCurrentWeatherEntry(
    @ColumnInfo(name = "tempF")
    override val temperature: Double,
    @ColumnInfo(name = "condition_text")
    override val conditionText: String,
    @ColumnInfo(name = "condition_icon")
    override val conditionIconUrl: String,
    @ColumnInfo(name = "windMph")
    override val windSpeed: Double,
    @ColumnInfo(name = "windDir")
    override val windDirection: String,
    @ColumnInfo(name = "precipIn")
    override val precipitationVolume: Double,
    @ColumnInfo(name = "feelslikeF")
    override val feelsLikeTemperature: Double,
    @ColumnInfo(name = "visMiles")
    override val visibilityDistance: Double
) : UnitSpecificWeatherEntry