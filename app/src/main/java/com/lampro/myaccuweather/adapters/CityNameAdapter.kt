package com.lampro.myaccuweather.adapters

import com.lampro.myaccuweather.R
import com.lampro.myaccuweather.base.BaseRecyclerViewAdapter
import com.lampro.myaccuweather.databinding.LayoutItemCityBinding
import com.lampro.myaccuweather.objects.locationdata.LocationItem
import com.lampro.myaccuweather.utils.PrefManager

class CityNameAdapter : BaseRecyclerViewAdapter<LocationItem, LayoutItemCityBinding>() {
    override fun getLayout(): Int = R.layout.layout_item_city
    override fun onBindViewHolder(holder: BaseViewHolder<LayoutItemCityBinding>, position: Int) {
        holder.binding.itemBody.setOnClickListener {
            listener?.invoke(it, mListData[position], position)
            val listLocation = mutableListOf<LocationItem>()
            PrefManager.getListLocation()?.let { it1 -> listLocation.addAll(it1) }

            listLocation.removeIf {
                it.cityName.equals(mListData[position].cityName)
            }
            listLocation.add(0, mListData[0])
            PrefManager.saveListLocation(listLocation)

        }
        holder.binding.cityResponse = mListData[position]
    }
}