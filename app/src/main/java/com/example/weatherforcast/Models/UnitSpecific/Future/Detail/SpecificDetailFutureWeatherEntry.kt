package com.example.weatherforcast.Models.UnitSpecific.Future.Detail

import org.threeten.bp.LocalDate

interface SpecificDetailFutureWeatherEntry {

    val date: LocalDate
    val maxTemperature: Double
    val minTemperature: Double
    val avgTemperature: Double
    val conditionText: String
    val conditionIconUrl: String
    val maxWindSpeed: Double
    val totalPrecipitation: Double
    val avgVisibilityDistance: Double
    val uv: Double

}