package com.example.weatherforcast.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforcast.Models.DateTypeConverter
import com.example.weatherforcast.Models.UnitSpecific.Future.List.SpecificSimpleFutureWeatherEntry
import com.example.weatherforcast.ViewModels.FutureForcastViewModel
import com.example.weatherforcast.R
import com.example.weatherforcast.ViewModels.Factories.FutureWeatherFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.future_forcast_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDate


class FutureForcastFragment : BaseScopedFragment() , KodeinAware {

    override val kodein by closestKodein()

    private val futureWeatherFactory:FutureWeatherFactory by instance()

    private lateinit var viewModel: FutureForcastViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_forcast_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Forecast"

        viewModel = ViewModelProviders.of(this , futureWeatherFactory).get(FutureForcastViewModel::class.java)

        bindUi()
    }

    private fun bindUi() = launch (Dispatchers.Main){
        var weathcerList = viewModel.weather.await()
        var weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(this@FutureForcastFragment , Observer {
            if (it == null) return@Observer
            (activity as? AppCompatActivity)?.supportActionBar?.title = it.name
        })

        weathcerList.observe(this@FutureForcastFragment , Observer {

            if (it == null) return@Observer

            group_loading.visibility = View.GONE

            (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Next Week"

            initRecyclerViewList(it.toWeatherListItem())
        })

    }

    private fun List<SpecificSimpleFutureWeatherEntry>.toWeatherListItem():List<WeatherListItem>{
        return this.map {
            WeatherListItem(it)
        }
    }

    private fun initRecyclerViewList(items : List<WeatherListItem>){

        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FutureForcastFragment.context)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener{ item, view ->
            (item as? WeatherListItem)?.let {
                goToDetail(it.futureWeatherEntry.date , view)
            }

        }

    }

    private fun goToDetail(date:LocalDate , view:View){
        var dateConverted = DateTypeConverter.dateToString(date)
        var action = FutureForcastFragmentDirections.goToDetail(dateConverted!!)
        Navigation.findNavController(view).navigate(action)
    }

}
