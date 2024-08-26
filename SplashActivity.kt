package com.daquv.ioneask

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.JavaNetCookieJar
import okhttp3.Response
import org.json.JSONObject
import java.net.CookieManager
import java.util.concurrent.Callable


class SplashActivity : AppCompatActivity() {

    val urlScheme = "ibkmportal://ibk?p=ava"
    val mportalPage = "http://ibk.kr/mp"
    var imageSize : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splash = installSplashScreen()
        setContentView(R.layout.activity_splash)

        splash.setOnExitAnimationListener(object : SplashScreen.OnExitAnimationListener {
            override fun onSplashScreenExit(splashScreenViewProvider: SplashScreenViewProvider) {
                Log.d("DAQUV_INFO","onSplashScreenExit")
                try {
                    imageSize = splashScreenViewProvider.iconView.width
                } catch (e : Exception) {
                    Log.e("DAQUV_ERR",e.message.toString())
                }
            }
        })

        val commonDisposable = CompositeDisposable()
        val callable = Callable<Response> {
            //val url = "https://nsemp.ibk.co.kr/ava/exapi/ibkCrm/v1/auth/getAppInfo"
            val url = "http://203.235.68.48:5102/ava/exapi/ibkCrm/v1/auth/getAppInfo"

            val jsonData = "{ \"os_type\":\"AOS\" }"
            val client: HttpClient = HttpClient.Builder()
                .setUrl(url)
                .setTimeout(5)
                .setCookie(JavaNetCookieJar(CookieManager()))
                .setJsonData(jsonData)
                .isPost(true)
                .builder()
            client.call.execute()
        }

        commonDisposable.add(Observable.fromCallable(callable).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response -> //Response
                if (response.isSuccessful) {
                    val result = response.body!!.string()
                    Log.d("DAQUV_INFO", result)

                    val jsonObject = JSONObject(result)
                    if (!jsonObject.has("body")) {
                        init()
                        return@subscribe
                    }
                    val bodyObjects = jsonObject.getJSONObject("body")
                    if (!bodyObjects.has("version") || !bodyObjects.has("show_yn")) {
                        init()
                        return@subscribe
                    }

                    val current = BuildConfig.VERSION_NAME.replace(".", "")
                    val version = bodyObjects.optString("version").replace(".", "")
                    val use = bodyObjects.optString("show_yn")
                    if (use.equals("Y", true) &&
                        version.toFloat() > current.toFloat()
                    ) {
                        //엠포탈 다운로드 페이지 URL 로 이동
                        val builder = AlertDialog.Builder(this)
                        builder.setMessage("새로운 버전이 출시되었습니다.\n업데이트 후 이용해 주시기 바랍니다.")
                        builder.setPositiveButton("확인", null)
                        builder.setOnDismissListener {
                            Log.d("DAQUV_INFO","InstallActivity")
                            val intent = Intent(this@SplashActivity, InstallActivity::class.java)
                            if(imageSize != 0) {
                                intent.putExtra("imageSize", imageSize)
                            }
                            startActivity(intent)
                            overridePendingTransition(0, 0)
                            finish()
                        }
                        builder.show()
                    } else {
                        init()
                    }
                }
            }) { throwable ->
                init()
                Log.e("DAQUV_ERR", throwable.message!!)
            })
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    private fun init() {
        try {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.data = Uri.parse(urlScheme)
            startActivity(intent)
            finishAffinity()
        } catch (e: RuntimeException) {
//            //엠포탈 다운로드 페이지 URL 로 이동
//            val builder = AlertDialog.Builder(this)
//            builder.setCancelable(false)
//            builder.setMessage("M포탈 앱이 설치되어있지 않습니다.\nM포탈 다운로드 사이트로 이동됩니다.")
//            builder.setPositiveButton("확인", null)
//            builder.setOnDismissListener {
//                val intent = Intent()
//                intent.action = Intent.ACTION_VIEW
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.data = Uri.parse(mportalPage)
//                startActivity(intent)
//                finishAffinity()
//            }
//            builder.show()

            val builder = AlertDialog.Builder(this@SplashActivity)
            val dialogView = layoutInflater.inflate(R.layout.custom_alert , null)
            val message = dialogView.findViewById<TextView>(R.id.custom_dialog_message)
            message.text = "앱 자동설치를 위해 설치권한이 필요합니다.\n*허용안함을 누르면 앱 다운로드 페이지로 이동합니다."
            builder.setView(dialogView)
            builder.setCancelable(false)
            builder.setPositiveButton("허용") { dialogInterface, i ->
                finishAffinity()
            }
            builder.create().show()
        }
    }
}