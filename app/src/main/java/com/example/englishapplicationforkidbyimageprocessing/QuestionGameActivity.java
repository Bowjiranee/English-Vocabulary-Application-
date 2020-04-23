package com.example.englishapplicationforkidbyimageprocessing;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.TimingLogger;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.englishapplicationforkidbyimageprocessing.Database.GameDatabaseHelper;
import com.example.englishapplicationforkidbyimageprocessing.Database.NgrokDatabaseHelper;
import com.example.englishapplicationforkidbyimageprocessing.Database.QuizGameDatabaseHelper;
import com.example.englishapplicationforkidbyimageprocessing.Model.QuestionsGame;
import com.example.englishapplicationforkidbyimageprocessing.Model.WordsGame;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.w3c.dom.Text;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import info.hoang8f.widget.FButton;

public class QuestionGameActivity extends AppCompatActivity {

    private FButton mButtonA;
    private FButton mButtonB;
    private FButton mButtonC;
    QuizGameDatabaseHelper quizHelper;
    QuestionsGame currectQuestion;
    ArrayList<QuestionsGame> list;
    int qid = 0;
    private FButton mButtonEnd;
    private TextView mLevel;

    private FButton mNext;
    private Random mRandom;
    private NgrokDatabaseHelper mNgrokHelper;
    private TextView mTextViewScore;
    private int scoreQuiz = 0;

    private MediaPlayer mp;

    private AlertDialog alertDialog;
    private  AlertDialog.Builder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_game);

        mLevel = findViewById(R.id.levelGame_quiz);

        mButtonA = findViewById(R.id.button_quiz_a);
        mButtonB = findViewById(R.id.button_quiz_b);
        mButtonC = findViewById(R.id.button_quiz_c);
        mTextViewScore = findViewById(R.id.scorePlayer_quiz);
        mNext = findViewById(R.id.next_button_quiz);

        mButtonA.setShadowEnabled(true);
        mButtonA.setShadowHeight(20);
        mButtonA.setCornerRadius(20);
        mButtonA.setButtonColor(getResources().getColor(R.color.cream));

        mButtonB.setShadowEnabled(true);
        mButtonB.setShadowHeight(20);
        mButtonB.setCornerRadius(20);
        mButtonA.setButtonColor(getResources().getColor(R.color.cream));

        mButtonC.setShadowEnabled(true);
        mButtonC.setShadowHeight(20);
        mButtonC.setCornerRadius(20);
        mButtonA.setButtonColor(getResources().getColor(R.color.cream));


        mNgrokHelper = new NgrokDatabaseHelper(this);
        mNgrokHelper.getReadableDatabase();

        quizHelper = new QuizGameDatabaseHelper(this);
        quizHelper.getWritableDatabase();

        if (quizHelper.getAllOfTheQuestions().size() == 0){
            quizHelper.wordsQuestion();
        }

        list = quizHelper.getAllOfTheQuestions();

        //get questions random
        Collections.shuffle(list);

        currectQuestion = list.get(qid);

        mLevel.setText(Integer.toString(qid));

        readDataFromDatabase();
//        nextImageQuestion();


        mButtonEnd = findViewById(R.id.end_button_quiz);

        mNext.setShadowEnabled(true);
        mNext.setShadowHeight(20);
        mNext.setCornerRadius(20);
        mNext.setButtonColor(getResources().getColor(R.color.light_green));

        mButtonEnd.setShadowEnabled(true);
        mButtonEnd.setShadowHeight(20);
        mButtonEnd.setCornerRadius(20);
        mButtonEnd.setButtonColor(getResources().getColor(R.color.red));

        mButtonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionGameActivity.this,ScoreEasyActivity.class);
                intent.putExtra("scores",String.valueOf(scoreQuiz));
                startActivity(intent);
                finish();
            }
        });

//        List<QuestionsGame> quizList = quizHelper.getAllOfTheQuestions();
//        for (QuestionsGame i : quizList){
//            Log.i("display1", "i : "+i.getQuestion()+" "+i.getAnswer() );
//            Log.i("display1", "current : "+currectQuestion.getQuestion()+" "+currectQuestion.getAnswer());
//        }

    }


    private void readDataFromDatabase() {

        List<QuestionsGame> wordsGamesList = quizHelper.getAllOfTheQuestions();

        mRandom = new Random();
        int n = mRandom.nextInt(52)+1; //random image 1 - 52

        for (QuestionsGame i : wordsGamesList) {

            if (n == i.getId()) {
//                TimingLogger timings = new TimingLogger("QuestionTimer", "methodA");
//                timings.addSplit("dowload image task");
//                timings.dumpToLog();
                new DownloadImageTask((ImageView) findViewById(R.id.image_view_quiz))
                        .execute(i.getQuestion()); //i.getURL


                mButtonA.setText(i.getOpta());
                mButtonB.setText(i.getOptb());
                mButtonC.setText(i.getOptc());

                final String text = i.getAnswer();

                Log.i("display2", i.getId()+" "+i.getAnswer()+" "+n+" "+text);

                mButtonA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("display3", mButtonA.getText().toString()+" "+text);

                            if (mButtonA.getText().toString().equals(text)){
                                mButtonA.setBackgroundColor(getResources().getColor(R.color.green));
                                showAlertDialog(R.layout.dialog_postive_layout);
                                scoreQuiz++;
                                mp = MediaPlayer.create(QuestionGameActivity.this,R.raw.correct_sound);
                                mp.start();
                                mButtonA.setEnabled(false);
                                mButtonB.setEnabled(false);
                                mButtonC.setEnabled(false);
                            }else{
                                mButtonA.setBackgroundColor(getResources().getColor(R.color.red));
                                showAlertDialog(R.layout.dialog_negative_layout);
                                mp = MediaPlayer.create(QuestionGameActivity.this,R.raw.wrong_sound);
                                mp.start();
                                mButtonA.setEnabled(false);
                                mButtonB.setEnabled(false);
                                mButtonC.setEnabled(false);
                            }

                        mTextViewScore.setText(String.valueOf(scoreQuiz));

                    }
                });

                mButtonB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("display3", mButtonB.getText().toString()+" "+text);

                            if (mButtonB.getText().toString().equals(text)) {
                                mButtonB.setBackgroundColor(getResources().getColor(R.color.green));
                                showAlertDialog(R.layout.dialog_postive_layout);
                                scoreQuiz++;
                                mp = MediaPlayer.create(QuestionGameActivity.this,R.raw.correct_sound);
                                mp.start();
                                mButtonA.setEnabled(false);
                                mButtonB.setEnabled(false);
                                mButtonC.setEnabled(false);
                            } else {
                                mButtonB.setBackgroundColor(getResources().getColor(R.color.red));
                                showAlertDialog(R.layout.dialog_negative_layout);
                                mp = MediaPlayer.create(QuestionGameActivity.this,R.raw.wrong_sound);
                                mp.start();
                                mButtonA.setEnabled(false);
                                mButtonB.setEnabled(false);
                                mButtonC.setEnabled(false);
                            }
                        mTextViewScore.setText(String.valueOf(scoreQuiz));

                    }
                });

                mButtonC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("display3", mButtonC.getText().toString()+" "+text);
                            if (mButtonC.getText().toString().equals(text)){
                                mButtonC.setBackgroundColor(getResources().getColor(R.color.green));
                                showAlertDialog(R.layout.dialog_postive_layout);
                                scoreQuiz++;
                                mp = MediaPlayer.create(QuestionGameActivity.this,R.raw.correct_sound);
                                mp.start();
                                mButtonA.setEnabled(false);
                                mButtonB.setEnabled(false);
                                mButtonC.setEnabled(false);
                            }else{
                                mButtonC.setBackgroundColor(getResources().getColor(R.color.red));
                                showAlertDialog(R.layout.dialog_negative_layout);
                                mp = MediaPlayer.create(QuestionGameActivity.this,R.raw.wrong_sound);
                                mp.start();
                                mButtonA.setEnabled(false);
                                mButtonB.setEnabled(false);
                                mButtonC.setEnabled(false);
                            }
                        mTextViewScore.setText(String.valueOf(scoreQuiz));

                    }
                });

                Log.i("display4", String.valueOf(scoreQuiz));

            }
        }

        qid++;
        mLevel.setText(Integer.toString(qid));

        nextImageQuestion();
    }

    private void nextImageQuestion() {
        mButtonA.setButtonColor(getResources().getColor(R.color.cream));
        mButtonB.setButtonColor(getResources().getColor(R.color.cream));
        mButtonC.setButtonColor(getResources().getColor(R.color.cream));

        mButtonA.setEnabled(true);
        mButtonB.setEnabled(true);
        mButtonC.setEnabled(true);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(qid < 10){
                    readDataFromDatabase();
                }else{
                    Intent intent = new Intent(QuestionGameActivity.this,ScoreEasyActivity.class);
                    intent.putExtra("scores",String.valueOf(scoreQuiz));
                    startActivity(intent);
                    finish();
                }
            }
        });


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

    private void showAlertDialog(int layout){
        dialogBuilder = new AlertDialog.Builder(QuestionGameActivity.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        ImageView dialogButton = layoutView.findViewById(R.id.exit_dialog);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
//        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        },1500);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}
