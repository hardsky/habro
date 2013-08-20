package com.hardskygames.habro;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class HabroActivity extends Activity
{
	public static RssItem selectedRssItem = null;
	
	private static String feedUrl = "http://habrahabr.ru/rss/hubs/";
	private ListView rssListView = null;
	private ArrayList<RssItem> rssItems = new ArrayList<RssItem>();
	private ArrayAdapter<RssItem> aa = null;
	private Button m_btnRefresh = null;
	
	public ListView getListView(){
		return rssListView;
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// get the listview from layout.xml
		rssListView = (ListView) findViewById(R.id.lstArticles);
		m_btnRefresh = (Button) findViewById(R.id.btn_refresh);
		// here we specify what to execute when individual list items clicked
		rssListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		
			public void onItemClick(AdapterView<?> av, View view, int index, long arg3) {
			    selectedRssItem = rssItems.get(index);			
				// we call the other activity that shows a single rss item in
				// one page
				Intent intent = new Intent("com.hardskygames.habro.displayRssItem");
				startActivity(intent);
			}
		});
		m_btnRefresh.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				refressRssList();
			}
		});
		
		//adapters are used to populate list. they take a collection,
		//a view (in our example R.layout.list_item
		aa = new ItemAdapter(this, R.layout.item_article, rssItems);
		    //here we bind array adapter to the list
		rssListView.setAdapter(aa);
		refressRssList();
		
    }
    
	private void refressRssList() {
	
	    ArrayList<RssItem> newItems = RssItem.getRssItems(feedUrl);
	    rssItems.clear();
	    rssItems.addAll(newItems);    
	    aa.notifyDataSetChanged();
	}
    
}
