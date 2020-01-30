package com.example.englishapplicationforkidbyimageprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishapplicationforkidbyimageprocessing.Database.GameDatabaseHelper;
import com.example.englishapplicationforkidbyimageprocessing.Database.NgrokDatabaseHelper;
import com.example.englishapplicationforkidbyimageprocessing.Model.WordsGame;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import info.hoang8f.widget.FButton;

public class Playing2Activity extends AppCompatActivity {

    private FButton mNextButton;
    private FButton mEndButton;

    private TextView mLevel;
    private TextView mScore;
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
    private int mCounter = 1;
    private Double distance;

    private static final int PERMISSION_RECORD_AUDIO = 1;
    private RecordWaveTask recordTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing2);

        //initializing tts
        mTTS = new TextToSpeech(getBaseContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    mTTS.setLanguage(Locale.UK);
                    mTTS.setSpeechRate(1.5f);
                }
            }
        });

        mNextButton = findViewById(R.id.next_button2);
        mNextButton.setButtonColor(getResources().getColor(R.color.light_green));
        mEndButton = findViewById(R.id.end_button2);

        mLevel = findViewById(R.id.levelGame2);
        mScore = findViewById(R.id.scorePlayer2);

        mEndButton.setButtonColor(getResources().getColor(R.color.orange));
        mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Playing2Activity.this,SelectLearnPlayActivity.class);
                startActivity(intent);
            }
        });

        //Test Name
        mTestImageName = findViewById(R.id.image_text_view2);
        mTestSoundName = findViewById(R.id.sound_text_view2);

        mGameHelper = new GameDatabaseHelper(this);
        mNgrokHelper = new NgrokDatabaseHelper(this);

        //Add and Read Data to Database
        addDataToDatabase();
        readDataFromDatabase();

        //counter level initialize
        mLevel.setText("not next : " + Integer.toString(1));


        //create button next click to next data and count data < 11
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLevel.setText("next : " + Integer.toString(mCounter));

                if(qid < list.size()-1){
                    qid++;
                    currectWords = list.get(qid);
                    readDataFromDatabase();
                    Log.i("mCounter",Integer.toString(mCounter));
                    if(mCounter == 12){
                        Toast.makeText(Playing2Activity.this,"next x = 10",Toast.LENGTH_SHORT).show();
                        mNextButton.setButtonColor(getResources().getColor(R.color.white));
                        Intent intent = new Intent(Playing2Activity.this,ScorePlayer2Activity.class);
                        startActivity(intent);
                        Playing2Activity.this.finish(); //close activity to select character
                    }
                }
            }
        });

//        if(mCounter == 4){
//            Intent intent = new Intent(Playing2Activity.this,ScorePlayer2Activity.class);
//            startActivity(intent);
//        }

        list = mGameHelper.getAllOfTheQuestions();  //read all data
        currectWords = list.get(qid);

        //if not data store "test" data in db
        if(mGameHelper.getAllOfTheQuestions().size() == 0){
            mGameHelper.wordsQuestion();
        }

        //====================================== SOUND ANSWER ===================================================//
        //Touch button speak game
//        findViewById(R.id.button_speak_study).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
//                    Toast.makeText(getApplicationContext(), "กำลังบันทึกเสียง...", Toast.LENGTH_LONG).show(); //กดค้าง ปล่อยคือบันทึก
//                    // Permission already available
//                    audioRecorderReady();
//
//                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
//
//                    if (!recordTask.isCancelled() && recordTask.getStatus() == AsyncTask.Status.RUNNING) {
//                        recordTask.cancel(false);
//                        Toast.makeText(getApplicationContext(), "บันทึกเสียงเรียบร้อยเเล้ว...", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(Playing2Activity.this, "ไม่สามารถบันทึกเสียงได้!!", Toast.LENGTH_SHORT).show();
//                    }
//////                    uploadEngineSound();
////                    uploadUserSound();
//                }
//                return true;
//            }
//        });

        // Restore the previous task or create a new one if necessary
        recordTask = (RecordWaveTask) getLastCustomNonConfigurationInstance();
        if (recordTask == null) {
            recordTask = new RecordWaveTask(this);
        } else {
            recordTask.setContext(this);
        }

    }

    //Read data for show and save TTS
    private void readDataFromDatabase() {
        Toast.makeText(Playing2Activity.this, "กำลังหาไฟล์จากดาต้าเบส...", Toast.LENGTH_LONG).show();

        // Reading all students
        Log.d("Playing2Activity", "Reading all Words..");
        List<WordsGame> wordsGamesList = mGameHelper.getAllOfTheQuestions();
        Random rand = new Random();
        int n = rand.nextInt(52)+1;

        for (WordsGame i : wordsGamesList) {
            String log = "Id: " + i.getId() + " ,Image: " + i.getImage() + " ,Sound: " + i.getSound() + " ,Sound: " + i.getScore();
            Log.d("Playing2Activity", log);

            if (n == i.getId()) {

                //Save sound from db to storage for input server
                listenText = i.getSound();
                HashMap<String, String> myHashRender = new HashMap<String, String>();
                myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
                        listenText);

                File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
                path.mkdirs();
                String tempFilename = listenText + ".wav";  //save words system to sdCard
                String tempDestFile = path.getAbsolutePath() + "/" + tempFilename;
                mTTS.synthesizeToFile(listenText, myHashRender, tempDestFile);

                new DownloadImageTask((ImageView) findViewById(R.id.game_player2_image_view))
                        .execute(i.getImage());

                Toast.makeText(Playing2Activity.this, "เเสดงรูป "+i.getSound(), Toast.LENGTH_LONG).show();
                mTestImageName.setText(i.getSound());

//                uploadEngineSound(i.getSound());
            }
        }
        mCounter++;
//        if(mCounter > 11){
//            Intent intent = new Intent(PlayingActivity.this,ScoreActivity.class);
//            startActivity(intent);
//        }

    }

    //for show bitmap image
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

    //Add data
    private void addDataToDatabase() {
        Log.d("MainActivity", "Inserting ..");

        Toast.makeText(Playing2Activity.this, "Inserting...", Toast.LENGTH_LONG).show();

        String url = mNgrokHelper.getNgrokPath();

        //inserting vegetable...
        mGameHelper.addWordsQuestion(new WordsGame(1, "https://"+url+".ngrok.io/wordsapp/dataset/beansprouts.php", "Bean Sprouts",""));
        mGameHelper.addWordsQuestion(new WordsGame(2, "https://"+url+".ngrok.io/wordsapp/dataset/broccoli.php", "Broccoli",""));
        mGameHelper.addWordsQuestion(new WordsGame(3, "https://"+url+".ngrok.io/wordsapp/dataset/cabbage.php", "Cabbage",""));
        mGameHelper.addWordsQuestion(new WordsGame(4, "https://"+url+".ngrok.io/wordsapp/dataset/carrot.php", "Carrot",""));
        mGameHelper.addWordsQuestion(new WordsGame(5, "https://"+url+".ngrok.io/wordsapp/dataset/chilli.php", "Chilli",""));
        mGameHelper.addWordsQuestion(new WordsGame(6, "https://"+url+".ngrok.io/wordsapp/dataset/cucumber.php", "Cucumber",""));
        mGameHelper.addWordsQuestion(new WordsGame(7, "https://"+url+".ngrok.io/wordsapp/dataset/garlic.php", "Garlic",""));
        mGameHelper.addWordsQuestion(new WordsGame(8, "https://"+url+".ngrok.io/wordsapp/dataset/ginger.php", "Ginger",""));
        mGameHelper.addWordsQuestion(new WordsGame(9, "https://"+url+".ngrok.io/wordsapp/dataset/kale.php", "Kale",""));
        mGameHelper.addWordsQuestion(new WordsGame(10, "https://"+url+".ngrok.io/wordsapp/dataset/lemon.php", "Lemon",""));
        mGameHelper.addWordsQuestion(new WordsGame(11, "https://"+url+".ngrok.io/wordsapp/dataset/mushroom.php", "Mushroom",""));
        mGameHelper.addWordsQuestion(new WordsGame(12, "https://"+url+".ngrok.io/wordsapp/dataset/onion.php", "Onion",""));
        mGameHelper.addWordsQuestion(new WordsGame(13, "https://"+url+".ngrok.io/wordsapp/dataset/potato.php", "Potato",""));
        mGameHelper.addWordsQuestion(new WordsGame(14, "https://"+url+".ngrok.io/wordsapp/dataset/whiteradish.php", "White Radish",""));

        //inserting fruit
        mGameHelper.addWordsQuestion(new WordsGame(15, "https://"+url+".ngrok.io/wordsapp/dataset/apple.php", "Apple",""));
        mGameHelper.addWordsQuestion(new WordsGame(16, "https://"+url+".ngrok.io/wordsapp/dataset/banana.php", "Banana",""));
        mGameHelper.addWordsQuestion(new WordsGame(17, "https://"+url+".ngrok.io/wordsapp/dataset/coconut.php", "Coconut",""));
        mGameHelper.addWordsQuestion(new WordsGame(18, "https://"+url+".ngrok.io/wordsapp/dataset/corn.php", "Corn",""));
        mGameHelper.addWordsQuestion(new WordsGame(19, "https://"+url+".ngrok.io/wordsapp/dataset/grape.php", "Grape",""));
        mGameHelper.addWordsQuestion(new WordsGame(20, "https://"+url+".ngrok.io/wordsapp/dataset/guava.php", "Guava",""));
        mGameHelper.addWordsQuestion(new WordsGame(21, "https://"+url+".ngrok.io/wordsapp/dataset/kiwi.php", "Kiwi",""));
        mGameHelper.addWordsQuestion(new WordsGame(22, "https://"+url+".ngrok.io/wordsapp/dataset/mango.php", "Mango",""));
        mGameHelper.addWordsQuestion(new WordsGame(23, "https://"+url+".ngrok.io/wordsapp/dataset/orange.php", "Orange",""));
        mGameHelper.addWordsQuestion(new WordsGame(24, "https://"+url+".ngrok.io/wordsapp/dataset/papaya.php", "Papaya",""));
        mGameHelper.addWordsQuestion(new WordsGame(25, "https://"+url+".ngrok.io/wordsapp/dataset/pepper.php", "Pepper",""));
        mGameHelper.addWordsQuestion(new WordsGame(26, "https://"+url+".ngrok.io/wordsapp/dataset/pineapple.php", "Pineapple",""));
        mGameHelper.addWordsQuestion(new WordsGame(27, "https://"+url+".ngrok.io/wordsapp/dataset/pumpkin.php", "Pumpkin",""));
        mGameHelper.addWordsQuestion(new WordsGame(28, "https://"+url+".ngrok.io/wordsapp/dataset/strawberry.php", "Strawberry",""));
        mGameHelper.addWordsQuestion(new WordsGame(29, "https://"+url+".ngrok.io/wordsapp/dataset/tomato.php", "Tomato",""));
        mGameHelper.addWordsQuestion(new WordsGame(30, "https://"+url+".ngrok.io/wordsapp/dataset/watermelon.php", "Watermelon",""));

        //inserting animal
        mGameHelper.addWordsQuestion(new WordsGame(31, "https://"+url+".ngrok.io/wordsapp/dataset/ant.php", "Ant",""));
        mGameHelper.addWordsQuestion(new WordsGame(32, "https://"+url+".ngrok.io/wordsapp/dataset/bear.php", "Bear",""));
        mGameHelper.addWordsQuestion(new WordsGame(33, "https://"+url+".ngrok.io/wordsapp/dataset/bird.php", "Bird",""));
        mGameHelper.addWordsQuestion(new WordsGame(34, "https://"+url+".ngrok.io/wordsapp/dataset/buffalo.php", "Buffalo",""));
        mGameHelper.addWordsQuestion(new WordsGame(35, "https://"+url+".ngrok.io/wordsapp/dataset/butterfly.php", "Butterfly",""));
        mGameHelper.addWordsQuestion(new WordsGame(36, "https://"+url+".ngrok.io/wordsapp/dataset/cat.php", "Cat",""));
        mGameHelper.addWordsQuestion(new WordsGame(37, "https://"+url+".ngrok.io/wordsapp/dataset/chicken.php", "Chicken",""));
        mGameHelper.addWordsQuestion(new WordsGame(38, "https://"+url+".ngrok.io/wordsapp/dataset/cow.php", "Cow",""));
        mGameHelper.addWordsQuestion(new WordsGame(39, "https://"+url+".ngrok.io/wordsapp/dataset/dog.php", "Dog",""));
        mGameHelper.addWordsQuestion(new WordsGame(40, "https://"+url+".ngrok.io/wordsapp/dataset/duck.php", "Duck",""));
        mGameHelper.addWordsQuestion(new WordsGame(41, "https://"+url+".ngrok.io/wordsapp/dataset/elephant.php", "Elephant",""));
        mGameHelper.addWordsQuestion(new WordsGame(42, "https://"+url+".ngrok.io/wordsapp/dataset/fish.php", "Fish",""));
        mGameHelper.addWordsQuestion(new WordsGame(43, "https://"+url+".ngrok.io/wordsapp/dataset/giraffe.php", "Giraffe",""));
        mGameHelper.addWordsQuestion(new WordsGame(44, "https://"+url+".ngrok.io/wordsapp/dataset/horse.php", "Horse",""));
        mGameHelper.addWordsQuestion(new WordsGame(45, "https://"+url+".ngrok.io/wordsapp/dataset/monkey.php", "Monkey",""));
        mGameHelper.addWordsQuestion(new WordsGame(46, "https://"+url+".ngrok.io/wordsapp/dataset/pig.php", "Pig",""));
        mGameHelper.addWordsQuestion(new WordsGame(47, "https://"+url+".ngrok.io/wordsapp/dataset/rabbit.php", "Rabbit",""));
        mGameHelper.addWordsQuestion(new WordsGame(48, "https://"+url+".ngrok.io/wordsapp/dataset/sheep.php", "Sheep",""));
        mGameHelper.addWordsQuestion(new WordsGame(49, "https://"+url+".ngrok.io/wordsapp/dataset/shrimp.php", "Shrimp",""));
        mGameHelper.addWordsQuestion(new WordsGame(50, "https://"+url+".ngrok.io/wordsapp/dataset/squid.php", "Squid",""));
        mGameHelper.addWordsQuestion(new WordsGame(51, "https://"+url+".ngrok.io/wordsapp/dataset/squirrel.php", "Squirrel",""));
        mGameHelper.addWordsQuestion(new WordsGame(52, "https://"+url+".ngrok.io/wordsapp/dataset/turtle.php", "Turtle",""));
    }

    //====================================== SOUND ANSWER ===================================================//

    private void audioRecorderReady() {
        switch (recordTask.getStatus()) {
            case RUNNING:
                Toast.makeText(this, "Task already running...", Toast.LENGTH_SHORT).show();
                return;
            case FINISHED:
                recordTask = new RecordWaveTask(this);
                break;
            case PENDING:
                if (recordTask.isCancelled()) {
                    recordTask = new RecordWaveTask(this);
                }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy", Locale.KOREA);
        Date now = new Date();
        File wavFile = new File(Environment.getExternalStorageDirectory(),"/game_record_"+formatter.format(now)+".wav");
//        Toast.makeText(this, wavFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        recordTask.execute(wavFile);
    }
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        recordTask.setContext(null);
        return recordTask;
    }
    private static class RecordWaveTask extends AsyncTask<File, Void, Object[]> {

        // Configure me!
        private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
        private static final int SAMPLE_RATE = 44100; // Hz
        private static final int ENCODING = AudioFormat.ENCODING_PCM_16BIT;
        private static final int CHANNEL_MASK = AudioFormat.CHANNEL_IN_MONO;
        //

        private static final int BUFFER_SIZE = 2 * AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_MASK, ENCODING);

        private Context ctx;

        private RecordWaveTask(Context ctx) {
            setContext(ctx);
        }

        private void setContext(Context ctx) {
            this.ctx = ctx;
        }

        /**
         * Opens up the given file, writes the header, and keeps filling it with raw PCM bytes from
         * AudioRecord until it reaches 4GB or is stopped by the user. It then goes back and updates
         * the WAV header to include the proper final chunk sizes.
         *
         * @param files Index 0 should be the file to write to
         * @return Either an Exception (error) or two longs, the filesize, elapsed time in ms (success)
         */
        @Override
        protected Object[] doInBackground(File... files) {
            AudioRecord audioRecord = null;
            FileOutputStream wavOut = null;
            long startTime = 0;
            long endTime = 0;

            try {
                // Open our two resources
                audioRecord = new AudioRecord(AUDIO_SOURCE, SAMPLE_RATE, CHANNEL_MASK, ENCODING, BUFFER_SIZE);
                wavOut = new FileOutputStream(files[0]);

                // Write out the wav file header
                writeWavHeader(wavOut, CHANNEL_MASK, SAMPLE_RATE, ENCODING);

                // Avoiding loop allocations
                byte[] buffer = new byte[BUFFER_SIZE];
                boolean run = true;
                int read;
                long total = 0;

                // Let's go
                startTime = SystemClock.elapsedRealtime();
                audioRecord.startRecording();
                while (run && !isCancelled()) {
                    read = audioRecord.read(buffer, 0, buffer.length);

                    // WAVs cannot be > 4 GB due to the use of 32 bit unsigned integers.
                    if (total + read > 4294967295L) {
                        // Write as many bytes as we can before hitting the max size
                        for (int i = 0; i < read && total <= 4294967295L; i++, total++) {
                            wavOut.write(buffer[i]);
                        }
                        run = false;
                    } else {
                        // Write out the entire read buffer
                        wavOut.write(buffer, 0, read);
                        total += read;
                    }
                }
            } catch (IOException ex) {
                return new Object[]{ex};
            } finally {
                if (audioRecord != null) {
                    try {
                        if (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                            audioRecord.stop();
                            endTime = SystemClock.elapsedRealtime();
                        }
                    } catch (IllegalStateException ex) {
                        //
                    }
                    if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                        audioRecord.release();
                    }
                }
                if (wavOut != null) {
                    try {
                        wavOut.close();
                    } catch (IOException ex) {
                        //
                    }
                }
            }

            try {
                // This is not put in the try/catch/finally above since it needs to run
                // after we close the FileOutputStream
                updateWavHeader(files[0]);
            } catch (IOException ex) {
                return new Object[] { ex };
            }

            return new Object[] { files[0].length(), endTime - startTime };
        }

        /**
         * Writes the proper 44-byte RIFF/WAVE header to/for the given stream
         * Two size fields are left empty/null since we do not yet know the final stream size
         *
         * @param out         The stream to write the header to
         * @param channelMask An AudioFormat.CHANNEL_* mask
         * @param sampleRate  The sample rate in hertz
         * @param encoding    An AudioFormat.ENCODING_PCM_* value
         * @throws IOException
         */
        private static void writeWavHeader(OutputStream out, int channelMask, int sampleRate, int encoding) throws IOException {
            short channels;
            switch (channelMask) {
                case AudioFormat.CHANNEL_IN_MONO:
                    channels = 1;
                    break;
                case AudioFormat.CHANNEL_IN_STEREO:
                    channels = 2;
                    break;
                default:
                    throw new IllegalArgumentException("Unacceptable channel mask");
            }

            short bitDepth;
            switch (encoding) {
                case AudioFormat.ENCODING_PCM_8BIT:
                    bitDepth = 8;
                    break;
                case AudioFormat.ENCODING_PCM_16BIT:
                    bitDepth = 16;
                    break;
                case AudioFormat.ENCODING_PCM_FLOAT:
                    bitDepth = 32;
                    break;
                default:
                    throw new IllegalArgumentException("Unacceptable encoding");
            }

            writeWavHeader(out, channels, sampleRate, bitDepth);
        }

        /**
         * Writes the proper 44-byte RIFF/WAVE header to/for the given stream
         * Two size fields are left empty/null since we do not yet know the final stream size
         *
         * @param out        The stream to write the header to
         * @param channels   The number of channels
         * @param sampleRate The sample rate in hertz
         * @param bitDepth   The bit depth
         * @throws IOException
         */
        private static void writeWavHeader(OutputStream out, short channels, int sampleRate, short bitDepth) throws IOException {
            // Convert the multi-byte integers to raw bytes in little endian format as required by the spec
            byte[] littleBytes = ByteBuffer
                    .allocate(14)
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .putShort(channels)
                    .putInt(sampleRate)
                    .putInt(sampleRate * channels * (bitDepth / 8))
                    .putShort((short) (channels * (bitDepth / 8)))
                    .putShort(bitDepth)
                    .array();

            // Not necessarily the best, but it's very easy to visualize this way
            out.write(new byte[]{
                    // RIFF header
                    'R', 'I', 'F', 'F', // ChunkID
                    0, 0, 0, 0, // ChunkSize (must be updated later)
                    'W', 'A', 'V', 'E', // Format
                    // fmt subchunk
                    'f', 'm', 't', ' ', // Subchunk1ID
                    16, 0, 0, 0, // Subchunk1Size
                    1, 0, // AudioFormat
                    littleBytes[0], littleBytes[1], // NumChannels
                    littleBytes[2], littleBytes[3], littleBytes[4], littleBytes[5], // SampleRate
                    littleBytes[6], littleBytes[7], littleBytes[8], littleBytes[9], // ByteRate
                    littleBytes[10], littleBytes[11], // BlockAlign
                    littleBytes[12], littleBytes[13], // BitsPerSample
                    // data subchunk
                    'd', 'a', 't', 'a', // Subchunk2ID
                    0, 0, 0, 0, // Subchunk2Size (must be updated later)
            });
        }

        /**
         * Updates the given wav file's header to include the final chunk sizes
         *
         * @param wav The wav file to update
         * @throws IOException
         */
        private static void updateWavHeader(File wav) throws IOException {
            byte[] sizes = ByteBuffer
                    .allocate(8)
                    .order(ByteOrder.LITTLE_ENDIAN)
                    // There are probably a bunch of different/better ways to calculate
                    // these two given your circumstances. Cast should be safe since if the WAV is
                    // > 4 GB we've already made a terrible mistake.
                    .putInt((int) (wav.length() - 8)) // ChunkSize
                    .putInt((int) (wav.length() - 44)) // Subchunk2Size
                    .array();

            RandomAccessFile accessWave = null;
            //noinspection CaughtExceptionImmediatelyRethrown
            try {
                accessWave = new RandomAccessFile(wav, "rw");
                // ChunkSize
                accessWave.seek(4);
                accessWave.write(sizes, 0, 4);

                // Subchunk2Size
                accessWave.seek(40);
                accessWave.write(sizes, 4, 4);
            } catch (IOException ex) {
                // Rethrow but we still close accessWave in our finally
                throw ex;
            } finally {
                if (accessWave != null) {
                    try {
                        accessWave.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onCancelled(Object[] results) {
            // Handling cancellations and successful runs in the same way
            onPostExecute(results);
        }

        @Override
        protected void onPostExecute(Object[] results) {
            Throwable throwable = null;
            if (results[0] instanceof Throwable) {
                // Error
                throwable = (Throwable) results[0];
                Log.e(RecordWaveTask.class.getSimpleName(), throwable.getMessage(), throwable);
            }

            // If we're attached to an activity
            if (ctx != null) {
                if (throwable == null) {
                    // Display final recording stats
                    double size = (long) results[0] / 1000000.00;
                    long time = (long) results[1] / 1000;
                    Toast.makeText(ctx, String.format(Locale.getDefault(), "%.2f MB / %d seconds",
                            size, time), Toast.LENGTH_LONG).show();
                } else {
                    // Error
                    Toast.makeText(ctx, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_RECORD_AUDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    audioRecorderReady();
                } else {
                    // Permission denied
                    Toast.makeText(this, "\uD83D\uDE41", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
