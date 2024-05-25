package com.lampro.myaccuweather.adapters

import android.content.ContentValues.TAG
import android.util.Log
import com.lampro.myaccuweather.R
import com.lampro.myaccuweather.base.BaseRecyclerViewAdapter
import com.lampro.myaccuweather.databinding.LayoutItemDayBinding
import com.lampro.myaccuweather.objects.dailyweatherresponse.DailyWeather
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class DailyWeatherAdapter : BaseRecyclerViewAdapter<DailyWeather, LayoutItemDayBinding>(){
    override fun getLayout(): Int = R.layout.layout_item_day
    override fun onBindViewHolder(holder: BaseViewHolder<LayoutItemDayBinding>, position: Int) {

        holder.binding.itemBody.setOnClickListener{
            listener?.invoke(it,mListData[position],position)
            val epochSeconds = mListData[position].dt.toLong()
            val instant = Instant.ofEpochSecond(epochSeconds)
            val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            val formatter = DateTimeFormatter.ofPattern("EE yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            val formattedDate = dateTime.format(formatter)
            Log.d(TAG, "onBindViewHolder: " + formattedDate)
        }
        holder.binding.dailyWeather = mListData[position]
        val epochSeconds = mListData[position].dt.toLong()
        val instant = Instant.ofEpochSecond(epochSeconds)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("EE", Locale.ENGLISH)
        val formattedDate = dateTime.format(formatter)
        Log.d(TAG, "onBindViewHolder: $position")
        holder.binding.hourItem.setText(formattedDate)
        if (position == 0) {
            holder.binding.itemBody.setBackgroundResource(R.drawable.bg_dailyday_gradient_2)
        }

    }
}
