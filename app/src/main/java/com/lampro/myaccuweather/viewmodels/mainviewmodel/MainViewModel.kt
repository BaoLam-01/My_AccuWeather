package com.lampro.myaccuweather.viewmodels.mainviewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lampro.myaccuweather.ui.fragments.LocationFragment

class MainViewModel: ViewModel(){
     val myData =  MutableLiveData<Int>(null)
}