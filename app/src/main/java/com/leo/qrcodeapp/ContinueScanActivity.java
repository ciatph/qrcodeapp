package com.leo.qrcodeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;

import com.leo.qrcodeapp.qreader.QRDataListener;
import com.leo.qrcodeapp.qreader.QREader;

public class ContinueScanActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private QREader qrEader;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_scan);

        surfaceView = findViewById(R.id.camera_view);
        textView = findViewById(R.id.txt_read);

        qrEader = new QREader.Builder(this, surfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                Log.d("QEReader", "Value: " + data);
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
        qrEader.initAndStart(surfaceView);
    }

    protected void onPause(){
        super.onPause();
        qrEader.releaseAndCleanup();
    }
}
