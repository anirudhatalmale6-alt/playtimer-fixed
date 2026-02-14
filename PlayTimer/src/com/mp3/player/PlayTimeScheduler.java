package com.mp3.player;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class PlayTimeScheduler {

	Timer timer;
	String musicToPlay = "";
	static MP3Player player = new MP3Player();
	String startDate;
	// SimpleDateFormat sdfSource = new SimpleDateFormat("dd MMM yy HH:mm:ss");
	SimpleDateFormat sdfSource = new SimpleDateFormat(
			"E MMM dd HH:mm:ss zzz yyyy");

	public PlayTimeScheduler(String startdate, long seconds, String musicToPlay) {

		this.startDate = startdate;
		timer = new Timer(); // At this line a new Thread will be created
		this.musicToPlay = musicToPlay;
		timer.schedule(new RemindTask(), seconds); // delay in milliseconds
	}

	class RemindTask extends TimerTask {

		@Override
		public void run() {

			try {

				LOG.INFO("Started Schedule " + startDate);
				player.stop();
				player.clearPlayList();

				Date compareDate = addTimeBySecondsDemo(sdfSource.parse(startDate), 1);
				
				ConnectionStatusTray.setToolTip("Performing your Action");
				Main.statuslabel.setText("Performing your Action");
				
				for (Date date : Main.dateList) {
					if (date.after(compareDate)) {

						Main.nextlabel.setText(" Next Music Scheduled in " + date);
						break;
					}
				}

				player.add(new File(musicToPlay));
				player.setRepeat(false);
				player.play();
				timer.cancel();
			} catch (IllegalArgumentException | ParseException e) {
				LOG.INFO(e.getMessage());
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
		PlayTimeScheduler reminderBeep = new PlayTimeScheduler("", 500,
				"C:\\Users\\admin\\Desktop\\Nadodimannargale - TamilWire.com.mp3");
		System.out.println("Remindertask is scheduled with Java timer.");
		reminderBeep = new PlayTimeScheduler("", 10000,
				"C:\\Users\\admin\\Desktop\\Nadodimannargale - TamilWire.com.mp3");
		System.out.println("Remindertask is scheduled with Java timer.");
		reminderBeep = new PlayTimeScheduler("", 15000,
				"C:\\Users\\admin\\Desktop\\Nadodimannargale - TamilWire.com.mp3");
		System.out.println("Remindertask is scheduled with Java timer.");

	}

	public static Date addTimeBySecondsDemo(Date date, int sec) {

		Calendar calender = Calendar.getInstance();
		calender.setTimeInMillis(date.getTime());
		calender.add(Calendar.SECOND, sec);
		Date changeDate = calender.getTime();
		
		return changeDate;
	}
}
