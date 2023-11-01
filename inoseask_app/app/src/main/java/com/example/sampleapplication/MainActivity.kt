package com.example.sampleapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        //String urlScheme ="https://ibk.kr/mpd?p=ava";
        //String urlScheme ="ibkmportal://ibk?p=ava";
        val urlScheme = "kakao816961d76321eb6c24be53904a5d4ca9://kakaolink"
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(urlScheme)
        startActivity(intent)
        finishAffinity()
    }
//
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
}