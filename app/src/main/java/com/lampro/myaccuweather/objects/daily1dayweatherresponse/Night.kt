package com.lampro.myaccuweather.objects.daily1dayweatherresponse


import com.google.gson.annotations.SerializedName

data class Night(
    @SerializedName("CloudCover")
    var cloudCover: Int,
    @SerializedName("Evapotranspiration")
    var evapotranspiration: EvapotranspirationX,
    @SerializedName("HasPrecipitation")
    var hasPrecipitation: Boolean,
    @SerializedName("HoursOfIce")
    var hoursOfIce: Int,
    @SerializedName("HoursOfPrecipitation")
    var hoursOfPrecipitation: Int,
    @SerializedName("HoursOfRain")
    var hoursOfRain: Int,
    @SerializedName("HoursOfSnow")
    var hoursOfSnow: Int,
    @SerializedName("Ice")
    var ice: IceX,
    @SerializedName("IceProbability")
    var iceProbability: Int,
    @SerializedName("Icon")
    var icon: Int,
    @SerializedName("IconPhrase")
    var iconPhrase: String,
    @SerializedName("LongPhrase")
    var longPhrase: String,
    @SerializedName("PrecipitationIntensity")
    var precipitationIntensity: String,
    @SerializedName("PrecipitationProbability")
    var precipitationProbability: Int,
    @SerializedName("PrecipitationType")
    var precipitationType: String,
    @SerializedName("Rain")
    var rain: RainX,
    @SerializedName("RainProbability")
    var rainProbability: Int,
    @SerializedName("RelativeHumidity")
    var relativeHumidity: RelativeHumidityX,
    @SerializedName("ShortPhrase")
    var shortPhrase: String,
    @SerializedName("Snow")
    var snow: SnowX,
    @SerializedName("SnowProbability")
    var snowProbability: Int,
    @SerializedName("SolarIrradiance")
    var solarIrradiance: SolarIrradianceX,
    @SerializedName("ThunderstormProbability")
    var thunderstormProbability: Int,
    @SerializedName("TotalLiquid")
    var totalLiquid: TotalLiquidX,
    @SerializedName("WetBulbGlobeTemperature")
    var wetBulbGlobeTemperature: WetBulbGlobeTemperatureX,
    @SerializedName("WetBulbTemperature")
    var wetBulbTemperature: WetBulbTemperatureX,
    @SerializedName("Wind")
    var wind: WindX,
    @SerializedName("WindGust")
    var windGust: WindGustX
)