package com.daquv.hub.data.request


import com.daquv.hub.presentation.util.network.TranJson


class REQ_SVC_ARGUMENT() : TranJson() {

    /** 통신 요청 구분 코드*/
    val TRAN_ID = "APP_SVC_P040"

    /** 약관목록조회 */
    /*''':전체, CERT : 휴대폰본인인증, SVC1 : 아바타, SVC2 : 경리나라정보동의*/
    val AGRM_GRP_CD = "AGRM_GRP_CD"

    //{JSONData={"CNTS_CRTS_KEY":"d394fae4-414c-77dd-1648-874550002060",
    // "DEVICE_INST_ID":"",
    // "TRAN_NO":"APP_SVC_P040",
    // "ENC_YN":"N",
    // "REQ_DATA":{"AGRM_GRP_CD":"SVC4"}}}

    init {
        id = TRAN_ID
        put(AGRM_GRP_CD, "SVC4")
    }

}