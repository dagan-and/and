package com.daquv.hub.data.retrofit

import com.daquv.hub.data.model.*
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface RetrofitApi {

    /**
     * TTS Response
     * 암호화 N
     **/
    @GET("exapi/clova/callTtsBinaryString")
    fun getTTS(
        @Query("text") keyword: String
    ): Single<TTSTResponse>

    @POST("/auth/istn/signin")
    fun getLoginToken(
        @Body param: JsonObject
    ): Single<Response<LoginTokenModel>>

    @POST("exapi/nlu/utterance")
    fun getSTTResult(
        @Body param: JsonObject
    ): Single<STTResultModel>

    @Multipart
    @POST("/webview/sg/v1/fnnr/rest/empPhoneConn")
    fun getCallNumber(
        @Part("utterance") utterance: String,
        @Part("entities[0].entity") entity: String,
        @Part("entities[0].value") value: String,
    ): Single<CallModel>

    @GET
    fun getMGFile(@Url url: String): Call<String>

    @GET("/auth/issue")
    fun getRSAPubKey(): Single<RsaKeyModel>

    @POST
    fun getArgument(
        @Url url: String,
        @Body param: JsonObject
    ): Single<ArgumentModel>

    @POST
    fun getRequestCode(
        @Url url: String,
        @Body param: JsonObject
    ): Single<RequestCodeModel>

    @POST
    fun getVerifyCode(
        @Url url: String,
        @Body param: JsonObject
    ): Single<VerifyCodeModel>

    @POST
    fun getSTT(
        @Url url: String,
        @Body param: JsonObject
    ): Single<STTDataModel>

/*



    @FormUrlEncoded
    @POST("istn/api/v1/signin")
    fun getLoginToken(
        @Field("param") userId: String?,
        @Field("body") body: String?
    ): Single<LoginTokenModel>

    @Headers(
        "X-NCP-APIGW-API-KEY-ID: (발급 받은 Client ID)",
        "X-NCP-APIGW-API-KEY: (발급 받은 Secret Key)"
    )

    @Header("X-Naver-Client-Id") String id,
    @Header("X-Naver-Client-Secret") String pw,
    @Path("type") String type,
    @Query("p") page: Int,

    @FormUrlEncoded
    @POST("/api/getdata")
    fun postData(@FieldMap param: HashMap<String?, Any?>?): Call<TTSTResponse?>?

    HashMap<String, Object> param = new HashMap<String, Object>();
    param.put("authKey", "12345");
    param.put("id", "1");
*/

}