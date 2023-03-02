package com.daquv.hub.stt.utils;

import android.content.Context;
import android.media.AudioRecord;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;

public class RecordFile {


    /**
     * 녹음기 파일 저장 관련 변수
     */
    FileOutputStream outputStream;
    String byteBuffer = "";
    boolean isRecording  = false;
    Context mContext;
    private static int SAMPLE_RATE;
    private static int bufferSize = 0;
    private static int maxBufferSize = 50;

    public RecordFile(@NonNull Context context , Callback callback) {
        mContext = context;
        mCallback = callback;
    }


    public static abstract class Callback {
        public void onSaveFile(String fileName) {
        }
    }

    private final RecordFile.Callback mCallback;

    /**
     * 녹음기 파일 저장 셋업
     * mCallback.onVoiceStart() 같은 라인에 추가
     */
    public void recordSetup(int sampleRate) {
        byteBuffer = "";
        isRecording = true;
        SAMPLE_RATE = sampleRate;
    }


    /**
     * 녹음기 파일 Buffer 저장
     * @param buffer
     * @param status
     * mCallback.onVoice(mBuffer, size) 같은 라인에 추가
     */
    public void recordCapture(byte[] buffer, int status) {
        if (status == AudioRecord.ERROR_INVALID_OPERATION || status == AudioRecord.ERROR_BAD_VALUE)
            return;

        bufferSize++;
        byteBuffer += byteToHexString(buffer);
        if(bufferSize % maxBufferSize == 0) {
            String temp = byteBuffer;
            byteBuffer = "";
            saveFile(temp, bufferSize / maxBufferSize);
        }

    }

    private static String byteToHexString(byte[] byteArray) {
        StringBuilder sb = new StringBuilder(byteArray.length * 2);
        for (byte b : byteArray) {
            String var11 = String.format("%02x", b);
            sb.append(var11.toUpperCase(Locale.getDefault()));
        }
        return sb.toString();
    }

    private static byte[] hexToByteArray(String hex) {
        byte[] var10000;
        if (hex != null && hex.length() % 2 == 0) {
            byte[] bytes = new byte[hex.length() / 2];

            for(int i = 0; i < hex.length(); i += 2) {
                int var6 = i + 2;
                String var5 = hex.substring(i, var6);
                byte var9 = 16;
                byte value = (byte)Integer.parseInt(var5, var9);
                double var7 = (double)(i / 2);
                bytes[(int)Math.floor(var7)] = value;
            }

            var10000 = bytes;
        } else {
            var10000 = new byte[0];
        }

        return var10000;
    }

    /**
     *  음성 인식파일 저장 종료
     *  Thread.currentThread().isInterrupted() , end() 같은 라인에 추가
     */
    public void saveFile(String byteBuffer ,int number) {
        try {
            isRecording = false;

            File f2 = new File(mContext.getFilesDir() + "/stt_" + number + ".wav");
            rawToWave(hexToByteArray(byteBuffer), f2);
            mCallback.onSaveFile(f2.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void rawToWave(final byte[] rawData, final File waveFile) throws IOException {


        DataOutputStream output = null;
        try {
            //TODO 추후에 파일로 저장할 필요없이 바이트로 만들어서 서버로 바로 던지기
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            output = new DataOutputStream(baos);
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

//            byte[] result = baos.toByteArray();
//            String value = Base64.encodeToString(result, Base64.DEFAULT);
//
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
