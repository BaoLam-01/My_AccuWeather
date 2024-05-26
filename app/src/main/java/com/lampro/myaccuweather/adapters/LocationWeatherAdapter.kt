package com.lampro.myaccuweather.adapters

import android.location.Location
import com.lampro.myaccuweather.R
import com.lampro.myaccuweather.base.BaseRecyclerViewAdapter
import com.lampro.myaccuweather.databinding.LayoutItemLocationBinding
import com.lampro.myaccuweather.objects.currentweatherresponse.CurrentWeatherResponse
import com.lampro.myaccuweather.objects.locationdata.Locationitem

class LocationWeatherAdapter: BaseRecyclerViewAdapter<Locationitem, LayoutItemLocationBinding>(){
    override fun getLayout(): Int = R.layout.layout_item_location
    override fun onBindViewHolder(
        holder: BaseViewHolder<LayoutItemLocationBinding>,
        position: Int
    ) {
        holder.binding.location = mListData[position]

        holder.binding.itemBody.setOnClickListener{
            listener?.invoke(it,mListData[position],position)
        }
    }

}