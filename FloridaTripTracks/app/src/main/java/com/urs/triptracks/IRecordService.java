/**	 TripTracks, Copyright 2014 Florida Transportation Authority
 *                                    Florida, USA
 *
 * 	 @author Hema Sahu <hema.sahu@urs.com>
 *
 *   This file is part of TripTracks.
 *
 */

package com.urs.triptracks;

public interface IRecordService {
	public int  getState();
	public void startRecording(TripData trip);
	public void cancelRecording();
	public long finishRecording(); // returns trip-id
	public long getCurrentTrip();  // returns trip-id
	public void pauseRecording();
	public void resumeRecording();
	public void reset();
	public void setListener(RecordingActivity ra);
}
