package com.example.englishapplicationforkidbyimageprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import info.hoang8f.widget.FButton;

public class ScorePlayer2Activity extends AppCompatActivity {

    private FButton mHomeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_player2);

        mHomeButton = findViewById(R.id.home_button2);
        mHomeButton.setButtonColor(getResources().getColor(R.color.pink));
        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScorePlayer2Activity.this,SelectLearnPlayActivity.class);
                startActivity(intent);
            }
        });
    }


}
