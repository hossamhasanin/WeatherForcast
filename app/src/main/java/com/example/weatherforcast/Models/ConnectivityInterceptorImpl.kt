package com.example.weatherforcast.Models

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.weatherforcast.Externals.Connectivity
import com.example.weatherforcast.Externals.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptorImpl (context: Context , connectivityValue : MutableLiveData<Connectivity>) : ConnectivityInterceptor {

    private val appContext = context.applicationContext
    private var connectivityValue = connectivityValue

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline())
            throw NoConnectivityException()
        return chain.proceed(chain.request())
    }

    private fun isOnline():Boolean{
        if (connectivityValue.value == Connectivity.CONNECTED){
            return true
        } else if (connectivityValue.value == Connectivity.DISCONNECTED){
            return false
        } else {
            // The Android api is lower than api 24
            val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

    }
}