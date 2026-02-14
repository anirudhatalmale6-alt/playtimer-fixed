package com.win.util;

public class Main {

	public static void main(String[] args) {

		String value;
		try {

			String workingDir = System.getProperty("user.dir");

			WinRegistry.writeStringValue(
				    WinRegistry.HKEY_CURRENT_USER,                              //HKCU - no admin needed
				   "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run",
				   "timelyplayer",
				   "\"" + workingDir + "\\PlayTimer.exe" + "\"");

			value = WinRegistry.readString(
				    WinRegistry.HKEY_CURRENT_USER,                              //HKCU
				   "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run",
				   "timelyplayer");

			System.out.println("Windows Distribution = " + value);

			WinRegistry.deleteValue(WinRegistry.HKEY_CURRENT_USER,              //HKCU
				   "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run",
				   "timelyplayer");

			value = WinRegistry.readString(
				    WinRegistry.HKEY_CURRENT_USER,                              //HKCU
				   "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run",
				   "timelyplayer");

			System.out.println("Windows Distribution = " + value);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
