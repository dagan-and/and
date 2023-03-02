package com.daquv.hub

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import java.io.IOException
import java.net.SocketException


class App : Application() {

    private val lifecycleListener: LifecycleObserver = CycleListener()
    companion object {
        lateinit var instance: App
            private set

        fun context(): Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        setupLifecycleObserver()
        setRxErrorHandler()
    }

    private fun setupLifecycleObserver() {
        //ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleListener)
        ProcessLifecycleOwner.get().lifecycle.addObserver(MyLifecyclieObserver())
    }

    /**
     * RxJava 사용중에 발생한 ErrorHandler
     */
    private fun setRxErrorHandler() {
        RxJavaPlugins.setErrorHandler { e ->
            var error = e
            if (error is UndeliverableException) {
                error = e.cause
            }
            if (error is IOException || error is SocketException) {
                // fine, irrelevant network problem or API that throws on cancellation
                return@setErrorHandler
            }
            if (error is InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return@setErrorHandler
            }
            if (error is NullPointerException || error is IllegalArgumentException) {
                // that's likely a bug in the application
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), error)
                return@setErrorHandler
            }
            if (error is IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), error)
                return@setErrorHandler
            }
        }
    }



}
