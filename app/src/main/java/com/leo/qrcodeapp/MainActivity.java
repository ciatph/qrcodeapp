package com.leo.qrcodeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.leo.qrcodeapp.barcode.BarcodeCaptureActivity;

public class MainActivity extends AppCompatActivity {
    private TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Text from scanned QR code
        textResult = findViewById(R.id.txt_result);

        // Add click event to the camera button
        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera();
            }
        });
    }

    /**
     * Start the native device camera
     */
    private void startCamera(){
        Intent intent = new Intent(this, BarcodeCaptureActivity.class);
        startActivityForResult(intent, StatusCodes.INSTANCE.BARCODE_READER_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        String message = "";

        if(requestCode == StatusCodes.INSTANCE.BARCODE_READER_REQUEST_CODE && resultCode == CommonStatusCodes.SUCCESS){
            if(data != null){
                Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                Log.d("-scan result", barcode.displayValue + "!");
                message = barcode.displayValue;
                textResult.setText(barcode.displayValue);
            }

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
