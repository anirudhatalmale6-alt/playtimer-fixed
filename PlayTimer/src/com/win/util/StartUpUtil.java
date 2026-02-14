package com.win.util;

import java.lang.reflect.InvocationTargetException;

public class StartUpUtil {
	
	public static void StartEnable(String workingDir) {
		
		try {
			WinRegistry.writeStringValue(
				    WinRegistry.HKEY_LOCAL_MACHINE,                             //HKEY
				   "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run",           //Key
				   "timelyplayer",
				   "\"" + workingDir + "\\PlayTimer.exe" + "\"");
		} catch (IllegalArgumentException | IllegalAccessException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	
	public static void StartDisable() {
		
		try {
			WinRegistry.deleteValue(WinRegistry.HKEY_LOCAL_MACHINE,                             //HKEY
					   "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run",           //Key
					   "timelyplayer");
		} catch (IllegalArgumentException | IllegalAccessException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	
}
