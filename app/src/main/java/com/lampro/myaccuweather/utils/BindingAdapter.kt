package com.lampro.myaccuweather.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.lampro.myaccuweather.R

object BindingAdapter {
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun bindAccuUrl2Image(imageView: ImageView, weatherIcon: Int?) {
        val formattedNumber = String.format("%02d",weatherIcon)
        Glide.with(imageView.context).load("https://developer.accuweather.com/sites/default/files/$formattedNumber-s.png").into(imageView)
    }
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun bindOpenUrl2Image(imageView: ImageView, weatherIcon: String?) {
        Glide.with(imageView.context).load("https://openweathermap.org/img/wn/$weatherIcon@2x.png").into(imageView)
    }
    @BindingAdapter("loadingGif")
    @JvmStatic
    fun loadImgUrl(imageView: ImageView, gif: Int?) {
        Glide.with(imageView.context).load(gif).into(imageView)
    }
}