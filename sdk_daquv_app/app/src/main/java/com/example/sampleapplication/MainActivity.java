package com.example.sampleapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.daquv.sdk.DaquvConfig;
import com.daquv.sdk.presentation.DaquvView;
import com.daquv.sdk.DaquvSDK;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //오디오 권한 체크
        //위치 권한 체크
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            android.Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, REQUEST_PERMISSION);
            return;
        }
        init();
    }

    private void init() {
        //Domain URL 세팅 (필수X 없으면 기본값으로 세팅됩니다.)
        DaquvConfig.crmUrl = "http://203.235.68.65:5102" + "/ava"; //내부망 주소
        //DaquvConfig.crmWASUrl = "다큐브 음성,NLU 서버";

        //DAQUV VIEW 설정
        DaquvView daquvView = findViewById(R.id.daquv_view);
        daquvView.setFragmentManager(getSupportFragmentManager());
        daquvView.setViewListener(new DaquvView.ViewListener() {
            @Override
            public void onAttached() {

            }

            @Override
            public void onDetached() {
                finishAffinity();
            }
        });
        //로그인 정보 설정
        //setLoginInfo("사번")
        daquvView.setLoginInfo("025125");

        //DAQUV VIEW 실행
        daquvView.launch();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init();
            } else {
                Toast.makeText(this, "오디오 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}