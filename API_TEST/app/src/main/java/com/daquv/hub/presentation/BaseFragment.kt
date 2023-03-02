package com.daquv.hub.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.daquv.api.test.R
import com.daquv.hub.presentation.util.Logger

open class BaseFragment : Fragment(), View.OnClickListener{

    /** 클래스 구분 태그  */
    private val TAG = javaClass.simpleName

    /** Layout View  */
    protected var mLayoutView: View? = null

    /** Fragment 요청 코드  */
    protected var mRequestCode = 0

    /** Fragment 요청 데이터  */
    protected var mRequestData: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.info(this.javaClass.simpleName + " onCreate () call")
        super.onCreate(savedInstanceState)

        mLayoutView = null

        Logger.dev("saveInstanceState isNull :: " + (savedInstanceState == null))
        if (savedInstanceState != null) {
            mRequestData = savedInstanceState
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Logger.info(this.javaClass.simpleName + " onSaveInstanceState () call")
        super.onSaveInstanceState(outState)

        if (mRequestData != null) outState.putAll(mRequestData)

        val keySet = outState.keySet()
        for (key in keySet) {
            Logger.dev("key :: " + key + " : " + outState[key])
        }
    }

    override fun onResume() {
        Logger.info(this.javaClass.simpleName + " onResume () call")
        super.onResume()
    }

    /**
     * 화면 타이틀바 초기화
     * <br><br>
     * - Fragment Activity 가 BaseActivity 를 상속받은 Activity 인 경우에만 설정 <br>
     * - BaseFragment setRequestData () 를 통해 전달받은 데이터를 통해 타이틀 초기화 <br>
     */
    protected fun initTitle() {
        initTitle(null)
    }

    /**
     * 화면 타이틀바 초기화
     * <br><br>
     * - Fragment Activity 가 BaseActivity 를 상속받은 Activity 인 경우에만 설정 <br>
     * - BaseFragment setRequestData () 를 통해 전달받은 데이터를 통해 타이틀 초기화 <br>
     * @param titleClickListener TitleBar Click Listener (null 일 경우 재설정하지 않음, 기본 설정 유지)
     */
    protected fun initTitle(titleClickListener: View.OnClickListener?) {

    }

    // TODO :: 애니메이션 적용 필요한 경우 아래 Method 호출
    /* ====================================================
    Activity 호출 및 종료 Method Override START
    - 화면 표시 Animation 사용
    ==================================================== */
    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        if (activity == null) return

        requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        if (activity == null) return

        requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
    /* ====================================================
    Activity 호출 및 종료 Method Override END
    ==================================================== */

    override fun onClick(v: View?) {

    }

    /**
     * Fragment 를 호출한 화면으로 결과 Callback 필요 여부 반환
     * @return Fragment 를 호출한 화면으로 결과 Callback 필요 여부
     */
    fun isNeedCallbackResult(): Boolean {
        Logger.info("mRequestCode :: $mRequestCode")
        return mRequestCode != 0
    }

    /**
     * Fragment 요청 코드 저장
     * @param requestCode Fragment 요청 코드
     */
    fun setRequestCode(requestCode: Int) {
        Logger.info("setRequestCode () call >> requestCode :: $requestCode")
        mRequestCode = requestCode
    }

    /**
     * Fragment 요청 코드 반환
     * @return Fragment 요청 코드
     */
    fun getRequestCode(): Int {
        Logger.info("getRequestCode () call")
        return mRequestCode
    }

    /**
     * Fragment 요청 Data 저장
     * @param requestData Fragment 요청 Data
     */
    fun setRequestData(requestData: Bundle) {
        Logger.info("requestData isNull :: " + (requestData == null))
        mRequestData = requestData
    }

    /**
     * Fragment 요청 Data 반환
     * @return Fragment 요청 Data
     */
    fun getRequestData(): Bundle? {
        Logger.info("getRequestData () call")
        return mRequestData
    }


    /**
     * Fragment 를 호출한 화면으로 결과 Callback
     * @param resultCode 결과코드
     */
    protected fun callbackFragmentResult(resultCode: Int) {
        callbackFragmentResult(resultCode, null)
    }

    /**
     * Fragment 를 호출한 화면으로 결과 Callback
     * @param resultCode 결과코드
     * @param data 결과 데이터
     */
    protected fun callbackFragmentResult(resultCode: Int, data: Intent?) {
        Logger.info("resultCode :: $resultCode")
        Logger.info("data isNull :: " + (data == null))

        Logger.dev("getActivity() isNull :: " + (activity == null))
        if (activity == null) return
    }

}