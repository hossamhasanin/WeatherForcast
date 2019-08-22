package com.example.weatherforcast.Models.UnitSpecific.Future.List

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDate

data class MetricFutureWeatherEntry(
    @ColumnInfo(name = "date")
    override val date: LocalDate,
    @ColumnInfo(name = "avgtempC")
    override val avgTemperature: Double,
    @ColumnInfo(name = "condition_text")
    override val conditionText: String,
    @ColumnInfo(name = "condition_icon")
    override val conditionIconUrl: String
) : SpecificSimpleFutureWeatherEntry