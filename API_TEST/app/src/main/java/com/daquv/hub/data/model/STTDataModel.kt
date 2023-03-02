package com.daquv.hub.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class STTDataModel(
    @SerializedName("result")
    val data: STTDataModelResult
) : Serializable


data class STTDataModelResult(
    @SerializedName("state")
    val state: String,

    @SerializedName("state_sec")
    val stateSec: String = "",

    @SerializedName("vad_total_sec")
    val vadTotalSec: String = ""
) : Serializable

