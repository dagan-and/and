package com.daquv.hub.presentation.util.network.constant;

/**
* 통신 처리 상수 정의
**/
public class ComTranKey {

    /**
    * 통신 요청 상수 정의
    **/
    public static class REQ {
        /*
        요청공통데이터
        1 	CNTS_CRTS_KEY		컨텐츠인증키	C	100 	●
        2 	TRAN_NO		API-ID	C	20 	●
        3 	DEVICE_INST_ID		디바이스설치ID	C	100 	●		공백일 경우 최초설치 또는 재설치
        4 	ENC_YN		암호화여부	C	1 	○
         */
        /** 통신 데이터 (JSON) Key - 통신 요청 전체 데이터 Key */
        public static final String KEY_JSON_DATA = "JSONData";
        /** 컨텐츠인증키 Key */
        public static final String CNTS_CRTS_KEY_CODE = "CNTS_CRTS_KEY";    //
        /** 통신 요청 구분 CODE (API-ID) Key */
        public static final String TRAN_NO = "TRAN_NO";    //
        /** 디바이스 설치 ID Key */
        public static final String DEVICE_INST_ID = "DEVICE_INST_ID"; // 디바이스 설치 ID
        /** 데이터 암호화 여부 Key */
        public static final String ENC_YN = "ENC_YN";
        /** 요청 데이터 Key */
        public static final String REQ_DATA = "REQ_DATA";

        /** App 통신 요청 인증키 */
        public static final String API_REQ_AUTH_KEY      = "d394fae4-414c-77dd-1648-874550002060";
    }

    /**
    * 통신 응답 상수 정의
    **/
    public static class RES {
        /*
        응답공통데이터
        1 	RSLT_CD		응답코드	C	15 		●
        2 	RSLT_MSG		응답메시지	C	300 		●
        3 	RSLT_PROC_GB		응답처리구분	C	1 		●	'1':콜센타
         */
        /** 응답 코드 Key */
        public static final String RSLT_CD = "RSLT_CD";
        /** 응답 메시지 Key */
        public static final String RSLT_MSG = "RSLT_MSG";
        /** 응답처리구분 Key */
        public static final String RSLT_PROC_GB = "RSLT_PROC_GB";
        /** 응답 데이터 Key */
        public static final String RESP_DATA = "RESP_DATA";

        /** MG 응답 데이터 Key */
        public static final String _tran_res_data = "_tran_res_data";
    }
}
