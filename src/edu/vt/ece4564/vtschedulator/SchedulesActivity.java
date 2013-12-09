/*
 * VT Schedulator
 * 12/11/2013
 */

package edu.vt.ece4564.vtschedulator;

import java.util.ListIterator;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import org.jasypt.util.text.BasicTextEncryptor;
import edu.vt.ece4564.shared.Course;
import edu.vt.ece4564.shared.CourseTime;
import edu.vt.ece4564.shared.Schedule;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

//-------------------------------------------------------------------------
/**
 * Schedules Activity
 * Passes user input to the server and waits for a reply.
 */
public class SchedulesActivity extends Activity implements OnFinished {
	// Global Variables
	Button backButton;
	Button nextButton;
	ListView scheduleView;
	static final String host = "bmacattack.dyndns.org";
	//static final String host = "192.168.1.209";
	static final String urlLocal = "http://" + host + ":8081/api?";
	String id = null;
	int newCount = 0;
	int getNew = 0;
	String user;
	String pass;
	String major;
	int min;
	int max;
	boolean useDars;
	boolean last;
	String[] classes;
	ClassAdapter adapter;

	// Display Variables
	ArrayList<String> finalList = new ArrayList<String>();
	ArrayList<ArrayList<Course>> sortList = new ArrayList<ArrayList<Course>>();
	ArrayList<Course> testList = new ArrayList<Course>();
	public ArrayList<Schedule> displaySchedule;
	private ListIterator<Schedule> scheduleIttr;
	int scheduleCount;
	Timer timer = new Timer();
	GetStatus getStatus = new GetStatus(this);
	boolean first = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_schedules);

			// GUI elements
			backButton = (Button) this.findViewById(R.id.button5);
			backButton.setEnabled(false);

			nextButton = (Button) this.findViewById(R.id.button7);
			nextButton.setEnabled(false);
			scheduleView = (ListView) this.findViewById(R.id.listView1);
			user = getIntent().getStringExtra("username");
			pass = getIntent().getStringExtra("password");
			last = getIntent().getBooleanExtra("last", false);
	         min = getIntent().getIntExtra("Min", 12);
            max = getIntent().getIntExtra("Max", 19);
            useDars = getIntent().getBooleanExtra("useDars", true);
            classes = getIntent().getStringArrayExtra("classes");

			// Listener for button to go back to main
			backButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// Switch to Main Activity
                    ListView listview = (ListView) findViewById(R.id.listView1);
                    nextButton.setEnabled(true);
                    adapter.setList(scheduleIttr.previous());
                    if (!scheduleIttr.hasPrevious()) backButton.setEnabled(false);
                    adapter.notifyDataSetChanged();

				}
			});

			// Listener to get new Schedule
			nextButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

			        ListView listview = (ListView) findViewById(R.id.listView1);
			        backButton.setEnabled(true);
			        Schedule sch = scheduleIttr.next();
			        if (sch == null || sch.getCourses() == null) {
			            nextButton.setEnabled(false);
			            return;
			        }
			        adapter.setList(sch);
			        adapter.notifyDataSetChanged();
			        if (!scheduleIttr.hasNext()) nextButton.setEnabled(false);
				}
			});


			// Request Schedules
			if (!last) {
    			GetTask task = new GetTask();
    			task.execute(user, pass, "BSCPECPE", Integer.toString(min),
    					Integer.toString(max), String.valueOf(useDars));
    			try {
    				id = task.get();
    				getStatus.latestId = id;
    				getStatus.User = user;
    				timer.scheduleAtFixedRate(getStatus, 60000, 30000);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			} catch (ExecutionException e) {
    				e.printStackTrace();
    			}
			} else {
			    RetTask task = new RetTask(this);
			    task.execute(user, pass, "last", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public void onBackPressed() {
        finish();
    }

	@Override
	public void postRun(ArrayList<Schedule> schedules) {
	    if (schedules == null) return;
		displaySchedule = new ArrayList<Schedule>(schedules);
		scheduleIttr = displaySchedule.listIterator();
		if (scheduleIttr.hasNext()) nextButton.setEnabled(true);
		backButton.setEnabled(false);
//		makeCurrentSchedule();
//		makeSortList();

        ListView listview = (ListView) findViewById(R.id.listView1);
        //makeFinalList();
        adapter = new ClassAdapter(this);
        adapter.setList(scheduleIttr.next());
        listview.setAdapter(adapter);


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

	// Wait for the Schedules to be made
	protected class GetStatus extends TimerTask {
		public String latestId;
		public String User;
		OnFinished listner;

		public GetStatus(OnFinished listner) {
			this.listner = listner;
		}

		@Override
		public void run() {
			try {
				String send = "mode=process&username="
						+ URLEncoder.encode(User, "UTF-8") + "&id="
						+ URLEncoder.encode(latestId, "UTF-8");
				URL url = new URL(urlLocal + send);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setRequestMethod("GET");
				connection.setDoInput(true);
				connection.connect();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));

				if (reader.readLine().equals("Success")) {
					timer.cancel();
					RetTask ret = new RetTask(listner);

					if (newCount == 5) {
						newCount = 0;
						getNew++;
					}
					ret.execute(user, pass, id, Integer.toString(getNew));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	// Requests a Schedule
	protected class GetTask extends AsyncTask<String, Integer, String> {
		@Override
		/*
		 * @param params[0] The username
		 *
		 * @param params[1] The password in plain text
		 *
		 * @param params[2] The users major, currently it is as hokieSpa
		 * requires. eg bs in Cpe is bscpecpe
		 *
		 * @param params[3] the min amount of credits to take
		 *
		 * @param params[4] The max amount of credits to take
		 *
		 * @return an String containing the unique value to later grab the
		 * schedules
		 */
		protected String doInBackground(String... params) {
			String requestCode = null;
			try {
				BasicTextEncryptor encrypter = new BasicTextEncryptor();
				encrypter.setPassword("Sj872!=nc>S2%whs6");
				String send = "mode=request&username="
						+ URLEncoder.encode(params[0], "UTF-8")
						+ "&passwd="
						+ URLEncoder.encode(encrypter.encrypt(params[1]),
								"UTF-8") + "&major="
						+ URLEncoder.encode(params[2], "UTF-8") + "&min="
						+ URLEncoder.encode(params[3], "UTF-8") + "&max="
						+ URLEncoder.encode(params[4], "UTF-8") + "&usedars="
						+ URLEncoder.encode(params[5], "UTF-8");

				StringBuilder builder = new StringBuilder();
				builder.append(send);
				int i = 0;

				for (String clas : classes) {
					builder.append("&class" + String.valueOf(i++) + "="
							+ URLEncoder.encode(clas, "UTF-8"));
				}

				URL url = new URL(urlLocal + builder.toString());
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setRequestMethod("GET");
				connection.setDoInput(true);
				connection.connect();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				requestCode = reader.readLine();
				connection.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return requestCode;
		}
	}

	// Gets the schedule from the server. All parameters are Strings
	protected class RetTask extends
			AsyncTask<String, Integer, ArrayList<Schedule>> {
		ArrayList<Schedule> schedules = null;
		OnFinished listner;

		public RetTask(OnFinished listner) {
			this.listner = listner;
		}

		@Override
		/*
		 * @param params[0] The username
		 *
		 * @param params[1] The password in plain text
		 *
		 * @param params[2] the Id to be fetched, returned from the request
		 *
		 * @param params[3] The location. 0 gets the first 5 schedules, 1 gets
		 * the next 5 and so on
		 *
		 * @return an Arraylist of schedules
		 */
		protected ArrayList<Schedule> doInBackground(String... params) {
			try {
				BasicTextEncryptor encrypter = new BasicTextEncryptor();
				encrypter.setPassword("Sj872!=nc>S2%whs6");
				String send = "mode=grab&username="
						+ URLEncoder.encode(params[0], "UTF-8")
						+ "&passwd="
						+ URLEncoder.encode(encrypter.encrypt(params[1]),
								"UTF-8") + "&id="
						+ URLEncoder.encode(params[2], "UTF-8") + "&loc="
						+ URLEncoder.encode(params[3], "UTF-8");

				URL url = new URL(urlLocal + send);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setRequestMethod("GET");
				connection.setDoInput(true);
				connection.connect();

				ObjectInputStream input = new ObjectInputStream(
						connection.getInputStream());
				schedules = (ArrayList<Schedule>) input.readObject();

				connection.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return schedules;
		}

		@Override
		protected void onPostExecute(ArrayList<Schedule> result) {
			listner.postRun(result);
		}
	}

	// Sorts the List of Courses
	private void makeSortList() {
		ArrayList<Course> day1 = new ArrayList<Course>();
		ArrayList<Course> day2 = new ArrayList<Course>();
		ArrayList<Course> day3 = new ArrayList<Course>();
		ArrayList<Course> day4 = new ArrayList<Course>();
		ArrayList<Course> day5 = new ArrayList<Course>();

		for (int i = 0; i < testList.size(); i++) {
			CourseTime test = testList.get(i).getTime();

			int[] days;
			days = test.getDays();

			for (int k = 0; k < days.length; k++) {
				if (days[k] == 1) {
					day1.add(testList.get(i));
				} else if (days[k] == 2) {
					day2.add(testList.get(i));
				} else if (days[k] == 3) {
					day3.add(testList.get(i));
				} else if (days[k] == 4) {
					day4.add(testList.get(i));
				} else if (days[k] == 5) {
					day5.add(testList.get(i));
				}
			}
		}

		sortList.add(sortTimes(day1));
		sortList.add(sortTimes(day2));
		sortList.add(sortTimes(day3));
		sortList.add(sortTimes(day4));
		sortList.add(sortTimes(day5));
	}

	// Sorts the Course Times
	private ArrayList<Course> sortTimes(ArrayList<Course> courses) {
		Course courseMin = new Course();

		for (int i = 0; i < courses.size(); i++) {
			courseMin = courses.get(i);

			for (int k = 0; k < courses.size(); k++) {
				if (courses.get(k).getTime().getStartTime() > courseMin
						.getTime().getStartTime()) {
					courseMin = courses.get(k);
					Course courseTemp = new Course();
					courseTemp = courses.get(i);
					courses.set(i, courses.get(k));
					courses.set(k, courseTemp);
				}
			}
		}

		return courses;
	}

	// Makes the final list of courses
	private void makeFinalList() {
		String Day = "";

		for (int i = 0; i < sortList.size(); i++) {
			if (i == 0) {
				Day = "Monday";
			} else if (i == 1) {
				Day = "Tuesday";
			} else if (i == 2) {
				Day = "Wednesday";
			} else if (i == 3) {
				Day = "Thursday";
			} else if (i == 4) {
				Day = "Friday";
			}

			finalList.add(Day);

			for (int k = 0; k < sortList.get(i).size(); k++) {
				finalList.add("CRN: " + sortList.get(i).get(k).getCrn()
						+ "\nClassName: " + sortList.get(i).get(k).getName()
						+ "\nTime: "
						+ sortList.get(i).get(k).getTime().toString());
			}

			finalList.add("\n");
		}
	}

	// Makes the Schedules to be displayed
	public void makeCurrentSchedule() {
		testList.clear();

		if (first)
			scheduleCount = 0;
		first = false;

		for (int i = 0; i < displaySchedule.get(scheduleCount).getCourses()
				.size(); i++) {
			testList.add(displaySchedule.get(scheduleCount).getCourses().get(i));
		}
	}
}
