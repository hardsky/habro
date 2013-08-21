package com.hardskygames.habro;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class FeedLoader{
	private DatabaseHelper m_dbHelper = null;
	private HttpURLConnection m_conn = null;
	private static final SimpleDateFormat RFC822 = new SimpleDateFormat(
		      "EEE, dd MMM yyyy HH:mm:ss Z", java.util.Locale.ENGLISH);
	
	public FeedLoader(DatabaseHelper dbHelper){
		m_dbHelper = dbHelper;
	}

	public void connect(String feedUrl) throws IOException{
		URL url = new URL(feedUrl);
		m_conn = (HttpURLConnection) url.openConnection();
	}

	public void close(){
		if(m_conn != null){
			m_conn.disconnect();
		}
	}

	public boolean load() throws DOMException, ParseException, IOException, ParserConfigurationException, SAXException{
		if (m_conn.getResponseCode() != HttpURLConnection.HTTP_OK)
			return false;

		InputStream is = m_conn.getInputStream();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();	
		Document document = db.parse(is);
		Element element = document.getDocumentElement();
		NodeList nodeList = element.getElementsByTagName("item");
		if (nodeList.getLength() <= 0)
			return false;

		m_dbHelper.clearDb();

		for (int i = 0; i < nodeList.getLength(); i++) {

			Element entry = (Element) nodeList.item(i);
	
			Element _titleE = (Element) entry.getElementsByTagName("title").item(0);
			Element _pubDateE = (Element) entry.getElementsByTagName("pubDate").item(0);
			Element _linkE = (Element) entry.getElementsByTagName("link").item(0);
	
			String _title = _titleE.getFirstChild().getNodeValue();
			Date _pubDate = RFC822.parse(_pubDateE.getFirstChild().getNodeValue());
			String _link = _linkE.getFirstChild().getNodeValue();
			
			m_dbHelper.writeDb(_title, _pubDate, _link);
		}
		
		return true;
	}	
}
