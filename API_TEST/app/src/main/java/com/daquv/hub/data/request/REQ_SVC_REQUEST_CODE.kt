package com.daquv.hub.data.request


import com.daquv.hub.presentation.util.network.TranJson


class REQ_SVC_REQUEST_CODE(
    appId : String,
    cellPhoneNo: String,
    birthday: String,
    ecnmc: String,
    name: String,
    cellPhoneVendor: String,
    frnr : String
) : TranJson() {

    //{"IN_FRNR_DV_CD":"1","APP_ID":"IBKCRM","CLPH_NO":"01075761690","TEL_CMM_CD":"01","BRDT":"198921071","ECNMC_TEL_CMM_YN":"Y","NAME":"김용식"}
    //{"RSLT_CD":"B100","TRAN_NO":"APP_SVC_P041","RESP_DATA":{"RSLT_CD":"B100","TRSC_UNQ_NO":"20230113142704948","RSLT_MSG":"입력하신 정보가 일치하지 않습니다."},"RSLT_MSG":"입력하신 정보가 일치하지 않습니다.","RESP_TIME":"142705","RESP_DATE":"20230113"}
    //{"RSLT_CD":"0000","TRAN_NO":"APP_SVC_P041","RESP_DATA":{"RSLT_CD":"0000","TRSC_UNQ_NO":"20230113142916558","RSLT_MSG":"정상처리되었습니다."},"RSLT_MSG":"정상처리되었습니다.","RESP_TIME":"142916","RESP_DATE":"20230113"}


    /** 통신 요청 구분 코드*/
    val TRAN_ID = "APP_SVC_P041"

    /*
        휴대폰번호	C	11	●
        생년월일	C	9	●		9번째 자리(1,3 :남자, 2,4:여자)
        알뜰폰여부	C	1	●		Y: 알뜰폰, N: 일반
        이름	C	30	●
        통신사코드	C	2	●		01: SKT, 02: KT, 03: LG U+
        내외국인구분코드	C	1	●		1: 내국인, 2: 외국인
*/
    val CLPH_NO = "CLPH_NO"
    val BRDT = "BRDT"
    val ECNMC_TEL_CMM_YN = "ECNMC_TEL_CMM_YN"
    val NAME = "NAME"
    val TEL_CMM_CD = "TEL_CMM_CD"
    val IN_FRNR_DV_CD = "IN_FRNR_DV_CD"
    val APP_ID = "APP_ID"

    init {
        id = TRAN_ID
        put(CLPH_NO, cellPhoneNo);
        put(BRDT, birthday);
        put(ECNMC_TEL_CMM_YN, ecnmc);
        put(NAME, name);
        put(TEL_CMM_CD, cellPhoneVendor);
        put(IN_FRNR_DV_CD, frnr);
        put(APP_ID, appId);
    }

}