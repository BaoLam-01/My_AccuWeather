package com.lampro.myaccuweather.adapters

import com.lampro.myaccuweather.R
import com.lampro.myaccuweather.base.BaseRecyclerViewAdapter
import com.lampro.myaccuweather.databinding.LayoutItemLocationBinding
import com.lampro.myaccuweather.objects.locationdata.LocationItem
import com.lampro.myaccuweather.utils.PrefManager

class LocationWeatherAdapter: BaseRecyclerViewAdapter<LocationItem, LayoutItemLocationBinding>(){
    override fun getLayout(): Int = R.layout.layout_item_location
    private lateinit var callBack : IOnLocationClick
    override fun onBindViewHolder(
        holder: BaseViewHolder<LayoutItemLocationBinding>,
        position: Int
    ) {
        holder.binding.location = mListData[position]
        holder.binding.itemBody.setOnClickListener{
            listener?.invoke(it,mListData[position],position)
            callBack.onLocationClick(mListData[position])
        }
        if (PrefManager.getCurrentUnits() == "℃") {
            holder.binding.tvLocationTemp.text = mListData[position].temp.toString() + "℃"
        } else {
            val c = mListData[position].temp
            val f = (c * 9/5f) + 32
            holder.binding.tvLocationTemp.text = f.toInt().toString() + "℉"
        }
    }
    fun setCallBack(callBack : IOnLocationClick){this.callBack = callBack}
    interface IOnLocationClick{
        fun onLocationClick(locationItem: LocationItem)
    }
}
