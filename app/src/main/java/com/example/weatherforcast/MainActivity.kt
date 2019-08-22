package com.example.weatherforcast

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.weatherforcast.Externals.LifeCycleLocationObserver
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 12

class MainActivity : AppCompatActivity() , KodeinAware {

    override val kodein by closestKodein()
    private val fusedLocationProviderClient:FusedLocationProviderClient by instance()

    var fusedLocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
        }

    }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        navController = Navigation.findNavController(this , R.id.nav_host_fragment)

        // This would take the id of each item in the bottom navigation menu and would go to the next fragment
        // that holds this id
        bottom_nav.setupWithNavController(navController)

        // Setting thr action bar with navigation to show a back arrow to go to previous fragment
        NavigationUI.setupActionBarWithNavController( this , navController)


        if (hasLocationPermission()){
            bindLocationManager()
        } else {
            //requestLocationPermission()
        }

    }

    // Setting the drawable icon to backward arrow (leave it null if you don't wanna put a custom icon)
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController , null)
    }

    private fun bindLocationManager(){
        LifeCycleLocationObserver(
            this ,
            fusedLocationProviderClient,
            fusedLocationCallback
        )
    }

    private fun requestLocationPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSION_ACCESS_COARSE_LOCATION
        )
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == MY_PERMISSION_ACCESS_COARSE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                bindLocationManager()
            else
                Toast.makeText(this, "Please, set location manually in settings", Toast.LENGTH_LONG).show()
        }
    }

}
