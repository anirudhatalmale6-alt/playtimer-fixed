package com.mp3.images;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Parse {
	
	public static void main(String[] args) {
		
		SimpleDateFormat sdfSource = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy");
		String date = "Tue Jan 21 18:32:01 IST 2014";
		try {
			System.out.println(sdfSource.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
