package com.daquv.hub.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class VerifyCodeModel(

    @SerializedName("RESP_DATA")
    val response: RequestCodeResponse?
)

@Keep
data class VerifyCodeResponse(
    @SerializedName("RSLT_CD")
    val rslt_cd: String,

    @SerializedName("RSLT_MSG")
    val msg: String,

)











