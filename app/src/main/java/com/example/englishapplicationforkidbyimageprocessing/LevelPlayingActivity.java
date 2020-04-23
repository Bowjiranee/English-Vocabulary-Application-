package com.example.englishapplicationforkidbyimageprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.logging.Level;

import info.hoang8f.widget.FButton;

public class LevelPlayingActivity extends AppCompatActivity {
    private FButton mEasyBtn;
    private FButton mHardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_playing);

        mEasyBtn = findViewById(R.id.easy_btn);
        mHardBtn = findViewById(R.id.hard_btn);


        mEasyBtn.setShadowEnabled(true);
        mEasyBtn.setShadowHeight(25);
        mEasyBtn.setCornerRadius(25);
        mEasyBtn.setButtonColor(getResources().getColor(R.color.light_green));
        mEasyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelPlayingActivity.this,QuestionGameActivity.class);
                startActivity(intent);
            }
        });

        mHardBtn.setShadowEnabled(true);
        mHardBtn.setShadowHeight(25);
        mHardBtn.setCornerRadius(25);
        mHardBtn.setButtonColor(getResources().getColor(R.color.orange));
        mHardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelPlayingActivity.this,SelectCharacterActivity.class);
                startActivity(intent);
            }
        });
    }
}
