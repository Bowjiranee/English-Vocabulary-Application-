package com.example.englishapplicationforkidbyimageprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import info.hoang8f.widget.FButton;

public class ScorePlayerActivity extends AppCompatActivity {
    private FButton mHomeBtn;

    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_player);

        mHomeBtn = findViewById(R.id.home_button);
        mHomeBtn.setButtonColor(getResources().getColor(R.color.pink));
        mHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScorePlayerActivity.this,SelectLearnPlayActivity.class);
                startActivity(intent);
            }
        });

        mImageView = findViewById(R.id.image_view_character);
    }
}
