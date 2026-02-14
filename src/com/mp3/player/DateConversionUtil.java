package com.mp3.player;

/*
 Convert date string from one format to another format using SimpleDateFormat
 This example shows how to convert format of a string containing date
 and time to other formats using Java SimpleDateFormat class.
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyStore.Entry;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import au.com.bytecode.opencsv.CSVReader;

public class DateConversionUtil {

	
	static String defaultPath = "";
	
	public static void main(String[] args) {

		// string containing date in one format
/*		String strDate = "12 Dec 07 00:00:00";

		try {
			// create SimpleDateFormat object with source string date format
			SimpleDateFormat sdfSource = new SimpleDateFormat("dd MMM yy HH:mm:ss");

			// parse the string into Date object
			Date date = sdfSource.parse(strDate);

			SimpleDateFormat date_format = new SimpleDateFormat("dd MMM yyyy");

			strDate = date_format.format(date);
			System.out.println("Converted date is : " + strDate);

			SimpleDateFormat sdfDestination = new SimpleDateFormat(
					"E MMM dd HH:mm:ss a zzz YYYY");

			// parse the date into another format
			strDate = sdfDestination.format(date);

			System.out
					.println("Date is converted from dd MMM yy format to MMM-dd-yyyy hh:mm:ss");
			System.out.println("Converted date is : " + strDate);

		} catch (ParseException pe) {
			System.out.println("Parse Exception : " + pe);
		}*/
		
		
		try {
			csvToSchedule("C:\\Users\\SaravananS\\Desktop\\time.csv");
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Map<String, List<ScheduleTime>> csvToSchedule(String csvFilename) 
	throws FileNotFoundException, IOException, ParseException {

		String[] row = null;
		CSVReader csvReader = null;
		Map<String, List<ScheduleTime>> scheduleMap = new TreeMap<String, List<ScheduleTime>>();
		List<ScheduleTime> scheduleList = new ArrayList<ScheduleTime>();

		try {
			csvReader = new CSVReader(new FileReader(csvFilename));
			List content = csvReader.readAll();

			SimpleDateFormat date_format = new SimpleDateFormat("dd MMM yyyy");
			SimpleDateFormat sdfSource = new SimpleDateFormat("dd MMM yy HH:mm:ss");
			SimpleDateFormat sdfDestination = new SimpleDateFormat("E MMM dd HH:mm:ss zzz YYYY");
			String strDate = "";
			String conDate = "";

			for (Object object : content) {

				row = (String[]) object;
				// parse the string into Date object
				Date date;
				
				try {
					
					if (row.length != 2
							&& "".equalsIgnoreCase(row [0].trim())) {
						continue;
					}
					
					date = sdfSource.parse(row[0]);
					strDate = date_format.format(date);
					
					if (scheduleMap.containsKey(strDate)) {
						
						List<ScheduleTime> time = scheduleMap.get(strDate);
						
						if (time.size() < 7) {
							
							ScheduleTime schedule = new ScheduleTime();
							schedule.setStartDate(date);
							
							if (row[1].contains(File.separator)) {
								schedule.setMusicToBePlayed(row[1]);
							} else {
								schedule.setMusicToBePlayed(defaultPath + row[1]);
							}
							
							time.add(schedule);
						}
						
					} else {
						
						List<ScheduleTime> time = new ArrayList<ScheduleTime>();
						
						ScheduleTime schedule = new ScheduleTime();
						schedule.setStartDate(date);
						
						if (row[1].contains(File.separator)) {
							schedule.setMusicToBePlayed(row[1]);
						} else {
							schedule.setMusicToBePlayed(defaultPath + row[1]);
						}
						
						time.add(schedule);
						scheduleMap.put(strDate, time);
					}
					
				} catch (ParseException e) {
					throw e;
				}
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (csvReader != null) {
				csvReader.close();
			}
		}
		
		return scheduleMap;
	}

}

/*
 * Typical output would be Date is converted from dd/MM/yy format to MM-dd-yyyy
 * hh:mm:ss Converted date is : 12-12-2007 12:00:00
 */