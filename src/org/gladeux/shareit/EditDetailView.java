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
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditDetailView extends Activity
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
						   createdDate,
						   updatedDate,
						   categoryID = "";
	
	public int currentID = 0;
	
	public Date now = new Date();
	
	public FileInputStream fileInputStream;
	
	public TextView title, content;
	
	public ImageView ok_button;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Get rid of the title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
				
		setContentView(R.layout.activity_edit_detail_view);
		
		title = (TextView) findViewById(R.id.edit_detail_title);
		content = (TextView) findViewById(R.id.edit_detail_description);
		ok_button = (ImageView) findViewById(R.id.edit_detail_ok_button);

		Log.d(TAG, "onCreate is called.");
		
		title.setText(getIntent().getStringExtra(KEY_TOPICLABEL));
		content.setText(getIntent().getStringExtra(KEY_CONTENT));
		currentID = Integer.parseInt(getIntent().getStringExtra(KEY_ID));
		createdDate = getIntent().getStringExtra(KEY_CREATEDDATE);
		updatedDate = getIntent().getStringExtra(KEY_UPDATEDDATE);
		categoryID = getIntent().getStringExtra("categoryID");
		
		Log.d(TAG, "all data have been loaded.");
		
		ok_button.setOnClickListener(new View.OnClickListener()
		{
		
			@Override
			public void onClick(View view)
			{
				if (title.getText().toString().equals("") || content.getText().toString().equals(""))
				{
					Toast.makeText(EditDetailView.this, "메모 이름과 내용을 꼭 써주세요.", Toast.LENGTH_SHORT).show();
				}
			
				else
				{
					editDetail();
				}
			}
		});
	}
	
	private void editDetail()
	{
		try
		{
			filename = "data" + categoryID + filename;
			fileInputStream = openFileInput(filename);

			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = db.parse(fileInputStream);

			fileInputStream.close();
			
			Log.d(TAG, "detail XML file has been loaded and parsed!");
			
			Element root = doc.getDocumentElement();
			
			Node  newDetail = doc.createElement(KEY_TOPIC);
			
			Node newID = doc.createElement(KEY_ID),
					   newDetailLabel = doc.createElement(KEY_TOPICLABEL), 
					   newDetailContent = doc.createElement(KEY_CONTENT), 
					   newImages = doc.createElement(KEY_IMAGES),
					   newCreatedDate = doc.createElement(KEY_CREATEDDATE),
					   newUpdatedDate = doc.createElement(KEY_UPDATEDDATE);
	
			newID.appendChild(doc.createTextNode(String.valueOf(currentID)));
			newDetailLabel.appendChild(doc.createTextNode(title.getText().toString()));
			newDetailContent.appendChild(doc.createTextNode(content.getText().toString()));
			
			newCreatedDate.appendChild(doc.createTextNode(createdDate));
			newUpdatedDate.appendChild(doc.createTextNode(now.toString()));

			newDetail.appendChild(newID);
			newDetail.appendChild(newDetailLabel);
			newDetail.appendChild(newDetailContent);
			newDetail.appendChild(newImages);
			newDetail.appendChild(newCreatedDate);
			newDetail.appendChild(newUpdatedDate);
			
			root.replaceChild(newDetail, doc.getElementsByTagName(KEY_TOPIC).item(currentID - 1));
			
			Log.d(TAG, "current topic element has been modified and saved.");
			
			TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(openFileOutput(filename, Context.MODE_PRIVATE)));
			
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
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (TransformerConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (TransformerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (TransformerFactoryConfigurationError e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			// Set the intent
			Log.d(TAG, "From EditDetailView to DetailView Intent.");
			Intent intent = new Intent(EditDetailView.this, DetailView.class);

			intent.putExtra("categoryID", categoryID);
			setResult(RESULT_OK, intent);

			finish();
		}
	}
	
	@Override
	public void onBackPressed()
	{
		// Set the intent
		Log.d(TAG, "From EditDetailView to DetailView Intent.");
		Intent intent = new Intent(EditDetailView.this, DetailView.class);

		intent.putExtra("categoryID", categoryID);
		setResult(RESULT_OK, intent);

		finish();
	}
}
