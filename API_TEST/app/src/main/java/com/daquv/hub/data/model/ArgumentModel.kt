package com.daquv.hub.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class ArgumentModel(

    @SerializedName("RESP_DATA")
    val response: ArgumentResponse?
)
@Keep
data class ArgumentResponse(
    @SerializedName("AGRM_LIST")
    val data: ArrayList<ArgumentData>?
)

@Keep
data class ArgumentData(

    @SerializedName("AGRM_NM")
    val name: String,

    @SerializedName("AGRM_URL")
    val url: String,

)










