package com.hardskygames.habro;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class ArticleActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_page);
		
	    RssItem selectedRssItem = HabroActivity.selectedRssItem;
	    WebView page = (WebView)findViewById(R.id.wv_article);
	    page.loadUrl(selectedRssItem.getLink());
	}
}
