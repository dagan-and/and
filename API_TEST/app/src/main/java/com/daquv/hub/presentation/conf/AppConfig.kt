package com.daquv.hub.presentation.conf

import android.content.Context
import android.widget.Toast
import com.daquv.api.test.BuildConfig
import com.daquv.hub.presentation.util.SharedPref
import com.daquv.hub.presentation.util.Logger

import com.daquv.hub.data.model.MGData
import com.daquv.hub.presentation.conf.real.RealConfig
import com.daquv.hub.presentation.conf.test.TestConfig
import com.daquv.hub.presentation.util.network.NetworkUtils

object AppConfig {

    /** 개발 / 운영 (배포) 앱 여부 (true : 운영 배포용 / false : 개발용)  */
    var IS_APP_REALEASE: Boolean = !BuildConfig.DEBUG

    /** 운영 환경설정 여부 (true : 운영 환경설정 / false : 개발 환경설정)  */
    var IS_REAL_CONFIG: Boolean = !BuildConfig.DEBUG

    /** RES_MG (Mobile Gate) 요청 마스터 ID  */ //public String MG_REQ_MASTER_ID          = "A_AVATAR_G_1" + "_v" + BuildConfig.VERSION_NAME.replace(".", ""); // BuildConfig.VERSION_NAME.replace(".", "-");
    var MG_REQ_MASTER_ID = "A_VOX_G_1" + "_v" + BuildConfig.VERSION_NAME.replace(
        ".",
        "_"
    ) // BuildConfig.VERSION_NAME.replace(".", "-");

    /** 자동로그아웃 시간 (default 10분)  */
    val AUTO_LOGOUT_TIME by lazy {60 * 1000 * 10}

    /** 통신 타임아웃 (default 3분)  */
    val NETWORK_TIMEOUT by lazy {60 * 1000 * 3}

    /** Site URL (도메인 URL)  */
    var SITE_URL: String = "http://api-stt-daquv.42maru.com/"

    /** Site URL (에스크아바타) */
    val ASK_AVATAR_URL : String = "https://app.askavatar.ai/GateWay"

    /** MG 요청 URL  */
    var SITE_MG_REQ_URL: String? = null

    /** 서비스 통신 요청 URL  */
    var SITE_API_REQ_URL: String? = null

    /** 서비스 통신 요청 URL  */
    var CRYPTO_KEY = "AVATARMOBILE"

    /**
     * 커스텀 UserAgent 초기값
     */
    var COMM_CUSTOM_USER_AGENT: String? = null

    /** API 요청 토큰 값  */
    var API_TOKEN_VALUE : String? = null

    /**
     * 구글 모델 적용 여부
     */
    var STTModeActive : Boolean = true

    /**
     *  OKhttp 결과값 HTML(임시 데이터)
     */
    var htmlData : String = ""

    /**
     *  STT 모델 Url
     */
    var STTModelUrl : String? = ""


    /**
     * 앱 설정 초기화
     * <br></br><br></br>
     * - 개발용 앱의 경우 서버 선택 팝업 및 개발용 앱 Toast 알림 표시 <br></br>
     * @param ctx Context
     * @param callback 앱 설정완료 Callback Listener
     */
    fun initialize(ctx: Context, callback: OnAppConfigListener?) {
        if (IS_APP_REALEASE) {
            // 운영 배포용 앱 설정
            Logger.setEnable(false)
            SharedPref.initialize(ctx)
            initialize(ctx, true, callback)
        } else {
            Logger.setEnable(true)
            Logger.setPrintPath(false)
            Logger.setLevel(
                Logger.LOG_LEVEL.DEV,
                Logger.LOG_LEVEL.INFO,
                Logger.LOG_LEVEL.WARN,
                Logger.LOG_LEVEL.ERROR
            )
            SharedPref.initialize(ctx)
            initialize(ctx, false, callback)
            Toast.makeText(ctx, "개발용 앱을 실행합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 앱 설정 초기화
     * @param ctx Context
     * @param isRealConf 운영 / 개발 환경 여부 (true : 운영 / false : 개발)
     * @param callback 앱 설정완료 Callback Listener
     */
    private fun initialize(ctx: Context, isRealConf: Boolean, callback: OnAppConfigListener?) {
        IS_REAL_CONFIG = isRealConf
        if (isRealConf) {
            // 운영 환경 설정
            SITE_URL = RealConfig.SITE_URL
            SITE_MG_REQ_URL = RealConfig.SITE_MG_REQ_URL.toString() + MG_REQ_MASTER_ID
            SITE_API_REQ_URL = RealConfig.SITE_API_REQ_URL
        } else {
            // 개발 환경 설정
            SITE_URL = TestConfig.SITE_URL
            SITE_API_REQ_URL = TestConfig.SITE_API_REQ_URL
            SITE_MG_REQ_URL = TestConfig.SITE_MG_REQ_URL.toString() + MG_REQ_MASTER_ID
        }

        if (callback != null) callback.onCompleteInitialize(true)
    }


    /**
     * MG 응답 데이터 초기화
     * @param context context
     * @param mgData MG 응답 데이터
     */
    fun initializeMG(context: Context?, mgData: MGData) {
        // 서버에서 받은 RES_MG 데이터로 도메인 URL 설정
        //SITE_URL = mgData.getString(RES_MG.c_site_url)

        // 웹뷰 쿠키 Clear
        NetworkUtils.clearCookie(context, SITE_URL)

        //LOGIN TOKEN Clear
        API_TOKEN_VALUE = null

        //STT 모델 URL 설정
        STTModelUrl = mgData.google_hint_url
    }

    /**
     * AppConfig 메모리 정리 여부 체크
     * SITE_URL : Retrofit 에서 사용하는 API URL
     * API_TOKEN_VALUE : API 헤더 토큰
     * UserInfo : User 정보
     * RecommendQuest : 추천 질의 리스트
     */
    fun isClearedData(checkLoinValue : Boolean) : Boolean{
        //로그인 이후 페이지에서 체크시 로그인에서 받아오는 값까지 체크
        if(checkLoinValue) {
            if (SITE_URL == null || API_TOKEN_VALUE == null) {
                return true
            }
        } else {
        //로그인 페이지에서는 API URL 정보만 체크
            if (SITE_URL == null) {
                return true
            }
        }
        return false
    }



}