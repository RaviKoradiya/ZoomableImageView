package com.ravikoradiya.zoomableimageviewmaster

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.ravikoradiya.zoomableimageview.ZoomableImageView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val zoomableImageView = findViewById(R.id.iv_zoomable) as ZoomableImageView
        zoomableImageView.setImageUrl("http://...")


    }
}
