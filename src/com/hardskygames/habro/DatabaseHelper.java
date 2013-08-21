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
	public static final String COLUMN_TITLE = "ART_TITLE";
	public static final String COLUMN_DATE = "ART_DATE";
	public static final String COLUMN_LINK = "ART_LINK";
	public static final String[] DATABASE_COLUMNS = new String[] {COLUMN_TITLE, COLUMN_DATE, COLUMN_LINK };
	public static final String TABLE_NAME = "articles";

	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
				BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
				COLUMN_TITLE + " TEXT, " +
				COLUMN_DATE + " INTEGER, " +
				COLUMN_LINK + " TEXT" +
                ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP VIEW IF EXISTS " + TABLE_NAME + ";");		
		onCreate(db);
	}

	public Cursor queryDb(){
		return getReadableDatabase().query(TABLE_NAME, new String[] { BaseColumns._ID, COLUMN_TITLE, COLUMN_DATE, COLUMN_LINK }, null, null, null, null, COLUMN_DATE + " desc");
	}

	public void clearDb(){
		getWritableDatabase().delete(TABLE_NAME, null, null);
	}

	public void writeDb(String title, Date date, String link){
		ContentValues values = new ContentValues(3);
		values.put(COLUMN_TITLE, title);
		values.put(COLUMN_DATE, date.getTime());
		values.put(COLUMN_LINK, link);		
		getWritableDatabase().insert(TABLE_NAME, null, values);
	}
}
