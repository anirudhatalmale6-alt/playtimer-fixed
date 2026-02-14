package com.win.util;

/**
 * Utility to add/remove the application from Windows startup.
 *
 * Uses HKEY_CURRENT_USER instead of HKEY_LOCAL_MACHINE so that
 * no Administrator privileges are required.
 */
public class StartUpUtil {

    private static final String RUN_KEY = "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run";
    private static final String APP_NAME = "timelyplayer";

    public static void StartEnable(String workingDir) {
        try {
            // Use HKCU (current user) - does not require admin rights
            WinRegistry.writeStringValue(
                WinRegistry.HKEY_CURRENT_USER,
                RUN_KEY,
                APP_NAME,
                "\"" + workingDir + "\\PlayTimer.exe\"");
        } catch (Exception e) {
            System.err.println("StartUpUtil: Could not enable startup: " + e.getMessage());
        }
    }

    public static void StartDisable() {
        try {
            WinRegistry.deleteValue(
                WinRegistry.HKEY_CURRENT_USER,
                RUN_KEY,
                APP_NAME);
        } catch (Exception e) {
            System.err.println("StartUpUtil: Could not disable startup: " + e.getMessage());
        }
    }
}
