package com.example.czoperkotlin.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.example.czoperkotlin.R
import com.example.czoperkotlin.ui.geoList.GeoListFragment
import com.example.czoperkotlin.ui.mapView.MapFragment
import com.example.czoperkotlin.ui.searchView.SearchFragment

class NavigationActivity : AppCompatActivity() {

    private lateinit var textMessage: TextView
    private val PERMISSION_REQUEST_CODE = 100

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, GeoListFragment(), getString(R.string.geoListFragmentTag))
                    .commit()
            }
            R.id.navigation_dashboard -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, MapFragment(), getString(R.string.mapFragmentTag))
                    .commit()
            }
            R.id.navigation_notifications -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, SearchFragment(), getString(R.string.searchFragmentTag))
                    .commit()
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        textMessage = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        checkPermissions()
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        }
    }
}
