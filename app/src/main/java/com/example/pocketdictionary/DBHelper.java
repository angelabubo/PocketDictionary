package com.example.pocketdictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.text.DateFormat;
import java.util.Date;

public class DBHelper extends SQLiteAssetHelper {

    public static final String DATABASE = "database_dictionary.db";

    public static final String DICTIONARY_TBL = "dictionary";
    public static final int COL_ID = 0;
    public static final int COL_WORD = 1;
    public static final int COL_DEFINITION = 2;

    public static final String FAVORITES_TBL = "favorites";
    public static final int COL_WORD_ID = 1;

    public static final String FEEDBACK_TBL = "feedback";
    public static final int COL_DATE_RCVD = 1;
    public static final int COL_RATING = 2;
    public static final int COL_EMAIL = 3;
    public static final int COL_COMMENT = 4;

    public static final int VERSION = 1;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE, null, VERSION);
    }

    public void createTables()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("create table favorites(id INTEGER PRIMARY KEY AUTOINCREMENT, word_id INTEGER)");
        }
        catch (Exception ex)
        {
            Log.d("PocketDictionary", "createTables: " + ex.toString());
        }

        try {
            String feedbackCreation = "create table feedback(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "date_received datetime," +
                    "rating DECIMAL," +
                    "email varchar(150)," +
                    "comment varchar(500))";
            db.execSQL(feedbackCreation);
        }
        catch (Exception ex)
        {
            Log.d("PocketDictionary", "createTables: " + ex.toString());
        }
    }

    public Cursor getAllWords()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from dictionary", null);
        return  result;
    }

    public Cursor getAWord(String word)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from dictionary where word ='" + word +"'", null);
        return  result;
    }

    public Cursor getAllFavorites()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select dictionary.id, dictionary.word, dictionary.definition from dictionary, favorites where " +
                "dictionary.id = favorites.word_id", null);
        return  result;
    }

    public Cursor getFavoriteWord(String word)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from dictionary, favorites where " +
                "dictionary.id = favorites.word_id AND " + "dictionary.word = '" + word + "'", null);
        return  result;
    }

    public Cursor getFavoriteWordById(int wordId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from favorites where " +
                "favorites.word_id = " + wordId, null);
        return  result;
    }

    //Returns true is word is a favorite and false if not
    public boolean setFavoriteWordById(int wordId)
    {
        boolean isFavorite = false;
        try {
            Cursor result = getFavoriteWordById(wordId);

            if (result.getCount() <= 0)
            {
                SQLiteDatabase db = this.getWritableDatabase();

                //Insert in favorites table
                isFavorite = true;
                ContentValues cv = new ContentValues();
                cv.put("word_id", wordId);
                db.insert("favorites", null, cv);
            }
            else
            {
                //Delete entry in favorites table
                deleteFavoriteWord(wordId);
                isFavorite = false;
            }
        }
        catch (Exception ex)
        {
            Log.d("PocketDictionary", "setFavoriteWord: " + ex.toString());
        }

        return isFavorite;
    }

    public void deleteFavoriteWord(int wordId)
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();

            //Delete entry in favorites table
            db.delete("favorites", "word_id" + "=" + wordId, null);

        }
        catch (Exception ex)
        {
            Log.d("PocketDictionary", "deleteFavoriteWord: " + ex.toString());
        }
    }

    public long saveFeedback(float rating, String email, String comment)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowId = -1;

        try {
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            ContentValues cv = new ContentValues();
            cv.put("date_received", currentDateTimeString);
            cv.put("rating", rating);
            cv.put("email", email);
            cv.put("comment", comment);
            rowId = db.insert("feedback", null, cv);
        }
        catch (Exception ex)
        {
            Log.d("PocketDictionary", "saveFeedback: " + ex.toString());
        }

        return rowId;
    }

    public Cursor getAFeedback(long rowId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from feedback where id = " + rowId, null);
        return  result;
    }

}
