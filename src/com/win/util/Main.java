package com.win.util;

/**
 * Test utility for WinRegistry operations.
 * Not the main entry point - see com.mp3.player.ConnectionStatusTray.
 */
public class Main {

    public static void main(String[] args) {

        String value;
        try {
            String workingDir = System.getProperty("user.dir");

            // Use HKCU (current user) - does not require admin rights
            WinRegistry.writeStringValue(
                WinRegistry.HKEY_CURRENT_USER,
                "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run",
                "timelyplayer",
                "\"" + workingDir + "\\PlayTimer.exe\"");

            value = WinRegistry.readString(
                WinRegistry.HKEY_CURRENT_USER,
                "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run",
                "timelyplayer");

            System.out.println("Startup entry = " + value);

            WinRegistry.deleteValue(
                WinRegistry.HKEY_CURRENT_USER,
                "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run",
                "timelyplayer");

            value = WinRegistry.readString(
                WinRegistry.HKEY_CURRENT_USER,
                "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run",
                "timelyplayer");

            System.out.println("After delete = " + value);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
