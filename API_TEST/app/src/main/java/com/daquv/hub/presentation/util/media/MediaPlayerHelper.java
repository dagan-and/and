package com.daquv.hub.presentation.util.media;

import android.content.Context;

import com.daquv.hub.presentation.util.Logger;


public class MediaPlayerHelper {
    private static final String TAG = MediaPlayerHelper.class.getSimpleName();

    // 미디어 플레이어를 세팅한다.
    private MediaPlayerUtil mMediaPlayerUtil;
    private final Context mContext;


    public interface OnMediaCallbackListener {
        void onFinish();
    }

    public MediaPlayerHelper(Context context) {
        reset();
        mContext = context;
        mMediaPlayerUtil = new MediaPlayerUtil(mContext);
    }



    public void startWav(String audioData, OnMediaCallbackListener callback) {
        try {
            mMediaPlayerUtil.iniCurrentPosition();
            mMediaPlayerUtil.playWav(audioData, callback);
        } catch (Exception e) {
            Logger.info(TAG + " MediaPlayer startWav Exception : " + e.toString());
        }
    }

    /**
     * 수화기,스피커 모드를 갱신함
     * -재생중이면 재생중인 시간을 저장했다가 해당 부분부터 다시 실행
     */
    public void updateStreamMode() {
        if(mMediaPlayerUtil != null && mMediaPlayerUtil.isPlaying()) {
            mMediaPlayerUtil.updateStreamMode();
        }
    }


    /**
     * 초기화
     **/
    public void reset() {
        if (mMediaPlayerUtil != null) {
            try {
                mMediaPlayerUtil.reset();
            } catch (Exception e) {
                Logger.error(e);
            }
        }
        mMediaPlayerUtil = null;
    }

    public void playAfterVoiceFail() {
        mMediaPlayerUtil.start(false);
    }

    public void pause() {
        mMediaPlayerUtil.pause();
    }

    public boolean isPlaying() {
        return mMediaPlayerUtil.isPlaying();
    }

    public void stop() {
        mMediaPlayerUtil.stop();
    }
}
