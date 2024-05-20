package com.lampro.myaccuweather.adapters

import android.widget.BaseAdapter
import com.lampro.myaccuweather.R
import com.lampro.myaccuweather.databinding.LayoutItemDayBinding
import com.lampro.myaccuweather.databinding.LayoutItemHourBinding
import com.lampro.myaccuweather.objects.dailyweatherresponse.DailyForecast
import com.lampro.myaccuweather.objects.dailyweatherresponse.DailyWeatherResponse
import com.lampro.weatherapp.base.BaseRecyclerViewAdapter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DailyWeatherAdapter : BaseRecyclerViewAdapter<DailyForecast,LayoutItemDayBinding>(){
    override fun getLayout(): Int = R.layout.layout_item_day
    override fun onBindViewHolder(holder: BaseViewHolder<LayoutItemDayBinding>, position: Int) {
        holder.binding.dailyForecasts = mListData[position]
        holder.binding.itemBody.setOnClickListener{
            listener?.invoke(it,mListData[position],position)
        }
        val epochSeconds = mListData[position].epochDate.toLong()
        val instant = Instant.ofEpochSecond(epochSeconds)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("HH.mm")
        val formattedDate = dateTime.format(formatter)
        holder.binding.hourItem.setText(formattedDate)
    }
}
