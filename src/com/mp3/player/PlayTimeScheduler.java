package com.mp3.player;

import jaco.mp3.player.MP3Player;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;


public class PlayTimeScheduler  {
    
	Timer timer;
	String musicToPlay = "";
	static MP3Player player = new MP3Player();
	String startDate;
	
    public PlayTimeScheduler(String startdate, long seconds, String musicToPlay) {
        
    	this.startDate = startdate;
    	timer = new Timer();  //At this line a new Thread will be created
        this.musicToPlay = musicToPlay;
        timer.schedule(new RemindTask(), seconds); //delay in milliseconds
    }
    
    class RemindTask extends TimerTask {
    	
        @Override
        public void run() {
        	
        	LOG.log.info("Started Schedule " + startDate);
        	player.stop();
        	player.clearPlayList();
        	player.add(new File(musicToPlay));
            player.setRepeat(false);
            player.play();
            timer.cancel(); 
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
