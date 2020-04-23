package com.example.englishapplicationforkidbyimageprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.VideoView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private VideoView mVideoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoView = findViewById(R.id.videoView);

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.homestart);
        mVideoView.setVideoURI(uri);
        mVideoView.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,SelectLearnPlayActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}
