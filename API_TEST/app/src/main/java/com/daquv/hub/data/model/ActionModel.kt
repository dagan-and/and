package com.daquv.hub.data.model

import androidx.annotation.Keep
import java.util.*


@Keep
data class ActionModel(
    val code: Int,
    val message : String,
    val objects: Any?
)






