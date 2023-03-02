package com.daquv.hub.data.request


import com.daquv.hub.presentation.util.network.TranJson


class REQ_SVC_STT (sttWord : String) : TranJson() {

    private val strFieldName1 : String = "utterance"

    /** 통신 요청 구분 코드  */
    val TRAN_ID = "APP_SVC_STT"

    /*
    POST http://3.36.201.40/api/utterance
    {
    "utterance": "영업 오더 이슈 알려줘",
    }
     */

    init {
        TranJson(TRAN_ID)
        put(strFieldName1, sttWord)
    }

}