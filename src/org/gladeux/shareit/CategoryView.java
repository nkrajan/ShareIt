package org.gladeux.shareit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class CategoryView extends Activity implements OnItemLongClickListener
{
	public static final String KEY_CATEGORYDATA = "categoryData",
											   KEY_ID = "id",
											   KEY_CATEGORY = "categoryLabel",
											   KEY_DESCRIPTION = "description",
											   KEY_IMAGEPATH = "imagePath",
											   TAG = CategoryView.class.getName();
	
	public ArrayList<HashMap<String, String>> categoryList;
	
	public ImageView listButton, newButton;
	
	public String filename = "category.xml";
	
	public int currentID = 0;
	
	public static final long CONFIRM_LENGTH = 2000;
	
	public FileInputStream fileInputStream;
	
	private boolean doubleBackButton = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Get rid of the title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_category_view);
		
		Log.d(TAG, "onCreate is called.");

		// Load the categories
		parseXML();
	}
	
	private void parseXML()
	{
		    try
			{
		    	Log.d(TAG, "parseXML is called.");
		    	
		    	categoryList = null;
		    	categoryList = new ArrayList<HashMap<String,String>>();

		    	// Grab the category XML file from internal memory
		    	fileInputStream = openFileInput(filename);

				// Get the DocumentBuilderFactory and DocumentBuilder
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				
				// Parse the category XML file from assets folder
				Document doc = db.parse(fileInputStream);
				
				fileInputStream.close();
				
				doc.getDocumentElement().normalize();
				
				Log.d(TAG, "category XML file is loaded, parsed, and normalized!");
				
				// Get all the categoryData elements
				NodeList nl = doc.getElementsByTagName(KEY_CATEGORYDATA);
				
				Log.d(TAG, "categoryData XML tags are parsed!");
				
				currentID = nl.getLength();

				for (int i = 0; i < nl.getLength(); i++)
				{
					// Create a new hash map
					HashMap<String, String> map = new HashMap<String, String>();
					
					// Get the current categoryData
					Element e = (Element) nl.item(i);
					
					Log.d(TAG, "Extracting XML data from categoryData" + i + " tag");
					
					// For each current categoryData element
					if(e.getNodeType() == Node.ELEMENT_NODE)
					{
						// Extract each element from current categoryData
						Element idElement = (Element) e.getElementsByTagName(KEY_ID).item(0),
								       categoryElement = (Element) e.getElementsByTagName(KEY_CATEGORY).item(0),
								       descriptionElement = (Element) e.getElementsByTagName(KEY_DESCRIPTION).item(0),
								       imagePathElement = (Element) e.getElementsByTagName(KEY_IMAGEPATH).item(0);
						
						Log.d(TAG, "Each XML element has been extracted.");

						// Get the text node from each element
						NodeList idText = idElement.getChildNodes(),
										 categoryText = categoryElement.getChildNodes(),
										 descriptionText = descriptionElement.getChildNodes(),
										 imagePathText = imagePathElement.getChildNodes();
						
						Log.d(TAG, "Text data in each XML element have been extracted.");
						
						// Extract the text from each text node
						String id = ((Node) idText.item(0)).getNodeValue().trim(),
								   category = ((Node) categoryText.item(0)).getNodeValue().trim(),
								   description = ((Node) descriptionText.item(0)).getNodeValue().trim(),
								   imagePath = ((Node) imagePathText.item(0)).getNodeValue().trim();
						
						Log.d(TAG, "Text data have been successfully assigned to String variables.");

						// Store each XML data into the hashmap
						map.put(KEY_ID, id);
						map.put(KEY_CATEGORY, category);
						map.put(KEY_DESCRIPTION, description);
						map.put(KEY_IMAGEPATH, imagePath);
						
						Log.d(TAG, "String variables have successfully been saved to the hashmap.");
						
						// Store current hashmap into the ArrayList
						categoryList.add(map);
					}
				}
			}
			catch (ParserConfigurationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (SAXException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				// Get the listview
				ListView listview = (ListView) findViewById(R.id.category_view_category_list);
				
				// Set the adapter
				listview.setAdapter(new CustomCategoryListAdapter(this, categoryList));
				
				registerForContextMenu(listview);
				
				Log.d(TAG, "The listview has successfully set its adapter.");

				newButton = (ImageView) findViewById(R.id.category_view_category_list_new_button);
				
				// For new button click event
				newButton.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						Log.d(TAG, "From CategoryView to NewCategoryView Intent.");
						Intent intent = new Intent(CategoryView.this, NewCategoryView.class);

						intent.putExtra(KEY_ID, currentID + 1);

						startActivity(intent);
						finish();
					}
				});
				
				// For the click event listener for the corresponding item
				listview.setOnItemClickListener(new OnItemClickListener()
				{

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id)
					{
						// Set the intent
						Log.d(TAG, "From CategoryView to DetailView Intent.");
						Intent intent = new Intent(CategoryView.this, DetailView.class);
						
						// Store needed information for next activity
						intent.putExtra(KEY_ID, categoryList.get(position).get(KEY_ID));
						intent.putExtra(KEY_CATEGORY, categoryList.get(position).get(KEY_CATEGORY));
						
						// Start the activity
						startActivity(intent);
						finish();
					}
				});
			}
	}
	
	@Override
	public void onBackPressed()
	{
		if(doubleBackButton)
		{
			super.onBackPressed();
			return;
		}
		
		doubleBackButton = true;
		Toast.makeText(this, R.string.exit_confirmation, Toast.LENGTH_LONG).show();
		
		new Handler().postDelayed(new Runnable()
		{
			
			@Override
			public void run()
			{
				doubleBackButton = false;
			}
		}, CONFIRM_LENGTH);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.category_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final Intent intent;
		AlertDialog.Builder alertDialogBuilder;
		
		switch (item.getItemId())
		{
			case R.id.edit_category_menu:
				intent = new Intent(CategoryView.this, EditCategoryView.class);
				
				// Store needed information for next activity
				intent.putExtra(KEY_ID, categoryList.get(info.position).get(KEY_ID));
				intent.putExtra(KEY_CATEGORY, categoryList.get(info.position).get(KEY_CATEGORY));
				intent.putExtra(KEY_DESCRIPTION, categoryList.get(info.position).get(KEY_DESCRIPTION));
				intent.putExtra(KEY_IMAGEPATH, categoryList.get(info.position).get(KEY_IMAGEPATH));

				// Build the dialog message box
				alertDialogBuilder = new AlertDialog.Builder(CategoryView.this);
				
				alertDialogBuilder.setTitle(R.string.edit_category_dialog_message_title)
											   .setMessage(R.string.edit_category_dialog_message_content)
											   .setPositiveButton(R.string.dialog_message_yes, new DialogInterface.OnClickListener()
											{
												
												@Override
												public void onClick(DialogInterface dialog, int which)
												{
													Log.d(TAG, "From CategoryView to EditCategoryView Intent.");

													// Start the activity
													startActivity(intent);
													finish();
												}
											})
											.setNegativeButton(R.string.dialog_message_no, new DialogInterface.OnClickListener()
											{
												
												@Override
												public void onClick(DialogInterface dialog, int which)
												{
													dialog.cancel();
												}
											});
				
				alertDialogBuilder.create().show();	
				break;
				
			case R.id.remove_category_menu:
				// Build the dialog message box
				alertDialogBuilder = new AlertDialog.Builder(CategoryView.this);
				
				alertDialogBuilder.setTitle(R.string.delete_category_dialog_message_title)
											   .setMessage(R.string.delete_category_dialog_message_content)
											   .setPositiveButton(R.string.dialog_message_yes, new DialogInterface.OnClickListener()
											{
												
												@Override
												public void onClick(DialogInterface dialog, int which)
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
														
														root.removeChild(doc.getElementsByTagName(KEY_CATEGORYDATA).item(Integer.parseInt(categoryList.get(info.position).get(KEY_ID)) - 1));

														Log.d(TAG, "target category element has been removed.");
														
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
														parseXML();
													}
												}
											})
											.setNegativeButton(R.string.dialog_message_no, new DialogInterface.OnClickListener()
											{
												
												@Override
												public void onClick(DialogInterface dialog, int which)
												{
													dialog.cancel();
												}
											});
				
				alertDialogBuilder.create().show();	
				break;

			default:
				return super.onContextItemSelected(item);
		}
		return  true;
	}
}
