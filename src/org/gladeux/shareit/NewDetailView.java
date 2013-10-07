package org.gladeux.shareit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class NewDetailView extends Activity
{
	public static final String KEY_TOPIC = "topic",
											   KEY_ID = "id",
											   KEY_TOPICLABEL = "topicLabel",
											   KEY_CONTENT = "content",
											   KEY_IMAGES = "images",
											   KEY_IMAGE = "image",
											   KEY_CREATEDDATE = "createdDate",
											   KEY_UPDATEDDATE = "updatedDate",
											  TAG = DetailView.class.getName();
	
	public String filename = ".xml",
						  categoryID = "";
	
	public int currentID = 0;
	
	public Date now = new Date();
	
	public FileInputStream fileInputStream;
	
	public TextView title, content;
	
	public ImageView ok_button;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// Get rid of the title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		Intent getIntent = getIntent();

		currentID = getIntent.getIntExtra(KEY_ID, 0);
		categoryID = getIntent.getStringExtra("categoryID");

		setContentView(R.layout.activity_new_detail_view);
		
		Log.d(TAG, "onCreate is called.");
		
		title = (TextView) findViewById(R.id.new_detail_title);
		content = (TextView) findViewById(R.id.new_detail_content);
		ok_button = (ImageView) findViewById(R.id.new_detail_ok_button);
		
		Log.d(TAG, "all data have been loaded.");

		ok_button.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View view)
			{
				if (title.getText().toString().equals("")
						|| content.getText().toString().equals(""))
				{
					Toast.makeText(NewDetailView.this, R.string.edit_confirmation, Toast.LENGTH_SHORT).show();
				}

				else
				{
					createNewDetail();
				}
			}

			private void createNewDetail()
			{
				try
				{
					filename = "data" + categoryID + filename;
					fileInputStream = openFileInput(filename);

					DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					Document doc = db.parse(fileInputStream);

					fileInputStream.close();
					
					Log.d(TAG, "detail XML file has been loaded and parsed!");

					Node root = doc.getFirstChild();

					// Get the latest categoryData tag
					Node newTopic = doc.createElement(KEY_TOPIC);
					Node newID = doc.createElement(KEY_ID),
							   newTopicLabel = doc.createElement(KEY_TOPICLABEL), 
							   newContent = doc.createElement(KEY_CONTENT),
							   newImages = doc.createElement(KEY_IMAGES),
							   newCreatedDate = doc.createElement(KEY_CREATEDDATE),
							   newUpdatedDate = doc.createElement(KEY_UPDATEDDATE);

					newID.appendChild(doc.createTextNode(String.valueOf(currentID)));
					newTopicLabel.appendChild(doc.createTextNode(title.getText()	.toString()));
					newContent.appendChild(doc.createTextNode(content.getText().toString()));
					newImages.appendChild(doc.createElement(KEY_IMAGE));
					newCreatedDate.appendChild(doc.createTextNode(now.toString()));
					newUpdatedDate.appendChild(doc.createTextNode(now.toString()));

					newTopic.appendChild(newID);
					newTopic.appendChild(newTopicLabel);
					newTopic.appendChild(newContent);
					newTopic.appendChild(newImages);
					newTopic.appendChild(newCreatedDate);
					newTopic.appendChild(newUpdatedDate);
					
					Log.d(TAG, "new detail element has been created and loaded.");

					root.appendChild(newTopic);
					
					Log.d(TAG, "new detail has been appended into the existing detail XML.");

					TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(openFileOutput(filename, MODE_PRIVATE)));

					Log.d(TAG, "detail XML file has been re-written.");
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (ParserConfigurationException e)
				{
					e.printStackTrace();
				}
				catch (SAXException e)
				{
					e.printStackTrace();
				}
				catch (TransformerConfigurationException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				catch (TransformerException e)
				{
					e.printStackTrace();
				}
				finally
				{
					// Set the intent
					Log.d(TAG, "From NewDetailView to DetailView Intent.");
					Intent intent = new Intent(NewDetailView.this, DetailView.class);

					intent.putExtra("categoryID", categoryID);
					setResult(RESULT_OK, intent);

					finish();
				}
			}
		});
	}
	
	@Override
	public void onBackPressed()
	{
		// Set the intent
		Log.d(TAG, "From NewDetailView to DetailView Intent.");
		Intent intent = new Intent(NewDetailView.this, DetailView.class);

		intent.putExtra("categoryID", categoryID);
		setResult(RESULT_OK, intent);

		finish();
	}
}
