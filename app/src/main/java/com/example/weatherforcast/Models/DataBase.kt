package com.example.weatherforcast.Models

import android.content.Context
import androidx.room.*
import com.example.weatherforcast.Models.Dao.CurrentWeatherDao
import com.example.weatherforcast.Models.Dao.FutureWeatherDao
import com.example.weatherforcast.Models.Dao.WeatherLocationDao
import com.example.weatherforcast.Models.Entities.CurrentWeatherEntry
import com.example.weatherforcast.Models.Entities.FutureWeather.FutureWeatherEntry
import com.example.weatherforcast.Models.Entities.WeatherLocation

@Database(
    entities = [CurrentWeatherEntry::class , WeatherLocation::class , FutureWeatherEntry::class] ,
    version = 1
)
@TypeConverters(DateTypeConverter::class)
abstract class DataBase : RoomDatabase() {

    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun weatherLocationDao(): WeatherLocationDao
    abstract fun futureWeatherDao(): FutureWeatherDao

    companion object {

        // This whole block contains the initialization of the database
        // @Volatile means that the variable called (instance) will only be taken once through the whole app
        // synchronized(LOCK) this block makes sure that the instance of the this database will be synchronized
        // through the whole app and only initialized one in each thread
        // LOCK this variable exists to make some sort of insurance that only one instance of this class would be initialized once
        // LOCK is a variable that like some sort of instance's container or something as i think !!
        // ?: this is an operator that returns the value of the variable if it is not null and returns the value on the right if it is
        // also {} is a kotlin function that returns an object of the class that is returned from
        @Volatile private var instance : DataBase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context:Context) =
            Room.databaseBuilder(context.applicationContext , DataBase::class.java , "weather.db").build()

    }

}