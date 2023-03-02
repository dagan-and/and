package com.daquv.hub.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Response sample
{
    "status": 200,
    "retCd": "00000",
    "timestamp": 1662436971908,
    "success": true,
    "message": "로그인 성공",
    "body": {
        //나중에 카테고리 별로 묶여서 작업이 되어야함
        "recomend_quest": [
        {
            "quest": "질의 1",
            "intent": "1",
            "imgUrl": "https://daquv.com/img/Logo_black.svg"
        },
        {
            "quest": "질의 2",
            "intent": "2",
            "imgUrl": "https://daquv.com/img/Logo_black.svg"
        },
        {
            "quest": "질의 3",
            "intent": "3",
            "imgUrl": "https://daquv.com/img/Logo_black.svg"
        }
        ],
        "userInfo": {
            "email": "test@istn.co.kr",
            "employeeNumber": "0002",
            "employeeName": "홍길동",
            "departmentName": "Solution",
            "teamName": "CI",
            "rankName": "주임"
        },
        "jwtToken": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJpbmZvQGRhcXV2LmNvbSIsInN1YiI 6InRlc3RAaXN0bi5jby5rciIsImV4cCI6MTY2MjQ5Njk3MSwiaWF0IjoxNjYyNDM2OTcxLCJ qdGkiOiJpc3RuVDEifQ.HZkqW3C_fsB_VU3BdiXlurBiLyniLvnRVK7AtOyFvGNQNaf3Nb0mxFHX_jcaiVh2bDphKv9eqsNqgwpxCURVmSVByO6WIssKKortFxYcdHAzj6REK3C4CAY34 58qJq7pmMEEaxU2nFuk_obytRmDWpgAexdbhkCHBQ7qKfQn_0EoK0uGxOSbJv_Gp19kLsL8cPacAFm2RpUIaviwdD8z_8OmPjIBajdvQeQOuijHZTED73RSs3cemMF7bq1yZIVTXVwb_2oU7r1eFz5VwNSTxFpXDj8dGGxFTfbRuReyAR9H2IHIK5CBPHYDsqCRRNcBlcfogQdL4dKAt77vA"
    }
}
*/
@Keep
data class LoginTokenModel (
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
    val body: BodyResponse?
) : Serializable

@Keep
data class BodyResponse (
    @SerializedName("recQuestDto")
    val recomendQuest: ArrayList<RecommendItem>,

    @SerializedName("userInfo")
    val userInfo: UserInfo,

    @SerializedName("jwtToken")
    val token: String
) : Serializable
@Keep
data class RecommendItem (
    @SerializedName("quest")
    val quest: String = "",

    @SerializedName("intent")
    val intent: String = "",

    @SerializedName("ctgry")
    val category: String = ""
) : Serializable
@Keep
data class UserInfo (
    @SerializedName("email")
    val email: String = "",

    @SerializedName("employeeNumber")
    val employeeNumber: String = "",

    @SerializedName("firstName")
    val firstName: String = "",

    @SerializedName("lastName")
    val lastName: String = "",

    @SerializedName("departmentName")
    val departmentName: String = "",

    @SerializedName("teamName")
    val teamName: String = "",

    @SerializedName("rankName")
    val rankName: String = ""
) : Serializable


