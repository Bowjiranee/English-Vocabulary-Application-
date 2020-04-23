package com.example.englishapplicationforkidbyimageprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.englishapplicationforkidbyimageprocessing.Database.NgrokDatabaseHelper;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import info.hoang8f.widget.FButton;

public class ScoreEasyActivity extends AppCompatActivity {

    private FButton mHomeButton;
    private TextView mTextViewScore;
    private NgrokDatabaseHelper mNgrokHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_easy);

        mTextViewScore = findViewById(R.id.easy_game_score_text_view);
        Intent intent = getIntent();
        if(intent == null){
            mTextViewScore.setText("0");
        }else{
            String scorePlayer = intent.getExtras().getString("scores");
            mTextViewScore.setText(scorePlayer);
        }

        mNgrokHelper = new NgrokDatabaseHelper(this);

//        deletefilesInServer();

        mHomeButton = findViewById(R.id.easy_home_button);
        mHomeButton.setShadowEnabled(true);
        mHomeButton.setShadowHeight(20);
        mHomeButton.setCornerRadius(20);
        mHomeButton.setButtonColor(getResources().getColor(R.color.pink));
        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoreEasyActivity.this,SelectLearnPlayActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

//    private void deletefilesInServer() {
//
//        String url = "https://" + mNgrokHelper.getNgrokPath() + ".ngrok.io" + "/wordsapp/sound/rundeletefilesUserUpload.php";
//        Ion.with(this)
//                .load(url)
//                .asString()
//                .setCallback(new FutureCallback<String>() {
//                    @Override
//                    public void onCompleted(Exception e, String result) {
//                    }
//                });
//    }
}
