package com.lampro.myaccuweather.ui.activities

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.lampro.myaccuweather.base.BaseActivity
import com.lampro.myaccuweather.databinding.ActivityMainBinding
import com.lampro.myaccuweather.ui.fragments.HomeWeatherFragment
import com.lampro.myaccuweather.ui.fragments.StartFragment
import com.lampro.myaccuweather.utils.PrefManager
import com.lampro.myaccuweather.viewmodels.mainviewmodel.MainViewModel
import java.io.Serializable

class MainActivity : BaseActivity<ActivityMainBinding>(), Serializable {

    override fun inflateBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (PrefManager.getStatus()) {
            replaceFragment(HomeWeatherFragment.newInstance(null, this), "", "")
        } else {
            replaceFragment(StartFragment.newInstance(this, null), "", "")
        }
    }
}