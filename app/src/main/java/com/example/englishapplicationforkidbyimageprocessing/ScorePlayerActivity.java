package com.example.englishapplicationforkidbyimageprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishapplicationforkidbyimageprocessing.Database.NgrokDatabaseHelper;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.IOException;

import info.hoang8f.widget.FButton;

public class ScorePlayerActivity extends AppCompatActivity {
    private FButton mHomeBtn;
    private TextView mTextViewScore;
    private NgrokDatabaseHelper mNgrokHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_player);

        mTextViewScore = findViewById(R.id.game_score1_text_view);
        Intent intent = getIntent();
        if(intent == null){
            mTextViewScore.setText("0");
        }else{
            String scorePlayer = intent.getExtras().getString("scores");
            mTextViewScore.setText(scorePlayer);
        }

        mNgrokHelper = new NgrokDatabaseHelper(this);

        deletefilesInServer();

        mHomeBtn = findViewById(R.id.home_button);
        mHomeBtn.setShadowEnabled(true);
        mHomeBtn.setShadowHeight(20);
        mHomeBtn.setCornerRadius(20);
        mHomeBtn.setButtonColor(getResources().getColor(R.color.pink));

        mHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScorePlayerActivity.this,SelectLearnPlayActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void deletefilesInServer() {

        String url = "https://" + mNgrokHelper.getNgrokPath() + ".ngrok.io" + "/wordsapp/sound/rundeletefiles.php";
        Ion.with(this)
                .load(url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                    }
                });
    }


}
