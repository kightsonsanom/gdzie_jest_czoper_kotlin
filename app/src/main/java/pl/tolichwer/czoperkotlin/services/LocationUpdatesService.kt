package pl.tolichwer.czoperkotlin.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.android.AndroidInjection
import dagger.android.DaggerService
import pl.tolichwer.czoperkotlin.R
import pl.tolichwer.czoperkotlin.ui.NavigationActivity
import javax.inject.Inject

const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 30000
const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = UPDATE_INTERVAL_IN_MILLISECONDS / 2
const val NOTIFICATION_ID = 34
const val PACKAGE_NAME = "pl.tolichwer.czoperkotlin.services"
const val EXTRA_STARTED_FROM_NOTIFICATION = "$PACKAGE_NAME.started_from_notification"
const val NOTIFICATION_CHANNEL_ID = "channel_01"

class LocationUpdatesService : DaggerService() {

    private val binder = LocalBinder()
    private var changingConfiguration = false

    private lateinit var location: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var locationRequest: LocationRequest

    @Inject
    lateinit var locationHelper: LocationServiceHelper

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult?.lastLocation)
            }
        }
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            notificationChannel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        createLocationRequest()
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        locationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun onNewLocation(newLocation: Location?) {
        location = newLocation!!

        locationHelper.startProcessingLocation(newLocation)
        if (!locationHelper.isServiceRunningInForeground()) {
            notificationManager.notify(NOTIFICATION_ID, getNotification())
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        changingConfiguration = true
    }

    override fun onBind(intent: Intent?): IBinder? {
        stopForeground(true)
        changingConfiguration = false
        return binder
    }

    inner class LocalBinder : Binder() {
        val service: LocationUpdatesService
            get() = this@LocationUpdatesService
    }

    override fun onUnbind(intent: Intent): Boolean {
        startForeground(NOTIFICATION_ID, getNotification())
        return true
    }

    private fun getNotification(): Notification {
        val intent = Intent(this, LocationUpdatesService::class.java)

        val notificationText = getString(R.string.location_update_text)
        val notificationTitle = getString(R.string.location_update_title)

        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true)

        val activityPendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, NavigationActivity::class.java), 0
        )

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .addAction(
                R.drawable.ic_exit_to_app_black_24dp, getString(R.string.launch_activity),
                activityPendingIntent
            )
            .setContentText(notificationText)
            .setContentTitle(notificationTitle)
            .setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setWhen(System.currentTimeMillis())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NOTIFICATION_CHANNEL_ID)
        }

        return builder.build()
    }

    fun requestLocationUpdates() {
        startService(Intent(applicationContext, LocationUpdatesService::class.java))
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback, Looper.myLooper()
            )
        } catch (unlikely: SecurityException) {
            Log.e("logTag", "Lost location permission. Could not request updates. $unlikely")
        }
    }
}