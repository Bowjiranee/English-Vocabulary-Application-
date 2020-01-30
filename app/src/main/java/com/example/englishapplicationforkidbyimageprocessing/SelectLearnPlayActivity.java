package com.example.englishapplicationforkidbyimageprocessing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import info.hoang8f.widget.FButton;

public class SelectLearnPlayActivity extends AppCompatActivity {
    private FButton mStudyButton;
    private FButton mPlayButton;
    private FButton mInputButton;
    private FButton mBtnVocab;
    private static final int PERMISSION_RECORD_AUDIO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_learn_play);

        mStudyButton = findViewById(R.id.study_button);
        mPlayButton = findViewById(R.id.play_button);
        mInputButton = findViewById(R.id.btnurl);

        mBtnVocab = findViewById(R.id.btnVocab);

        mBtnVocab.setButtonColor(getResources().getColor(R.color.orange));

        mInputButton.setButtonColor(getResources().getColor(R.color.light_blue));
        mInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectLearnPlayActivity.this,InputURLActivity.class);
                startActivity(intent);
            }
        });

        mStudyButton.setButtonColor(getResources().getColor(R.color.purple));
        mStudyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectLearnPlayActivity.this,SelectCategoryActivity.class);
                startActivity(intent);
            }
        });

        mPlayButton.setButtonColor(getResources().getColor(R.color.pink));
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(SelectLearnPlayActivity.this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Request permission
                    ActivityCompat.requestPermissions(SelectLearnPlayActivity.this,
                            new String[] { Manifest.permission.RECORD_AUDIO },
                            PERMISSION_RECORD_AUDIO);
                    return;
                }
                Intent intent = new Intent(SelectLearnPlayActivity.this,SelectCharacterActivity.class);
                startActivity(intent);
            }
        });

    }
}
