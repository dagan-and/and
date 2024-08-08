package com.example.sampleapplication;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.daquv.sdk.data.response.LoginTokenModel;
import com.daquv.sdk.ui.comm.ComDialogBuilder;
import com.daquv.sdk.ui.comm.OnCommDlgClickListener;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;

public class MainActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> daquvActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //********************************
        //키움 메뉴DB를 Assets 에서 로컬저장소로 저장하기 위한 코드 SDK와 무관함
        AssetHelper assetHelper = new AssetHelper();
        assetHelper.saveLocalStorage(this);
        //********************************

        WebView webView = findViewById(R.id.webview);
        webView.loadUrl("https://www.kiwoom.com/m/main");

        ImageView btnMic = findViewById(R.id.btnMic);
        btnMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("앱 업데이를 하시겠습니까?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        installAPK(MainActivity.this);
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                builder.create().show();
            }
        });

        // ActivityResultLauncher를 등록합니다.
        daquvActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        if(data.hasExtra("keypad")) {
                            ComDialogBuilder builder = new ComDialogBuilder(MainActivity.this);
                            builder.showConfirm(null, "키움 검색화면으로 이동", "확인");
                        } else {
                            String depth4name = data.getStringExtra("screenName");
                            String screenNum = data.getStringExtra("screenNum");
                            String serviceType = data.getStringExtra("serviceType");
                            String utterance = data.getStringExtra("utterance");

                            ComDialogBuilder builder = new ComDialogBuilder(MainActivity.this);
                            builder.showConfirm(null,
                                    "발화내용 : " + utterance + "\n" +
                                            "메뉴명 : " + depth4name + "\n" +
                                            "화면번호 : " + screenNum + "\n" +
                                            "서비스타입 : " + serviceType, "확인");
                        }
                    }
                });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 999) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(getPackageManager().canRequestPackageInstalls()){
                    installAPK(this);
                }
            }
        }
    }

    public void installAPK(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(!context.getPackageManager().canRequestPackageInstalls()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("업데이트를 위해서 앱 설치 권한이 필요합니다.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                        intent.setData(Uri.parse(String.format("package:%s", "com.daquv.sdk")));
                        startActivityForResult(intent,999);
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                builder.create().show();
                return;
            }
        }

        Dialog dialog = new Dialog(this);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 배경을 투명하게
        }
        dialog.setContentView(new ProgressBar(this)); // ProgressBar 위젯 생성
        dialog.setCanceledOnTouchOutside(false); // 외부 터치 막음
        dialog.setCancelable(false);
        dialog.show();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://app.daquv.com/biz_daquv/biz_daquv.apk")
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        });
                        Log.e("DAQUV_ERR", response.body().string());
                    }

                    File internalStorageDirectory = context.getFilesDir();
                    File file = new File(internalStorageDirectory, "app.apk");

                    try (BufferedSink sink = Okio.buffer(Okio.sink(file))) {
                        ResponseBody body = response.body();
                        sink.writeAll(body.source());
                    } catch (IOException e) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        });
                        Log.e("DAQUV_ERR", e.getMessage());
                    }

                    File apkFile = new File(context.getFilesDir(), "app.apk");
                    if (!apkFile.exists()) {
                        // 파일이 존재하지 않으면 설치를 진행하지 않음
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        });
                        return;
                    }

                    Uri apkUri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        // For Android 7.0 (API 24) and above
                        apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", apkFile);
                    } else {
                        apkUri = Uri.fromFile(apkFile);
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    context.startActivity(intent);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                    Log.e("DAQUV_ERR", e.getMessage());
                }
            }
        });
    }
}
