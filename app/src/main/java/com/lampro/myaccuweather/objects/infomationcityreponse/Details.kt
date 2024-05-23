package com.lampro.myaccuweather.objects.infomationcityreponse


import com.google.gson.annotations.SerializedName

data class Details(
    @SerializedName("BandMap")
    var bandMap: String,
    @SerializedName("CanonicalLocationKey")
    var canonicalLocationKey: String,
    @SerializedName("CanonicalPostalCode")
    var canonicalPostalCode: String,
    @SerializedName("Climo")
    var climo: String,
    @SerializedName("Key")
    var key: String,
    @SerializedName("LocalRadar")
    var localRadar: String,
    @SerializedName("LocationStem")
    var locationStem: String,
    @SerializedName("MarineStation")
    var marineStation: String,
    @SerializedName("MarineStationGMTOffset")
    var marineStationGMTOffset: Any,
    @SerializedName("MediaRegion")
    var mediaRegion: Any,
    @SerializedName("Metar")
    var metar: String,
    @SerializedName("NXMetro")
    var nXMetro: String,
    @SerializedName("NXState")
    var nXState: String,
    @SerializedName("PartnerID")
    var partnerID: Any,
    @SerializedName("Population")
    var population: Int,
    @SerializedName("PrimaryWarningCountyCode")
    var primaryWarningCountyCode: String,
    @SerializedName("PrimaryWarningZoneCode")
    var primaryWarningZoneCode: String,
    @SerializedName("Satellite")
    var satellite: String,
    @SerializedName("Sources")
    var sources: List<Source>,
    @SerializedName("StationCode")
    var stationCode: String,
    @SerializedName("StationGmtOffset")
    var stationGmtOffset: Int,
    @SerializedName("Synoptic")
    var synoptic: String,
    @SerializedName("VideoCode")
    var videoCode: String
)