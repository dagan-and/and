package com.daquv.hub.data.request

import com.daquv.hub.presentation.util.network.TranJson

class REQ_SVC_LOGIN(email : String, password: String, companyId: String) : TranJson() {

    private val strFieldName1 : String = "email"
    private val strFieldName2 : String = "password"
    private val strFieldName3 : String = "companyId"

    /** 통신 요청 구분 코드  */
    val TRAN_ID = "APP_SVC_LOGIN"

    /*
    method : POST
    url : http://3.36.201.40:8080/istn/api/v1/signin
    body :
    {
        "email" :"test@istn.co.kr",
        "password" : "12345",
        "companyId": "istnT1"
    }
     */

    init {
        TranJson(TRAN_ID)
        put(strFieldName1, email)
        put(strFieldName2, password)
        put(strFieldName3, companyId)
    }

}

