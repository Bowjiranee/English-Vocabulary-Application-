package com.example.englishapplicationforkidbyimageprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishapplicationforkidbyimageprocessing.Database.GameDatabaseHelper;
import com.example.englishapplicationforkidbyimageprocessing.Database.NgrokDatabaseHelper;
import com.example.englishapplicationforkidbyimageprocessing.Model.WavAudioRecorder;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class LearningImageProcessingActivity extends AppCompatActivity {

    private FButton mNextButton;
    private FButton mEndButton;

    private TextView mWordStudyTextView;
    private String listenText;
    private TextToSpeech mTTS;
    private Button mButtonListen;

    private static final int PERMISSION_RECORD_AUDIO = 0;
//    private RecordWaveTask recordTask = null;
    private WavAudioRecorder mRecorder;

    private NgrokDatabaseHelper mHelper;
    private GameDatabaseHelper mGameHelper;

    private String text;
    private Double distance;

    private String mAnswer;
    int qid = 1;

    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_image_processing);

        mWordStudyTextView = findViewById(R.id.study_text_view);
        mNextButton = findViewById(R.id.next_button);
        mEndButton = findViewById(R.id.end_button);
        mButtonListen = findViewById(R.id.button_listen);


        mHelper = new NgrokDatabaseHelper(this);
        mGameHelper = new GameDatabaseHelper(this);
        showImage();

//        showWords();
        mNextButton.setButtonColor(getResources().getColor(R.color.light_green));
        mNextButton.setShadowEnabled(true);
        mNextButton.setShadowHeight(25);
        mNextButton.setCornerRadius(25);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LearningImageProcessingActivity.this,CameraActivity.class);
                startActivity(intent);
            }
        });

        mEndButton.setButtonColor(getResources().getColor(R.color.red));
        mEndButton.setShadowEnabled(true);
        mEndButton.setShadowHeight(25);
        mEndButton.setCornerRadius(25);
        mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LearningImageProcessingActivity.this,SelectCategoryActivity.class);
                startActivity(intent);
            }
        });

        mTTS = new TextToSpeech(getBaseContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    mTTS.setLanguage(Locale.US);
//                    mTTS.setSpeechRate(1.0);
                }
            }
        });

        //Start screen this disable button
//        findViewById(R.id.button_speak_study).setEnabled(false);
        findViewById(R.id.button_speak_study).setVisibility(View.GONE);

        findViewById(R.id.text_view_speak1).setVisibility(View.GONE);

        mButtonListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.button_speak_study).setVisibility(View.VISIBLE);
                findViewById(R.id.text_view_speak1).setVisibility(View.VISIBLE);

                String toSpeak = mWordStudyTextView.getText().toString();
//                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                mTTS.speak(toSpeak, TextToSpeech.QUEUE_ADD, null); //speak words

                //Save file to mySDCards
                listenText = mWordStudyTextView.getText().toString();

                HashMap<String, String> myHashRender = new HashMap<String, String>();
                myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
                        listenText);

                File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/");
                path.mkdirs();
                String tempFilename = "words-english.wav";  //save words system to sdCard
                String tempDestFile = path.getAbsolutePath() + "/" + tempFilename;
                mTTS.synthesizeToFile(listenText, myHashRender, tempDestFile);
//                mGameHelper.addWordsSound(tempDestFile);

                findViewById(R.id.button_speak_study).setBackgroundResource(R.drawable.mic_enable);
                findViewById(R.id.button_speak_study).setEnabled(true);
            }
        });

        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy", Locale.KOREA);
        Date now = new Date();
//        File wavFile = new File(Environment.getExternalStorageDirectory(), "/study_record_" + formatter.format(now) + ".wav");
//        final String path = Environment.getExternalStorageDirectory()+"/study_record_"+formatter.format(now)+".wav";
        final String path = (Environment.getExternalStorageDirectory() +"/record_"+ "words-english-user" + ".wav");

        mRecorder = WavAudioRecorder.getInstanse();
        mRecorder.setOutputFile(path);

        //Recording sounds to storage
        findViewById(R.id.button_speak_study).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                findViewById(R.id.checkSoundImage).setBackgroundResource(0);

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(LearningImageProcessingActivity.this, "กำลังบันทึกเสียง...", Toast.LENGTH_SHORT).show();
                    mRecorder = WavAudioRecorder.getInstanse();
                    mRecorder.setOutputFile(path);
                    try{
                        mRecorder.prepare();
                        mRecorder.start();  // Recording is now started
                    }catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
//                    Log.i("stateMain", "Recording:Calling ");


                }else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    if (mRecorder != null){
                        mRecorder.stop();
                        mRecorder.release();  // You can reuse the object by going back to setAudioSource() step
                        mRecorder = null;
//                        Log.i("stateMain", "stopRecording:Calling ");
//                        Toast.makeText(LearningImageProcessingActivity.this, "บันทึกเสียงเรียบร้อยเเล้ว", Toast.LENGTH_SHORT).show();
                    }
                    uploadSound1(view);
                    return true;
                }
                return false;
            }
        });

//        Log.i("testt",qid+" "+listenText+" "+path);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mRecorder) {
            mRecorder.release();
        }
    }

    //Display Words Images
    private void showImage() {
        OutputStream out = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm", Locale.KOREA);
        Date now = new Date();
        String path = (Environment.getExternalStorageDirectory()+"/"+"words_"+formatter.format(now)+".jpg");

        ImageView image = findViewById(R.id.study_image_view);
        Matrix matrix = new Matrix();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bm = BitmapFactory.decodeFile(path);
        Bitmap rotated = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),matrix,true);
        if(bm == null){
            Toast.makeText(LearningImageProcessingActivity.this,"โหลดภาพไม่ขึ้น!!",Toast.LENGTH_SHORT).show();
        }
        try {
            out = new FileOutputStream(path);
            rotated.compress(Bitmap.CompressFormat.JPEG, 100, out);
            image.setImageBitmap(rotated);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bundle bundle = getIntent().getExtras();
        text = bundle.getString("results");
        if(text != null){
            mWordStudyTextView.setText(text);
        }else{
            mWordStudyTextView.setText("Not Found");
        }
//        mGameHelper.addImageSoundPath(path,text.trim()); //path image and path sound to database
    }


    //Upload Sound 2 sounds to Server
    private void uploadSound1(final View view) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy", Locale.KOREA);
        Date now = new Date();
        String path = (Environment.getExternalStorageDirectory()+"/words-english.wav");
        String url = "https://"+mHelper.getNgrokPath()+".ngrok.io"+"/wordsapp/sound/upload-tts-learn.php";
        Ion.with(this)
                .load(url)
                .setMultipartFile("upload_file", new File(path))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        uploadSound2(view); //thiss error!!
                    }
                });
    }

    private void uploadSound2(View view){
        Toast.makeText(getBaseContext(), "รอสักครู่นะคะ...", Toast.LENGTH_SHORT).show();
        String path = (Environment.getExternalStorageDirectory() +"/record_words-english-user.wav");

        String url = "https://"+mHelper.getNgrokPath()+".ngrok.io"+"/wordsapp/sound/upload-user-learn.php";
        Ion.with(this)
                .load(url)
                .setMultipartFile("upload_file", new File(path))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        processSpeechRecognize();
                    }
                });
    }

//    private void processDTW() {
//        Toast.makeText(getBaseContext(), "ระบบกำลังตรวจสอบเสียง...", Toast.LENGTH_LONG).show();
//        String url = "https://"+mHelper.getNgrokPath()+".ngrok.io"+"/wordsapp/sound/runsounds.php";
//        Ion.with(this)
//                .load(url)
//                .asString()
//                .setCallback(new FutureCallback<String>() {
//                    @Override
//                    public void onCompleted(Exception e, String result) {
////                        TextView textResult = findViewById(R.id.testTextView);
////                        textResult.setText(result);
//                        distance = Double.parseDouble(result); //แปลงค่า string to double
//                        checkSound();
//                    }
//                });
//    }

//    private void checkSound(){
//        if(distance > 270){
//            findViewById(R.id.checkSoundImage).setBackgroundResource(R.drawable.wrong);
//        }else{
//            findViewById(R.id.checkSoundImage).setBackgroundResource(R.drawable.corret);
//        }
//    }

    private void processSpeechRecognize() {

        String url = "https://" + mHelper.getNgrokPath() + ".ngrok.io" + "/wordsapp/sound/runspeech-recognition-learn.php";

        Ion.with(this)
                .load(url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        checkSound(result);
                    }
                });
    }

    private void checkSound(String result) {
        Log.i("testt",text.trim()+" || "+result.trim());
//        Toast.makeText(LearningImageProcessingActivity.this,"result: "+result.trim(),Toast.LENGTH_SHORT).show();
        //error mAnswer = null!
        if (text.trim().equalsIgnoreCase(result.trim())){
            findViewById(R.id.checkSoundImage).setBackgroundResource(R.drawable.corret);
            mp = MediaPlayer.create(LearningImageProcessingActivity.this,R.raw.correct_sound);
            mp.start();
        }else{
            findViewById(R.id.checkSoundImage).setBackgroundResource(R.drawable.wrong);
            mp = MediaPlayer.create(LearningImageProcessingActivity.this,R.raw.wrong_sound);
            mp.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_RECORD_AUDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
//                    audioRecorderReady();
                } else {
                    // Permission denied
                    Toast.makeText(this, "\uD83D\uDE41", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
