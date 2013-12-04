package edu.vt.ece4564.vtschedulator;

import android.widget.TextView;
import android.content.Context;
import sun.security.action.GetLongAction;
import android.view.LayoutInflater;
import edu.vt.ece4564.shared.Course;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 *
 *  @author Brian
 *  @version Dec 3, 2013
 */

public class ClassAdapter extends BaseAdapter
{
    ArrayList<ArrayList<Course>>  m_SortList = new ArrayList<ArrayList<Course>>();
    Context c;
    public ClassAdapter(Context c) {
        super();
        this.c = c;

    }
    public void setList(ArrayList<ArrayList<Course>> course) {
        m_SortList = course;
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return 5;
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflator = LayoutInflater.from(c);
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.textview_string, null);
        }
        TextView view = (TextView)convertView.findViewById(R.id.list_content);
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
            builder.append(Day + "\n");
            for (int k = 0; k < m_SortList.get(i).size(); k++) {
                builder.append("CRN: " + m_SortList.get(i).get(k).getCrn()
                        + "\nClassName: " + m_SortList.get(i).get(k).getName()
                        + "\nTime: "
                        + m_SortList.get(i).get(k).getTime().toString());
            }
            builder.append("\n");
            view.setText(builder.toString());
            view.setTextSize(20);
            return convertView;
    }

}
