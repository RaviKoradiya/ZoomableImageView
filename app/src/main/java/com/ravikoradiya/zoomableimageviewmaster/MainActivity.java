package com.ravikoradiya.zoomableimageviewmaster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ravikoradiya.zoomableimageview.ZoomableImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ZoomableImageView zoomableImageView = (ZoomableImageView) findViewById(R.id.iv_zoomable);
        zoomableImageView.setImageUrl("http://...");
    }
}
