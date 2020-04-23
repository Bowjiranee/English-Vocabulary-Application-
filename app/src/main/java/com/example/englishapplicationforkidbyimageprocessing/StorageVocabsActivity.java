package com.example.englishapplicationforkidbyimageprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishapplicationforkidbyimageprocessing.Database.GameDatabaseHelper;
import com.example.englishapplicationforkidbyimageprocessing.Model.WordsGame;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class StorageVocabsActivity extends AppCompatActivity {

//    private TextView mTextView;
//    private ImageButton mImageButton;

    ArrayList<String> textList;

    ArrayList<WordsGame> wordsList;
    private GameDatabaseHelper mGameHelper;
    private WordsGame currentWordsGame;
    int qid = 0;

    private String listenText;
    private TextToSpeech mTTS;

    private  ImageButton imagebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_vocabs);

//        mTextView = findViewById(R.id.textview_vocabs);
//        mImageButton = findViewById(R.id.btn_listen_words);

        mGameHelper = new GameDatabaseHelper(this);
        mTTS = new TextToSpeech(getBaseContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    mTTS.setLanguage(Locale.US);
//                    mTTS.setSpeechRate(1.5f);
                }
            }
        });
//        textList = new ArrayList<>();
//        wordsList = new ArrayList<>();

        TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);


        TableLayout tableLayout = findViewById(R.id.table_layout);

        //==================== Header =============================//
        //Add Header
        TableRow rowsHeader = new TableRow(this);
        rowsHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
        rowsHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));

        String[] headerText = {"No.","คำศัพท์","หมายถึง","ฟังเสียง"};
        for (String c : headerText){
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(18);
            tv.setTextColor(getResources().getColor(R.color.black));
            tv.setPadding(5, 5, 5, 5);
            tv.setText(c);
            rowsHeader.addView(tv);
        }
        tableLayout.addView(rowsHeader);

        //==================== Inside Table Layout =============================//

        wordsList = mGameHelper.getAllOfTheQuestions();

        SQLiteDatabase db = mGameHelper.getReadableDatabase();
        db.beginTransaction();

        try {
            String selectQuery = "SELECT * FROM "+ GameDatabaseHelper.WORDS_TABLE;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getColumnCount() !=0 && cursor != null) {
                while (cursor.moveToNext()) {
                    //Add DATA from DB
                    TableRow rows = new TableRow(this);
                    rows.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                    currentWordsGame = wordsList.get(qid);

                    ImageView imgMeaning = new ImageView(this);
                    imgMeaning.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    imgMeaning.setPadding(5, 5, 5, 8);
                    int idImage = getResources().getIdentifier("com.example.englishapplicationforkidbyimageprocessing:drawable/"+currentWordsGame.getMeaning(),null,null);
                    imgMeaning.setImageResource(idImage);
                    imgMeaning.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    String[] colText = {currentWordsGame.getId()+"", currentWordsGame.getSound()};
//                    String[] colText = {currentWordsGame.getId()+"", currentWordsGame.getSound()};

                    for (String text : colText) {
                        TextView tv = new TextView(this);

                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextSize(20);
                        tv.setTextColor(getResources().getColor(R.color.black));
                        tv.setText(text);
                        tv.setPadding(5, 32, 5, 5);
                        rows.addView(tv);
//                        Log.i("display1", ">> " + currentWordsGame.getId() + " " + text);
                    }
                    qid++;

                    Log.i("display5", ">> " + currentWordsGame.getSound() + " " + idImage);

                    rows.addView(imgMeaning,300,300);
                    tableLayout.addView(rows);

                    /*===================================================== listen Text current.getSound() ====================================================================*/
                    TableRow rows2 = new TableRow(this);
                    rows2.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                    ImageButton btn = new ImageButton(this);
                    btn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));
                    btn.setPadding(5, 5, 5, 8);
                    btn.setImageResource(R.drawable.speaker);
                    btn.setScaleType(ImageView.ScaleType.FIT_CENTER);

//                            btn.setBackgroundColor(getResources().getColor(R.color.grey));

//                    currentWordsGame = wordsList.get(qid);
//                        Log.i("display1", currentWordsGame.getSound());
//                        Button btn = new Button(this);

                    String[] colText2 = {currentWordsGame.getSound()};

                    for (final String text2 : colText2) {
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String toSpeak = text2;
                                mTTS.speak(toSpeak, TextToSpeech.QUEUE_ADD, null); //speak words
//                                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.i("display3", ">> " + qid + " " + text2);

                    }

                    rows.addView(btn,120,124);
                    tableLayout.addView(rows2,25,25);

                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            db.endTransaction();
            db.close();
        }

    }
}
