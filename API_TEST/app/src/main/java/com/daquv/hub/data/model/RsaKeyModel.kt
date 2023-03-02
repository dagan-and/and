package com.daquv.hub.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class RsaKeyModel(
    @SerializedName("timestamp")
    val timestamp: String = "",

    @SerializedName("status")
    val status: Int = 0,

    @SerializedName("retCd")
    val retCd: String = "",

    @SerializedName("body")
    val body: RsaBodyResponse?
) : Serializable

@Keep
data class RsaBodyResponse (
    @SerializedName("RSAModulus")
    val rsaModulus: String,

    @SerializedName("RSAExponent")
    val rsaExponent: String
) : Serializable