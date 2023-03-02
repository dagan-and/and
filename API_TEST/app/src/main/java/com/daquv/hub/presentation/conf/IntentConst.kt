package com.daquv.hub.presentation.conf

import android.app.Activity
import com.daquv.api.test.BuildConfig


class IntentConst {

    /**
     * 앱 접근권한 (Permission) 요청 Code 정의
     * - 동일한 request id가 발생하지 않도록 정의해서 사용
     */
    object PermissionRequestCode {
        /** 앱 필수 접근권한 요청 코드  */
        const val REQ_PERMISSIONS_APP_REQUIRED = 0x1000

        /** 음성인식 접근권한 요청 코드 (오디오 / 마이크 권한)  */
        const val REQ_PERMISSIONS_RECOG_VOICE = 0x1001

        /** 저장공간 접근권한 요청 코드 (저장공간 읽기 / 쓰기 권한)  */
        const val REQ_PERMISSIONS_STORAGE = 0x1002

        /** 연락처 접근권한 요청 코드  */
        const val REQ_PERMISSIONS_CONTRACTS = 0x1003

        /** 전화걸기 접근권한 요청 코드  */
        const val REQ_PERMISSIONS_CALL_PHONE = 0x1004

        /** 문자보내기 접근권한 요청 코드  */
        const val REQ_PERMISSIONS_SEND_SMS = 0x1005

        /** 웹 접근권한 요청 코드  */
        const val REQ_PERMISSIONS_WEB_CALL_PHONE = 0x1006

        /** 전화 접근권한 요청 코드  */
        const val REQ_READ_PHONE_STATE = 0x1007

        /** 전화 접근권한 요청 코드  */
        const val REQ_READ_PHONE_NUMBERS = 0x1008

        /** 마이크 접근권한 재시도 요청 코드  */
        const val REQ_PERMISSIONS_REREC_VOICE = 0x1009

        /** 전화 접근권한 재시도 요청 코드  */
        const val REQ_READ_PHONE_RESTATE = 0x1010
    }

    /**
     * Activity Request Code
     * - 동일한 request id가 생기지 않도록 정의해서 사용 <br></br>
     */
    object ActRequestCode {
        /** Intro 화면 요청 코드  */
        const val REQ_ACT_INTRO = 0x2000

        /** 회원가입 화면 요청 코드  */
        const val REQ_ACT_JOIN = 0x2001

        /** 약관동의 화면 요청 코드  */
        const val REQ_ACT_TERM = 0x2002

        /** 웹뷰 화면 요청 코드  */
        const val REQ_ACT_WEB = 0x2005

        /** 음성 인식 화면 요청 코드  */
        const val REQ_ACT_VOCIE_RECOG = 0x2006

        /** 공동인증서 리스트 요청 코드  */
        const val REQ_ACT_CERT_LIST = 0x2010

        /** 공동인증서 비밀번호 입력 화면 요청 코드  */
        const val REQ_ACT_CERT_PWD_INPUT = 0x2011

        /** 공동인증서 가져오기 화면 요청 코드  */
        const val REQ_ACT_CERT_IMPORT = 0x2012

        /** 비밀번호 입력 (보안키패드) 화면 요청 코드  */
        const val REQ_ACT_PWD_INPUT = 0x2013

        /** Call Activity Info 요청 코드 */
        const val REQ_PERMISSIONS_RESULT = 0x2007

        /** permission 화면 요청 코드  */
        const val REQ_ACT_PERMISSION = 0x2008

        /** 패턴 검증 화면 (1회 입력) 요청 코드  */
        const val REQ_ACT_PATTERN_AUTH = 0x2020

        /** 패턴 설정 화면 (2회 입력) 요청 코드  */
        const val REQ_ACT_PATTERN_SETTING = 0x2021

        /** 생체 인증 설정 화면 요청 코드  */
        const val REQ_ACT_BIO_SETTING = 0x2022

        /** 스크래핑 인증서 입력 화면 요청 코드  */
        const val REQ_ACT_CERT_LOGIN_SCRAP = 0x2030

        /** 계좌조회 스크래핑 화면 요청 코드  */
        const val REQ_ACT_ACCT_SCRAP = 0x2031

        /** 계좌조회 스크래핑 결과 화면 요청 코드  */
        const val REQ_ACT_RSLT_ACCT_SCRAP = 0x2032

        /** 추가인증 - 계좌 비밀번호 입력 화면 요청 코드  */
        const val REQ_ACT_ADD_INFO_ACCT_PWD_INPUT = 0x2033

        /** 추가인증 - 계좌조회 ID 로그인 화면 요청 코드  */
        const val REQ_ACT_ADD_INFO_ACCT_ID_LOGIN_INPUT = 0x2034

        /** 간편비밀번호 설정 화면 요청 코드  */
        const val REQ_ACT_SPWD_SETTING = 0x2035

        /** 간편비밀번호 로그인 요청 코드  */
        const val REQ_ACT_SPWD_LOGIN = 0x2036

        /** 직원 번호 인증 코드  */
        const val REQ_ACT_MEMBER_AUTH = 0x2037

        /** bio 로그인 요청 코드  */
        const val REQ_ACT_BIO_LOGIN = 0x2038

        /** 전화걸기  */
        const val REQ_ACT_PHONE_CALL = 0x2040
    }

    /**
     * Activity Result Code
     * <br></br><br></br>
     * - 동일한 Result Code 가 생기지 않도록 정의해서 사용
     */
    object ActResultCode {
        /** 성공  */
        const val RES_ACT_OK = Activity.RESULT_OK

        /** 실패 또는 취소  */
        const val RES_ACT_CANCELED = Activity.RESULT_CANCELED
    }

    /**
     * Extra 관련 상수
     */
    object Extras {
        /** WebView URL > WebView.loadUrl() Extra Key  */
        const val EXTRA_WEB_DOMAIN_URL = "EXTRA_WEB_DOMAIN_URL"

        /** WebView URL > WebView.loadUrl() Extra Key  */
        const val EXTRA_WEB_VIEW_URL = "EXTRA_WEB_VIEW_URL"

        /** WebView DATA > WebView.loadData() Extra Key  */
        const val EXTRA_WEB_VIEW_DATA = "EXTRA_WEB_VIEW_DATA"

        /** 웹뷰 화면 확대 가능 여부  */
        const val EXTRA_WEB_ZOOM_YN = "EXTRA_WEB_ZOOM_YN"

        /** WebView Post 방식 Load Parameter > WebView.postUrl (url, param) Extra Key  */
        const val EXTRA_WEB_POST_PARAM = "EXTRA_WEB_POST_PARAM"

        /** WebView Callback 함수명 Extra Key  */
        const val EXTRA_WEB_CALLBACK = "EXTRA_WEB_CALLBACK"

        /** WebView Callback Data Extra Key  */
        const val EXTRA_WEB_CALLBACK_DATA = "EXTRA_WEB_CALLBACK_DATA"

        /** WebView WebAction 정보 Extra Key  */
        const val EXTRA_WEB_ACTION_INFO = "EXTRA_WEB_ACTION_INFO"

        /** Content Title Extra Key  */
        const val EXTRA_CONTENT_TITLE = "EXTRA_CONTENT_TITLE"

        /** Content Title TYPE Code Extra Key  */
        const val EXTRA_CONTENT_TITLE_TYPE = "EXTRA_CONTENT_TITLE_TYPE"

        /** 공동인증서 정보 Extra Key  */
        const val EXTRA_CERT_INFO = "EXTRA_CERT_INFO"

        /** 교체 후 reload  */
        const val EXTRA_CERT_CHANGE_RELOAD = "EXTRA_CERT_CHANGE_RELOAD"

        /** 보안키패드 생성 Intent Data Extra Key  */
        const val EXTRA_TRANSKEY_DATA = "EXTRA_TRANSKEY_DATA"

        /** 보안키패드 입력 결과 Intent Data Extra Key  */
        const val EXTRA_TRANSKEY_RSLT_DATA = "EXTRA_TRANSKEY_RSLT_DATA"

        /** 스크래핑 은행정보 Extra Key  */
        const val EXTRA_SCRAP_BANK_INFO = "EXTRA_SCRAP_BANK_INFO"

        /** 스크래핑 은행정보 리스트 Extra Key  */
        const val EXTRA_SCRAP_BANK_LIST = "EXTRA_SCRAP_BANK_LIST"

        /** 스크래핑 계좌정보 Extra Key  */
        const val EXTRA_SCRAP_ACCT_INFO = "EXTRA_SCRAP_ACCT_INFO"

        /** 스크래핑 결과 데이터 Extra Key  */
        const val EXTRA_SCRAP_RSLT_DATA = "EXTRA_SCRAP_RSLT_DATA"

        /** 스크래핑 결과 - 추가인증이 필요한 결과 데이터 Extra Key  */
        const val EXTRA_SCRAP_RSLT_NEED_ADD_INFO_DATA = "EXTRA_SCRAP_RSLT_NEED_ADD_INFO_DATA"

        /** 스크래핑 결과 데이터 Extra Key  */
        const val EXTRA_SCRAP_DEPOSIT_RSLT_DATA = "EXTRA_SCRAP_DEPOSIT_RSLT_DATA"

        /** 스크래핑 결과 - 추가인증이 필요한 결과 데이터 Extra Key  */
        const val EXTRA_SCRAP_RSLT_NEED_ADD_DEPOSIT_INFO_DATA =
            "EXTRA_SCRAP_RSLT_NEED_ADD_DEPOSIT_INFO_DATA"

        /** 스크래핑 결과 데이터 Extra Key  */
        const val EXTRA_SCRAP_LOAN_RSLT_DATA = "EXTRA_SCRAP_LOAN_RSLT_DATA"

        /** 스크래핑 결과 - 추가인증이 필요한 결과 데이터 Extra Key  */
        const val EXTRA_SCRAP_RSLT_NEED_ADD_LOAN_INFO_DATA =
            "EXTRA_SCRAP_RSLT_NEED_ADD_LOAN_INFO_DATA"

        /** Push 수신 정보  */
        const val EXTRA_PUSH_INFO = "EXTRA_PUSH_INFO"

        /** 음성인식 텍스트 Extra Key  */
        const val EXTRA_VOICE_RECOG_TXT = "EXTRA_VOICE_RECOG_TXT"

        /** 음성인식 데이터 Extra Key  */
        const val EXTRA_VOICE_RECOG_DATA = "EXTRA_VOICE_RECOG_DATA"

        /** 음성인식 인간 데이터 Extra Key  */
        const val EXTRA_VOICE_RECOG_DATA_PERSON = "EXTRA_VOICE_RECOG_DATA_PERSON"

        /** 음성인식 문자 데이터 Extra Key  */
        const val EXTRA_VOICE_RECOG_DATA_MESSAGE = "EXTRA_VOICE_RECOG_DATA_MESSAGE"

        /** 캡쳐 콜백 Extra Key  */
        const val EXTRA_CAPTURE_CALLBACK = "EXTRA_CAPTURE_CALLBACK"

        /** 캡쳐 패스 Extra Key  */
        const val EXTRA_CAPTURE_PATH = "EXTRA_CAPTURE_PATH"

        /** 약관타입 Extra Key  */
        const val EXTRA_TERM_TYPE = "EXTRA_TERM_TYPE"

        /** 약관인증 Extra Key  */
        const val EXTRA_TERM_CONDITION_RESULT = "EXTRA_TERM_CONDITION_RESULT"
    }

    /**
     * Intent Action 정의
     */
    object Action {
        /** 메인 Refresh Action  */
        const val ACTION_REFRESH_MAIN = "com.daquv.voxai.action_refresh_main"

        /** 로그인 완료 Action  */
        const val ACTION_CMPL_LOGIN = "com.daquv.voxai.action_cmpl_login"

        /** Push 수신 Action 추가  */
        val ACTION_RECEIVE_PUSH: String =
            BuildConfig.APPLICATION_ID.toString() + "." + "ACTION_RECEIVE_PUSH"

        /** 자동로그아웃 Action  */
        const val ACTION_AUTO_LOGOUT = "com.daquv.voxai.action_auto_logout"

        /** 로그아웃 Action  */
        const val ACTION_LOGOUT = "com.daquv.voxai.action_logout"

        /** 로그인 Action  */
        const val ACTION_LOGIN = "com.daquv.voxai.action_login"

        /** 웹뷰 요청 Action  */
        const val ACTION_WEB_VIEW = "com.daquv.voxai.action_WEB_VIEW"
    }
}