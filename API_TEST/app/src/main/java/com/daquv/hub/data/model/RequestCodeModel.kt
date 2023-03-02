package com.daquv.hub.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RequestCodeModel(

    @SerializedName("RESP_DATA")
    val response: RequestCodeResponse?
)

@Keep
data class RequestCodeResponse(
    @SerializedName("RSLT_CD")
    val rslt_cd: String,

    @SerializedName("TRSC_UNQ_NO")
    val trsc_unq_no: String,

    @SerializedName("RSLT_MSG")
    val msg: String,

)











