package com.lampro.myaccuweather.adapters

import com.lampro.myaccuweather.R
import com.lampro.myaccuweather.databinding.LayoutItemLocationBinding
import com.lampro.myaccuweather.objects.currentweatherresponse.CurrentWeatherResponseItem
import com.lampro.weatherapp.base.BaseRecyclerViewAdapter

class LocationWeatherAdapter: BaseRecyclerViewAdapter<CurrentWeatherResponseItem,LayoutItemLocationBinding>(){
    override fun getLayout(): Int = R.layout.layout_item_location
    override fun onBindViewHolder(
        holder: BaseViewHolder<LayoutItemLocationBinding>,
        position: Int
    ) {
        holder.binding.currentResponse  = mListData[position]

        holder.binding.itemBody.setOnClickListener{
            listener?.invoke(it,mListData[position],position)
        }
    }

}