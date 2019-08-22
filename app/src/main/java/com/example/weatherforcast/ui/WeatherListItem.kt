package com.example.weatherforcast.ui

import com.example.weatherforcast.Externals.GlideApp
import com.example.weatherforcast.Models.UnitSpecific.Future.List.MetricFutureWeatherEntry
import com.example.weatherforcast.Models.UnitSpecific.Future.List.SpecificSimpleFutureWeatherEntry
import com.example.weatherforcast.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_future_forcast.*
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle


class WeatherListItem(
    val futureWeatherEntry: SpecificSimpleFutureWeatherEntry
) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            textView_condition.text = futureWeatherEntry.conditionText
            updateTemprature()
            updateDate()
            updateWeatherIcon()
        }
    }

    override fun getLayout() = R.layout.item_future_forcast

    private fun ViewHolder.updateDate(){
        val dtFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        textView_date.text = futureWeatherEntry.date.format(dtFormatter)
    }

    private fun ViewHolder.updateTemprature(){
        val unit = if (futureWeatherEntry is MetricFutureWeatherEntry) "°C"
        else "°F"
        var temprature = futureWeatherEntry.avgTemperature.toString()
        textView_temperature.text = "$temprature$unit"
    }

    private fun ViewHolder.updateWeatherIcon(){
        GlideApp.with(this.containerView).load("http:" + futureWeatherEntry.conditionIconUrl)
            .into(imageView_condition_icon)
    }

}