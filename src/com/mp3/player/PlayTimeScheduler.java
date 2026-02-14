package com.mp3.player;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class PlayTimeScheduler  {

	Timer timer;
	String musicToPlay = "";
	static SimpleMP3Player player = new SimpleMP3Player();
	String startDate;

    public PlayTimeScheduler(String startdate, long seconds, String musicToPlay) {

    	this.startDate = startdate;
    	timer = new Timer();  //At this line a new Thread will be created
        this.musicToPlay = musicToPlay;
        LOG.log.info("Scheduling playback: " + musicToPlay + " in " + (seconds/1000) + " seconds (at " + startdate + ")");
        timer.schedule(new RemindTask(), seconds); //delay in milliseconds
    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {

        	try {
	        	LOG.log.info("Timer fired! Playing: " + musicToPlay + " (scheduled for " + startDate + ")");

	        	File musicFile = new File(musicToPlay);
	        	if (!musicFile.exists()) {
	        		LOG.log.error("Music file NOT FOUND: " + musicToPlay);
	        		return;
	        	}

	        	LOG.log.info("Music file exists, size: " + musicFile.length() + " bytes");
	        	player.stop();
	        	player.playFile(musicFile);
	            LOG.log.info("player.playFile() called successfully");
        	} catch (Exception e) {
        		LOG.log.error("Error playing music: " + e.getMessage());
        		e.printStackTrace();
        	} finally {
        		timer.cancel();
        	}
        }
    }


    public void cancel() {

    	if (timer != null) {
    		timer.cancel();
    	}
    }

    public String getStartDate() {
    	return startDate;
    }

    public static void main(String args[]) {
        System.out.println("Java timer is about to start");
        PlayTimeScheduler reminderBeep = new PlayTimeScheduler("", 500, "C:\\Users\\admin\\Desktop\\Nadodimannargale - TamilWire.com.mp3");
        System.out.println("Remindertask is scheduled with Java timer.");
        reminderBeep = new PlayTimeScheduler("", 10000, "C:\\Users\\admin\\Desktop\\Nadodimannargale - TamilWire.com.mp3");
        System.out.println("Remindertask is scheduled with Java timer.");
        reminderBeep = new PlayTimeScheduler("", 15000, "C:\\Users\\admin\\Desktop\\Nadodimannargale - TamilWire.com.mp3");
        System.out.println("Remindertask is scheduled with Java timer.");

    }
}
