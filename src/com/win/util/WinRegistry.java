package com.win.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Windows Registry utility class.
 *
 * Replaces the old reflection-based approach that broke on JDK 11+
 * (WindowsPreferences internal methods changed signatures from int to long).
 *
 * Uses the Windows 'reg.exe' command-line tool directly via ProcessBuilder,
 * which works reliably across all JDK versions (8, 11, 17, 21+).
 */
public class WinRegistry {

    public static final int HKEY_CURRENT_USER = 0x80000001;
    public static final int HKEY_LOCAL_MACHINE = 0x80000002;
    public static final int REG_SUCCESS = 0;
    public static final int REG_NOTFOUND = 2;
    public static final int REG_ACCESSDENIED = 5;

    private WinRegistry() {}

    /**
     * Converts an hkey constant to its string representation for reg.exe.
     */
    private static String hkeyToString(int hkey) {
        if (hkey == HKEY_CURRENT_USER) {
            return "HKCU";
        } else if (hkey == HKEY_LOCAL_MACHINE) {
            return "HKLM";
        } else {
            throw new IllegalArgumentException("Unsupported hkey: " + hkey);
        }
    }

    /**
     * Read a string value from the Windows Registry.
     *
     * @param hkey      HKEY_CURRENT_USER or HKEY_LOCAL_MACHINE
     * @param key       Registry key path
     * @param valueName Name of the value to read
     * @return the value string, or null if not found
     */
    public static String readString(int hkey, String key, String valueName) {
        try {
            String fullKey = hkeyToString(hkey) + "\\" + key;
            ProcessBuilder pb = new ProcessBuilder(
                "reg", "query", fullKey, "/v", valueName
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // Look for the line containing the value
                // Format: "    valueName    REG_SZ    value"
                if (line.contains(valueName) && line.contains("REG_SZ")) {
                    String[] parts = line.trim().split("\\s+REG_SZ\\s+", 2);
                    if (parts.length == 2) {
                        process.waitFor();
                        return parts[1].trim();
                    }
                }
            }

            process.waitFor();
            return null;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Write a string value to the Windows Registry.
     *
     * @param hkey      HKEY_CURRENT_USER or HKEY_LOCAL_MACHINE
     * @param key       Registry key path
     * @param valueName Name of the value
     * @param value     The string value to write
     */
    public static void writeStringValue(int hkey, String key,
                                        String valueName, String value) {
        try {
            String fullKey = hkeyToString(hkey) + "\\" + key;
            ProcessBuilder pb = new ProcessBuilder(
                "reg", "add", fullKey,
                "/v", valueName,
                "/t", "REG_SZ",
                "/d", value,
                "/f"  // Force overwrite without prompt
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Consume output to prevent blocking
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
            while (reader.readLine() != null) { /* drain */ }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("WinRegistry: Failed to write registry value. "
                    + "Exit code: " + exitCode
                    + " (try running as Administrator for HKLM keys)");
            }

        } catch (Exception e) {
            System.err.println("WinRegistry: Error writing registry: " + e.getMessage());
        }
    }

    /**
     * Delete a value from the Windows Registry.
     *
     * @param hkey      HKEY_CURRENT_USER or HKEY_LOCAL_MACHINE
     * @param key       Registry key path
     * @param valueName Name of the value to delete
     */
    public static void deleteValue(int hkey, String key, String valueName) {
        try {
            String fullKey = hkeyToString(hkey) + "\\" + key;
            ProcessBuilder pb = new ProcessBuilder(
                "reg", "delete", fullKey,
                "/v", valueName,
                "/f"  // Force without prompt
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Consume output
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
            while (reader.readLine() != null) { /* drain */ }

            process.waitFor();

        } catch (Exception e) {
            System.err.println("WinRegistry: Error deleting registry value: " + e.getMessage());
        }
    }

    /**
     * Delete a key from the Windows Registry.
     *
     * @param hkey HKEY_CURRENT_USER or HKEY_LOCAL_MACHINE
     * @param key  Registry key path to delete
     */
    public static void deleteKey(int hkey, String key) {
        try {
            String fullKey = hkeyToString(hkey) + "\\" + key;
            ProcessBuilder pb = new ProcessBuilder(
                "reg", "delete", fullKey, "/f"
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
            while (reader.readLine() != null) { /* drain */ }

            process.waitFor();

        } catch (Exception e) {
            System.err.println("WinRegistry: Error deleting registry key: " + e.getMessage());
        }
    }

    /**
     * Create a key in the Windows Registry.
     *
     * @param hkey HKEY_CURRENT_USER or HKEY_LOCAL_MACHINE
     * @param key  Registry key path to create
     */
    public static void createKey(int hkey, String key) {
        try {
            String fullKey = hkeyToString(hkey) + "\\" + key;
            ProcessBuilder pb = new ProcessBuilder(
                "reg", "add", fullKey, "/f"
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
            while (reader.readLine() != null) { /* drain */ }

            process.waitFor();

        } catch (Exception e) {
            System.err.println("WinRegistry: Error creating registry key: " + e.getMessage());
        }
    }
}
