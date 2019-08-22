package com.example.weatherforcast.ui

import android.opengl.Visibility
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.weatherforcast.Externals.GlideApp
import com.example.weatherforcast.ViewModels.CurrentWeatherViewModel
import com.example.weatherforcast.R
import com.example.weatherforcast.ViewModels.Factories.CurrentWeatherFactory
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class CurrentWeatherFragment : BaseScopedFragment() , KodeinAware {

    override val kodein by closestKodein()

    private val currentWeatherFactory : CurrentWeatherFactory by instance()

    companion object {
        fun newInstance() = CurrentWeatherFragment()
    }

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Current"
        viewModel = ViewModelProviders.of(this , currentWeatherFactory).get(CurrentWeatherViewModel::class.java)

        bindUi()

    }

    private fun bindUi() = launch(Dispatchers.Main) {
        // await() here because this function is happening in IO thread wich is not the main thread
        // so we wanna wait till this thread finish to get our values
        var weathcer = viewModel.weather.await()
        var weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(this@CurrentWeatherFragment , Observer { location ->
            if (location == null) return@Observer

            (activity as? AppCompatActivity)?.supportActionBar?.title = location.tzId
            Log.v("locf" , location.country)
        })

        weathcer.observe(this@CurrentWeatherFragment , Observer {
            if (it == null) return@Observer

            group_loading.visibility = View.GONE
            (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
            updateTempreature(it.temperature , it.feelsLikeTemperature)
            updateCondition(it.conditionText)
            updatePrecipitation(it.precipitationVolume)
            updateWind(it.windDirection , it.windSpeed)
            updateVisibility(it.visibilityDistance)

            GlideApp.with(this@CurrentWeatherFragment.activity!!).load("http:${it.conditionIconUrl}").dontAnimate().into(imageView_condition_icon)
            Log.v("locf" , "http:${it.conditionIconUrl}")
        })
    }

    private fun choosingUnits(metric:String , imprial:String) : String{
        return if (viewModel.isMetric) metric else imprial
    }

    private fun updateTempreature(temp: Double , feelsLike: Double){
        var unit = choosingUnits(" C" , " F")
        textView_temperature.text = "$temp$unit"
        textView_feels_like_temperature.text = "Feels like $feelsLike$unit"
    }

    private fun updateCondition(condition: String) {
        textView_condition.text = condition
    }

    private fun updatePrecipitation(precipitationVolume: Double) {
        val unitAbbreviation = choosingUnits("mm", "in")
        textView_precipitation.text = "Preciptiation: $precipitationVolume $unitAbbreviation"
    }

    private fun updateWind(windDirection: String, windSpeed: Double) {
        val unitAbbreviation = choosingUnits("kph", "mph")
        textView_wind.text = "Wind: $windDirection, $windSpeed $unitAbbreviation"
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = choosingUnits("km", "mi.")
        textView_visibility.text = "Visibility: $visibilityDistance $unitAbbreviation"
    }



}
