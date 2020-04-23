package com.example.englishapplicationforkidbyimageprocessing.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.englishapplicationforkidbyimageprocessing.Model.QuestionsGame;
import com.example.englishapplicationforkidbyimageprocessing.Model.WordsGame;

import java.util.ArrayList;
import java.util.List;

public class QuizGameDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wordQuizDB.db";
    private static final int DATABASE_VERSION = 1;

    public static final String WORDS_QUIZ_TABLE = "word_game_quiz";
    public static final String COL_ID = "_id";
    public static final String COL_IMAGE = "image";
    public static final String COL_OPTA = "opta";
    public static final String COL_OPTB = "optb";
    public static final String COL_OPTC= "optc";
    public static final String COL_ANSWER = "answer";


    private static final String SQL_CREATE_TABLE_WORD_QUIZ
            = "CREATE TABLE " + WORDS_QUIZ_TABLE + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_IMAGE + " TEXT,"
            + COL_OPTA + " TEXT,"
            + COL_OPTB + " TEXT,"
            + COL_OPTC + " TEXT,"
            + COL_ANSWER + " TEXT"
            + ")";
    private String DROP_WORD_TABLE = "DROP TABLE IF EXISTS " + WORDS_QUIZ_TABLE;


    public QuizGameDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_WORD_QUIZ);
    }


    private void addAllQuestions(ArrayList<QuestionsGame> allQuestions) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (QuestionsGame question : allQuestions) {
                values.put(COL_IMAGE, question.getQuestion());
                values.put(COL_OPTA, question.getOpta());
                values.put(COL_OPTB, question.getOptb());
                values.put(COL_OPTC, question.getOptc());
                values.put(COL_ANSWER, question.getAnswer());
                db.insert(WORDS_QUIZ_TABLE, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void addWordsQuestion(QuestionsGame quizgame){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_IMAGE, quizgame.getQuestion());
        cv.put(COL_OPTA, quizgame.getOpta());
        cv.put(COL_OPTB, quizgame.getOptb());
        cv.put(COL_OPTC, quizgame.getOptc());
        cv.put(COL_ANSWER, quizgame.getAnswer());

        db.insert(WORDS_QUIZ_TABLE, null, cv);
        db.close();
    }

    public ArrayList<QuestionsGame> getAllOfTheQuestions() {

        ArrayList<QuestionsGame> questionsList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
        String selectQuery = "SELECT  * FROM " + WORDS_QUIZ_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do{
                QuestionsGame question = new QuestionsGame(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
                questionsList.add(question);
            }while (cursor.moveToNext());
        }
        return questionsList;
    }

    public void wordsQuestion(){
        this.addWordsQuestion(new QuestionsGame(0,"test", "test","test","test","test"));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //OnUpgrade is called when ever we upgrade or increment our database version no
        db.execSQL(DROP_WORD_TABLE);
        onCreate(db);
    }

}
