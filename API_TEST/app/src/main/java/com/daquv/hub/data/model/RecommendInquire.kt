package com.daquv.hub.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RecommendInquire(
    @SerializedName("entity")
    val entity: ArrayList<String>,

    @SerializedName("intent")
    val intent: String = "",
) : Serializable
