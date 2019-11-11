package pl.tolichwer.czoperkotlin.ui

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.support.DaggerAppCompatActivity
import pl.tolichwer.czoperkotlin.R
import pl.tolichwer.czoperkotlin.db.SharedPreferencesRepository
import pl.tolichwer.czoperkotlin.di.ViewModelFactory
import pl.tolichwer.czoperkotlin.services.LocationUpdatesService
import pl.tolichwer.czoperkotlin.ui.loginView.LoginActivityViewModel
import pl.tolichwer.czoperkotlin.ui.mapView.PERMISSION_REQUEST_CODE
import javax.inject.Inject

class NavigationActivity : DaggerAppCompatActivity() {

    private var isServiceBound = false
    private lateinit var locationService: LocationUpdatesService
    private lateinit var viewModel: LoginActivityViewModel

    @Inject
    lateinit var sharedPreferencesRepository: SharedPreferencesRepository

    @Inject
    lateinit var viewModelFactory: ViewModelFactory


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
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(LoginActivityViewModel::class.java)


        val navController = findNavController(R.id.nav_host_fragment)
        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        navView?.setupWithNavController(navController)

        checkPermissions()

        bindService(
            Intent(this, LocationUpdatesService::class.java), serviceConnection,
            Context.BIND_AUTO_CREATE
        )

    }

    override fun onResume() {
        super.onResume()
        isServiceBound = sharedPreferencesRepository.isLocationServiceRunning()
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                startGeoService()
            }
        }
    }

    private fun startGeoService() {
        Log.d("NavigationActivity", "startingGeoService")

        locationService.requestLocationUpdates()
    }



    override fun onStop() {
        if (isServiceBound) {
            unbindService(serviceConnection)
            sharedPreferencesRepository.setLocationServiceStatus(false)
        }

        super.onStop()
    }
}
