/*
 * VT Schedulator
 */

package edu.vt.ece4564.vtschedulator;

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

public class MainActivity extends Activity {
	// Global Variables
	TextView minText;
	TextView maxText;
	SeekBar seekMin;
	SeekBar seekMax;
	Button getButton;

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

			// Listener for button to get schedules
			getButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (v.equals(getButton)) {
						// Switch to Schedule Activity
						Intent i = getIntent();
						String user = i.getStringExtra("Username");
						String pass = i.getStringExtra("Password");
						String major = i.getStringExtra("Major");

						int min = seekMin.getProgress() + 12;
						int max = seekMax.getProgress() + 12;

						Intent intent = new Intent(getApplicationContext(),
								SchedulesActivity.class);
						intent.putExtra("Username", user);
						intent.putExtra("Password", pass);
						intent.putExtra("Major", major);
						intent.putExtra("Min", min);
						intent.putExtra("Max", max);
						startActivity(intent);
						finish();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			// Inflate the menu; this adds items to the action bar if it is
			// present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

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
