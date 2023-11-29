package com.daquv.ioneask

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
        val urlScheme ="ibkmportal://ibk?p=ava";

        try {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.data = Uri.parse(urlScheme)
            startActivity(intent)
        } catch (e : RuntimeException) {
            //앱포탈 다운로드 페이지 URL 로 이동
        }
        finishAffinity()
    }
}