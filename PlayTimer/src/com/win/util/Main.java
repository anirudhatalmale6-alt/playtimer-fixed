package com.win.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class Main {
	
	public static void main(String[] args) {
		
		String value;
		try {
			
			String workingDir = System.getProperty("user.dir");
			
			WinRegistry.writeStringValue(
				    WinRegistry.HKEY_LOCAL_MACHINE,                             //HKEY
				   "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run",           //Key
				   "timelyplayer",
				   "\"" + workingDir + "\\PlayTimer.exe" + "\"");
			
			value = 	WinRegistry.readString(
				    WinRegistry.HKEY_LOCAL_MACHINE,                             //HKEY
				   "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run",           //Key
				   "timelyplayer");
			
			System.out.println("Windows Distribution = " + value);  
			
			WinRegistry.deleteValue(WinRegistry.HKEY_LOCAL_MACHINE,                             //HKEY
				   "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run",           //Key
				   "timelyplayer");
			
			value = 	WinRegistry.readString(
				    WinRegistry.HKEY_LOCAL_MACHINE,                             //HKEY
				   "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run",           //Key
				   "timelyplayer");
			
			System.out.println("Windows Distribution = " + value);   

		} catch (IllegalArgumentException | IllegalAccessException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}                                              //ValueName
		
	}
}
