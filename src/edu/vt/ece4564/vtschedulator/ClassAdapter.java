/*
 * VT Schedulator
 * 12/11/2013
 */

package edu.vt.ece4564.vtschedulator;

import edu.vt.ece4564.shared.CourseTime;
import edu.vt.ece4564.shared.Schedule;
import android.widget.TextView;
import android.content.Context;
import android.view.LayoutInflater;
import edu.vt.ece4564.shared.Course;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

//-------------------------------------------------------------------------
/**
 * Class Adapter class
 * Posts the Schedules to the screen.
 */

public class ClassAdapter extends BaseAdapter {
	Schedule sortList;
	Context c;

	public ClassAdapter(Context c) {
		super();
		this.c = c;
	}

	public void setList(Schedule course) {
		sortList = course;
	}

	@Override
	public int getCount() {
		return 5;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflator = LayoutInflater.from(c);

		if (convertView == null) {
			convertView = inflator.inflate(R.layout.adapter_class, null);
		}

		TextView view = (TextView) convertView.findViewById(R.id.list_content);
		int i = position;

		String Day = "";
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

		StringBuilder builder = new StringBuilder();
		builder.append(Day);
		ArrayList<Course> courses = new ArrayList<Course>();
		for(Course course : sortList.getCourses()) {
		    if (course.getTime() != null) {
		        CourseTime time = course.getTime();
		        if (time.equals(Day)) courses.add(course);
		    }
		}

		for (int k = 0; k < courses.size(); k++) {
			builder.append("CRN: " + courses.get(k).getCrn()
					+ "\nClassName: " + courses.get(k).getName()
					+ "\nTime: " + courses.get(k).getTime().toString());
		}

		builder.append("\n");
		view.setText(builder.toString());
		return convertView;
	}
}
