package com.example.weatherforcast.Models.UnitSpecific

// This interface it is like a contractor , it describes the fields that we want to be exists in each class would inherit it

interface UnitSpecificWeatherEntry {
    val temperature: Double
    val conditionText: String
    val conditionIconUrl: String
    val windSpeed: Double
    val windDirection: String
    val precipitationVolume: Double
    val feelsLikeTemperature: Double
    val visibilityDistance: Double
}