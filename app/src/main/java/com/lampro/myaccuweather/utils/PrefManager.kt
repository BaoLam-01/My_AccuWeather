package com.lampro.myaccuweather.utils

import android.content.Context
import com.google.gson.Gson
import com.lampro.myaccuweather.MyApplication
import com.lampro.myaccuweather.objects.locationdata.LocationItem

class PrefManager {
    companion object{

        val PREF_NAME: String = "Status_App"
        val statusApp =
            MyApplication.getAppContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        fun setStatus(status: Boolean) {
            statusApp.edit().putBoolean("status", status).commit()
        }
        fun getStatus(): Boolean{
            return statusApp.getBoolean("status",false)
        }
        val CURRENT_LOCATION: String = "Current_Location"
        val currentLocation = MyApplication.getAppContext().getSharedPreferences(CURRENT_LOCATION,Context.MODE_PRIVATE)
        fun setLocation(lat: Double, lon: Double){
            currentLocation.edit().putFloat("lat", lat.toFloat()).commit()
            currentLocation.edit().putFloat("lon", lon.toFloat()).commit()
        }
        fun getLocationLat(): Double{
            return currentLocation.getFloat("lat", 0f).toDouble()
        }
        fun getLocationLon(): Double{
            return currentLocation.getFloat("lon", 0f).toDouble()
        }
        fun setLocationKey(locationKey: String){
            currentLocation.edit().putString("key", locationKey).commit()
        }
        fun getLocationKey(): String {
            return currentLocation.getString("key", "")!!
        }


        val LIST_LOCATION : String = "list_Location"
        val listLocation = MyApplication.getAppContext().getSharedPreferences(LIST_LOCATION,Context.MODE_PRIVATE)
        fun saveListLocation(list: List<LocationItem>) {
            listLocation.edit().putString("listLocation",Gson().toJson(list)).commit()
        }
        fun getListLocation(): List<LocationItem>?{
            val json = listLocation.getString("listLocation",null)
            json?.let {
                val listLocation = Gson().fromJson(json, Array<LocationItem>::class.java).toList()
                return listLocation
            }
            return null
        }
    }
}