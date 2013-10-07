package org.gladeux.shareit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class CustomDetailListAdapter extends BaseAdapter
{
	public static final String KEY_TOPIC = "topic",
			   								   KEY_ID = "id",
			   								   KEY_TOPICLABEL = "topicLabel",
			   								   KEY_CONTENT = "content",
			   								   KEY_IMAGES = "images",
			   								   KEY_IMAGE = "image",
			   								   KEY_CREATEDDATE = "createdDate",
			   								   KEY_UPDATEDDATE = "updatedDate";

	public LayoutInflater inflater;

	public List<HashMap<String, String>> topicList;

	public ViewHolder holder;
	
	public CustomDetailListAdapter(DetailView detailView,  ArrayList<HashMap<String, String>> topicList)
	{
		// Get the topicList from the caller
		this.topicList = topicList;
		
		// Initalize the LayoutInflater by the system service
		inflater = (LayoutInflater) detailView.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount()
	{
		// TODO Auto-generated method stub
		return topicList.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		View view = convertView;
		
		// If the selected view is empty
		if(view == null)
		{
			// Inflate the selected view with the XML layout
			view = inflater.inflate(R.layout.detail_view_detail_list_item, null);
			holder = new ViewHolder();
			
			// Get the default values
			holder.detail_list_item_topic = (TextView) view.findViewById(R.id.detail_list_item_topic);
			holder.detail_list_item_createdDate = (TextView) view.findViewById(R.id.detail_list_item_createdDate);
			holder.detail_list_item_updatedDate = (TextView) view.findViewById(R.id.detail_list_item_updatedDate);
			
			//Set the view's elements with the holder object
			view.setTag(holder);
		}
		
		else
		{
			// Grab the view's elements
			holder = (ViewHolder) view.getTag();
		}
		
		// Set each element with the categoryList's elements
		holder.detail_list_item_topic.setText(topicList.get(position).get(KEY_TOPICLABEL));
		holder.detail_list_item_createdDate.setText(topicList.get(position).get(KEY_CREATEDDATE));
		holder.detail_list_item_updatedDate.setText(topicList.get(position).get(KEY_UPDATEDDATE));

		return view;
	}

	private static class ViewHolder
	{
		TextView detail_list_item_updatedDate,
						 detail_list_item_topic,
						 detail_list_item_createdDate;
	}
}
