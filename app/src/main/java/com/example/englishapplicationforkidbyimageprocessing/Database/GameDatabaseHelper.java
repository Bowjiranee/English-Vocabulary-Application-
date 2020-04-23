package com.example.englishapplicationforkidbyimageprocessing.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.englishapplicationforkidbyimageprocessing.Model.WordsGame;

import java.util.ArrayList;

public class GameDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "wordDB.db";
    private static final int DATABASE_VERSION = 1;

    public static final String WORDS_TABLE = "word_game";
    public static final String COL_ID = "_id";
    public static final String COL_IMAGE = "image";
    public static final String COL_SOUND = "sound";
    public static final String COL_MEANING = "mean";


    private static final String SQL_CREATE_TABLE_WORD
            = "CREATE TABLE " + WORDS_TABLE + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_IMAGE + " TEXT,"
            + COL_SOUND + " TEXT,"
            + COL_MEANING + " TEXT"
            + ")";
    private String DROP_WORD_TABLE = "DROP TABLE IF EXISTS " + WORDS_TABLE;

    public GameDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create TABLE
        db.execSQL(SQL_CREATE_TABLE_WORD);
    }

    public void addWordsQuestion(WordsGame wordsGame){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_ID, wordsGame.getId());
        cv.put(COL_IMAGE, wordsGame.getImage());
        cv.put(COL_SOUND,wordsGame.getSound());
        cv.put(COL_MEANING,wordsGame.getMeaning());

        db.insert(WORDS_TABLE, null, cv);
        db.close();
    }

//    private void addAllQuestion(ArrayList<WordsGame> arrList) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            ContentValues cv = new ContentValues();
//            for (WordsGame words : arrList) {
//                cv.put(COL_IMAGE, words.getImage());
//                cv.put(COL_SOUND, words.getSound());
//                cv.put(COL_MEANING, words.getMeaning());
//                db.insert(WORDS_TABLE, null, cv);
//            }
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//            db.close();
//        }
//    }

    public ArrayList<WordsGame> getAllOfTheQuestions() {

        ArrayList<WordsGame> wordsList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + WORDS_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                WordsGame game = new WordsGame(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));

                wordsList.add(game);
            } while (cursor.moveToNext());
        }
        // return words list
        return wordsList;
    }

    public void wordsQuestion(){
        this.addWordsQuestion(new WordsGame(0,"test","test","test"));
    }

//    public void addImageSoundPath(String img,String sound){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(COL_IMAGE, img);
//        cv.put(COL_SOUND,sound);
//        db.insert(WORDS_TABLE, null, cv);
//        db.close();
//    }
//
//    public long getDataCount() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        long count = DatabaseUtils.queryNumEntries(db, WORDS_TABLE);
//        db.close();
//        return count;
//    }
//
//    public String getSoundPath(){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        Cursor cursor = db.rawQuery("SELECT " + COL_SOUND
//                + " FROM " + WORDS_TABLE  , null);
//        cursor.moveToLast();
//        String cr = cursor.getString(cursor.getColumnIndex(COL_SOUND));
//        return cr;
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP_WORD_TABLE);
        onCreate(db);
    }
}
