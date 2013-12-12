/*
 * VT Schedulator
 * 12/11/2013
 */

package edu.vt.ece4564.vtschedulator;

import android.widget.Toast;
import android.widget.CheckBox;
import java.util.regex.Pattern;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
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
	Button getLast;
	CheckBox useDars;
	TextView classesAdded;
	TextView addText;
	ArrayList<String> classes = new ArrayList<String>();
	boolean first = true;
	String username;
	String password;
	String major;

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
			getLast = (Button) findViewById(R.id.getLast);
			useDars = (CheckBox) findViewById(R.id.useDars);
			classesAdded.setText("No additional classes added");
			addText.setText("Add classes to include");
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
			username = pref.getString("pid", null);
			password = pref.getString("password", null);

			maxText.setText("Max:"+((Integer)(seekMax.getProgress()+12)).toString());
            minText.setText("Min:"+((Integer)(seekMin.getProgress()+12)).toString());

            seekMin.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

                   @Override
                   public void onProgressChanged(SeekBar seekBar, int progress,
                     boolean fromUser) {
                    // TODO Auto-generated method stub

                   }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                    //seekBarValue.setText(seekMin.getProgress());
                    //Toast.makeText(MainActivity.this, ((Integer)(seekMin.getProgress()+12)).toString(), Toast.LENGTH_SHORT).show();
                    minText.setText("Min:"+((Integer)(seekMin.getProgress()+12)).toString());

                } });

            seekMax.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

                   @Override
                   public void onProgressChanged(SeekBar seekBar, int progress,
                     boolean fromUser) {
                    // TODO Auto-generated method stub

                   }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                    //seekBarValue.setText(seekMin.getProgress());
                    //Toast.makeText(MainActivity.this, ((Integer)(seekMin.getProgress()+12)).toString(), Toast.LENGTH_SHORT).show();
                    maxText.setText("Max:"+((Integer)(seekMax.getProgress()+12)).toString());

                } });



			addText.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					addText.setText("");
				}
			});

			getLast.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v)
                {
                    if (username == null || password == null) {
                        return;
                    }
                    int min = seekMin.getProgress() + 12;
                    int max = seekMax.getProgress() + 12;

                    Intent intent = new Intent(MainActivity.this,
                            SchedulesActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    intent.putExtra("last", true);
                    startActivity(intent);

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
					Pattern pattern = Pattern.compile("[a-zA-Z]{2,4} [0-9]{4}");
					if (!pattern.matcher(text).matches()) return;
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
					    if (username == null || password == null) {
					        return;
					    }
						int min = seekMin.getProgress() + 12;
						int max = seekMax.getProgress() + 12;
						Toast.makeText(getApplicationContext(), "I'll be back!", Toast.LENGTH_LONG).show();
						Intent intent = new Intent(MainActivity.this,
								SchedulesActivity.class);
						intent.putExtra("Min", min);
						intent.putExtra("Max", max);
						intent.putExtra("username", username);
						intent.putExtra("password", password);

						intent.putExtra("classes",
								classes.toArray(new String[classes.size()]));
						intent.putExtra("useDars", useDars.isChecked());
						startActivity(intent);
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
				Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
				startActivity(settings);
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
