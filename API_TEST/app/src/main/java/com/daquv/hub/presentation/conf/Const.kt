package com.daquv.hub.presentation.conf

import android.Manifest
import android.os.Build

object Const {

    object Constants {
        const val TAG = "Vox DEBUG-----> "
    }

    /** 앱 필수 접근 권한 리스트
     * SDK 30 부터는 READ_PHONE_NUMBERS 만 사용
     * SDK 26~30 은 READ_PHONE_NUMBERS, READ_PHONE_STATE 을 사용
     * SDK 26 미만은 READ_PHONE_STATE 만 사용
     */
    fun APP_REQUIRED_PERMISSIONS(): Array<String> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        } else {
            return arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    /**
     * 인텐트 카테고리 정보
     * 매출:PL, 자금:FS, 개임개발비:GC, 손익:LC , 전화:PC
     */
    object Category {
        const val PL = "PL"
        const val FS = "FS"
        const val GC = "GC"
        const val LC = "LC"
        const val PC = "PC"
    }

    /**
     * NLU 인텐트 정보
     */
    object NLU_Intent {
        const val CAPTURE = "NNN015"
        const val CALL_PHONE = "NNN014"
        const val COUNT = "NNN017"
    }

    object Action {
        const val CAPTURE = 1001
    }

    /**
     * Preference 관련 상수 정의
     */
    object Preference {
        /** 로그인 패턴 데이터  */
        const val KEY_PREF_USER_PATTERN_DATA = "KEY_PREF_USER_PATTERN_DATA"

        /** 로그인 생체인증 사용 여부  */
        const val KEY_PREF_IS_USE_BIO_AUTH = "KEY_PREF_IS_USE_BIO_AUTH"

        /** 고객 CI  */
        const val KEY_PREF_CUST_CI = "KEY_PREF_CUST_CI"

        /** 패턴 여부  */
        const val KEY_PREF_PATTERN_SETTING_YN = "KEY_PREF_PATTERN_SETTING_YN"

        /** 로그인 여부 */
        const val KEY_PREF_LOGIN_YN = "KEY_PREF_LOGIN_YN"

        /** 로그인 간편번호 데이터  */
        const val KEY_PREF_USER_PASSWORD_DATA = "KEY_PREF_USER_PASSWORD_DATA"

        /** 로그인 간편번호 이전 데이터  */
        const val KEY_PREF_USER_PASSWORD_BEFORE_DATA = "KEY_PREF_USER_PASSWORD_BEFORE_DATA"

        /** 최초실행 여부  */
        const val KEY_PREF_IS_FIRST = "KEY_PREF_IS_FIRST"

        /** 코치마크 확인 여부  */
        const val KEY_PREF_COACH_MARK = "KEY_PREF_COACH_MARK"

        /** 음성 답변 on/off 여부  */
        const val KEY_PREF_VOICE_ANSWER = "KEY_PREF_VOICE_ANSWER_DATA"

        /** 음성 답변 Speaker/Voice 여부  */
        const val KEY_PREF_VOICE_SPEAKER = "KEY_PREF_VOICE_SPEAKER"

        /** 음성 받기 on/off 여부  */
        const val KEY_PREF_WEB_RESULT_TTS = "KEY_PREF_keyWebResultTTS"

        /** 개인정보 메시지 확인 여부  */
        const val KEY_PREF_PRIVACY = "KEY_PREF_PRIVACY"

        /** 조회성페이지 webview 호출 여부  */
        const val KEY_PREF_CHILDVIEW = "KEY_PREF_CHILDVIEW"

        /** 앱 화면 잠금 여부 */
        const val KEY_PREF_LOCK_APPLICATION = "KEY_PREF_LOCK_APPLICATION"

        /** STT URL 주소 */
        const val KEY_PREF_STT_URL = "KEY_PREF_STT_URL"

        /** User 정보 */
        const val KEY_USER_INFO = "KEY_USER_INFO"

        /** 추천 질의 리스트 */
        const val KEY_RECOMMEND_INQUIRE = "KEY_RECOMMEND_INQUIRE"

        /** 회사 리스트 */
        const val KEY_COMPANY_LIST = "KEY_COMPANY_LIST"

        /** 사용중인 회사 정보 */
        const val KEY_USING_COMPANY = "KEY_USING_COMPANY"

        /** 사용중인 서비스 */
        const val KEY_USING_SERVICE = "KEY_USING_SERVICE"

        /** User ID */
        const val KEY_USER_ID = "KEY_USER_ID"

        /** User PW */
        const val KEY_USER_PW = "KEY_USER_PW"

        /** User Company */
        const val KEY_USER_COM = "KEY_USER_COM"


    }
}