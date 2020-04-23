package com.example.englishapplicationforkidbyimageprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishapplicationforkidbyimageprocessing.Database.GameDatabaseHelper;
import com.example.englishapplicationforkidbyimageprocessing.Database.NgrokDatabaseHelper;
import com.example.englishapplicationforkidbyimageprocessing.Model.WavAudioRecorder;
import com.example.englishapplicationforkidbyimageprocessing.Model.WordsGame;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import info.hoang8f.widget.FButton;

public class EasyPlayingActivity extends AppCompatActivity {
    private FButton mNextButton;
    private FButton mEndButton;

    private TextView mLevel;
    private TextView mScore;
    private int scorePlayer = 0;

    private TextView mTestImageName;
    private TextView mTestSoundName;

    private ImageView mImageView;   //Random Question from server

    private GameDatabaseHelper mGameHelper;
    private NgrokDatabaseHelper mNgrokHelper;

    private String listenText;
    private TextToSpeech mTTS;

    int qid = 0;
    ArrayList<WordsGame> list;
    WordsGame currectWords;
    private Double distance;

    private int count = 1;
    private WavAudioRecorder mRecorder;
    ArrayList<Integer> countList;

    private Random mRandom;

    private int mCount = 3;

    private String mAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_playing);

        mNextButton = findViewById(R.id.easy_next_button1);
        mNextButton.setButtonColor(getResources().getColor(R.color.light_green));
        mEndButton = findViewById(R.id.easy_end_button1);

        mLevel = findViewById(R.id.easy_levelGame1);
        mScore = findViewById(R.id.easy_scorePlayer1);

        mNextButton.setShadowEnabled(true);
        mNextButton.setShadowHeight(20);
        mNextButton.setCornerRadius(20);

        mEndButton.setShadowEnabled(true);
        mEndButton.setShadowHeight(20);
        mEndButton.setCornerRadius(20);

        mEndButton.setButtonColor(getResources().getColor(R.color.orange));
        mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EasyPlayingActivity.this,ScoreEasyActivity.class);
                intent.putExtra("scores",String.valueOf(scorePlayer));
                startActivity(intent);
            }
        });

        //Test Name
//        mTestImageName = findViewById(R.id.image_text_view1);
//        mTestSoundName = findViewById(R.id.sound_text_view1);

        mGameHelper = new GameDatabaseHelper(this);
        list = mGameHelper.getAllOfTheQuestions();
        currectWords = list.get(qid);

        mNgrokHelper = new NgrokDatabaseHelper(this);
        mNgrokHelper.getReadableDatabase();

        //if not found data store "test" data in db
        if(mGameHelper.getAllOfTheQuestions().size() == 0){
            mGameHelper.wordsQuestion();
        }

//        mGameRandomHelper = new GameRandomDatabaseHelper(this);
//        randomList = mGameRandomHelper.getAllOfTheQuestions();
//
//        //if not found data store "test" data in db
//        if(mGameRandomHelper.getAllOfTheQuestions().size() == 0){
//            mGameRandomHelper.wordsQuestion();
//        }


        //========================= TTS ===========================//

        //initializing TTS
        mTTS = new TextToSpeech(getBaseContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    mTTS.setLanguage(Locale.US);
//                    mTTS.setSpeechRate(1.5f);

                    readDataFromDatabase(); //Read random data and save sound random TTS sound วางไว้ตรงนี้เพราะต้องบันทึกข้อเเรกด้วย
                }
            }
        });

        //qid = 1
        mLevel.setText(Integer.toString(qid));


        //====================================== SOUND ANSWER ===================================================//


        final String mRcordFilePath = Environment.getExternalStorageDirectory()+"/record_"+ qid +"_" + listenText + ".wav";
        Log.i("mRcordFilePath",mRcordFilePath); //qid = 1

        mRecorder = WavAudioRecorder.getInstanse();
        mRecorder.setOutputFile(mRcordFilePath);

        //Touch button speak game
        findViewById(R.id.easy_button_speak_game1).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    mRecorder = WavAudioRecorder.getInstanse();
                    mRecorder.setOutputFile(mRcordFilePath);
                    try{
                        mRecorder.prepare();
                        mRecorder.start();  // Recording is now started
                    }catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                    Log.i("stateMain", "Recording:Calling ");

                    Toast.makeText(EasyPlayingActivity.this, "กำลังบันทึกเสียง...", Toast.LENGTH_SHORT).show();

                }else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    if (mRecorder != null){
                        mRecorder.stop();
                        mRecorder.release();  // You can reuse the object by going back to setAudioSource() step
                        mRecorder = null;
                        Log.i("stateMain", "stopRecording:Calling ");
//                        Toast.makeText(PlayingActivity.this, "บันทึกเสียงเรียบร้อยเเล้ว", Toast.LENGTH_SHORT).show();
                    }
//                    uploadUserGameSound(view);
                    uploadTTSSound(view);
                    return true;
                }
                return false;
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mRecorder) {
            mRecorder.release();
        }
    }

    //====================================== RANDOM IMAGE AND SHOW IMAGE-VIEW ===================================================//

    private void readDataFromDatabase() {

        // Reading all students
        Log.d("PlayingActivity", "Reading all Words..");
        List<WordsGame> wordsGamesList = mGameHelper.getAllOfTheQuestions();

        mRandom = new Random();
        int n = mRandom.nextInt(52)+1; //random image 1 - 52

        for (WordsGame i : wordsGamesList) {

            if (n == i.getId()) {

                listenText = i.getSound();  //listenText = name sound ที่ random ได้

                Log.i("getSoundtest","listenText : "+ i.getSound());

                HashMap<String, String> myHashRender = new HashMap<String, String>();
                myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
                        listenText);

                File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
                path.mkdirs();
                String tempFilename = Integer.toString(count).concat("_").concat(listenText.concat(".wav"));  //save words system to sdCard
                String tempDestFile = path.getAbsolutePath() + "/" + tempFilename;
                mTTS.synthesizeToFile(listenText, myHashRender, tempDestFile);

                count++;

                new DownloadImageTask((ImageView) findViewById(R.id.easy_game_player1_image_view))
                        .execute(i.getImage());
                //===========================================================================================//
//                mGameRandomHelper.addWordsQuestion(new WordsRandomGame(qid,"test_"+listenText,""));

                mAnswer = i.getSound();
            }
        }   //end - if

        qid++;
        mLevel.setText(Integer.toString(qid));
        soundNextQuestion(qid);

//        Log.i("getSoundtest",Integer.toString(qid));
    }



    private void soundNextQuestion(int num){ //next question can save record order by 2,3,4 ...

        final String mRcordFilePath = Environment.getExternalStorageDirectory()+"/record_"+ qid +"_" + listenText + ".wav";
        mRecorder = WavAudioRecorder.getInstanse();
        mRecorder.setOutputFile(mRcordFilePath);

        Log.i("mRcordFilePath",mRcordFilePath); //qid = 1

        findViewById(R.id.easy_button_speak_game1).setBackgroundResource(R.drawable.mic_enable);
        findViewById(R.id.easy_button_speak_game1).setEnabled(true);
        findViewById(R.id.easy_button_speak_game1).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                findViewById(R.id.easy_check_sound_image_game1).setBackgroundResource(0);

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(EasyPlayingActivity.this, "กำลังบันทึกเสียง...", Toast.LENGTH_SHORT).show();

                    mRecorder = WavAudioRecorder.getInstanse();
                    mRecorder.setOutputFile(mRcordFilePath);
                    try{
                        mRecorder.prepare();
                        mRecorder.start();  // Recording is now started
                    }catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                    Log.i("stateMain", "Recording:Calling ");


                }else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    if (mRecorder != null){
                        mRecorder.stop();
                        mRecorder.release();  // You can reuse the object by going back to setAudioSource() step
                        mRecorder = null;
                        Log.i("stateMain", "stopRecording:Calling ");
//                        Toast.makeText(PlayingActivity.this, "บันทึกเสียงเรียบร้อยเเล้ว", Toast.LENGTH_SHORT).show();
                    }
                    uploadTTSSound(view);
                    return true;
                }
                return false;
            }
        });
    }

    public void btnClick(View view) {
        findViewById(R.id.easy_check_sound_image_game1).setBackgroundResource(0);

        if(qid < 10){
            currectWords = list.get(qid);
            readDataFromDatabase();
        }else{
            Intent intent = new Intent(EasyPlayingActivity.this,ScoreEasyActivity.class);
            intent.putExtra("scores",String.valueOf(scorePlayer));
            startActivity(intent);
            finish();
        }

    }


    //for show bitmap random image
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    //====================================== UPLOAD PROCESS AND CHECK SOUND  ===================================================//

    private void uploadTTSSound(final View view) {  //send record_system

        Toast.makeText(EasyPlayingActivity.this,"รอสักครู่นะคะ...",Toast.LENGTH_SHORT).show();
        String path = (Environment.getExternalStorageDirectory() + "/"+qid+"_"+listenText+".wav");
        Log.i("display4", listenText+" path > "+path);
        String url = "https://" + mNgrokHelper.getNgrokPath() + ".ngrok.io" + "/wordsapp/sound/upload-tts-learn.php";

        Ion.with(this)
                .load(url)
                .setMultipartFile("upload_file", new File(path))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
//                            mTestSoundName.setText(result);
                        uploadUserGameSound(view);
                    }
                });
//        }
    }

    private void uploadUserGameSound(View view){    //send record_user
        String path = (Environment.getExternalStorageDirectory() +"/record_"+ qid +"_" + listenText + ".wav");
        String url = "https://" + mNgrokHelper.getNgrokPath() + ".ngrok.io" + "/wordsapp/sound/easy_upload-user-game.php";

        Ion.with(this)
                .load(url)
                .setMultipartFile("upload_file", new File(path))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        processSpeechRecognize();
                        //       mTestSoundName.setText(result);
                    }
                });
    }

    private void processSpeechRecognize() {
//        Toast.makeText(getBaseContext(), "ระบบกำลังประมวลผลเสียง...", Toast.LENGTH_LONG).show();

        String url = "https://" + mNgrokHelper.getNgrokPath() + ".ngrok.io" + "/wordsapp/sound/run-google-speech-recognition.php";

        Ion.with(this)
                .load(url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
//                        processDTW();
//                        mTestSoundName.setText(result);
                        checkSound(result);
                    }
                });
    }

    private void checkSound(String result) {

//        mTestSoundName.setText("correct answer "+mAnswer);
//        mTestImageName.setText("result "+result);

        if (mAnswer.equals(result.trim())){
            findViewById(R.id.easy_check_sound_image_game1).setBackgroundResource(R.drawable.corret);
            scorePlayer++;
        }else{
            findViewById(R.id.easy_check_sound_image_game1).setBackgroundResource(R.drawable.wrong);
        }
        mScore.setText(String.valueOf(scorePlayer));
//        Log.i("scorePlayer",mAnswer+"||"+result+" "+String.valueOf(scorePlayer));


        findViewById(R.id.easy_button_speak_game1).setBackgroundResource(R.drawable.mic_disable);
        findViewById(R.id.easy_button_speak_game1).setEnabled(false);

    }
}
