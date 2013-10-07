package org.gladeux.shareit;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomCategoryListAdapter extends BaseAdapter
{
	public static final String KEY_CATEGORYDATA = "categoryData",
											   KEY_ID = "id",
											   KEY_CATEGORY = "categoryLabel",
											   KEY_DESCRIPTION = "description",
											   KEY_IMAGEPATH = "imagePath";

	public LayoutInflater inflater;
	
	public List<HashMap<String, String>> categoryList;
	
	public ViewHolder holder;
	
	public CustomCategoryListAdapter(CategoryView categoryView, ArrayList<HashMap<String, String>> categoryList)
	{
		// Get the categoryList from the caller
		this.categoryList = categoryList;
		
		// Initialize the LayoutInflater by the system service
		inflater = (LayoutInflater) categoryView.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount()
	{
		// TODO Auto-generated method stub
		return categoryList.size();
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
			view = inflater.inflate(R.layout.category_view_category_list_item, null);
			holder = new ViewHolder();
			
			// Get the default values
			holder.category_list_item_category = (TextView) view.findViewById(R.id.category_list_item_topic);
			holder.category_list_item_description = (TextView) view.findViewById(R.id.category_list_item_description);
			holder.category_list_item_image = (ImageView) view.findViewById(R.id.category_list_image);
			
			//Set the view's elements with the holder object
			view.setTag(holder);
		}
		
		else
		{
			// Grab the view's elements
			holder = (ViewHolder) view.getTag();
		}
		
		// Set each element with the categoryList's elements
		holder.category_list_item_category.setText(categoryList.get(position).get(KEY_CATEGORY));
		holder.category_list_item_description.setText(categoryList.get(position).get(KEY_DESCRIPTION));
		
		try
		{
			holder.category_list_item_image.setImageBitmap(BitmapFactory.decodeStream(view.getContext().openFileInput(categoryList.get(position).get(KEY_IMAGEPATH))));
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return view;
	}

	private static class ViewHolder
	{
		TextView category_list_item_category,
						 category_list_item_description;
		ImageView category_list_item_image;
	}
}
