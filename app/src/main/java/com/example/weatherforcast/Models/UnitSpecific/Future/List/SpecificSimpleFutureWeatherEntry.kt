package com.example.weatherforcast.Models.UnitSpecific.Future.List

import org.threeten.bp.LocalDate

interface SpecificSimpleFutureWeatherEntry {
    val date: LocalDate
    val avgTemperature: Double
    val conditionText: String
    val conditionIconUrl: String
}