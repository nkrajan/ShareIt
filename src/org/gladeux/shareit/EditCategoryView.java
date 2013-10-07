package org.gladeux.shareit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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


public class EditCategoryView extends Activity
{
	public static final String KEY_CATEGORYDATA = "categoryData",
											   KEY_ID = "id", 
											   KEY_CATEGORY = "categoryLabel",
											   KEY_DESCRIPTION = "description", 
											   KEY_IMAGEPATH = "imagePath", 
											   TAG = CategoryView.class.getName();
	
	public String filename = "category.xml",
						   imagePath = "def.png";
	
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
		
		setContentView(R.layout.activity_edit_category_view);
		
		Log.d(TAG, "onCreate is called.");
		
		title = (TextView) findViewById(R.id.edit_category_title);
		description = (TextView) findViewById(R.id.edit_category_description);
		ok_button = (ImageView) findViewById(R.id.edit_category_ok_button);
		
		currentID = Integer.parseInt(getIntent().getStringExtra(KEY_ID));
		title.setText(getIntent().getStringExtra(KEY_CATEGORY));
		description.setText(getIntent().getStringExtra(KEY_DESCRIPTION));
		imagePath = getIntent().getStringExtra(KEY_IMAGEPATH);
		
		Log.d(TAG, "all data have been loaded.");
		
		ok_button.setOnClickListener(new View.OnClickListener()
		{
		
			@Override
			public void onClick(View view)
			{
				if (title.getText().toString().equals("") || description.getText().toString().equals(""))
				{
					Toast.makeText(EditCategoryView.this, R.string.edit_confirmation, Toast.LENGTH_SHORT).show();
				}
			
				else
				{
					editCategory();
				}
			}
		});
	}
		
	private void editCategory()
	{
		try
		{
			fileInputStream = openFileInput(filename);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			
			DocumentBuilder db =factory.newDocumentBuilder();
			Document doc = db.parse(fileInputStream);
			
			fileInputStream.close();
			
			Log.d(TAG, "category XML file has been loaded and parsed!");
			
			Element root = doc.getDocumentElement();
			
			Node  newCategory = doc.createElement(KEY_CATEGORYDATA);
			
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
			
			root.replaceChild(newCategory, doc.getElementsByTagName(KEY_CATEGORYDATA).item(currentID - 1));
			
			Log.d(TAG, "current category element has been modified and saved.");
			
			TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(openFileOutput(filename, Context.MODE_PRIVATE)));
			
			Log.d(TAG, "category XML file has been re-written.");
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
			Log.d(TAG, "From EditCategoryView to CategoryView Intent.");
			startActivity(new Intent(EditCategoryView.this, CategoryView.class));
			finish();
		}
	}
	
	@Override
	public void onBackPressed()
	{
	Log.d(TAG, "From EditCategoryView to CategoryView Intent.");
	startActivity(new Intent(EditCategoryView.this, CategoryView.class));
	finish();
	}
}
