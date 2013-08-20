package com.hardskygames.habro;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ItemAdapter extends ArrayAdapter<RssItem> {

    protected ListView mListView;

    protected static class RowViewHolder {
        public TextView mTitle;
        public TextView mText;
    }
	
	public ItemAdapter(HabroActivity activity, int resource, List<RssItem> items) {
		super(activity, resource, items);
		mListView = activity.getListView();
	}
	
	@Override
	public View getView (int position, View convertView, ViewGroup parent){
		
        View view = View.inflate(getContext(), R.layout.item_article, null);
        TextView title = (TextView) view.findViewById(R.id.txt_title);
        TextView time = (TextView) view.findViewById(R.id.txt_time);
        
        RssItem item = getItem(position);
        title.setText(item.getTitle());
        time.setText(item.getDateStr());

        return view;		
	}

}
