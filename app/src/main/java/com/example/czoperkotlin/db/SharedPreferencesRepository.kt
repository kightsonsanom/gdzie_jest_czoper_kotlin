package com.example.czoperkotlin.db

import android.content.Context
import android.content.SharedPreferences
import com.example.czoperkotlin.R

class SharedPreferencesRepository (context: Context) {

    private val KEY_LOCATION_SERVICE_STATUS = "key_location_service_status"
    private val editor: SharedPreferences.Editor
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE)

    init{
        editor = sharedPreferences.edit()
    }


    fun isLocationServiceRunning(): Boolean{
        return sharedPreferences.getBoolean(KEY_LOCATION_SERVICE_STATUS, false)
    }

    fun setLocationServiceStatus(isRunning: Boolean){
        editor.putBoolean(KEY_LOCATION_SERVICE_STATUS,isRunning).commit()
    }






}