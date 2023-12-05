package com.daquv.ioneask

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import java.lang.Exception
import java.lang.RuntimeException

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        val urlScheme ="ibkmportal://ibk?p=ava"
        val downloadPage = "http://ibk.kr/mp"

        try {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.data = Uri.parse(urlScheme)
            startActivity(intent)
            finishAffinity()
        } catch (e : RuntimeException) {
            //엠포탈 다운로드 페이지 URL 로 이동
            val builder = AlertDialog.Builder(this)
            builder.setMessage("앱이 설치되어있지 않습니다.\nM포탈 다운로드 사이트로 이동됩니다.")
            builder.setPositiveButton("확인", null)
            builder.setOnDismissListener {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.data = Uri.parse(downloadPage)
                startActivity(intent)
                finishAffinity()
            }
            builder.show()
        }
    }
}