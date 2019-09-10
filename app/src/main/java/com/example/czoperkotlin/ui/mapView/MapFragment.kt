package com.example.czoperkotlin.ui.mapView

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.czoperkotlin.R
import com.example.czoperkotlin.databinding.MapFragmentBinding
import com.google.android.gms.maps.MapView
import dagger.android.support.DaggerFragment

class MapFragment : DaggerFragment() {

    private lateinit var mapView: MapView
    private lateinit var binding: MapFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LogTag", "MapFragment onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.map_fragment, container, false)
        mapView = binding.mapview
        mapView.onCreate(savedInstanceState)
        Log.d("LogTag", "MapFragment onCreateView")
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        Log.d("LogTag", "MapFragment onResume")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("LogTag", "MapFragment onActivityCreated")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("LogTag", "MapFragment onAttach")
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        Log.d("LogTag", "MapFragment onDestroy")
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        Log.d("LogTag", "MapFragment onPause")
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