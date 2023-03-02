package com.daquv.hub.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class CallModel (
    @SerializedName("timestamp")
    val timestamp: String = "",

    @SerializedName("status")
    val status: Int = 0,

    @SerializedName("retCd")
    val retCd: String = "",

    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("body")
    val body:  ArrayList<CallListResponse>?
) : Serializable

@Keep
data class CallListResponse (
    @SerializedName("phoneNumber")
    val phoneNumber: String = "",
    @SerializedName("employeeName")
    val employeeName: String = "",
    @SerializedName("employeeNumber")
    val employeeNumber : String = "",
    @SerializedName("employeeRank")
    val employeeRank : String = "",
    @SerializedName("companyName")
    val companyName : String = "",
    @SerializedName("departmentName")
    val departmentName : String = "",
    @SerializedName("email")
    val email : String = "",
    @SerializedName("utterance")
    val utterance : String = ""
) : Serializable