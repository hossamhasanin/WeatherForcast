package com.example.weatherforcast.Models.Providers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.weatherforcast.Externals.LocationPermissionNotGrantedException
import com.example.weatherforcast.Externals.asDeferred
import com.example.weatherforcast.Models.Entities.WeatherLocation
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"

class LocationProviderImpl(context: Context , private val fusedLocationProviderClient: FusedLocationProviderClient) : PreferenceProvder(context) , LocationProvider {

    private val appContext = context.applicationContext

    override suspend fun isLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        var deviceLocationChanged = try {
            hasDeviceLocationChanged(lastWeatherLocation)
        } catch (e: LocationPermissionNotGrantedException){
            false
        }
        var customLocationChanged = hasCustomLocationChanged(lastWeatherLocation)

        return deviceLocationChanged || customLocationChanged
    }


    override suspend fun getPreferredLocationString(): String {
        if (isUsingDeviceLocation()){
            try {
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return "${getCustomLocationName()}"
                return "${deviceLocation.latitude},${deviceLocation.longitude}"
            } catch (e:LocationPermissionNotGrantedException){
                return "${getCustomLocationName()}"
            }

        } else {
            return "${getCustomLocationName()}"
        }

    }

    private suspend fun hasDeviceLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        if (!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocation().await()
            ?: return false

        // Comparing doubles cannot be done with "=="
        val comparisonThreshold = 0.03
        return Math.abs(deviceLocation.latitude - lastWeatherLocation.lat) > comparisonThreshold &&
                Math.abs(deviceLocation.longitude - lastWeatherLocation.lon) > comparisonThreshold
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    private fun hasCustomLocationChanged(lastWeatherLocation: WeatherLocation) : Boolean {
        if (!isUsingDeviceLocation()) {
            return getCustomLocationName() != lastWeatherLocation.name
        }
        return false
    }

    private fun getCustomLocationName():String?{
        return preferences.getString(CUSTOM_LOCATION , null)
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?> {
        return if (hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermission() : Boolean{
        return ContextCompat.checkSelfPermission(appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

}