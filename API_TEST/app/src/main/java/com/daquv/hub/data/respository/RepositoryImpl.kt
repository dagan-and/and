package com.daquv.hub.data.respository

import com.daquv.hub.data.model.*
import com.daquv.hub.data.retrofit.RetrofitInstance
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.Response

//코루틴 singleton class->object로 변경
class RepositoryImpl : Repository {

    override fun getMgFileRepo(url: String): Call<String> {
        return RetrofitInstance.retrofitRx.getMGFile(url)
    }

    override fun getTTSRepo(keyword : String): Single<TTSTResponse> {
        return RetrofitInstance.retrofitRx.getTTS(keyword)
    }

    override fun getLoginTokenRepo(param: JsonObject): Single<Response<LoginTokenModel>> {
        return RetrofitInstance.retrofitRx.getLoginToken(param)
    }

    override fun getSttResultRepo(param: JsonObject): Single<STTResultModel> {
        return RetrofitInstance.retrofitRx.getSTTResult(param)
    }

    override fun getCallNumberRepo(utterance: String, entity : String, value : String): Single<CallModel> {
        return RetrofitInstance.retrofitRx.getCallNumber(utterance, entity, value)
    }

    override fun getRSAKeyRepo(): Single<RsaKeyModel> {
        return RetrofitInstance.retrofitRx.getRSAPubKey()
    }

    override fun getArgument(url : String , param: JsonObject): Single<ArgumentModel> {
        return RetrofitInstance.retrofitRx.getArgument(url ,param)
    }

    override fun getRequestCode(url : String , param: JsonObject): Single<RequestCodeModel> {
        return RetrofitInstance.retrofitRx.getRequestCode(url ,param)
    }

    override fun getVerifyCode(url : String , param: JsonObject): Single<VerifyCodeModel> {
        return RetrofitInstance.retrofitRx.getVerifyCode(url ,param)
    }

    override fun getSTT(url: String, param: JsonObject): Single<STTDataModel> {
        return RetrofitInstance.retrofitRx.getSTT(url ,param)
    }

}
/*
    //통신 call 구현
    override fun getTalkList(page: String) : Response<talkListResponse>{
        //var call : Call<talkListResponse?>? = RetrofitInstance.api.getPosts("")
        //call.enqueue(new Callback<talkListResponse>)
        var model : talkListResponse
        RetrofitInstance.api.getTalkList(page, "")
            .enqueue(object: Callback<talkListResponse>{
                override fun onFailure(call: Call<talkListResponse>, t: Throwable) {
                    //todo 실패처리
                    Log.d("Retrofit error1=",t.toString())
                }

                override fun onResponse(call: Call<talkListResponse>, response: Response<talkListResponse>) {
                    //todo 성공처리
                    //Log.d("Retrofit BODY",response.body().toString())

                    if(response.isSuccessful){
                        response.body()?.let{
                            model = response.body()!!
                            //Log.d("GGGGGGGG=",model.toString())

                        }
                    }else{
                        Log.d("Retrofit error2=","request failure: ${response.message()}")                    }

                }
            })

            return model
    }

    //통신 respose 구형

*/
