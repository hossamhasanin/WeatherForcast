package com.example.weatherforcast.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.weatherforcast.Externals.DateNotFoundException
import com.example.weatherforcast.Externals.GlideApp
import com.example.weatherforcast.Models.DateTypeConverter

import com.example.weatherforcast.R
import com.example.weatherforcast.ViewModels.Factories.DetailWeatherFactory
import com.example.weatherforcast.ViewModels.WeatherDetailViewModel
import kotlinx.android.synthetic.main.weather_detail_fragment.*
import kotlinx.android.synthetic.main.weather_detail_fragment.textView_condition
import kotlinx.android.synthetic.main.weather_detail_fragment.textView_precipitation
import kotlinx.android.synthetic.main.weather_detail_fragment.textView_temperature
import kotlinx.android.synthetic.main.weather_detail_fragment.textView_visibility
import kotlinx.android.synthetic.main.weather_detail_fragment.textView_wind
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class WeatherDetailFragment : BaseScopedFragment() , KodeinAware {


    override val kodein by closestKodein()


    private lateinit var viewModel: WeatherDetailViewModel

    // This line of code means that we initialize a DetailWeatherFactory from the kodein factory
    // We do this because we pass an argument date : LocalDate to the factory which we initialize using kodein in Application class
    private val detailWeatherFactoryInstanceFactory:((LocalDate) -> DetailWeatherFactory) by factory()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.weather_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var safeArgs = arguments?.let { WeatherDetailFragmentArgs.fromBundle(it) }
        var date = DateTypeConverter.stringToDate(safeArgs?.dateDeatail) ?: throw DateNotFoundException()

        viewModel = ViewModelProviders.of(this , detailWeatherFactoryInstanceFactory(date))
            .get(WeatherDetailViewModel::class.java)

        bindUi()

    }

    private fun bindUi() = launch (Dispatchers.Main){
        val weatherLocation = viewModel.weatherLocation.await()
        val weatherDetail = viewModel.weatherDeatail.await()

        weatherLocation.observe(this@WeatherDetailFragment , Observer {
            if (it == null) return@Observer

            (activity as? AppCompatActivity)?.supportActionBar?.title =
                if (it.name.isEmpty() || it.name == null ) it.tzId else it.name
        })

        weatherDetail.observe(this@WeatherDetailFragment , Observer {


            if (it == null) return@Observer

            (activity as? AppCompatActivity)?.supportActionBar?.subtitle = it.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
            updateTemperatures(it.avgTemperature,
                it.minTemperature, it.maxTemperature)
            updateCondition(it.conditionText)
            updatePrecipitation(it.totalPrecipitation)
            updateWindSpeed(it.maxWindSpeed)
            updateVisibility(it.avgVisibilityDistance)
            updateUv(it.uv)

            GlideApp.with(this@WeatherDetailFragment.activity!!).load("http:${it.conditionIconUrl}").dontAnimate().into(imageView_condition_icon)
            Log.v("locf" , "http:${it.conditionIconUrl}")
        })

    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetric) metric else imperial
    }

    private fun updateTemperatures(temperature: Double, min: Double, max: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        textView_temperature.text = "$temperature$unitAbbreviation"
        textView_min_max_temperature.text = "Min: $min$unitAbbreviation, Max: $max$unitAbbreviation"
    }

    private fun updateCondition(condition: String) {
        textView_condition.text = condition
    }

    private fun updatePrecipitation(precipitationVolume: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm", "in")
        textView_precipitation.text = "Precipitation: $precipitationVolume $unitAbbreviation"
    }

    private fun updateWindSpeed(windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        textView_wind.text = "Wind speed: $windSpeed $unitAbbreviation"
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("km", "mi.")
        textView_visibility.text = "Visibility: $visibilityDistance $unitAbbreviation"
    }

    private fun updateUv(uv: Double) {
        textView_uv.text = "UV: $uv"
    }

}
