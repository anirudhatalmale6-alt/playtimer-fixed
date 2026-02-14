package com.mp3.player;

import java.io.Serializable;
import java.util.Date;

public class ScheduleTime 
implements Serializable {
	
	public Date startDate;
	public boolean isRepeatable;
	public int frequency;
	public String musicToBePlayed;
	
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public boolean isRepeatable() {
		return isRepeatable;
	}
	public void setRepeatable(boolean isRepeatable) {
		this.isRepeatable = isRepeatable;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public String getMusicToBePlayed() {
		return musicToBePlayed;
	}
	public void setMusicToBePlayed(String musicToBePlayed) {
		this.musicToBePlayed = musicToBePlayed;
	}
}
