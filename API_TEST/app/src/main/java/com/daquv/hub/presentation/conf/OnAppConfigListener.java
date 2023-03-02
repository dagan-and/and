package com.daquv.hub.presentation.conf;

/**
* 앱 초기화 요청 결과 Callback Listener
**/
public interface OnAppConfigListener {

    /**
     * 앱 초기화 요청 결과 반환
     * @param isSuccess 초기화 성공 여부 (true : 성공 / false : 실패)
     */
    public void onCompleteInitialize (boolean isSuccess);
}
