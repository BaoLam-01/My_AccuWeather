package com.lampro.myaccuweather.objects.locationkeyresponse


import com.google.gson.annotations.SerializedName

data class LocationKeyResponse(
    @SerializedName("AdministrativeArea")
    var administrativeArea: AdministrativeArea,
    @SerializedName("Country")
    var country: Country,
    @SerializedName("DataSets")
    var dataSets: List<String>,
    @SerializedName("EnglishName")
    var englishName: String,
    @SerializedName("GeoPosition")
    var geoPosition: GeoPosition,
    @SerializedName("IsAlias")
    var isAlias: Boolean,
    @SerializedName("Key")
    var key: String,
    @SerializedName("LocalizedName")
    var localizedName: String,
    @SerializedName("ParentCity")
    var parentCity: ParentCity,
    @SerializedName("PrimaryPostalCode")
    var primaryPostalCode: String,
    @SerializedName("Rank")
    var rank: Int,
    @SerializedName("Region")
    var region: Region,
    @SerializedName("SupplementalAdminAreas")
    var supplementalAdminAreas: List<Any>,
    @SerializedName("TimeZone")
    var timeZone: TimeZone,
    @SerializedName("Type")
    var type: String,
    @SerializedName("Version")
    var version: Int
)