package com.example.czoperkotlin.ui.mapView

import androidx.databinding.BindingAdapter
import android.util.Log
import android.view.View

class MapBindingAdapter {

    @BindingAdapter("android:layout_width")
    fun setLayoutWidth(view: View, width: Double){
        Log.d("LogTag", "MapBindingAdapter")
    }

}