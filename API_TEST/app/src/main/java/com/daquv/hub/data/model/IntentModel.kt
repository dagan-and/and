package com.daquv.hub.data.model

import android.content.Intent
import androidx.annotation.Keep

@Keep
data class IntentModel(
    val code: Int,
    val intent: Intent
)