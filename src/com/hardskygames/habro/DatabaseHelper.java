package com.hardskygames.habro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "Habro.db";
	public static final int DATABASE_VERSION = 1;

	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS articles (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "art_title TEXT, " +
                "art_date TEXT, " +
                "art_link BOOLEAN" +
                ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP VIEW IF EXISTS articles;");
		
		onCreate(db);
	}

}
