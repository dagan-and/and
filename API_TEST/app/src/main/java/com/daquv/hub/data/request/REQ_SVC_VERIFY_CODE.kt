package com.daquv.hub.data.request


import com.daquv.hub.presentation.util.network.TranJson


class REQ_SVC_VERIFY_CODE(
    appId: String,
    clphNo: String,
    trscUnqNo: String,
    smsCertNo: String,
) : TranJson() {

    //{"SMS_CERT_NO":"073869","APP_ID":"IBKCRM","CLPH_NO":"01075761690","TRSC_UNQ_NO":"20230113163906632"}
    //2023-01-13 14:29:46.626 16018-16018/com.daquv.ibkcrm I/WEBCASH_INFO: object :: {"RSLT_CD":"B131","TRAN_NO":"APP_SVC_C028","RESP_DATA":{"RSLT_CD":"B131","CUST_CI":"","RSLT_MSG":"인증번호가 불일치합니다."},"RSLT_MSG":"인증번호가 불일치합니다.","RESP_TIME":"142947","RESP_DATE":"20230113"}
    //2023-01-13 14:30:47.096 16018-16018/com.daquv.ibkcrm I/WEBCASH_INFO: object :: {"RSLT_CD":"9999","TRAN_NO":"APP_SVC_C028","RESP_DATA":{"RSLT_CD":"9999","CUST_CI":"TRfIW986bUbDreeGxDL3Vuec%2FOTnO4ns8VuX5dgTxATKk%2FziqD1wncNL%2BJ7x760rA%2BymWQLwKj%2BtuPS8flv8Ug%3D%3D","RSLT_MSG":"가입된 휴대폰번호입니다."},"RSLT_MSG":"가입된 휴대폰번호입니다.","RESP_TIME":"143047","RESP_DATE":"20230113"}


    /** 통신 요청 구분 코드*/
    val TRAN_ID = "APP_SVC_C028"

    /*
        CLPH_NO		휴대폰번호
        TRSC_UNQ_NO		거래고유번호
        SMS_CERT_NO		SMS인증번호
        USER_INFO		연계사용자정보		C	500	○
        APP_ID		앱ID
     */
    val CLPH_NO = "CLPH_NO"
    val TRSC_UNQ_NO = "TRSC_UNQ_NO"
    val SMS_CERT_NO = "SMS_CERT_NO"
    val USER_INFO = "USER_INFO"
    val APP_ID = "APP_ID"

    init {
        id = TRAN_ID
        put(CLPH_NO, clphNo)
        put(TRSC_UNQ_NO, trscUnqNo)
        put(SMS_CERT_NO, smsCertNo)
        put(APP_ID, appId)
    }

}