package com.lampro.myaccuweather.utils

import android.content.Context
import com.lampro.myaccuweather.MyApplication

class PrefManager {
    companion object{

        val PREF_NAME: String = "Status_App"
        val sharedPreferences =
            MyApplication.getAppContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        fun setStatus(status: Boolean) {
            sharedPreferences.edit().putBoolean("status", status).commit()
        }
        fun getStatus(): Boolean{
            return sharedPreferences.getBoolean("status",false)
        }
    }
}