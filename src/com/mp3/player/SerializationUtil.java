package com.mp3.player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SerializationUtil {
	/**
	 * Create a simple Hashtable and serialize it to a file called
	 * HTExample.ser.
	 */
	public static void doSave(Map<String, List<ScheduleTime>> time) {
		try {
			
			FileOutputStream fileOut = new FileOutputStream(
					ConnectionStatusTray.getAppFile("PlayTimer.ser"));
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			LOG.log.info("Writing to PlayTimer.ser...");

			out.writeObject(time);
			out.flush();
			out.close();
			fileOut.close();
			
		} catch (FileNotFoundException e) {
			LOG.log.info("PlayTimer.ser File Not Found.");
			e.printStackTrace();
		} catch (IOException e) {
			LOG.log.info(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Loads the contents of a previously serialized object from a file called
	 * HTExample.ser.
	 */
	public static Map<String, List<ScheduleTime>> doLoad() {
		
		Map<String, List<ScheduleTime>> list = new HashMap<String, List<ScheduleTime>>();

		FileInputStream fileIn = null;
		ObjectInputStream in = null;
		
		try {
			fileIn = new FileInputStream(
					ConnectionStatusTray.getAppFile("PlayTimer.ser"));
			in = new ObjectInputStream(fileIn);

			LOG.log.info("Loading PlayTimer.ser Object...");
			Object tempObj;
			
			list = (Map<String, List<ScheduleTime>>) in.readObject();
			
		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
		} catch (FileNotFoundException e) {
//			e.printStackTrace();
		} catch (IOException e) {
//			e.printStackTrace();
		} finally {
			
			if (in != null) {
				try {
					in.close();
					if (fileIn != null) {
						fileIn.close();
					}
				} catch (IOException e) {
				}
			}
		}
		
		return list;
	}

	/**
	 * Sole entry point to the class and application.
	 * 
	 * @param args
	 *            Array of String arguments.
	 */
	public static void main(String[] args) {
		
		Map<String, List<ScheduleTime>> anotherList = doLoad();

		for (Entry<String, List<ScheduleTime>> key : anotherList.entrySet()) {
			
			System.out.println(key.getKey());
			
			for (ScheduleTime schedule : key.getValue()) {
					
					if (schedule == null) {
						continue;
					}
					
					System.out.println("Date " + schedule.getStartDate());
					System.out.println("Music " + schedule.getMusicToBePlayed());
			}
		}

	}
}
