package com.example.sampleapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daquv.sdk.DaquvConfig;
import com.daquv.sdk.presentation.DaquvView;
import com.daquv.sdk.utils.Logger;


public class DaquvActivity extends AppCompatActivity {


    DaquvView daquvView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daquv);
        daquvView = findViewById(R.id.daquv_view);
        daquvView.setFragmentManager(getSupportFragmentManager());
        daquvView.setHearViewString("종목정보","호가주문","국내잔고","당일매매","수익률현황");
        daquvView.setKeypadListener(new DaquvView.KeypadListener() {
            @Override
            public void onKeypad() {
                //키움 검색화면으로 이동
                Intent intent = new Intent();
                intent.putExtra("keypad",true);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        daquvView.setResultListener(new DaquvView.ResultListener() {
            @Override
            public void onResult(String screenName , String screenNum , String serviceType, String utterance) {
                Intent intent = new Intent();
                intent.putExtra("screenName", screenName);
                intent.putExtra("screenNum", screenNum);
                intent.putExtra("serviceType", serviceType);
                intent.putExtra("utterance", utterance);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        daquvView.setErrorListener(new DaquvView.ErrorListener() {
            @Override
            public void onError(int code, String message) {
                //ErrorLog 적재 API
                Logger.error(code + "::" + message);
                finish();
            }
        });
        daquvView.launch();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        daquvView.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        daquvView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}