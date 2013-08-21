package com.hardskygames.habro;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "Habro.db";
	public static final int DATABASE_VERSION = 2;
	public static final String[] DATABASE_COLUMNS = new String[] {"ART_TITLE", "ART_DATE", "ART_LINK" };

	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS articles (" +
				BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ART_TITLE TEXT, " +
                "ART_DATE INTEGER, " +
                "ART_LINK TEXT" +
                ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP VIEW IF EXISTS articles;");		
		onCreate(db);
	}

	public Cursor queryDb(){
		return getReadableDatabase().query("articles", new String[] { BaseColumns._ID, "ART_TITLE", "ART_DATE", "ART_LINK" }, null, null, null, null, "ART_DATE desc");
	}

	public void clearDb(){
		getWritableDatabase().delete("articles", null, null);
	}

	public void writeDb(String title, Date date, String link){
		ContentValues values = new ContentValues(3);
		values.put("ART_TITLE", title);
		values.put("ART_DATE", (long)(date.getTime() / 1000.0));
		values.put("ART_LINK", link);		
		getWritableDatabase().insert("articles", null, values);
	}
}
