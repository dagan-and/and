package com.example.sampleapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
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

        TextView button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ASKActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}