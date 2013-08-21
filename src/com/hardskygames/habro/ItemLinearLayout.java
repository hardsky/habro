package com.hardskygames.habro;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ItemLinearLayout extends LinearLayout{
	public ItemLinearLayout(Context context) {
		super(context);
	}
	
	public ItemLinearLayout(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	public ItemLinearLayout(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}
	
	private String m_link;

	public String getLink(){
		return m_link;
	}
	public void setLink(String link){
		m_link = link;
	}
}
