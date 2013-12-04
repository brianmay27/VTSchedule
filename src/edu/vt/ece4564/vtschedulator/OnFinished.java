package edu.vt.ece4564.vtschedulator;

import edu.vt.ece4564.shared.Schedule;
import java.util.ArrayList;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 *
 *  @author Brian
 *  @version Dec 3, 2013
 */

public interface OnFinished
{
    public void postRun(ArrayList<Schedule> schedules);
}
