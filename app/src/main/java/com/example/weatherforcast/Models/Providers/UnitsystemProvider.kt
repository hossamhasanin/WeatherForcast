package com.example.weatherforcast.Models.Providers

import com.example.weatherforcast.Externals.UnitSystem

interface UnitsystemProvider {
    fun getUnitSystem():UnitSystem
}