package com.lampro.myaccuweather.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lampro.myaccuweather.base.BaseFragment
import com.lampro.myaccuweather.databinding.FragmentStartBinding
import com.lampro.myaccuweather.ui.activities.MainActivity
import com.lampro.myaccuweather.utils.PrefManager


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class StartFragment : BaseFragment<FragmentStartBinding>() {
    private var param1: MainActivity? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStartBinding = FragmentStartBinding.inflate(layoutInflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as MainActivity?
            param2 = it.getString(ARG_PARAM2)
        }


        initView()



    }


    private fun initView() {

        binding.btnGetStart.setOnClickListener {
            param1?.replaceFragment(HomeWeatherFragment.newInstance(null,param1),"","")
            PrefManager.setStatus(true)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: MainActivity?, param2: String?) =
            StartFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}