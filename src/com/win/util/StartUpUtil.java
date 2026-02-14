package com.win.util;

public class StartUpUtil {

	public static void StartEnable(String workingDir) {

		try {
			WinRegistry.writeStringValue(
				    WinRegistry.HKEY_CURRENT_USER,                              //HKCU - no admin needed
				   "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run",
				   "timelyplayer",
				   "\"" + workingDir + "\\PlayTimer.exe" + "\"");
		} catch (Exception e) {
			// Startup enable failed
		}
	}

	public static void StartDisable() {

		try {
			WinRegistry.deleteValue(WinRegistry.HKEY_CURRENT_USER,              //HKCU - no admin needed
					   "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run",
					   "timelyplayer");
		} catch (Exception e) {
			// Startup disable failed
		}
	}

}
