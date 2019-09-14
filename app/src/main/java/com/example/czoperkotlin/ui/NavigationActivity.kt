package com.example.czoperkotlin.ui

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.czoperkotlin.R
import com.example.czoperkotlin.db.SharedPreferencesRepository
import com.example.czoperkotlin.services.LocationUpdatesService
import com.example.czoperkotlin.ui.mapView.MapFragment
import com.example.czoperkotlin.ui.positionList.PositionListFragment
import com.example.czoperkotlin.ui.searchView.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class NavigationActivity : DaggerAppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 100
    private var isServiceBound = false
    private lateinit var locationService: LocationUpdatesService

    @Inject
    lateinit var sharedPreferencesRepository: SharedPreferencesRepository

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, PositionListFragment(), getString(R.string.geo_list_fragment_tag))
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, MapFragment(), getString(R.string.map_fragment_tag))
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, SearchFragment(), getString(R.string.search_fragment_tag))
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocationUpdatesService.LocalBinder
            locationService = binder.service
            sharedPreferencesRepository.setLocationServiceStatus(true)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            sharedPreferencesRepository.setLocationServiceStatus(false)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)


        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        if (savedInstanceState == null) {
            val fragment = MapFragment()
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment, getString(R.string.search_fragment_tag))
                .commit()
            navView.selectedItemId = R.id.navigation_dashboard
        }
        bindService(
            Intent(this, LocationUpdatesService::class.java), serviceConnection,
            Context.BIND_AUTO_CREATE
        )

        checkPermissions()
    }

    override fun onResume() {
        super.onResume()
        isServiceBound = sharedPreferencesRepository.isLocationServiceRunning()

    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode){
            PERMISSION_REQUEST_CODE -> {
                startGeoService()
            }
        }
    }

    private fun startGeoService() {
        locationService.requestLocationUpdates()
    }

    override fun onStop() {
        if (isServiceBound){
            unbindService(serviceConnection)
            sharedPreferencesRepository.setLocationServiceStatus(false)
        }

        super.onStop()
    }

}
