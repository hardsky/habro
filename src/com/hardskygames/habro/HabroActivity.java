package com.hardskygames.habro;

import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class HabroActivity extends Activity
{
	private static String FEED_URL = "http://habrahabr.ru/rss/hubs/";
	private static String ACTION_WEBVIEW = "com.hardskygames.habro.displayRssItem";

	private ListView m_listView = null;
	private Button m_btnRefresh = null;
	private DatabaseHelper m_dbHelper = null;
	private ArticleListCursorAdapter m_adapter = null;
	private boolean m_dbReaded = false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		m_listView = (ListView) findViewById(R.id.lstArticles);
		m_btnRefresh = (Button) findViewById(R.id.btn_refresh);

		m_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View view, int index, long arg3) {
				ItemLinearLayout itemLayout = (ItemLinearLayout)view;
				Intent intent = new Intent(ACTION_WEBVIEW);
				intent.putExtra(ArticleActivity.LINK_KEY, itemLayout.getLink());
				startActivity(intent);
			}
		});
		
		m_btnRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new FeedFetchTask().execute(FEED_URL);
			}
		});
		
		m_dbHelper = new DatabaseHelper(this);
		m_dbHelper.getWritableDatabase();
		
		new FeedFetchTask().execute(FEED_URL);
		
		m_adapter = new ArticleListCursorAdapter(this, R.layout.item_article, m_dbHelper.queryDb(), DatabaseHelper.DATABASE_COLUMNS, new int[] { R.id.txt_title, R.id.txt_time });
		m_listView.setAdapter(m_adapter);
		
		m_dbReaded = true;
	}
    	
	@Override
	protected void onDestroy() {
		m_dbHelper.close();
		super.onDestroy();
	}
	
	private class FeedFetchTask extends AsyncTask<String, Void, Cursor> {
		
	    protected Cursor doInBackground(String... urls) {
	    	while(!m_dbReaded){
	    		try {
    				
	    			Thread.sleep(500);
    				
				} catch (InterruptedException e) {
					return null;
				}	    		
	    	}
	    	while(true){
		    	
	    		if(isCancelled())
		    		break;
	    		
		    	FeedLoader loader = null;
		    	try{
		    		loader = new FeedLoader(m_dbHelper);
		    		loader.connect(urls[0]);
			    	if(loader.load()){
			    		final Cursor cr = m_dbHelper.queryDb();
						return cr;
			    	}
		    	}
				catch(IOException io_ex){//connection
				}
				catch (Exception ex) {
					return null;
				}
		    	finally{
		    		loader.close();
		    	}
		    	
	    		try {
    				
	    			Thread.sleep(10000);
    				
				} catch (InterruptedException e) {
					return null;
				}
	    	}
	    	
	    	return null;
	    }
	    
	    protected void onPostExecute(Cursor cursor) {
	    	if(cursor == null)
	    		return;
	    	
	    	m_adapter.changeCursor(cursor);
			m_adapter.notifyDataSetChanged();
		}
	}	
}
