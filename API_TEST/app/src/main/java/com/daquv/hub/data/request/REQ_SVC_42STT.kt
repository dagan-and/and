package com.daquv.hub.data.request


import com.daquv.hub.presentation.util.network.TranJson


class REQ_SVC_42STT(
    audio_input: String,
    file_extension: String,
    category: String,
    weight: String,
) : TranJson() {


    /** 통신 요청 구분 코드*/
    val TRAN_ID = ""

    val AUDIO_INPUT = "audio_input"
    val FILE_EXTENSION = "file_extension"
    val CATEOGRY = "category"
    val WEIGHT = "weight"

    init {
        id = TRAN_ID
        put(AUDIO_INPUT, audio_input)
        put(FILE_EXTENSION, file_extension)
        put(CATEOGRY, category)
        put(WEIGHT, weight)
    }

}