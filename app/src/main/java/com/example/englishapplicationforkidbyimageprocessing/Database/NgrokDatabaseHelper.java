package com.example.englishapplicationforkidbyimageprocessing.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NgrokDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "inputNgrok.db";
    private static final int DATABASE_VERSION = 1;

    public static final String NGROK_TABLE = "ngrok_table";

    public static final String COL_ID = "_id";
    public static final String COL_PATH = "path";

    private String SQL_CREATE_TABLE_NGROK = "CREATE TABLE " + NGROK_TABLE + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_PATH + " TEXT" + ")";

    private String DROP_NGROK_TABLE = "DROP TABLE IF EXISTS " + NGROK_TABLE;


    public NgrokDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_NGROK);
//        ContentValues cv = new ContentValues();
//        cv.put(COL_PATH, "");
//        db.insert(NGROK_TABLE, null, cv);
    }

    public void addNgrokPath(String ng){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_PATH, ng);
        db.insert(NGROK_TABLE, null, cv);
        db.close();
    }

    public String getNgrokPath(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + COL_PATH
                + " FROM " + NGROK_TABLE  , null);
        cursor.moveToLast();
        String cr = cursor.getString(cursor.getColumnIndex(COL_PATH));
        return cr;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP_NGROK_TABLE);
    }
}
