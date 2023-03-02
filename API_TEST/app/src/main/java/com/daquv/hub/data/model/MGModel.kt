package com.daquv.hub.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class MGModel(

    @SerializedName("RESP_DATA")
    val mgResponse: MGResponse
)
@Keep
data class MGResponse(
    @SerializedName("_tran_res_data")
    val mgData: MGData
)

@Keep
data class MGData(

    @SerializedName("c_site_url")
    val c_site_url: String,

    @SerializedName("c_session_time")
    val c_session_time: String,

    @SerializedName("c_available_service")
    val c_available_service: Boolean,

    @SerializedName("c_available_act")
    val c_available_act: String,

    @SerializedName("c_program_ver")
    val c_program_ver: String,

    @SerializedName("c_minimum_ver")
    val c_minimum_ver: String,

    @SerializedName("c_update_act")
    val c_update_act: String,

    @SerializedName("c_appstore_url")
    val c_appstore_url: String,

    @SerializedName("c_register_pass")
    val c_register_pass: Boolean,

    @SerializedName("c_notice_yn")
    val c_notice_yn: Boolean,

    @SerializedName("c_notice_act")
    val c_notice_act: MgNotice,

    @SerializedName("c_web_url")
    val c_web_url: MgWebUrl,

    @SerializedName("google_hint_version")
    val google_hint_version: String,

    @SerializedName("google_hint_url")
    val google_hint_url: String
)
@Keep
data class MgNotice(
    @SerializedName("c_act_url")
    val c_act_url: String,

    @SerializedName("c_act")
    val c_act: String,
)
@Keep
data class MgWebUrl(
    @SerializedName("sign_up_url")
    val sign_up_url: String,

    @SerializedName("ques_main_url")
    val ques_main_url: String,

    @SerializedName("data_main_url")
    val data_main_url: String,

    @SerializedName("tts_main_url")
    val tts_main_url: String,

    @SerializedName("login_url")
    val login_url: String,
)










