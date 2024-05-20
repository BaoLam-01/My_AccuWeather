package com.lampro.myaccuweather.objects.hourlyweatherresponse


import com.google.gson.annotations.SerializedName

data class HourlyWeatherResponseItem(
    @SerializedName("Ceiling")
    var ceiling: Ceiling,
    @SerializedName("CloudCover")
    var cloudCover: Int,
    @SerializedName("DateTime")
    var dateTime: String,
    @SerializedName("DewPoint")
    var dewPoint: DewPoint,
    @SerializedName("EpochDateTime")
    var epochDateTime: Int,
    @SerializedName("Evapotranspiration")
    var evapotranspiration: Evapotranspiration,
    @SerializedName("HasPrecipitation")
    var hasPrecipitation: Boolean,
    @SerializedName("Ice")
    var ice: Ice,
    @SerializedName("IceProbability")
    var iceProbability: Int,
    @SerializedName("IconPhrase")
    var iconPhrase: String,
    @SerializedName("IndoorRelativeHumidity")
    var indoorRelativeHumidity: Int,
    @SerializedName("IsDaylight")
    var isDaylight: Boolean,
    @SerializedName("Link")
    var link: String,
    @SerializedName("MobileLink")
    var mobileLink: String,
    @SerializedName("PrecipitationIntensity")
    var precipitationIntensity: String,
    @SerializedName("PrecipitationProbability")
    var precipitationProbability: Int,
    @SerializedName("PrecipitationType")
    var precipitationType: String,
    @SerializedName("Rain")
    var rain: Rain,
    @SerializedName("RainProbability")
    var rainProbability: Int,
    @SerializedName("RealFeelTemperature")
    var realFeelTemperature: RealFeelTemperature,
    @SerializedName("RealFeelTemperatureShade")
    var realFeelTemperatureShade: RealFeelTemperatureShade,
    @SerializedName("RelativeHumidity")
    var relativeHumidity: Int,
    @SerializedName("Snow")
    var snow: Snow,
    @SerializedName("SnowProbability")
    var snowProbability: Int,
    @SerializedName("SolarIrradiance")
    var solarIrradiance: SolarIrradiance,
    @SerializedName("Temperature")
    var temperature: Temperature,
    @SerializedName("ThunderstormProbability")
    var thunderstormProbability: Int,
    @SerializedName("TotalLiquid")
    var totalLiquid: TotalLiquid,
    @SerializedName("UVIndex")
    var uVIndex: Int,
    @SerializedName("UVIndexText")
    var uVIndexText: String,
    @SerializedName("Visibility")
    var visibility: Visibility,
    @SerializedName("WeatherIcon")
    var weatherIcon: Int,
    @SerializedName("WetBulbGlobeTemperature")
    var wetBulbGlobeTemperature: WetBulbGlobeTemperature,
    @SerializedName("WetBulbTemperature")
    var wetBulbTemperature: WetBulbTemperature,
    @SerializedName("Wind")
    var wind: Wind,
    @SerializedName("WindGust")
    var windGust: WindGust
)