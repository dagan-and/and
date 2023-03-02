package com.daquv.hub.presentation.util.media;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Base64;
import android.util.Log;

import com.daquv.hub.presentation.util.SharedPref;

import java.io.FileOutputStream;

public class MediaPlayerUtil {
    private static final String TAG = MediaPlayerUtil.class.getSimpleName();

    private MediaPlayer mediaPlayer;
    private boolean isPrepared;
    private Context context;
    private int currentPosition = 0;
    private String mediaData;
    private MediaPlayerHelper.OnMediaCallbackListener mediaCallback;

    public MediaPlayerUtil(Context context) {
        this.context = context;
    }

    /**
     * 미디어를 재생시킬 PLAYER 를 init 한다. PCM_STREAM 제외
     **/
    private MediaPlayer initPlayer() {
        try {

            if (mediaData.equals("null") || mediaData == null || mediaData.isEmpty()) {
                if (mediaCallback != null) mediaCallback.onFinish();
                return null;
            }

                MediaPlayer mediaPlayer = new MediaPlayer();

                mediaPlayer.setDataSource(base64StringToFile(mediaData));
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setVolume(1, 1);
                mediaPlayer.setOnPreparedListener(mp -> {
                    Log.d(TAG, "playMedia onPrepared");
                    isPrepared = true;
                    start(true);
                });
                mediaPlayer.setOnCompletionListener(mp -> {
                    if (!isPrepared) return;
                    isPrepared = false;

                    Log.d(TAG, "playMedia onCompletion");

                    MediaPlayerStatus.INSTANCE.setState("MEDIA_COMPLETE");
                    MediaPlayerStatus.INSTANCE.setPlaytime(mp.getDuration());
                    if (mediaCallback != null) mediaCallback.onFinish();
                });
                mediaPlayer.setOnErrorListener((mp, i, i1) -> {
                    Log.d(TAG, "playMedia onError : " + i + ", " + i1);
                    // 에러 났을 때도 stopAndRunActOnOther 실행
                    return false;
                });
                mediaPlayer.setOnBufferingUpdateListener((mp, i) -> {
                    Log.d(TAG, "playMedia onBufferingUpdate : " + i);
                });
                return mediaPlayer;


        } catch (Exception e) {
            Log.i(TAG, "initPlayer Exception : " + e.toString());
        }
        return null;
    }

    private void initAndPrepareAsync() {
        mediaPlayer = initPlayer();
        if (mediaPlayer != null) mediaPlayer.prepareAsync();
        else { /* 미디어 플레이어 생성 실패 */ }
    }

    void playWav(String audioData, MediaPlayerHelper.OnMediaCallbackListener callback) {
        mediaCallback = callback;
        mediaData = audioData;
        release();
        // voice 처리
        initAndPrepareAsync();
    }


    /**
     * 미디어 플레이어를 play 시킨다.
     * isFirstStart true : started, false : resumed
     **/
    void start(boolean isFirstStart) {
        Log.d(TAG, "start!!!!!!!!!: ");
        try {
            if (mediaPlayer != null && isPrepared) {
                if(currentPosition > 0) {
                    mediaPlayer.seekTo(currentPosition);
                }
                mediaPlayer.start();
            }
        } catch (Exception e) {
            Log.i(TAG, "start Exception : " + e.toString());
        }
    }

    /**
     * 미디어 플레이어를 pause 시킨다.
     **/
    void pause() {
        try {
            if (mediaPlayer != null && isPrepared) {
                mediaPlayer.pause();
            }
        } catch (Exception e) {
            Log.i(TAG, "pause Exception : " + e.toString());
        }
    }

    /**
     * 미디어 플레이어를 mute 시키거나 해제한다.
     **/
    void mute(boolean mute) {
        try {
            if (mediaPlayer != null && isPrepared)
                mediaPlayer.setVolume(mute ? 0 : 1, mute ? 0 : 1);
        } catch (Exception e) {
            Log.i(TAG, "mute Exception : " + e.toString());
        }
    }

    /**
     * 미디어 플레이어의 재생중 여부를 리턴한다.
     **/
    boolean isPlaying() {
        try {
            if (mediaPlayer != null) return mediaPlayer.isPlaying();
        } catch (Exception e) {
            Log.i(TAG, "isPlaying Exception : " + e.toString());
        }
        return false;
    }

    /**
     * 미디어 플레이어를 전화/외장 스피커로 송출한다
     */
    void updateStreamMode() {
        if (mediaPlayer != null) {
            currentPosition = mediaPlayer.getCurrentPosition();
            if(mediaPlayer.getCurrentPosition() - 500 > 0) {
                currentPosition = mediaPlayer.getCurrentPosition() - 500;
            } else {
                currentPosition = mediaPlayer.getCurrentPosition();
            }
            release();
            initAndPrepareAsync();
        }
    }

    /**
     * currentPosition 초기화
     */
    void iniCurrentPosition() {
        currentPosition = 0;
    }

    /**
     * 재생중인 미디어를 stop 하고 미디어 플레이어를 Release 시킨다.
     **/
    void stop() {
        try {
            if (mediaPlayer != null) {
                if (isPrepared) updateMediaStatus("MEDIA_STOPPED", mediaPlayer.getCurrentPosition());
                else updateMediaStatus("MEDIA_STOPPED", 0);
            }

        } catch (Exception e) {
            Log.i(TAG, "stop Exception : " + e);
        }
        release();
    }

    /**
     * 미디어 플레이어의 현재 상태를 서버로 전송한다.
     **/
    private void updateMediaStatus(String state, int playtime) {
        Log.d(TAG, "updateMediaStatus state=" + state);
        try {

            MediaPlayerStatus.INSTANCE.setState(state);
            MediaPlayerStatus.INSTANCE.setPlaytime(playtime);

        } catch (Exception e) {
            Log.i(TAG, "updateMediaStatus Exception : " + e.toString());
        }
    }

    /**
     * 미디어 플레이어와 오디오트랙을 release 시킨다.
     **/
    private void release() {
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        Log.i(TAG, "releaseMediaPlayer()");
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (Exception e) {
            Log.i(TAG, "releaseMediaPlayer Exception : " + e.toString());
        }
        isPrepared = false;
    }

    void reset() {
        release();
    }

    //임시 wav file write
    public String base64StringToFile(String base64AudioData) {
        try {
            byte[] decoded = Base64.decode(base64AudioData, 1);
            FileOutputStream os = context.openFileOutput("mediastream.mp3", Context.MODE_PRIVATE);
            os.write(decoded);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context.getFileStreamPath("mediastream.mp3").getAbsolutePath();
    }
}