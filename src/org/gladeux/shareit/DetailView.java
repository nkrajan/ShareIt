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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class DetailView extends Activity
{
	public static final String KEY_TOPIC = "topic",
											   KEY_ID = "id",
											   KEY_TOPICLABEL = "topicLabel",
											   KEY_CONTENT = "content",
											   KEY_IMAGES = "images",
											   KEY_IMAGE = "image",
											   KEY_CREATEDDATE = "createdDate",
											   KEY_UPDATEDDATE = "updatedDate",
											   KEY_CATEGORY = "categoryLabel",
											   KEY_CATEGORYID = "id",
											   TAG = DetailView.class.getName();
	
	public static String categoryID = "",
									  category = "",
									  filename = "";
	
	public int currentID = 0;

	public ArrayList<HashMap<String, String>> topicList = new ArrayList<HashMap<String,String>>();
	
	public ImageView listButton, newButton;
	
	public FileInputStream fileInputStream;
	
	public TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Get rid of the title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		Intent getIntent = getIntent();

		categoryID = getIntent.getStringExtra(KEY_CATEGORYID);
		category = getIntent.getStringExtra(KEY_CATEGORY);

		setContentView(R.layout.activity_detail_view);
		
		title = (TextView) findViewById(R.id.detail_view_menu_label);
		title.setText(category);
		
		filename = "data" + categoryID + ".xml";
		
		Log.d(TAG, "onCreate is called.");
		
		// Load the categories
		parseXML();
	}
	
	private void parseXML()
	{
		    try
			{
		    	Log.d(TAG, "parseXML is called.");
		    	
				// Get the DocumentBuilderFactory and DocumentBuilder
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				
				// Parse the category XML file from assets folder
				Document doc = db.parse(openFileInput(filename));
				doc.getDocumentElement().normalize();
				
				Log.d(TAG, "Opening " + filename);
				Log.d(TAG, "detail XML file is loaded, parsed, and normalized!");
				
				// Get all the categoryData elements
				NodeList nl = doc.getElementsByTagName(KEY_TOPIC);
				
				currentID = nl.getLength();
				
				Log.d(TAG, "topic XML tags are parsed!");

				for (int i = 0; i < nl.getLength(); i++)
				{
					// Create a new hash map
					HashMap<String, String> map = new HashMap<String, String>();
					
					// Get the current categoryData
					Element e = (Element) nl.item(i);
					
					// For each current categoryData element
					if(e.getNodeType() == Node.ELEMENT_NODE)
					{
						// Extract each element from current categoryData
						Element idElement = (Element) e.getElementsByTagName(KEY_ID).item(0),
								       topicElement = (Element) e.getElementsByTagName(KEY_TOPICLABEL).item(0),
								       contentElement = (Element) e.getElementsByTagName(KEY_CONTENT).item(0),
								       createdDateElement = (Element) e.getElementsByTagName(KEY_CREATEDDATE).item(0),
								       updatedDateElement = (Element) e.getElementsByTagName(KEY_UPDATEDDATE).item(0);
						
						Log.d(TAG, "Each XML element has been extracted.");

						// Get the text node from each element
						NodeList idText = idElement.getChildNodes(),
										 topicText = topicElement.getChildNodes(),
										 contentText = contentElement.getChildNodes(),
										 createdDateText = createdDateElement.getChildNodes(),
										 updatedDateText = updatedDateElement.getChildNodes();
						
						Log.d(TAG, "Text data in each XML element have been extracted.");
						
						// Extract the text from each text node
						String id = ((Node) idText.item(0)).getNodeValue().trim(),
								   topicLabel = ((Node) topicText.item(0)).getNodeValue().trim(),
								   content = ((Node) contentText.item(0)).getNodeValue().trim(),
								   createdDate = ((Node) createdDateText.item(0)).getNodeValue().trim(),
								   updatedDate = ((Node) updatedDateText.item(0)).getNodeValue().trim();
						
						Log.d(TAG, "Text data have been successfully assigned to String variables.");

						// Store each XML data into the hashmap
						map.put(KEY_ID, id);
						map.put(KEY_TOPICLABEL, topicLabel);
						map.put(KEY_CONTENT, content);
						map.put(KEY_CREATEDDATE, createdDate);
						map.put(KEY_UPDATEDDATE, updatedDate);
						
						Log.d(TAG, "String variables have successfully been saved to the hashmap.");
						
						// Store current hashmap into the ArrayList
						topicList.add(map);
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
				ListView listview = (ListView) findViewById(R.id.detail_view_detail_list);
				
				// Create the custom list adapter
				CustomDetailListAdapter adapter = new CustomDetailListAdapter(this, topicList);
				
				// Set the adapter
				listview.setAdapter(adapter);
				
				Log.d(TAG, "The listview has successfully set its adapter.");
				
				registerForContextMenu(listview);
				
				newButton = (ImageView) findViewById(R.id.detail_view_detail_list_new_button);
				
				newButton.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						Log.d(TAG, "From DetailView to NewDetailView Intent.");
						Intent intent = new Intent(DetailView.this, NewDetailView.class);

						intent.putExtra(KEY_ID, currentID + 1);
						intent.putExtra("categoryID", categoryID);

						startActivityForResult(intent, 1);
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
						Log.d(TAG, "From DetailView to TopicView Intent.");
						Intent intent = new Intent(DetailView.this, TopicView.class);
						
						// Store needed information for next activity
						intent.putExtra(KEY_ID, topicList.get(position).get(KEY_ID));
						intent.putExtra(KEY_TOPICLABEL, topicList.get(position).get(KEY_TOPICLABEL));
						intent.putExtra(KEY_CONTENT, topicList.get(position).get(KEY_CONTENT));
						intent.putExtra(KEY_CREATEDDATE, topicList.get(position).get(KEY_CREATEDDATE));
						intent.putExtra(KEY_UPDATEDDATE, topicList.get(position).get(KEY_UPDATEDDATE));
						intent.putExtra("categoryID", categoryID);

						// Start the activity
						startActivityForResult(intent, 2);
					}
				});
			}
	}
	
	@Override
	public void onBackPressed()
	{
		// Set the intent
		Log.d(TAG, "From DetailView to CategoryView Intent.");
		Intent intent = new Intent(DetailView.this, CategoryView.class);

		// Start the activity
		startActivity(intent);
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// NewDetailView
		if(requestCode == 1 || requestCode == 2 || requestCode == 3)
		{
			if(resultCode == RESULT_OK)
			{
				categoryID = data.getStringExtra("categoryID");
				
				// Load the categories
				topicList = null;
				topicList = new ArrayList<HashMap<String,String>>();
				parseXML();
			}
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.detail_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final Intent intent;
		AlertDialog.Builder alertDialogBuilder;
		
		switch (item.getItemId())
		{
			case R.id.edit_detail_menu:
				intent = new Intent(DetailView.this, EditDetailView.class);
				
				// Store needed information for next activity
				intent.putExtra(KEY_ID, topicList.get(info.position).get(KEY_ID));
				intent.putExtra(KEY_TOPICLABEL, topicList.get(info.position).get(KEY_TOPICLABEL));
				intent.putExtra(KEY_CONTENT, topicList.get(info.position).get(KEY_CONTENT));
				intent.putExtra(KEY_CREATEDDATE, topicList.get(info.position).get(KEY_CREATEDDATE));
				intent.putExtra(KEY_UPDATEDDATE, topicList.get(info.position).get(KEY_UPDATEDDATE));
				intent.putExtra("categoryID", categoryID);

				alertDialogBuilder = new AlertDialog.Builder(DetailView.this);
				
				alertDialogBuilder.setTitle(R.string.edit_detail_dialog_message_title)
											   .setMessage(R.string.edit_detail_dialog_message_content)
											   .setPositiveButton(R.string.dialog_message_yes, new DialogInterface.OnClickListener()
											{
												
												@Override
												public void onClick(DialogInterface dialog, int which)
												{
													Log.d(TAG, "From DetailView to EditDetailView Intent.");

													// Start the activity
													startActivityForResult(intent, 3);
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
				
			case R.id.remove_detail_menu:
				// Build the dialog message box
				alertDialogBuilder = new AlertDialog.Builder(DetailView.this);
				
				alertDialogBuilder.setTitle(R.string.delete_detail_dialog_message_title)
											   .setMessage(R.string.delete_detail_dialog_message_content)
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
														
														Log.d(TAG, "detail XML file has been loaded and parsed!");
														
														Element root = doc.getDocumentElement();
														
														root.removeChild(doc.getElementsByTagName(KEY_TOPIC).item(Integer.parseInt(topicList.get(info.position).get(KEY_ID)) - 1));
														
														Log.d(TAG, "target detail element has been removed.");
														
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
														// Load the categories
														topicList = null;
														topicList = new ArrayList<HashMap<String,String>>();
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
