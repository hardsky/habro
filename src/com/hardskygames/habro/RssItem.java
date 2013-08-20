package com.hardskygames.habro;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;

public class RssItem {	
	private String m_title;
	private Date m_date;
	private String m_link;
	private static final SimpleDateFormat RFC822 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", java.util.Locale.ENGLISH);	
	  
	public RssItem(String title, Date date, String link){
		m_title = title;
		m_date = date;
		m_link = link;
	}
	
	public String getTitle() {
		return m_title;
	}
	public String getDateStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM - HH:mm:ss");
	    String result = sdf.format(m_date);
	    return result;
	}
	public Date getDate(){
		return m_date;
	}
	public String getLink() {
		return m_link;
	}
	
public static ArrayList<RssItem> getRssItems(String feedUrl) {

	ArrayList<RssItem> rssItems = new ArrayList<RssItem>();
	
	RssItem rssItemT = new RssItem("Habro news", new Date(), "http://habrahabr.ru/");
	
	rssItems.add(rssItemT);
	
	try {
		//open an URL connection make GET to the server and
		//take xml RSS data
		URL url = new URL(feedUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
		InputStream is = conn.getInputStream();
		
		//DocumentBuilderFactory, DocumentBuilder are used for
		//xml parsing
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		//using db (Document Builder) parse xml data and assign
		//it to Element
		Document document = db.parse(is);
		Element element = document.getDocumentElement();
		
		//take rss nodes to NodeList
		NodeList nodeList = element.getElementsByTagName("item");
		
		if (nodeList.getLength() > 0) {
			for (int i = 0; i < nodeList.getLength(); i++) {
			
				//take each entry (corresponds to <item></item> tags in
				//xml data
				
				Element entry = (Element) nodeList.item(i);
				
				Element _titleE = (Element) entry.getElementsByTagName("title").item(0);
				Element _pubDateE = (Element) entry.getElementsByTagName("pubDate").item(0);
				Element _linkE = (Element) entry.getElementsByTagName("link").item(0);
				
				String _title = _titleE.getFirstChild().getNodeValue();
				Date _pubDate = RFC822.parse(_pubDateE.getFirstChild().getNodeValue()); // new Date(_pubDateE.getFirstChild().getNodeValue());
				String _link = _linkE.getFirstChild().getNodeValue();
				
				//create RssItemObject and add it to the ArrayList
				RssItem rssItem = new RssItem(_title, _pubDate, _link);
				
				rssItems.add(rssItem);
			}
		}
		
	}
	} catch (Exception e) {
	  //TODO
	}
	
	Collections.sort(rssItems, new Comparator<RssItem>(){
		public int compare(RssItem item1, RssItem item2) {
			//reverse order
		    return item2.getDate().compareTo(item1.getDate());
		  }		
	});
	
	return rssItems;
}

}	
