package com.lampro.myaccuweather.adapters

import androidx.constraintlayout.widget.ConstraintSet
import com.lampro.myaccuweather.R
import com.lampro.myaccuweather.base.BaseRecyclerViewAdapter
import com.lampro.myaccuweather.databinding.LayoutItemRainHourBinding
import com.lampro.myaccuweather.objects.hourlyweatherresponse.HourlyWeatherResponse
import com.lampro.myaccuweather.objects.hourlyweatherresponse.HourlyWeatherResponseItem
import com.lampro.myaccuweather.utils.PrefManager
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class HourlyRainAdapter :
    BaseRecyclerViewAdapter<HourlyWeatherResponseItem, LayoutItemRainHourBinding>() {
    override fun getLayout(): Int = R.layout.layout_item_rain_hour
    override fun onBindViewHolder(
        holder: BaseViewHolder<LayoutItemRainHourBinding>,
        position: Int
    ) {

        holder.binding.itemBody.setOnClickListener {
            listener?.invoke(it, mListData[position], position)
        }
        holder.binding.hourlyWeatherResponse = mListData[position]
        holder.binding.tvPercentRain.text = mListData[position].rainProbability.toString() + "%"
        val epochSeconds = mListData[position].epochDateTime.toLong()
        val instant = Instant.ofEpochSecond(epochSeconds)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("HH.mm")
        val formattedDate = dateTime.format(formatter)
        holder.binding.tvTimeRain.setText(formattedDate)

        val constraintSet = ConstraintSet()
        constraintSet.clone(holder.binding.clRainHour)
        constraintSet.constrainHeight(
            holder.binding.vPercentRain.id,
            ConstraintSet.MATCH_CONSTRAINT
        )
        constraintSet.constrainPercentHeight(
            holder.binding.vPercentRain.id,
             mListData[position].rainProbability / 100f
        )
        constraintSet.applyTo(holder.binding.clRainHour)

        when (mListData[position].rainProbability) {
            in 0 .. 5-> {
                holder.binding.vPercentRain.setBackgroundResource(R.drawable.percent_rain_verylow)
                holder.binding.tvPercentRain.text = "<5%"
                constraintSet.constrainPercentHeight(
                    holder.binding.vPercentRain.id,
                    0.05f
                )
                constraintSet.applyTo(holder.binding.clRainHour)
            }
            in 6 .. 10 -> {
                holder.binding.vPercentRain.setBackgroundResource(R.drawable.percent_rain_verylow)
            }
            in 11 .. 20-> {
                holder.binding.vPercentRain.setBackgroundResource(R.drawable.percent_rain_low)
            }
            in 21 .. 40 -> {
                holder.binding.vPercentRain.setBackgroundResource(R.drawable.percent_rain_low2)
            }
            in 41 .. 60 -> {
                holder.binding.vPercentRain.setBackgroundResource(R.drawable.percent_rain_medium)
            }
            in 61 .. 90 -> {
                holder.binding.vPercentRain.setBackgroundResource(R.drawable.percnet_rain_high)
            }
            in 91 .. 100 -> {
                holder.binding.vPercentRain.setBackgroundResource(R.drawable.percent_rain_very_high)
            }
        }
    }
}