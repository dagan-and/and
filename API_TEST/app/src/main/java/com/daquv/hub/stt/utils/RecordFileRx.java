package com.daquv.hub.stt.utils;

import android.content.Context;
import android.media.AudioRecord;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.daquv.hub.data.model.STTDataModel;
import com.daquv.hub.data.request.REQ_SVC_42STT;
import com.daquv.hub.data.respository.Repository;
import com.daquv.hub.data.respository.RepositoryImpl;
import com.daquv.hub.domain.usecase.GetSTTUseCase;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class RecordFileRx {

    private static final int maxBufferSize = 60;
    private static final Long unitTime = 2000L;

    PublishSubject<byte[]> rxStream;
    ByteArrayOutputStream totalBuffer;
    Long recordingTime = 0L;
    boolean isRecording = false;
    Context mContext;
    int SAMPLE_RATE;
    int bufferSize = 0;
    Repository repository;
    CompositeDisposable disposable;
    ExecutorService executorService;

    public RecordFileRx(@NonNull Context context, Callback callback) {
        mContext = context;
        mCallback = callback;
        repository = new RepositoryImpl();
        disposable = new CompositeDisposable();
        executorService = Executors.newSingleThreadExecutor();
    }


    public static abstract class Callback {
        public void onSaveFile(String fileName) {
        }

        public void onVAD() {
        }
    }

    private final RecordFileRx.Callback mCallback;

    /**
     * 녹음기 파일 저장 셋업
     */
    public void setup(int sampleRate) {
        isRecording = true;
        SAMPLE_RATE = sampleRate;
        totalBuffer = new ByteArrayOutputStream();
    }

    public void close() {
        /* 음성 발화가 끝나면 지금까지 버퍼를 파일로 생성 */
        if (totalBuffer != null && totalBuffer.size() > 0) {
            saveFile(totalBuffer.toByteArray(), "full");
        }
        totalBuffer = null;
        isRecording = false;
        bufferSize = 0;
    }

    public void onStart() {
        recordingTime = System.currentTimeMillis();
    }

    /**
     * 녹음기 파일 Buffer 저장
     *
     * @param buffer
     * @param status mCallback.onVoice(mBuffer, size) 같은 라인에 추가
     */
    public void capture(byte[] buffer, int status) {
        if (status == AudioRecord.ERROR_INVALID_OPERATION || status == AudioRecord.ERROR_BAD_VALUE)
            return;
        bufferSize++;
        try {
            totalBuffer.write(buffer);
            byte[] temp = new byte[0];
            Long now = System.currentTimeMillis();
            if (now - recordingTime >= unitTime) {
                recordingTime = now;
                temp = buffer;
            }
            /*
              전달 받은 데이터가 있으면 파일로 저장
              API 가 나오면 API 실행하는것으로 수정
             */
            if (temp != null && temp.length > 0) {
                saveFile(totalBuffer.toByteArray(), String.valueOf(bufferSize / maxBufferSize));

                REQ_SVC_42STT reqData = null;
                try {
                    reqData = new REQ_SVC_42STT(rawToBinary(totalBuffer.toByteArray()), "pcm", "default-JPBART", "19.0");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Disposable mDisposable = null;
                try {
                    mDisposable = new GetSTTUseCase(repository).invoke("http://api-stt-daquv.42maru.com/predict", reqData.getGsonObject())
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(new Consumer<STTDataModel>() {
                                @Override
                                public void accept(STTDataModel s) throws Throwable {
                                    Log.d("DDDDD", "Result::" + new Gson().toJson(s));
                                    mCallback.onSaveFile(new Gson().toJson(s));
                                    if (s.getData().getState().equals("2")) {
                                        mCallback.onVAD();
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Throwable {
                                    Log.d("DDDDD", throwable.getMessage());
                                    mCallback.onSaveFile(throwable.getMessage());
                                }
                            });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                disposable.add(mDisposable);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 음성 인식파일 저장 종료
     * Thread.currentThread().isInterrupted() , end() 같은 라인에 추가
     */
    public void saveFile(byte[] buffer, String number) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    isRecording = false;

                    File f2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/stt_" + number + ".wav");
                    rawToWave(buffer, f2);
                    mCallback.onSaveFile(f2.getName());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String rawToBinary(final byte[] rawData) throws IOException {
        return Base64.encodeToString(rawData, Base64.NO_WRAP);
//        DataOutputStream output = null;
//        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            output = new DataOutputStream(baos);
//            // WAVE header
//            writeString(output, "RIFF"); // chunk id
//            writeInt(output, 36 + rawData.length); // chunk size
//            writeString(output, "WAVE"); // format
//            writeString(output, "fmt "); // subchunk 1 id
//            writeInt(output, 16); // subchunk 1 size
//            writeShort(output, (short) 1); // audio format (1 = PCM)
//            writeShort(output, (short) 1); // number of channels
//            writeInt(output, SAMPLE_RATE); // sample rate
//            writeInt(output, SAMPLE_RATE * 2); // byte rate
//            writeShort(output, (short) 2); // block align
//            writeShort(output, (short) 16); // bits per sample
//            writeString(output, "data"); // subchunk 2 id
//            writeInt(output, rawData.length); // subchunk 2 size
//            // Audio data (conversion big endian -> little endian)
//            short[] shorts = new short[rawData.length / 2];
//            ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
//            ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
//            for (short s : shorts) {
//                bytes.putShort(s);
//            }
//            output.write(rawData);
//            byte[] result = baos.toByteArray();
//            return Base64.encodeToString(result, Base64.NO_WRAP);
//
//        } finally {
//            if (output != null) {
//                output.close();
//            }
//        }
    }

    private void rawToWave(final byte[] rawData, final File waveFile) throws IOException {
        DataOutputStream output = null;
        try {
            output = new DataOutputStream(new FileOutputStream(waveFile));
            // WAVE header
            writeString(output, "RIFF"); // chunk id
            writeInt(output, 36 + rawData.length); // chunk size
            writeString(output, "WAVE"); // format
            writeString(output, "fmt "); // subchunk 1 id
            writeInt(output, 16); // subchunk 1 size
            writeShort(output, (short) 1); // audio format (1 = PCM)
            writeShort(output, (short) 1); // number of channels
            writeInt(output, SAMPLE_RATE); // sample rate
            writeInt(output, SAMPLE_RATE * 2); // byte rate
            writeShort(output, (short) 2); // block align
            writeShort(output, (short) 16); // bits per sample
            writeString(output, "data"); // subchunk 2 id
            writeInt(output, rawData.length); // subchunk 2 size
            // Audio data (conversion big endian -> little endian)
            short[] shorts = new short[rawData.length / 2];
            ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
            ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
            for (short s : shorts) {
                bytes.putShort(s);
            }
            output.write(rawData);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    private void writeInt(final DataOutputStream output, final int value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
        output.write(value >> 16);
        output.write(value >> 24);
    }

    private void writeShort(final DataOutputStream output, final short value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
    }

    private void writeString(final DataOutputStream output, final String value) throws IOException {
        for (int i = 0; i < value.length(); i++) {
            output.write(value.charAt(i));
        }
    }


}
