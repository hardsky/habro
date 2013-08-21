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
	private static final String FEED_URL = "http://habrahabr.ru/rss/hubs/";
	private static final String ACTION_WEBVIEW = "com.hardskygames.habro.displayRssItem";

	private ListView m_listView = null;
	private Button m_btnRefresh = null;
	private DatabaseHelper m_dbHelper = null;
	private ArticleListCursorAdapter m_adapter = null;
	
	private static volatile boolean m_fetchTaskInUse = false;
	private static final Object m_fetchSyncObj = new Object();
	
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
				
		m_adapter = new ArticleListCursorAdapter(this, R.layout.item_article, m_dbHelper.queryDb(), DatabaseHelper.DATABASE_COLUMNS, new int[] { R.id.txt_title, R.id.txt_time });
		m_listView.setAdapter(m_adapter);

		new FeedFetchTask().execute(FEED_URL);		
	}
    	
	@Override
	protected void onDestroy() {
		m_dbHelper.close();
		super.onDestroy();
	}
	
	private class FeedFetchTask extends AsyncTask<String, Void, Cursor> {
			
	    protected Cursor doInBackground(String... urls) {
	    	if(m_fetchTaskInUse)
	    		return null;
	    	
	    	synchronized (m_fetchSyncObj) {
						
		    	if(m_fetchTaskInUse)
		    		return null;
	    		
		    	m_fetchTaskInUse = true;
		    	Cursor res = null;
		    	
		    	while(true){
			    	
		    		if(isCancelled()){
			    		break;
		    		}
		    		
			    	FeedLoader loader = null;
			    	try{
			    		loader = new FeedLoader(m_dbHelper);
			    		loader.connect(urls[0]);
				    	if(loader.load())
				    		res = m_dbHelper.queryDb();
				    	
				    	break;
			    	}
					catch(IOException io_ex){//connection troubles, we will wait, than try again
					}
					catch (Exception ex) {
						break;
					}
			    	finally{
		    			loader.close();
			    	}
			    	
		    		try {	    				
		    			Thread.sleep(10000);	    				
					} catch (InterruptedException e) {
						break;
					}
		    	}
		    	
		    	m_fetchTaskInUse = false;
		    	return res;
			}	    	
	    }
	    
	    protected void onPostExecute(Cursor cursor) {
	    	if(cursor == null)
	    		return;
	    	
	    	m_adapter.changeCursor(cursor);
			m_adapter.notifyDataSetChanged();
		}	    
	}	
}
