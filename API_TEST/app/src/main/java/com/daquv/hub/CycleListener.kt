package com.daquv.hub

import android.app.Activity
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class CycleListener : Activity(), LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForeground() {
        //Logger.dev("CycleListener >>> onForeground")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackground() {
        //Logger.dev("CycleListener >>> onBackground")
        //StopMediaPlayer.getInstance(this).stopPlay()
    }
}