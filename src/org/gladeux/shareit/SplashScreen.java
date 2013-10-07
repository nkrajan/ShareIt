package org.gladeux.shareit;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity
{

	// Class variable declarations
	private long SPLASH_TIME = 3000;
	
	private String TAG = SplashScreen.class.getName();

	private static final String PREFS = "Prefs";

	public String filename = "category.xml",
						   xmlTag = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<category></category>";
	
	public String[] assetFiles;
	
	public FileOutputStream outFile;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Fullscreen mode
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Display the content
		setContentView(R.layout.activity_splash_screen);

		Log.d(TAG, "onCreate is called.");

		SharedPreferences settings = getSharedPreferences(PREFS,
				Context.MODE_PRIVATE);

		// For the first launch
		if (settings.getBoolean("firstLaunch", true))
		{
			Log.d(TAG, "The launch is its first time.");

			try
			{
				// Create category XML file
				outFile = openFileOutput(filename, Context.MODE_PRIVATE);

				// Write the XML tag and body tag
				outFile.write(xmlTag.getBytes());

				Log.d(TAG, "new category XML file has been created.");

				// Close the file
				outFile.close();

				Log.d(TAG, "asset files are now loading.");

				assetFiles = getAssets().list("");

				for (String filename : assetFiles)
				{
					InputStream in = null;
					OutputStream out = null;

					// Open current asset file
					in = getAssets().open(filename);
					out = openFileOutput(filename, Context.MODE_PRIVATE);

					byte[] buffer = new byte[1024];
					int read;

					// Copy it into the internal memory
					while ((read = in.read(buffer)) != -1)
					{
						out.write(buffer, 0, read);
					}

					in.close();
					out.close();
				}
			}
			catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Log.d(TAG, "First launch task has been completed.");

			// Set the launch flag
			settings.edit().putBoolean("firstLaunch", false).commit();
		}

		else
		{
			Log.d(TAG, "The launch is NOT the first time.");
		}

		// Fire up the intent launcher
		IntentLauncher intentLauncher = new IntentLauncher();
		intentLauncher.start();
	}

	private class IntentLauncher extends Thread
	{

		@Override
		public void run()
		{
			try
			{
				// Sleep for the splash time
				sleep(SPLASH_TIME);
			}
			catch (Exception e)
			{
				Log.e(TAG, e.getMessage());
			}
			finally
			{
				// Proceed to CategoryView activity
				Log.d(TAG, "From SplashScreen to CategoryView Intent.");
				startActivity(new Intent(SplashScreen.this, CategoryView.class));

				finish();
			}
		}
	}
}
