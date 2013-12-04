/*
 * VT Schedulator
 * 12/11/2013
 */

package edu.vt.ece4564.vtschedulator;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

//-------------------------------------------------------------------------
/**
 * Settings Activity 
 * Allows the user to set their Username, Password, and Major.
 */
public class SettingsActivity extends Activity {
	// Global Variables
	EditText username;
	EditText password;
	Spinner majorSpinner;
	Button saveButton;
	TextView userText;
	TextView passText;
	TextView majorText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_settings);

			// GUI elements
			username = (EditText) this.findViewById(R.id.addClas);
			password = (EditText) this.findViewById(R.id.editText2);
			majorSpinner = (Spinner) this.findViewById(R.id.spinner1);
			saveButton = (Button) this.findViewById(R.id.button4);
			userText = (TextView) this.findViewById(R.id.addClass);
			passText = (TextView) this.findViewById(R.id.viewAdd);
			majorText = (TextView) this.findViewById(R.id.textView5);

			// Listener for button to save user information
			saveButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String user = username.getText().toString();
					String pass = password.getText().toString();
					String major = majorSpinner.getSelectedItem().toString();
					major = major.substring(0, major.indexOf(" "));

					SharedPreferences.Editor editor = getPreferences(
							MODE_PRIVATE).edit();
					editor.putString("Username", user);
					editor.putString("Password", pass);
					editor.putString("Major", major);
					editor.commit();

					// Switch to Main Activity
					startActivity(new Intent(getApplicationContext(),
							MainActivity.class));
					finish();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}