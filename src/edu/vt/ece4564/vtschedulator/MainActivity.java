/*
 * VT Schedulator
 * 12/11/2013
 */

package edu.vt.ece4564.vtschedulator;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

//-------------------------------------------------------------------------
/**
 * Main Activity 
 * Allows the user to pick the minimum and maximum number of credits they 
 * want to take as well as any classes they already know they want to take.
 */
public class MainActivity extends Activity {
	// Global Variables
	TextView minText;
	TextView maxText;
	SeekBar seekMin;
	SeekBar seekMax;
	Button getButton;
	Button addClass;
	TextView classesAdded;
	TextView addText;
	ArrayList<String> classes = new ArrayList<String>();
	boolean first = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);

			// GUI elements
			minText = (TextView) this.findViewById(R.id.textView1);
			maxText = (TextView) this.findViewById(R.id.textView2);
			seekMin = (SeekBar) this.findViewById(R.id.seekBar1);
			seekMax = (SeekBar) this.findViewById(R.id.seekBar2);
			getButton = (Button) this.findViewById(R.id.button2);
			addClass = (Button) findViewById(R.id.addButton);
			classesAdded = (TextView) findViewById(R.id.viewAdd);
			addText = (TextView) findViewById(R.id.addClas);
			classesAdded.setText("No additional classes added");
			addText.setText("Add classes to include");
			
			
			addText.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					addText.setText("");
				}
			});

			// Listener for button to add a class
			addClass.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (first)
						classesAdded.setText("");
					first = false;
					String text = addText.getText().toString();
					classes.add(text);
					addText.setText("Class added");
					classesAdded.append(text + "\n");
				}
			});

			// Listener for button to get schedules
			getButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (v.equals(getButton)) {
						// Switch to Schedule Activity
						int min = seekMin.getProgress() + 12;
						int max = seekMax.getProgress() + 12;

						Intent intent = new Intent(getApplicationContext(),
								SchedulesActivity.class);
						intent.putExtra("Min", min);
						intent.putExtra("Max", max);
						intent.putExtra("classes",
								classes.toArray(new String[classes.size()]));
						startActivity(intent);
						finish();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Inflate the menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			// This adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// If menu item is selected
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		try {
			switch (item.getItemId()) {
			// Switch to Settings Activity
			case R.id.Settings:
				startActivity(new Intent(getApplicationContext(),
						SettingsActivity.class));
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
