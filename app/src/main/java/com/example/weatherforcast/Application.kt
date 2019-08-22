package com.example.weatherforcast

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.example.weatherforcast.Externals.Connectivity
import com.example.weatherforcast.Models.*
import com.example.weatherforcast.Models.Providers.LocationProvider
import com.example.weatherforcast.Models.Providers.LocationProviderImpl
import com.example.weatherforcast.Models.Providers.UnitsystemProvider
import com.example.weatherforcast.Models.Providers.UnitsystemProviderImpl
import com.example.weatherforcast.Models.Repositories.WeatherRepository
import com.example.weatherforcast.Models.Repositories.WeatherRepositoryImpl
import com.example.weatherforcast.ViewModels.Factories.CurrentWeatherFactory
import com.example.weatherforcast.ViewModels.Factories.DetailWeatherFactory
import com.example.weatherforcast.ViewModels.Factories.FutureWeatherFactory
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*
import org.threeten.bp.LocalDate

class Application : Application() , KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@Application))

        bind() from singleton { DataBase(instance()) }
        bind() from  singleton { instance<DataBase>().currentWeatherDao() }
        bind() from  singleton { instance<DataBase>().weatherLocationDao() }
        bind() from  singleton { instance<DataBase>().futureWeatherDao() }
        bind<ConnectivityInterceptor>() with  singleton { ConnectivityInterceptorImpl(instance() , connectivityValue) }
        bind() from singleton { ApixuApiService(instance()) }
        bind<WeatherNetworkDataSource>() with  singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with  singleton { LocationProviderImpl(instance() , instance()) }
        bind<WeatherRepository>() with  singleton { WeatherRepositoryImpl(instance() , instance() , instance() , instance() , instance()) }
        bind<UnitsystemProvider>() with  singleton { UnitsystemProviderImpl(instance()) }
        bind() from provider { CurrentWeatherFactory(instance() , instance()) }
        bind() from provider { FutureWeatherFactory(instance() , instance()) }
        bind() from factory { dateDetail : LocalDate -> DetailWeatherFactory(dateDetail , instance() , instance()) }

    }

    val connectivityValue : MutableLiveData<Connectivity>  = MutableLiveData()

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        PreferenceManager.setDefaultValues(this , R.xml.prefrences , false)

        val connectivityManager : ConnectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                                                        as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback(){
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    connectivityValue.postValue(Connectivity.CONNECTED)
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    connectivityValue.postValue(Connectivity.DISCONNECTED)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    connectivityValue.postValue(Connectivity.DISCONNECTED)
                }
            })
        } else {
            connectivityValue.postValue(Connectivity.NONE)
        }
    }

}