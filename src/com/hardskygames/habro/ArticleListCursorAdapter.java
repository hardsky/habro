package com.hardskygames.habro;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ArticleListCursorAdapter extends SimpleCursorAdapter{

	private static final SimpleDateFormat m_dateFormater = new SimpleDateFormat("dd.MM - HH:mm:ss");

    public ArticleListCursorAdapter (Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }
 
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
 
		int titleCol = cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE);
		int dateCol = cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE);
		int linkCol = cursor.getColumnIndex(DatabaseHelper.COLUMN_LINK);

		String title = cursor.getString(titleCol);
		String date = m_dateFormater.format(new Date(cursor.getLong(dateCol)));
		String link = cursor.getString(linkCol);

		ItemLinearLayout layout = (ItemLinearLayout)View.inflate(context, R.layout.item_article, null);
		TextView txtTitle = (TextView) layout.findViewById(R.id.txt_title);
		TextView txtDate = (TextView) layout.findViewById(R.id.txt_time);
	
		txtTitle.setText(title);
		txtDate.setText(date);
		layout.setLink(link);

		return layout;
    }
    
    @Override
    public void bindView(View v, Context context, Cursor cursor) {
 
		int titleCol = cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE);
		int dateCol = cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE);
		int linkCol = cursor.getColumnIndex(DatabaseHelper.COLUMN_LINK);

		String title = cursor.getString(titleCol);
		String date = m_dateFormater.format(new Date(cursor.getLong(dateCol)));
		String link = cursor.getString(linkCol);

		ItemLinearLayout layout = (ItemLinearLayout)v;
		TextView txtTitle = (TextView) layout.findViewById(R.id.txt_title);
		TextView txtDate = (TextView) layout.findViewById(R.id.txt_time);
	
		txtTitle.setText(title);
		txtDate.setText(date);
		layout.setLink(link);
    }
 
    
}
