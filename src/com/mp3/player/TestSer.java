package com.mp3.player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class TestSer {

	public static void main(String arr[]) {
		Map<String, List<ScheduleTime>> hashmap = new TreeMap<String, List<ScheduleTime>>();
		
		
		List<ScheduleTime> list = new ArrayList<ScheduleTime>();
		
		ScheduleTime time = new ScheduleTime();
		time.setStartDate(new Date());
		time.setMusicToBePlayed("1");

		list.add(time);

		time = new ScheduleTime();
		time.setStartDate(new Date());
		time.setMusicToBePlayed("2");

		list.add(time);
		
		hashmap.put("key1", list);
		hashmap.put("key2", list);

		FileOutputStream fos;
		try {
			fos = new FileOutputStream("PlayTimer.ser");

			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(hashmap);
			oos.close();

			FileInputStream fis = new FileInputStream("PlayTimer.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Map<String, List<ScheduleTime>> anotherList = (Map<String, List<ScheduleTime>>) ois
					.readObject();

			for (Entry<String, List<ScheduleTime>> key : anotherList.entrySet()) {
				
				System.out.println(key.getKey());
				
				for (ScheduleTime schedule : key.getValue()) {
						System.out.println("Date " + schedule.getStartDate());
						System.out.println("Music " + schedule.getMusicToBePlayed());
				}
			}
			
			ois.close();

			System.out.println(anotherList);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}
