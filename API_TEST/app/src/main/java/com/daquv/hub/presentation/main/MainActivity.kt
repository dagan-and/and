package com.daquv.hub.presentation.main

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.daquv.api.test.R
import com.daquv.api.test.databinding.ActivityMainBinding
import com.daquv.hub.App
import com.daquv.hub.data.respository.Repository
import com.daquv.hub.data.respository.RepositoryImpl
import com.daquv.hub.domain.usecase.GetTTSUseCase
import com.daquv.hub.presentation.BaseActivity
import com.daquv.hub.presentation.conf.Const
import com.daquv.hub.presentation.conf.IntentConst
import com.daquv.hub.presentation.util.Logger
import com.daquv.hub.presentation.util.PermissionUtils
import com.daquv.hub.stt.utils.RecordFileRx
import com.daquv.hub.stt.utils.VoiceRecorder
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*


class MainActivity : BaseActivity() {

    lateinit var mainViewModel: MainViewModel
    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!
    private var disposable: Disposable? = null
    private var voiceRecorder : VoiceRecorder? = null
    private var recordFile : RecordFileRx?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository: Repository = RepositoryImpl()
        val getTTSUseCase: GetTTSUseCase = GetTTSUseCase(repository)
        val mainViewModelFactory = MainViewModelFactory(getTTSUseCase)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        mBinding!!.viewModel = mainViewModel
        setContentView(binding.root)

        //권한 체크하기
        if (checkPermission(this)!!) {
            initPermission(this)
        } else {
            initSTTEngine()
        }

        mBinding!!.recordButton.setOnClickListener {
            if(mBinding!!.recordButton.tag != null && mBinding!!.recordButton.tag == true) {
                voiceRecorder!!.stop()
                voiceRecorder!!.dismiss()
            } else {
                voiceRecorder!!.start()
            }
        }

        mBinding!!.dynamicLm.setOnClickListener {
            mainViewModel.dynamicLM()
        }

        mainViewModel.message.observe(this, androidx.lifecycle.Observer{
            runOnUiThread {
                val log = TextView(this@MainActivity)
                log.text = it
                log.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
                mBinding!!.logContainer.addView(log)
            }
        })

        mBinding!!.tmap.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tmap://route?goalx=$37.3172863&goaly=$126.84426&goalname=$title")).apply {
//                `package` = "com.skt.skaf.l001mtm091"
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            }
//            startActivity(intent)
            //android.content.ActivityNotFoundException: No Activity found to handle Intent { act=android.intent.action.VIEW dat=tmap://route/... flg=0x10000000 pkg=com.skt.skaf.l001mtm091 }

//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tmap://route?goalx=$37.3172863&goaly=$126.84426&goalname=$title")).apply {
//                `package` = "com.skt.tmap.ku"
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            }
//            startActivity(intent)

            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.data = Uri.parse("geo:37.3848633,127.1233389?q=경기도 성남시 분당구 서현동 263(서현역)")
            intent.`package` = "com.skt.tmap.ku"
            startActivity(intent)

        }
    }

    fun initSTTEngine() {
        recordFile = RecordFileRx(App.context() , object : RecordFileRx.Callback() {
            override fun onSaveFile(fileName: String?) {
                super.onSaveFile(fileName)
                runOnUiThread {
                    val log = TextView(this@MainActivity)
                    log.text = fileName
                    log.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
                    mBinding!!.logContainer.addView(log)
                }
            }

            override fun onVAD() {
                super.onVAD()
                voiceRecorder!!.stop()
                voiceRecorder!!.dismiss()
            }

        })
        voiceRecorder = VoiceRecorder(object : VoiceRecorder.Callback() {
            override fun onVoice(data: ByteArray?, size: Int) {
                super.onVoice(data, size)
                recordFile!!.capture(data,size)
            }

            override fun onVoiceEnd() {
                super.onVoiceEnd()
                recordFile!!.close()
                runOnUiThread {
                    mBinding!!.recordButton.text = "녹음 시작"
                    mBinding!!.recordButton.tag = false
                }
            }

            override fun onVoiceStart() {
                super.onVoiceStart()
                recordFile!!.setup(voiceRecorder!!.sampleRate)
                recordFile!!.onStart()
                runOnUiThread {
                    mBinding!!.recordButton.text = "녹음 중지"
                    mBinding!!.recordButton.tag = true
                    mBinding!!.logContainer.removeAllViews()
                }
            }
        })
    }

    //필수 접근 권한 검사
    private fun checkPermission(context: Activity): Boolean? {
        return PermissionUtils.checkDePermission(
            context,
            IntentConst.PermissionRequestCode.REQ_PERMISSIONS_APP_REQUIRED,
            *Const.APP_REQUIRED_PERMISSIONS()
        )
    }

    /**
     * SpeechService 서비스를 앱에 등록
     */
    override fun onStart() {
        super.onStart()
        mainViewModel.lifeCycleStart()
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.lifeCycleResume()
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.lifeCyclePause()
    }

    override fun onStop() {
        super.onStop()
        mainViewModel.lifeCycleStop()
    }

    /**
     * SpeechService 서비스를 앱에 해지
     */
    override fun onDestroy() {
        mBinding = null
        if(voiceRecorder != null) {
            voiceRecorder!!.stop()
            voiceRecorder!!.dismiss()
        }
        if(recordFile != null) {
            recordFile!!.close()
        }
        super.onDestroy()
        disposable?.let { disposable!!.dispose() }
    }

    // 앱 필수 접근 권한 검사 후 로그인 이동
    private fun initPermission(context: Activity) {
        if (PermissionUtils.checkDePermission(
                context,
                IntentConst.PermissionRequestCode.REQ_PERMISSIONS_APP_REQUIRED,
                *Const.APP_REQUIRED_PERMISSIONS()
            )
        ) {
            PermissionUtils.checkPermission(
                context,
                IntentConst.PermissionRequestCode.REQ_PERMISSIONS_APP_REQUIRED,
                *Const.APP_REQUIRED_PERMISSIONS()
            )
        } else {
            initSTTEngine()
        }
    }


    private fun isPermissionDenied(
        code: String,
        permissions: Array<String?>,
        grantResults: IntArray
    ): Boolean? {
        val arrPermissions = ArrayList(Arrays.asList(*permissions))
        return if (arrPermissions.contains(code) && grantResults[arrPermissions.indexOf(code)] != PackageManager.PERMISSION_GRANTED) {
            true
        } else false
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Logger.info("reqCode :: \$requestCode")
        Logger.info("permissions :: \$permissions")
        Logger.info("grantResults :: \$grantResults")

        // 접근권한 허용 여부
        val isGrant = PermissionUtils.checkReqPermissionResult(grantResults)
        if (requestCode == IntentConst.PermissionRequestCode.REQ_PERMISSIONS_APP_REQUIRED) {
            // 기본 필수 접근 권한 검사 결과 처리
            if (!isGrant) {
                var message = ""
                if (isPermissionDenied(
                        Manifest.permission.RECORD_AUDIO,
                        permissions,
                        grantResults
                    )!!
                ) {
                    message = "오디오 권한이 필요합니다"
                }
                val builder1 = AlertDialog.Builder(this)
                builder1.setMessage(message)
                builder1.setCancelable(true)
                builder1.setPositiveButton(
                    "Yes"
                ) { dialog, id ->
                    dialog.cancel()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.parse("package:$packageName"))
                    startActivity(intent)
                }
                builder1.setNegativeButton(
                    "No"
                ) { dialog, id ->
                    dialog.cancel()
                    finish()
                }
                val alert11 = builder1.create()
                alert11.show()
            } else {
                initSTTEngine()
            }
        }
    }

    /** 뒤로가기  */
    private var backBtnTime: Long = 0
    override fun onBackPressed() {

        // 디바이스 Back 버튼 선택 > 앱 종료
        val curTime = System.currentTimeMillis()
        val gapTime = curTime - backBtnTime

        if (gapTime in 0..2000) {
            finish()
        } else {
            backBtnTime = curTime
            Toast.makeText(this, getString(R.string.application_close), Toast.LENGTH_SHORT).show()
        }
    }

}