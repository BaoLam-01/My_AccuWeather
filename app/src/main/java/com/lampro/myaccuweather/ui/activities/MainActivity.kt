package com.lampro.myaccuweather.ui.activities

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.lampro.myaccuweather.R
import com.lampro.myaccuweather.base.BaseActivity
import com.lampro.myaccuweather.databinding.ActivityMainBinding
import com.lampro.myaccuweather.ui.fragments.HomeWeatherFragment
import com.lampro.myaccuweather.ui.fragments.StartFragment
import com.lampro.myaccuweather.utils.PrefManager
import java.io.Serializable
import java.util.Locale

class MainActivity : BaseActivity<ActivityMainBinding>(), Serializable {

    override fun inflateBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusBar)
            window.navigationBarColor = ContextCompat.getColor(this,R.color.navigationBar)
        }
        if (PrefManager.getStatus()) {
            replaceFragment(HomeWeatherFragment.newInstance(null, this), "", "")
        } else {
            replaceFragment(StartFragment.newInstance(this, null), "", "")
        }
        if (PrefManager.getCurrentLang() == "") {
            val language: String = resources.configuration.locales[0].language
            setLanguage(language)
            PrefManager.setCurrentLang(language)
            Log.d(TAG, "onCreate: " + language)
        } else {
            setLanguage(PrefManager.getCurrentLang())
        }
    }

    private fun setLanguage(language: String) {
        val configuration = resources.configuration
        val locale = Locale(language)
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}