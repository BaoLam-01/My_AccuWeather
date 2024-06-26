package com.lampro.myaccuweather.adapters

import com.lampro.myaccuweather.R
import com.lampro.myaccuweather.base.BaseRecyclerViewAdapter
import com.lampro.myaccuweather.databinding.LayoutItemHourBinding
import com.lampro.myaccuweather.objects.hourlyweatherresponse.HourlyWeatherResponseItem
import com.lampro.myaccuweather.utils.PrefManager
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class HourlyWeatherAdapter : BaseRecyclerViewAdapter<HourlyWeatherResponseItem, LayoutItemHourBinding>(){
    override fun getLayout(): Int = R.layout.layout_item_hour

    override fun onBindViewHolder(holder: BaseViewHolder<LayoutItemHourBinding>, position: Int) {
        holder.binding.hourlyWeatherResponse= mListData[position]
        holder.binding.itemBody.setOnClickListener{
            listener?.invoke(it,mListData[position],position)
        }
        val epochSeconds = mListData[position].epochDateTime.toLong()
        val instant = Instant.ofEpochSecond(epochSeconds)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("HH.mm")
        val formattedDate = dateTime.format(formatter)
        holder.binding.tvhourItem.setText(formattedDate)
        holder.binding.tvUnits.text = PrefManager.getCurrentUnits()
    }

}