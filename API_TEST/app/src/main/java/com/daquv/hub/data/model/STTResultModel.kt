package com.daquv.hub.data.model
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Response sample
{
    "status": 200,
    "retCd" : "00000",
    "timestamp" : 1661924019691,
    "success" : true,
    "message":"성공적으로 호출되었습니다.",
    body" : {
        "url" : “/istn/salse/orderIssue.html”
        "tail":"Y",
        "ttsText":"hello..",
        "tts":"afasdf~~",
        “intent” :”intent code”,
        "entities":[
            "entity1",
            "entity2",
            "entity3"
        ]
    }
 }
 */

@Keep
data class STTResultModel(
    @SerializedName("timestamp")
    val timestamp: String = "",

    @SerializedName("status")
    val status: Int = 0,

    @SerializedName("retCd")
    val retCd: String = "",

    @SerializedName("success")
    var success: Boolean,

    @SerializedName("body")
    val body: STTBodyResponse?
)
@Keep
data class STTBodyResponse (
    @SerializedName("url")
    val url: String? = "",

    @SerializedName("tail")
    var tail: String?,

    @SerializedName("tts")
    val tts: String? = "",

    @SerializedName("ttsText")
    var ttsText: String? = "",

    @SerializedName("intent")
    val intent: String? = "",

    @SerializedName("webViewFlag")
    val webViewFlag: String? = "",

    @SerializedName("entities")
    var entities : ArrayList<Entities> = arrayListOf<Entities>()
)

@Keep
data class Entities (
    @SerializedName("entity")
    val entity: String? = "",

    @SerializedName("value")
    val value: String? = "",

    @SerializedName("fromDate")
    val fromDate: String? = "",

    @SerializedName("toDate")
    val toDate: String? = "",
)


