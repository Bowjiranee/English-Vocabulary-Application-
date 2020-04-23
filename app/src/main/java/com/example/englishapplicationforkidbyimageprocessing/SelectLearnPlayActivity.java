package com.example.englishapplicationforkidbyimageprocessing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.englishapplicationforkidbyimageprocessing.Database.GameDatabaseHelper;
import com.example.englishapplicationforkidbyimageprocessing.Database.NgrokDatabaseHelper;
import com.example.englishapplicationforkidbyimageprocessing.Database.QuizGameDatabaseHelper;
import com.example.englishapplicationforkidbyimageprocessing.Model.QuestionsGame;
import com.example.englishapplicationforkidbyimageprocessing.Model.WordsGame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import info.hoang8f.widget.FButton;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SelectLearnPlayActivity extends AppCompatActivity {
    private FButton mStudyButton;
    private FButton mPlayButton;
    private FButton mInputButton;

    private FButton mVocabsButton;

    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;

    private NgrokDatabaseHelper mNgrokHelper;
    private GameDatabaseHelper mGameHelper;

    private QuizGameDatabaseHelper mQuizHelper;

    ArrayList<QuestionsGame> arrlist;
    ArrayList<WordsGame> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_learn_play);

        mStudyButton = findViewById(R.id.study_button);
        mPlayButton = findViewById(R.id.play_button);
        mInputButton = findViewById(R.id.btnurl);
        mVocabsButton = findViewById(R.id.btnVocabs);

        mInputButton.setShadowEnabled(true);
        mInputButton.setShadowHeight(20);
        mInputButton.setCornerRadius(20);

        mInputButton.setButtonColor(getResources().getColor(R.color.light_blue));
        mInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectLearnPlayActivity.this,InputURLActivity.class);
                startActivity(intent);
            }
        });

        mStudyButton.setShadowEnabled(true);
        mStudyButton.setShadowHeight(25);
        mStudyButton.setCornerRadius(25);

        mStudyButton.setButtonColor(getResources().getColor(R.color.purple));
        mStudyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectLearnPlayActivity.this,SelectCategoryActivity.class);
                startActivity(intent);
            }
        });

        mNgrokHelper = new NgrokDatabaseHelper(this);
        mGameHelper = new GameDatabaseHelper(this);
        mQuizHelper = new QuizGameDatabaseHelper(this);

        list = mGameHelper.getAllOfTheQuestions();
        arrlist = mQuizHelper.getAllOfTheQuestions();

        mPlayButton.setShadowEnabled(true);
        mPlayButton.setShadowHeight(25);
        mPlayButton.setCornerRadius(25);

        mPlayButton.setButtonColor(getResources().getColor(R.color.pink));
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckPermissions()) {

                        addDataToDatabase();
                        addQuestionToDB();
                        Intent intent = new Intent(SelectLearnPlayActivity.this,LevelPlayingActivity.class);
                        startActivity(intent);

                }else{
                    RequestPermissions();
                }
            }
        });
//        Log.i("SelectLP",wordsText);

        mVocabsButton.setShadowEnabled(true);
        mVocabsButton.setShadowHeight(20);
        mVocabsButton.setCornerRadius(20);

        mVocabsButton.setButtonColor(getResources().getColor(R.color.orange));
        mVocabsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectLearnPlayActivity.this,StorageVocabsActivity.class);
                startActivity(intent);
            }
        });
    }

    //Add Random data
    private void addDataToDatabase() {
        Log.d("PlayingActivity", "Inserting ..");

        String url = mNgrokHelper.getNgrokPath();


        //inserting vegetable...
        mGameHelper.addWordsQuestion(new WordsGame(1, "https://"+url+".ngrok.io/wordsapp/dataset/broccoli.php", "broccoli","s_broccoli"));
        mGameHelper.addWordsQuestion(new WordsGame(2, "https://"+url+".ngrok.io/wordsapp/dataset/cabbage.php", "cabbage","s_cabbage"));
        mGameHelper.addWordsQuestion(new WordsGame(3, "https://"+url+".ngrok.io/wordsapp/dataset/carrot.php", "carrot","s_carrot"));
        mGameHelper.addWordsQuestion(new WordsGame(4, "https://"+url+".ngrok.io/wordsapp/dataset/chili.php", "chili","s_chili"));
        mGameHelper.addWordsQuestion(new WordsGame(5, "https://"+url+".ngrok.io/wordsapp/dataset/cucumber.php", "cucumber","s_cucumber"));
        mGameHelper.addWordsQuestion(new WordsGame(6, "https://"+url+".ngrok.io/wordsapp/dataset/garlic.php", "garlic","s_garlic"));
        mGameHelper.addWordsQuestion(new WordsGame(7, "https://"+url+".ngrok.io/wordsapp/dataset/ginger.php", "ginger","s_ginger"));
        mGameHelper.addWordsQuestion(new WordsGame(8, "https://"+url+".ngrok.io/wordsapp/dataset/lemon.php", "lemon","s_lemon"));
        mGameHelper.addWordsQuestion(new WordsGame(9, "https://"+url+".ngrok.io/wordsapp/dataset/mushroom.php", "mushroom","s_mushroom"));
        mGameHelper.addWordsQuestion(new WordsGame(10, "https://"+url+".ngrok.io/wordsapp/dataset/onion.php", "onion","s_onion"));
        mGameHelper.addWordsQuestion(new WordsGame(11, "https://"+url+".ngrok.io/wordsapp/dataset/potato.php", "potato","s_potato"));
        mGameHelper.addWordsQuestion(new WordsGame(12, "https://"+url+".ngrok.io/wordsapp/dataset/whiteradish.php", "whiteradish","s_whiteradish"));

        //inserting fruit
        mGameHelper.addWordsQuestion(new WordsGame(13, "https://"+url+".ngrok.io/wordsapp/dataset/apple.php", "apple","s_apple"));
        mGameHelper.addWordsQuestion(new WordsGame(14, "https://"+url+".ngrok.io/wordsapp/dataset/banana.php", "banana","s_banana"));
        mGameHelper.addWordsQuestion(new WordsGame(15, "https://"+url+".ngrok.io/wordsapp/dataset/coconut.php", "coconut","s_coconut"));
        mGameHelper.addWordsQuestion(new WordsGame(16, "https://"+url+".ngrok.io/wordsapp/dataset/corn.php", "corn","s_corn"));
        mGameHelper.addWordsQuestion(new WordsGame(17, "https://"+url+".ngrok.io/wordsapp/dataset/grape.php", "grape","s_grape"));
        mGameHelper.addWordsQuestion(new WordsGame(18, "https://"+url+".ngrok.io/wordsapp/dataset/guava.php", "guava","s_guava"));
        mGameHelper.addWordsQuestion(new WordsGame(19, "https://"+url+".ngrok.io/wordsapp/dataset/kiwi.php", "kiwi","s_kiwi"));
        mGameHelper.addWordsQuestion(new WordsGame(20, "https://"+url+".ngrok.io/wordsapp/dataset/mango.php", "mango","s_mango"));
        mGameHelper.addWordsQuestion(new WordsGame(21, "https://"+url+".ngrok.io/wordsapp/dataset/orange.php", "orange","s_orange"));
        mGameHelper.addWordsQuestion(new WordsGame(22, "https://"+url+".ngrok.io/wordsapp/dataset/papaya.php", "papaya","s_papaya"));
        mGameHelper.addWordsQuestion(new WordsGame(23, "https://"+url+".ngrok.io/wordsapp/dataset/pepper.php", "pepper","s_pepper"));
        mGameHelper.addWordsQuestion(new WordsGame(24, "https://"+url+".ngrok.io/wordsapp/dataset/pineapple.php", "pineapple","s_pineapple"));
        mGameHelper.addWordsQuestion(new WordsGame(25, "https://"+url+".ngrok.io/wordsapp/dataset/pumpkin.php", "pumpkin","s_pumpkin"));
        mGameHelper.addWordsQuestion(new WordsGame(26, "https://"+url+".ngrok.io/wordsapp/dataset/strawberry.php", "strawberry","s_strawberry"));
        mGameHelper.addWordsQuestion(new WordsGame(27, "https://"+url+".ngrok.io/wordsapp/dataset/tomato.php", "tomato","s_tomato"));
        mGameHelper.addWordsQuestion(new WordsGame(28, "https://"+url+".ngrok.io/wordsapp/dataset/watermelon.php", "watermelon","s_watermelon"));

        //inserting animal
        mGameHelper.addWordsQuestion(new WordsGame(29, "https://"+url+".ngrok.io/wordsapp/dataset/ant.php", "ant","s_ant"));
        mGameHelper.addWordsQuestion(new WordsGame(30, "https://"+url+".ngrok.io/wordsapp/dataset/bear.php", "bear","s_bear"));
        mGameHelper.addWordsQuestion(new WordsGame(31, "https://"+url+".ngrok.io/wordsapp/dataset/bird.php", "bird","s_bird"));
        mGameHelper.addWordsQuestion(new WordsGame(32, "https://"+url+".ngrok.io/wordsapp/dataset/buffalo.php", "buffalo","s_buffalo"));
        mGameHelper.addWordsQuestion(new WordsGame(33, "https://"+url+".ngrok.io/wordsapp/dataset/butterfly.php", "butterfly","s_butterfly"));
        mGameHelper.addWordsQuestion(new WordsGame(34, "https://"+url+".ngrok.io/wordsapp/dataset/cat.php", "cat","s_cat"));
        mGameHelper.addWordsQuestion(new WordsGame(35, "https://"+url+".ngrok.io/wordsapp/dataset/chicken.php", "chicken","s_chicken"));
        mGameHelper.addWordsQuestion(new WordsGame(36, "https://"+url+".ngrok.io/wordsapp/dataset/cow.php", "cow","s_cow"));
        mGameHelper.addWordsQuestion(new WordsGame(37, "https://"+url+".ngrok.io/wordsapp/dataset/dog.php", "dog","s_dog"));
        mGameHelper.addWordsQuestion(new WordsGame(38, "https://"+url+".ngrok.io/wordsapp/dataset/duck.php", "duck","s_duck"));
        mGameHelper.addWordsQuestion(new WordsGame(39, "https://"+url+".ngrok.io/wordsapp/dataset/elephant.php", "elephant","s_elephant"));
        mGameHelper.addWordsQuestion(new WordsGame(40, "https://"+url+".ngrok.io/wordsapp/dataset/fish.php", "fish","s_fish"));
        mGameHelper.addWordsQuestion(new WordsGame(41, "https://"+url+".ngrok.io/wordsapp/dataset/giraffe.php", "giraffe","s_giraffe"));
        mGameHelper.addWordsQuestion(new WordsGame(42, "https://"+url+".ngrok.io/wordsapp/dataset/horse.php", "horse","s_horse"));
        mGameHelper.addWordsQuestion(new WordsGame(43, "https://"+url+".ngrok.io/wordsapp/dataset/monkey.php", "monkey","s_monkey"));
        mGameHelper.addWordsQuestion(new WordsGame(44, "https://"+url+".ngrok.io/wordsapp/dataset/pig.php", "pig","s_pig"));
        mGameHelper.addWordsQuestion(new WordsGame(45, "https://"+url+".ngrok.io/wordsapp/dataset/rabbit.php", "rabbit","s_rabbit"));
        mGameHelper.addWordsQuestion(new WordsGame(46, "https://"+url+".ngrok.io/wordsapp/dataset/sheep.php", "sheep","s_sheep"));
        mGameHelper.addWordsQuestion(new WordsGame(47, "https://"+url+".ngrok.io/wordsapp/dataset/shrimp.php", "shrimp","s_shrimp"));
        mGameHelper.addWordsQuestion(new WordsGame(48, "https://"+url+".ngrok.io/wordsapp/dataset/squid.php", "squid","s_squid"));
        mGameHelper.addWordsQuestion(new WordsGame(49, "https://"+url+".ngrok.io/wordsapp/dataset/squirrel.php", "squirrel","s_squirrel"));
        mGameHelper.addWordsQuestion(new WordsGame(50, "https://"+url+".ngrok.io/wordsapp/dataset/turtle.php", "turtle","s_turtle"));
    }

    void addQuestionToDB(){
//        ArrayList<QuestionsGame> arrlist = new ArrayList<>();

        String url = mNgrokHelper.getNgrokPath();

        mQuizHelper.addWordsQuestion(new QuestionsGame(1,"https://"+url+".ngrok.io/wordsapp/dataset/broccoli.php","broccoli","apple","banana","broccoli"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(2,"https://"+url+".ngrok.io/wordsapp/dataset/cabbage.php", "garlic","corn","cabbage","cabbage"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(3,"https://"+url+".ngrok.io/wordsapp/dataset/carrot.php", "onion","carrot","papaya","carrot"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(4, "https://"+url+".ngrok.io/wordsapp/dataset/chili.php", "chili","potato","apple","chili"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(5, "https://"+url+".ngrok.io/wordsapp/dataset/cucumber.php", "kiwi","cucumber","pepper","cucumber"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(6, "https://"+url+".ngrok.io/wordsapp/dataset/garlic.php", "papaya", "coconut","garlic","garlic"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(7, "https://"+url+".ngrok.io/wordsapp/dataset/ginger.php", "ginger","watermelon","fish","ginger"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(8, "https://"+url+".ngrok.io/wordsapp/dataset/lemon.php", "lemon","orange","coconut","lemon"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(9, "https://"+url+".ngrok.io/wordsapp/dataset/mushroom.php", "giraffe","mushroom","ant","mushroom"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(10, "https://"+url+".ngrok.io/wordsapp/dataset/onion.php","cat", "onion","grape","onion"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(11, "https://"+url+".ngrok.io/wordsapp/dataset/potato.php", "potato","guava","tomato","potato"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(12, "https://"+url+".ngrok.io/wordsapp/dataset/whiteradish.php", "whiteradish","carrot","onion","whiteradish"));

        //inserting fruit
        mQuizHelper.addWordsQuestion(new QuestionsGame(13, "https://"+url+".ngrok.io/wordsapp/dataset/apple.php", "orange","apple","banana","apple"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(14, "https://"+url+".ngrok.io/wordsapp/dataset/banana.php", "banana","cabbage","chili","banana"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(15, "https://"+url+".ngrok.io/wordsapp/dataset/coconut.php", "whiteradish","coconut","orange","coconut"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(16, "https://"+url+".ngrok.io/wordsapp/dataset/corn.php", "carrot","tomato","corn","corn"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(17, "https://"+url+".ngrok.io/wordsapp/dataset/grape.php","brocoli" ,"grape","corn","grape"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(18, "https://"+url+".ngrok.io/wordsapp/dataset/guava.php","carrot", "guava","chili","guava"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(19, "https://"+url+".ngrok.io/wordsapp/dataset/kiwi.php", "kiwi","onion","potato","kiwi"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(20, "https://"+url+".ngrok.io/wordsapp/dataset/mango.php", "orange","apple", "mango","mango"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(21, "https://"+url+".ngrok.io/wordsapp/dataset/orange.php","apple", "orange","bird","orange"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(22, "https://"+url+".ngrok.io/wordsapp/dataset/papaya.php", "onion","lemon", "papaya","papaya"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(23, "https://"+url+".ngrok.io/wordsapp/dataset/pepper.php","mango", "pepper","corn","pepper"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(24, "https://"+url+".ngrok.io/wordsapp/dataset/pineapple.php","fish", "pineapple","elephant","pineapple"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(25, "https://"+url+".ngrok.io/wordsapp/dataset/pumpkin.php", "horse","pineapple", "pumpkin","pumpkin"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(26, "https://"+url+".ngrok.io/wordsapp/dataset/strawberry.php", "strawberry","apple","banana","strawberry"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(27, "https://"+url+".ngrok.io/wordsapp/dataset/tomato.php","pig", "tomato","turtle","tomato"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(28, "https://"+url+".ngrok.io/wordsapp/dataset/watermelon.php","apple", "watermelon","fish","watermelon"));

        //inserting animal
        mQuizHelper.addWordsQuestion(new QuestionsGame(29, "https://"+url+".ngrok.io/wordsapp/dataset/ant.php", "ant","bird","dog","ant"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(30, "https://"+url+".ngrok.io/wordsapp/dataset/bear.php", "dog","rabbit", "bear","bear"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(31, "https://"+url+".ngrok.io/wordsapp/dataset/bird.php","corn", "bird","mushroom","bird"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(32, "https://"+url+".ngrok.io/wordsapp/dataset/buffalo.php","corn", "buffalo","apple","buffalo"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(33, "https://"+url+".ngrok.io/wordsapp/dataset/butterfly.php", "cat","fish", "butterfly","butterfly"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(34, "https://"+url+".ngrok.io/wordsapp/dataset/cat.php", "cat","rabbit","dog","cat"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(35, "https://"+url+".ngrok.io/wordsapp/dataset/chicken.php", "chicken","papaya","monkey","chicken"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(36, "https://"+url+".ngrok.io/wordsapp/dataset/cow.php", "pepper","cow","mango","cow"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(37, "https://"+url+".ngrok.io/wordsapp/dataset/dog.php", "shrimp","cat", "dog","dog"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(38, "https://"+url+".ngrok.io/wordsapp/dataset/duck.php","dog", "duck","rabbit","duck"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(39, "https://"+url+".ngrok.io/wordsapp/dataset/elephant.php","shrimp","cow", "elephant","elephant"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(40, "https://"+url+".ngrok.io/wordsapp/dataset/fish.php","shrimp", "fish","sheep","fish"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(41, "https://"+url+".ngrok.io/wordsapp/dataset/giraffe.php", "cow","giraffe","ant","giraffe"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(42, "https://"+url+".ngrok.io/wordsapp/dataset/horse.php", "horse","coconut","bird","horse"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(43, "https://"+url+".ngrok.io/wordsapp/dataset/monkey.php", "horse","monkey","fish","monkey"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(44, "https://"+url+".ngrok.io/wordsapp/dataset/pig.php", "pig","dog","duck","pig"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(45, "https://"+url+".ngrok.io/wordsapp/dataset/rabbit.php", "rabbit","tomato","papaya","rabbit"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(46, "https://"+url+".ngrok.io/wordsapp/dataset/sheep.php", "tomato","sheep","rabbit","sheep"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(47, "https://"+url+".ngrok.io/wordsapp/dataset/shrimp.php", "shrimp","carrot","apple","shrimp"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(48, "https://"+url+".ngrok.io/wordsapp/dataset/squid.php", "fish","squid","sheep","squid"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(49, "https://"+url+".ngrok.io/wordsapp/dataset/squirrel.php", "turtle","squirrel","monkey","squirrel"));
        mQuizHelper.addWordsQuestion(new QuestionsGame(50, "https://"+url+".ngrok.io/wordsapp/dataset/turtle.php", "turtle","shrimp","duck","turtle"));
//
    }
    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }
    private void RequestPermissions() {
        ActivityCompat.requestPermissions(SelectLearnPlayActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

}
