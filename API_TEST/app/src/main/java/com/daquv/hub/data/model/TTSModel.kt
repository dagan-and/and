package com.daquv.hub.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class TTSTestModel {

}
@Keep
data class TTSTResponse(
    @SerializedName("status")
    val status: String = "000",

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("detailMsg")
    val detailMsg: String = "",

    @SerializedName("error_code")
    val error_code: String = "",

    @SerializedName("error_message")
    val error_message: String = "",

    @SerializedName("body")
    val items: TTSDataResponse

)

@Keep
data class TTSDataResponse (

    @SerializedName("binaryString")
    val binaryString: String = "",
    @SerializedName("binaryResult")
    val binaryResult: String = "",
    @SerializedName("outputStream")
    val outputStream: String = "",
    @SerializedName("msg")
    val msg: String = ""
)