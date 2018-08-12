package com.leo.qrcodeapp;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.TextView;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

public class MainActivity extends AppCompatActivity {

    // QREader
    private SurfaceView surfaceView;
    private QREader qrEader;
    private TextView textView;

    // Camera permissions
    boolean hasCamPermission = false;
    private static final String cameraPermission = Manifest.permission.CAMERA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hasCamPermission = RuntimePermissionUtil.checkPermissonGranted(this, cameraPermission);

        // Setup SurfaceView
        surfaceView = findViewById(R.id.camera_view);
        textView = findViewById(R.id.text_info);

        // Initialize QREader
        qrEader = new QREader.Builder(this, surfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(data);
                    }
                });
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(surfaceView.getHeight())
                .width(surfaceView.getWidth())
                .build();
    }


    @Override
    protected void onResume(){
        super.onResume();

        // Initialize and start SurfaceView
        qrEader.initAndStart(surfaceView);
    }


    @Override
    protected void onPause(){
        super.onPause();

        // Cleanup
        qrEader.releaseAndCleanup();
    }
}
