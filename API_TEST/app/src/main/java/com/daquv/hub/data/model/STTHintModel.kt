package com.daquv.hub.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class STTHintModel {

    @SerializedName("company_code")
    var companyCode : String = ""

    @SerializedName("version_code")
    var versionCode : String = ""

    @SerializedName("hint_value")
    val hintValue: Float = 5f

    @SerializedName("hint_array")
    var hintArray : ArrayList<String?> = ArrayList()

    @SerializedName("point_value")
    val pointValue: Float = 10f

    @SerializedName("point_array")
    var pointArray : ArrayList<String?> = ArrayList()
}