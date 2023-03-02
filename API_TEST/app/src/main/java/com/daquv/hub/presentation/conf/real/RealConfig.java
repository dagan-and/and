package com.daquv.hub.presentation.conf.real;

/**
* 운영 환경설정
**/
public class RealConfig {

    /** Site URL (도메인 URL) */
    public static final String SITE_URL                     = "http://3.36.201.40:8080";
    /** RES_MG 요청 URL */
    public static final String SITE_MG_REQ_URL              = "https://mg-dev.bizplay.co.kr/MgGate?master_id=";
    /** 서비스 통신 요청 URL */
    public static final String SITE_API_REQ_URL             = SITE_URL + "/api/v1/askavata/convertToBinaryString";
}
