# PlayTimer - Fixed for Modern JDK

A Java Swing application that plays MP3 audio at scheduled times throughout the day.

## Changes from Original
- **Fixed WinRegistry** - Replaced broken Java reflection approach with ProcessBuilder-based `reg.exe` commands (compatible with JDK 11, 17, 21+)
- **Fixed Startup Launch** - App now auto-loads schedules and plays music on system startup without requiring manual restart
- **Fixed Registry Access** - Changed from HKLM (requires admin) to HKCU (current user, no admin needed)
- **Fixed Deprecated API** - Replaced `Properties.save()` with `Properties.store()`
- **Updated Java Target** - Source/target updated from 1.7 to 1.8 for compatibility with latest LTS JDK
- **Fixed Main Class** - Entry point correctly set to `ConnectionStatusTray`

## Building
Open in NetBeans and use **Clean & Build**, or from command line:
```
ant clean jar
```

## Requirements
- JDK 8 or later (tested with JDK 21)
- Windows OS (for system tray and registry startup features)

