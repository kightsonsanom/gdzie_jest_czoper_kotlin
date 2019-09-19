package pl.tolichwer.czoperkotlin.ui.mapView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.MapView
import dagger.android.support.DaggerFragment
import pl.tolichwer.czoperkotlin.R
import pl.tolichwer.czoperkotlin.databinding.MapFragmentBinding

class MapFragment : DaggerFragment() {

    private lateinit var mapView: MapView
    private lateinit var binding: MapFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.map_fragment, container, false)
        mapView = binding.mapview
        mapView.onCreate(savedInstanceState)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }
}