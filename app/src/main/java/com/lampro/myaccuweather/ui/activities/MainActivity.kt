package com.lampro.myaccuweather.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.lampro.myaccuweather.R
import com.lampro.myaccuweather.databinding.ActivityMainBinding
import com.lampro.weatherapp.base.BaseActivity
import com.lampro.weatherapp.ui.fragments.HomeFragment
import kotlinx.coroutines.supervisorScope
import java.io.Serializable

class MainActivity : BaseActivity<ActivityMainBinding>(), Serializable {
    override fun inflateBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFragment(HomeFragment.newInstance(this, null), "", "")
    }


}