package com.hardskygames.habro;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class ArticleActivity extends Activity {

	public static String LINK_KEY = "link";	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_page);
		
	    WebView page = (WebView)findViewById(R.id.wv_article);
	    String link = getIntent().getStringExtra(LINK_KEY);
	    page.loadUrl(link);
	}
}
