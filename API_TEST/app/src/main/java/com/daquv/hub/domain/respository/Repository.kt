package com.daquv.hub.data.respository

import com.daquv.hub.data.model.*
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.Response

interface Repository {

    //Call --> fun getTalkList(page : Int) : Call<TalkListResponse>
    //Response --> suspend fun getFriendsList(page : Int) : Response<FriendsResponse>
    //Single --> fun getFriendsListRx(page : Int) : Single<FriendsResponse>

    /**
     *  MG 파일 요청
     */
    fun getMgFileRepo(url: String) : Call<String>

    /**
     *  TTS 음성 파일 요청
     */
    fun getTTSRepo(keyword : String) : Single<TTSTResponse>

    /**
     *  로그인 토큰 요청
     */
    fun getLoginTokenRepo(param: JsonObject) : Single<Response<LoginTokenModel>>

    /**
     *  STT Word 전송
     */
    fun getSttResultRepo(param: JsonObject) : Single<STTResultModel>

    /**
     *  전화걸기 모델 요청
     */
    fun getCallNumberRepo(utterance: String, entity : String, value : String) : Single<CallModel>

    /**
     *  RSA 공개키 요청
     */
    fun getRSAKeyRepo() : Single<RsaKeyModel>

    /**
     *  약관 목록 조회
     */
    fun getArgument(url : String , param: JsonObject) : Single<ArgumentModel>

    /**
     *  인증번호 요청
     */
    fun getRequestCode(url : String , param: JsonObject) : Single<RequestCodeModel>

    /**
     *  인증번호 검증
     */
    fun getVerifyCode(url : String , param: JsonObject) : Single<VerifyCodeModel>


    /**
     *  42마루 STT 서비스
     */
    fun getSTT(url : String , param: JsonObject) : Single<STTDataModel>
}