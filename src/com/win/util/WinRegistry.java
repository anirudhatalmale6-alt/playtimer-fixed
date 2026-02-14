package com.win.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Windows Registry utility using reg.exe commands.
 * Replaces the old reflection-based approach that broke in JDK 11+
 * (WindowsPreferences internal methods changed from int to long params).
 */
public class WinRegistry {
    public static final int HKEY_CURRENT_USER = 0x80000001;
    public static final int HKEY_LOCAL_MACHINE = 0x80000002;
    public static final int REG_SUCCESS = 0;

    private WinRegistry() {}

    private static String hkeyToString(int hkey) {
        if (hkey == HKEY_CURRENT_USER) return "HKCU";
        if (hkey == HKEY_LOCAL_MACHINE) return "HKLM";
        throw new IllegalArgumentException("Unsupported hkey: " + hkey);
    }

    /**
     * Read a string value from the registry.
     */
    public static String readString(int hkey, String key, String valueName) {
        try {
            String fullKey = hkeyToString(hkey) + "\\" + key;
            ProcessBuilder pb = new ProcessBuilder(
                "reg", "query", fullKey, "/v", valueName);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.contains("REG_SZ")) {
                    // Format: valueName    REG_SZ    value
                    int idx = line.indexOf("REG_SZ");
                    if (idx >= 0) {
                        return line.substring(idx + 6).trim();
                    }
                }
            }
            p.waitFor();
        } catch (Exception e) {
            // Registry read failed
        }
        return null;
    }

    /**
     * Write a string value to the registry.
     */
    public static void writeStringValue(int hkey, String key,
            String valueName, String value) {
        try {
            String fullKey = hkeyToString(hkey) + "\\" + key;
            ProcessBuilder pb = new ProcessBuilder(
                "reg", "add", fullKey, "/v", valueName,
                "/t", "REG_SZ", "/d", value, "/f");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.waitFor();
        } catch (Exception e) {
            // Registry write failed
        }
    }

    /**
     * Delete a value from the registry.
     */
    public static void deleteValue(int hkey, String key, String value) {
        try {
            String fullKey = hkeyToString(hkey) + "\\" + key;
            ProcessBuilder pb = new ProcessBuilder(
                "reg", "delete", fullKey, "/v", value, "/f");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.waitFor();
        } catch (Exception e) {
            // Registry delete failed
        }
    }

    /**
     * Delete a registry key.
     */
    public static void deleteKey(int hkey, String key) {
        try {
            String fullKey = hkeyToString(hkey) + "\\" + key;
            ProcessBuilder pb = new ProcessBuilder(
                "reg", "delete", fullKey, "/f");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.waitFor();
        } catch (Exception e) {
            // Registry key delete failed
        }
    }

    /**
     * Create a registry key.
     */
    public static void createKey(int hkey, String key) {
        try {
            String fullKey = hkeyToString(hkey) + "\\" + key;
            ProcessBuilder pb = new ProcessBuilder(
                "reg", "add", fullKey, "/f");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.waitFor();
        } catch (Exception e) {
            // Registry key create failed
        }
    }
}
