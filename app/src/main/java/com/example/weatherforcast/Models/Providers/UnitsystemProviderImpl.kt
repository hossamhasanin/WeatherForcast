package com.example.weatherforcast.Models.Providers

import android.content.Context
import androidx.preference.PreferenceManager
import com.example.weatherforcast.Externals.UnitSystem

const val UNIT_SYSTEM = "UNIT_SYSTEM"

class UnitsystemProviderImpl(context: Context) : PreferenceProvder(context) , UnitsystemProvider {

    override fun getUnitSystem(): UnitSystem {
        var unitSelected = preferences.getString(UNIT_SYSTEM , UnitSystem.METRIC.name)
        return UnitSystem.valueOf(unitSelected!!)
    }
}