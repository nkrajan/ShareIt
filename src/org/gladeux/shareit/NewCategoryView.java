package org.gladeux.shareit;

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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NewCategoryView extends Activity
{
	public static final String KEY_CATEGORYDATA = "categoryData",
											   KEY_ID = "id", 
											   KEY_CATEGORY = "categoryLabel",
											   KEY_DESCRIPTION = "description", 
											   KEY_IMAGEPATH = "imagePath", 
											   TAG = CategoryView.class.getName();
	
	public String filename = "category.xml",
						   imagePath = "def.png",
						   xmlTag = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";

	public int currentID = 0;

	public FileInputStream fileInputStream;

	public TextView title, description;
	
	public ImageView ok_button;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Get rid of the title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_new_category_view);
		
		Log.d(TAG, "onCreate is called.");

		currentID = getIntent().getIntExtra(KEY_ID, 1);

		title = (TextView) findViewById(R.id.new_category_title);
		description = (TextView) findViewById(R.id.new_category_description);
		ok_button = (ImageView) findViewById(R.id.new_category_ok_button);
		
		Log.d(TAG, "all data have been loaded.");

		ok_button.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View view)
			{
				if (title.getText().toString().equals("")
						|| description.getText().toString().equals(""))
				{
					Toast.makeText(NewCategoryView.this, R.string.edit_confirmation, Toast.LENGTH_SHORT).show();
				}

				else
				{
					createNewCategory();
				}
			}
		});
	}

	private void createNewCategory()
	{
		try
		{
			fileInputStream = openFileInput(filename);

			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(fileInputStream);

			fileInputStream.close();
			
			Log.d(TAG, "category XML file has been loaded and parsed!");

			Node category = doc.getFirstChild();

			// Get the latest categoryData tag
			Node newCategory = doc.createElement(KEY_CATEGORYDATA);
			Node newID = doc.createElement(KEY_ID),
					   newCategoryLabel = doc.createElement(KEY_CATEGORY), 
					   newDescription = doc.createElement(KEY_DESCRIPTION),
					   newImagePath = doc.createElement(KEY_IMAGEPATH);

			newID.appendChild(doc.createTextNode(String.valueOf(currentID)));
			newCategoryLabel.appendChild(doc.createTextNode(title.getText().toString()));
			newDescription.appendChild(doc.createTextNode(description.getText().toString()));
			newImagePath.appendChild(doc.createTextNode(imagePath));

			newCategory.appendChild(newID);
			newCategory.appendChild(newCategoryLabel);
			newCategory.appendChild(newDescription);
			newCategory.appendChild(newImagePath);
			
			Log.d(TAG, "new category element has been created and loaded.");

			category.appendChild(newCategory);
			
			Log.d(TAG, "new category has been appended into the existing category XML.");

			TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(openFileOutput(filename,	Context.MODE_PRIVATE)));
			
			String newCategoryTopic = "data" + String.valueOf(currentID);
			
			Log.d(TAG, "category XML file has been re-written.");

			FileOutputStream output = openFileOutput(newCategoryTopic + ".xml", Context.MODE_PRIVATE);
			
			output.write(xmlTag.getBytes());
			output.write(("<" + newCategoryTopic + ">\n").getBytes());
			output.write(("</" + newCategoryTopic + ">\n").getBytes());
			output.close();
			
			Log.d(TAG, "new detail XML file has been created and initialized.");
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
			Log.d(TAG, "From NewCategoryView to CategoryView Intent.");
			startActivity(new Intent(NewCategoryView.this, CategoryView.class));
			finish();
		}
	}
	
	@Override
	public void onBackPressed()
	{
		Log.d(TAG, "From NewCategoryView to CategoryView Intent.");
		startActivity(new Intent(NewCategoryView.this, CategoryView.class));
		finish();
	}
}
