package com.mp3.player;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * 
 * ConnectionStatusTray class is used to run the application on 
 * windows tray manager. 
 * 
 * @author Saran S
 *
 */
public class ConnectionStatusTray {

	String TOOL_TIP = "Play Timer";
	String MESSAGE_HEADER = "Play Timer";
	TrayIcon processTrayIcon = null;

	static Main main;
	static String passkey = "Martin";
	
	public static void main(String[] args) {

		try {

			// Fix working directory: when Windows auto-starts the app,
			// the working directory may be C:\Windows\System32 instead
			// of the app's directory. This causes PlayTimer.ser, config.properties,
			// and other relative-path files to not be found.
			// Solution: detect app directory and store it for all file operations.
			initAppHome();

			if(!isAppAlreadyRunning()){

				JOptionPane.showMessageDialog(null,
						"Programi osht i qelun o jaran!");
		       System.exit(0);
		    }
			
			try {
			    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			        if ("Nimbus".equals(info.getName())) {
			            UIManager.setLookAndFeel(info.getClassName());
			            break;
			        }
			    }
				
//				UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//				UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
				
//				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
//				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				
/*				 Toolkit.getDefaultToolkit().setDynamicLayout(true);
				 System.setProperty("sun.awt.noerasebackground", "true");
				 JFrame.setDefaultLookAndFeelDecorated(true);
				 JDialog.setDefaultLookAndFeelDecorated(true);

				 try {
				     UIManager.setLookAndFeel("de.muntjak.tinylookandfeel.TinyLookAndFeel");
				 } catch(Exception ex) {
				     ex.printStackTrace();
				 } */
				
			} catch (Exception e) {
			    // If Nimbus is not available, you can set the GUI to another look and feel.
			}
			
			main = new Main();
			ConnectionStatusTray connectionStatusTray = new ConnectionStatusTray();
			connectionStatusTray.createAndAddApplicationToSystemTray();

			// Auto-start: load schedules immediately on startup
			// Without this, schedules only loaded when user clicked tray icon
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					main.main(null);
				}
			});

			// Safety net: save schedules on JVM shutdown (covers unexpected exits)
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						if (Main.scheduleMap != null && !Main.scheduleMap.isEmpty()) {
							SerializationUtil.doSave(Main.scheduleMap);
						}
					} catch (Exception e) {
						// Best effort save
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method creates the AWT items and add it to the System tray.
	 * 
	 * @throws IOException
	 */
	private void createAndAddApplicationToSystemTray() throws IOException {

		// Check the SystemTray support
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}

		final PopupMenu popup = new PopupMenu();
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream inputStream = classLoader
				.getResourceAsStream("playtime.png");
		Image img = ImageIO.read(inputStream);

		final TrayIcon trayIcon = new TrayIcon(img, TOOL_TIP);
		this.processTrayIcon = trayIcon;
		final SystemTray tray = SystemTray.getSystemTray();

		// Create a popup menu components1
		MenuItem aboutItem = new MenuItem("About");
		MenuItem exitItem = new MenuItem("Exit");

		// Add components to popup menu
		popup.add(aboutItem);
		popup.addSeparator();
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);

		trayIcon.setImageAutoSize(true);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			LOG.log.error("System Tray Not Supporeted");
			return;
		}
		
		trayIcon.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		        if (e.getClickCount() == 1) {
		        	main.main(null);
		        }
		    }
		});
		

		// Add listener to aboutItem.
		aboutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"This dialog box is run the About menu item");
			}
		});

		// Add listener to exitItem.
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LOG.log.info("Exiting - saving schedules");
				SerializationUtil.doSave(Main.scheduleMap);
				tray.remove(trayIcon);
				LOG.log.info("Exiting the application.");
				System.exit(0);
			}
		});
	}

	
	/**
	 * App home directory - where PlayTimer.ser, config.properties etc. live.
	 */
	static String appHome = null;

	/**
	 * Detect the app's installation directory from the JAR/class location.
	 * When launched from Windows startup, CWD may be System32.
	 */
	private static void initAppHome() {
		try {
			String jarPath = ConnectionStatusTray.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI().getPath();
			File jarFile = new File(jarPath);
			File appDir = jarFile.isDirectory() ? jarFile : jarFile.getParentFile();
			// If running from NetBeans build/classes, go up to project root
			if (appDir.getName().equals("classes")) {
				File buildDir = appDir.getParentFile();
				if (buildDir != null && buildDir.getName().equals("build")) {
					appDir = buildDir.getParentFile();
				}
			}
			// If running from bin/ (Eclipse output), go up to project root
			if (appDir.getName().equals("bin")) {
				appDir = appDir.getParentFile();
			}
			appHome = appDir.getAbsolutePath();
		} catch (Exception e) {
			appHome = System.getProperty("user.dir");
		}
	}

	/**
	 * Get a File object relative to the app's home directory.
	 * Use this instead of new File("relative") to ensure correct paths
	 * when the app is auto-started by Windows.
	 */
	public static File getAppFile(String relativePath) {
		if (appHome == null) {
			return new File(relativePath);
		}
		return new File(appHome, relativePath);
	}

	private static boolean isAppAlreadyRunning() {
	    // socket concept is shown at http://www.rbgrn.net/content/43-java-single-application-instance
	    // but this one is really great
	    try {
	        final File file = getAppFile("PlayTimer.txt");
	        final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
	        final FileLock fileLock = randomAccessFile.getChannel().tryLock();
	        if (fileLock != null) {
	            Runtime.getRuntime().addShutdownHook(new Thread() {
	                public void run() {
	                    try {
	                        fileLock.release();
	                        randomAccessFile.close();
	                        file.delete();
	                    } catch (Exception e) {
	                        LOG.log.error("Tried to run another instance of the application");
	                    }
	                }
	            });
	            return true;
	        }
	    } catch (Exception e) {
	    	LOG.log.error("Tried to run another instance of the application");
	    }
	    return false;
	}
	
}