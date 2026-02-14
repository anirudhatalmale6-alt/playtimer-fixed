package com.mp3.player;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

import com.mp3.player.PlayTimeScheduler.RemindTask;

/**
 * Simple demo that uses java.util.Timer to schedule a task to execute once 5
 * seconds have passed.
 */

public class Reminder {

	public static Timer timer;
	long limit = 0;
	JLabel text;
	long current = System.currentTimeMillis();

	public Reminder(int seconds, JLabel text) {
		timer = new Timer();
		this.text = text;
		this.limit = seconds * 1000;
		current = System.currentTimeMillis();
		timer.schedule(new RemindTask(), seconds);
	}

	class RemindTask extends TimerTask {

		public void run() {

			boolean run = true;

			while (run) {

				long time = System.currentTimeMillis();
				long passed = time - current;
				long remaining = limit - passed;

				if (remaining <= 0) {
					text.setText("00:00:00");
					text.revalidate();
					timer.cancel();
					run = false;

				} else {

					long seconds = remaining / 1000;
					long minutes = seconds / 60;
					long hours = minutes / 60;
					text.setText(String.format("%02d:%02d:%02d", hours,
							minutes, seconds % 60));
					text.revalidate();
				}
			}

		}
	}
	
	public void cancel() {
		
		if (timer != null) {
			timer.cancel();
			timer.purge();
		}
	}
	

	public static void main(String args[]) {
		/*
		 * new Reminder(5); System.out.format("Task scheduled.%n");
		 */

		new Reminder(261, new JLabel());
	}
}