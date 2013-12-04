/*
 * VT Schedulator
 * 12/11/2013
 */

package edu.vt.ece4564.vtschedulator;

import edu.vt.ece4564.shared.Schedule;
import java.util.ArrayList;

// -------------------------------------------------------------------------
/**
 * OnFinished interface
 */
public interface OnFinished {
	public void postRun(ArrayList<Schedule> schedules);
}