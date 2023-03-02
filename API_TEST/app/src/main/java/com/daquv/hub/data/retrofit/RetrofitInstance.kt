package com.daquv.hub.data.retrofit

import android.util.Log
import com.daquv.hub.presentation.conf.AppConfig
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.net.CookieHandler
import java.net.CookieManager
import java.util.concurrent.TimeUnit


object RetrofitInstance {

    private val cookieHandler: CookieHandler = CookieManager()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(AppConfig.SITE_URL)
            .client(provideOkHttpClient(AppInterceptor()))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api : RetrofitApi by lazy {
        retrofit.create(RetrofitApi::class.java)
    }

    val retrofitRx: RetrofitApi by lazy {
        Retrofit.Builder()
            .baseUrl(AppConfig.SITE_URL)
            //.client(OkHttpClient())
            .client(provideOkHttpClient(AppInterceptor()))  //헤더에 공통 토큰 사용
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitApi::class.java)
    }

    private fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient
            = OkHttpClient.Builder().run {
        addInterceptor(interceptor)
        if(!AppConfig.IS_APP_REALEASE) {
            addInterceptor(httpLoggingInterceptor())
        }
        addInterceptor(BasicAuthInterceptor("daquv","DAQUV@42maru"))
        //API Session 유지를 위한 쿠키 설정
//        cookieJar(JavaNetCookieJar(cookieHandler))

        connectTimeout(15, TimeUnit.SECONDS)
        readTimeout(15, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
        build()
    }



    private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.i("Retrofit" , message)
            }

        })
        return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain) : Response = with(chain) {
            val builder = request().newBuilder()
            proceed(builder.build())
        }
    }

    class BasicAuthInterceptor(user: String?, password: String?) : Interceptor {

        private var credentials: String? = null

        init {
            credentials = Credentials.basic(user!!, password!!)
        }

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain) : Response = with(chain) {
            val request: Request = chain.request()
            val authenticatedRequest: Request = request.newBuilder()
                .header("Authorization", credentials!!).build()
            return chain.proceed(authenticatedRequest)
        }
    }

}