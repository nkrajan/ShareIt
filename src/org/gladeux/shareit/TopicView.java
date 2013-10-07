package org.gladeux.shareit;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;


public class TopicView extends Activity
{
	public String id,
						   topic,
						   content,
						   categoryID,
						   createdDate,
						   updatedDate;
	
	public static final String KEY_TOPIC = "topic",
			  								   KEY_ID = "id",
			  								   KEY_TOPICLABEL = "topicLabel",
			  								   KEY_CONTENT = "content",
			  								   KEY_IMAGES = "images",
			  								   KEY_IMAGE = "image",
			  								   KEY_CREATEDDATE = "createdDate",
			  								   KEY_UPDATEDDATE = "updatedDate",
			  								   KEY_CATEGORYID = "categoryID",
			  								   TAG = DetailView.class.getName();

	public ArrayList<HashMap<String, String>> topicList = new ArrayList<HashMap<String,String>>();
	
	public TextView topicTitle, topicContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// Get rid of the title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_topic_view);
		
		Log.d(TAG, "onCreate is called.");
		
		topicTitle = (TextView) findViewById(R.id.topic_view_title);
		topicContent = (TextView) findViewById(R.id.topic_view_contents);
		
		Intent getIntent = getIntent();
		
		categoryID = getIntent.getStringExtra(KEY_CATEGORYID);
		id = getIntent.getStringExtra(KEY_ID);
		topic = getIntent.getStringExtra(KEY_TOPICLABEL);
		content = getIntent.getStringExtra(KEY_CONTENT);
		createdDate = getIntent.getStringExtra(KEY_CREATEDDATE);
		updatedDate = getIntent.getStringExtra(KEY_UPDATEDDATE);
		
		Log.d(TAG, "all data have been loaded.");

		topicTitle.setText(topic);
		topicContent.setText(content);
		topicContent.setMovementMethod(new ScrollingMovementMethod());
	}
	
	@Override
	public void onBackPressed()
	{
		// Set the intent
		Log.d(TAG, "From TopicView to DetailView Intent.");
		Intent intent = new Intent(TopicView.this, DetailView.class);

		intent.putExtra("categoryID", categoryID);
		setResult(RESULT_OK, intent);
		finish();
	}
}
