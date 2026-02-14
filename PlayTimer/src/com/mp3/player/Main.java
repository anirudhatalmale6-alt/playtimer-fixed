package com.mp3.player;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DateEditor;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextPane;
import javax.swing.SpinnerDateModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import org.jdesktop.swingx.JXStatusBar;

import com.win.util.StartUpUtil;

public class Main {

	static boolean startup;
	static Properties prop = new Properties();
	
	static { 
		
    	try {
               //load a properties file
    		prop.load(new FileInputStream("config.properties"));
            DateConversionUtil.defaultPath = prop.getProperty("filepath");
            startup = Boolean.valueOf(prop.getProperty("showonstartup"));
            
    	} catch (IOException ex) {

    		LOG.ERROR("Unable to read the config.properties file");
    		System.exit(0);
        }
	}
	
	static JFrame frame = null;
	static Controller controller = new Controller();
	static Main main = new Main();
	static BrowseButtonListener listener = main.new BrowseButtonListener();
	static FixedSizeNumberDocument replacelistener = main.new FixedSizeNumberDocument(
			4);
	static FixedSizeNumberDocument datelistener = main.new FixedSizeNumberDocument(
			4);
	static ToggleListener togglelistener = main.new ToggleListener();
	static ItemListner itemListener = main.new ItemListner();
	Map<String, List<ScheduleTime>> map = new HashMap<String, List<ScheduleTime>>();
	static Map<String, PlayTimeScheduler> reminderList = new TreeMap<String, PlayTimeScheduler>();
	static Map<String, List<ScheduleTime>> scheduleMap = new TreeMap<String, List<ScheduleTime>>();
	static List<ScheduleTime> scheduleList = new ArrayList<ScheduleTime>();
	static Vector<String> schedule_List = new Vector<String>();
	static JFileChooser chooser = new JFileChooser();
	static FileNameExtensionFilter mp3filter = new FileNameExtensionFilter(
			"MP3 FILES", "mp3", "mp3");
	static JFileChooser csvChooser = new JFileChooser();
	static FileNameExtensionFilter csvfilter = new FileNameExtensionFilter(
			"TXT FILES", "txt", "txt");
	static JMenuBar menuBar = new JMenuBar();
	static JMenu menu = new JMenu("File");
	static JMenu menu1 = new JMenu("Settings");
	static JMenuItem exit = new JMenuItem("Exit");
	static JMenuItem about = new JMenuItem("About");
	static JMenuItem load = new JMenuItem("Load Schedules Manually");
	static JMenuItem clearAllSchedules = new JMenuItem("Clear All Schedules");
	static JCheckBoxMenuItem startOnSystemStartup = new JCheckBoxMenuItem(
			"Start on system startup");
	static JLabel remainderlabel = new JLabel();
	static JLabel nextlabel = new JLabel();
	static List<Date> dateList = new ArrayList<Date>();
	
	static JXStatusBar status = new JXStatusBar();
	static JLabel statuslabel = new JLabel("Ready");
	
	public static void start() {

		if (frame != null) {
			frame.setVisible(true);
			return;
		}

		frame = new JFrame();
		
		frame.setTitle("Xhamia e Muhocit");

		Date d = new Date();
		controller.getjCalendarPanel1().setDate(d);
		controller.getjCalendarPanel1().addPropertyChangeListener("Date",
				new JPanelCalendarChange(controller));

		setDayInList();

		SpinnerDateModel model = new SpinnerDateModel();
		model.setCalendarField(Calendar.MINUTE);
		controller.getjSpinner1().setModel(model);

		DateEditor editor = new JSpinner.DateEditor(controller.getjSpinner1(),
				"H:mm:ss");

		controller.getjSpinner1().setEditor(editor);

		model = new SpinnerDateModel();
		model.setCalendarField(Calendar.MINUTE);
		controller.getjSpinner2().setModel(model);

		editor = new JSpinner.DateEditor(controller.getjSpinner2(), "H:mm:ss");

		controller.getjSpinner2().setEditor(editor);

		model = new SpinnerDateModel();
		model.setCalendarField(Calendar.MINUTE);

		controller.getjSpinner3().setModel(model);
		controller.getjSpinner3().setEditor(
				new JSpinner.DateEditor(controller.getjSpinner3(), "H:mm:ss"));

		model = new SpinnerDateModel();
		model.setCalendarField(Calendar.MINUTE);

		controller.getjSpinner4().setModel(model);
		controller.getjSpinner4().setEditor(
				new JSpinner.DateEditor(controller.getjSpinner4(), "H:mm:ss"));

		model = new SpinnerDateModel();
		model.setCalendarField(Calendar.MINUTE);

		controller.getjSpinner5().setModel(model);
		controller.getjSpinner5().setEditor(
				new JSpinner.DateEditor(controller.getjSpinner5(), "H:mm:ss"));

		model = new SpinnerDateModel();
		model.setCalendarField(Calendar.MINUTE);

		controller.getjSpinner6().setModel(model);
		controller.getjSpinner6().setEditor(
				new JSpinner.DateEditor(controller.getjSpinner6(), "H:mm:ss"));

		model = new SpinnerDateModel();
		model.setCalendarField(Calendar.MINUTE);

		controller.getjSpinner7().setModel(model);
		controller.getjSpinner7().setEditor(
				new JSpinner.DateEditor(controller.getjSpinner7(), "H:mm:ss"));

		((DefaultEditor) controller.getjSpinner1().getEditor()).getTextField()
				.setEditable(false);
		((DefaultEditor) controller.getjSpinner2().getEditor()).getTextField()
				.setEditable(false);
		((DefaultEditor) controller.getjSpinner3().getEditor()).getTextField()
				.setEditable(false);
		((DefaultEditor) controller.getjSpinner4().getEditor()).getTextField()
				.setEditable(false);
		((DefaultEditor) controller.getjSpinner5().getEditor()).getTextField()
				.setEditable(false);
		((DefaultEditor) controller.getjSpinner6().getEditor()).getTextField()
				.setEditable(false);
		((DefaultEditor) controller.getjSpinner7().getEditor()).getTextField()
				.setEditable(false);

		controller.getjButton1().setActionCommand("Browse1");
		controller.getjButton1().addActionListener(listener);

		controller.getjButton2().setActionCommand("Browse2");
		controller.getjButton2().addActionListener(listener);

		controller.getjButton3().setActionCommand("Browse3");
		controller.getjButton3().addActionListener(listener);

		controller.getjButton4().setActionCommand("Browse4");
		controller.getjButton4().addActionListener(listener);

		controller.getjButton5().setActionCommand("Browse5");
		controller.getjButton5().addActionListener(listener);

		controller.getjButton6().setActionCommand("Browse6");
		controller.getjButton6().addActionListener(listener);

		controller.getjButton7().setActionCommand("Browse7");
		controller.getjButton7().addActionListener(listener);

		controller.getjButton8().setActionCommand("Browse");
		controller.getjButton8().addActionListener(listener);

		controller.getReplaceBtn().setActionCommand("Replace");
		controller.getReplaceBtn().addActionListener(listener);
		
		controller.getClearLogBtn().setActionCommand("Clear");
		controller.getClearLogBtn().addActionListener(listener);

		controller.getReplaceTxt().setDocument(replacelistener);
		controller.getDateTxt().setDocument(datelistener);

		controller.getjButton6().setEnabled(false);
		controller.getjTextField6().setEnabled(false);
		controller.getjSpinner6().setEnabled(false);

		controller.getjButton7().setEnabled(false);
		controller.getjTextField7().setEnabled(false);
		controller.getjSpinner7().setEnabled(false);

		controller.getjToggleButton1().addActionListener(togglelistener);
		controller.getjCheckBox1().addItemListener(itemListener);
		controller.getjCheckBox2().addItemListener(itemListener);

		controller.getReplaceFileTxt().setEditable(false);
		
//		DefaultCaret caret = (DefaultCaret) controller.getjTextPane1().getCaret();
//		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		File serFile = new File("PlayTimer.ser");

		if (serFile.exists()) {

			serFile = null;
			scheduleMap.clear();
			reminderList.clear();
			System.gc();

			scheduleMap = (Map<String, List<ScheduleTime>>) SerializationUtil
					.doLoad();

			for (Entry<String, List<ScheduleTime>> key : scheduleMap.entrySet()) {

				if (key.getValue() == null) {
					continue;
				}

				List<ScheduleTime> list = new ArrayList<ScheduleTime>(
						key.getValue());

				for (ScheduleTime time : list) {

					if (time == null) {
						continue;
					}

					long val = time.getStartDate().getTime()
							- System.currentTimeMillis();

					if (val >= 0) {
						
						addTimeBySecondsDemo(time.getStartDate(), 1); 
						reminderList.put(
								time.getStartDate().toString(),
								new PlayTimeScheduler(time.getStartDate()
										.toString(), val, time
										.getMusicToBePlayed()));
					}
				}
			}
		}
		
		Collections.sort(dateList);

		resetPane();

		controller.getjScrollPane1().setVisible(false);
		controller.getjList1().setVisible(false);

		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame,
						"This dialog box is run the About menu item");
			}
		});

		menuBar.add(menu);
//		menu.add(about); remove about
		menu.add(exit);

		menuBar.add(menu1);

		// menuBar.add(menu);
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				Map<String, List<ScheduleTime>> csvschedule;
				try {

					System.gc();
					int value = csvChooser.showOpenDialog(frame);
					String selectedFile = "";

					if (value > 0) {
						return;
					} else {
						selectedFile = csvChooser.getSelectedFile().toString();
					}

					csvschedule = DateConversionUtil
							.csvToSchedule(selectedFile);

					scheduleMap.clear();
					cancelReminders();
					scheduleMap.putAll(csvschedule);

					LOG.INFO("Loading new schedules from CSV");

					for (String key : scheduleMap.keySet()) {

						csvschedule.get(key);
						for (ScheduleTime time : scheduleMap.get(key)) {

							long val = time.getStartDate().getTime()
									- System.currentTimeMillis();

							if (val >= 0) {
								
								addTimeBySecondsDemo(time.getStartDate(), 1);
								reminderList
										.put(time.getStartDate().toString(),
												new PlayTimeScheduler(time
														.getStartDate()
														.toString(), val, time
														.getMusicToBePlayed()));
							}
						}

					}
					
					Collections.sort(dateList);
					resetSchedules();
					csvschedule.clear();
					System.gc();
					
				} catch (IOException ioe) {
					LOG.ERROR("Exception Occured while reading the csv file");
					JOptionPane.showMessageDialog(frame, ioe.getMessage());
				} catch (ParseException pe) {
					LOG.ERROR("Unable to parse the csv file");
					JOptionPane.showMessageDialog(frame, pe.getMessage());
				}

			}
		});

		menu1.add(load);

		clearAllSchedules.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				cancelReminders();
				scheduleMap.clear();
				resetSchedules();
				System.gc();
			}
		});

		menu1.add(clearAllSchedules);

		startOnSystemStartup.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if (e.getStateChange() == ItemEvent.SELECTED) {
					StartUpUtil.StartEnable(System.getProperty("user.dir"));
					prop.setProperty("showonstartup", "true");
		            try {
						prop.save(new FileOutputStream("config.properties"), "playtimer properties");
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
				} else {
					prop.setProperty("showonstartup", "false");
		            try {
						prop.save(new FileOutputStream("config.properties"), "playtimer properties");
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					StartUpUtil.StartDisable();
				}
			}

		});
		
		startOnSystemStartup.setSelected(startup);

		menu1.add(startOnSystemStartup);
		chooser.setFileFilter(mp3filter);
		csvChooser.setFileFilter(csvfilter);

		resetTextPane();
		
		frame.setIconImage(new javax.swing.ImageIcon(Main.class
				.getResource("/com/mp3/images/playtime.png")).getImage());
		frame.setJMenuBar(menuBar);

		JPanel borderPanel = new JPanel(new BorderLayout());
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
		centerPanel.add(controller);
		centerPanel.add(Box.createVerticalGlue()); // to place the panel in
													// center when maximized
		JPanel centerPanelConstrain = new JPanel(new GridBagLayout());
		centerPanelConstrain.add(centerPanel);
		borderPanel.add(centerPanelConstrain, BorderLayout.CENTER);
		
		
		
		JPanel panel = new JPanel(new BorderLayout());
		JPanel labelpanel = new JPanel(new FlowLayout());
		labelpanel.add(remainderlabel);
		labelpanel.add(nextlabel);
		
		panel.add(labelpanel, BorderLayout.NORTH);
		panel.add(new JLabel("<html>1. Idea kreatort : Adil Ramadani,  Visar. Limani." + "<br>" 
							+ "2. Kontributort : Fatos Ramadani,  Arben Ramadani (Shpenovit), Shkumbin Ademi, Fatos Limani + i gjith populli." + "<br>"
							+ "3. zhvill : Visar Limani</html"), BorderLayout.CENTER);
//		panel.add(new JLabel());
//		panel.add(new JLabel());
		borderPanel.add(panel, BorderLayout.SOUTH);

		status.add(statuslabel);
		frame.getContentPane().add(borderPanel);
		frame.getContentPane().add(status, BorderLayout.SOUTH);
		
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		ConnectionStatusTray.setToolTip("Ready");

	}

	private static class JPanelCalendarChange implements PropertyChangeListener {
		private Controller m_me;

		public JPanelCalendarChange(Controller me) {
			m_me = me;
		}

		public void propertyChange(PropertyChangeEvent evt) {

			Calendar c = Calendar.getInstance();
			c.setTime(m_me.getjCalendarPanel1().getDate());
			int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			m_me.getjList1().setSelectedIndex(dayOfWeek - 1);

			resetPane();
		}
	}

	class BrowseButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if ("Browse".equalsIgnoreCase(e.getActionCommand())) {

				int value = csvChooser.showOpenDialog(frame);

				String selectedFile = "";
				if (value > 0) {
					return;
				} else {
					selectedFile = csvChooser.getSelectedFile().toString();
				}

				controller.getReplaceFileTxt().setText(selectedFile);
				
			} else if ("Replace".equalsIgnoreCase(e.getActionCommand())) {

				if ("".equalsIgnoreCase(controller.getReplaceFileTxt()
						.getText())) {

					JOptionPane.showMessageDialog(frame,
							"Please Select a file to replace the Date.",
							"Choose File", JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				BufferedReader br = null;
				BufferedWriter out = null;
				String input = controller.getReplaceFileTxt().getText();

				ArrayList<String> lines = new ArrayList<String>();
				String line = null;
				try {
					File f1 = new File(input);
					FileReader fr = new FileReader(f1);
					br = new BufferedReader(fr);

					String textToReplace = controller.getDateTxt().getText();
					String replaceWith = controller.getReplaceTxt().getText();

					while ((line = br.readLine()) != null) {
						if (line.contains(textToReplace))
							line = line.replaceAll(textToReplace, replaceWith);

						lines.add(line);
					}
					fr.close();
					br.close();

					FileWriter fw = new FileWriter(f1);
					out = new BufferedWriter(fw);
					for (String s : lines)
						out.write(s + "\n");
					out.flush();
					out.close();
					
					JOptionPane.showMessageDialog(frame,
							"Date Successfully replaced from " + textToReplace + " to " + replaceWith,
							"Date Replaced Successfully", JOptionPane.INFORMATION_MESSAGE);

					
					LOG.INFO("Date Successfully replaced from " + textToReplace + " to " + replaceWith);
				} catch (Exception ex) {
					
					LOG.ERROR("Failed to replace Dates");
					
					JOptionPane.showMessageDialog(frame,
							"Unable to replace the Date.",
							"Date Replace Failed", JOptionPane.ERROR_MESSAGE);

				}

			} else if ("Clear".equalsIgnoreCase(e.getActionCommand())) {
				
				String filename = "logs/playtimer.log";
				
				if (filename != null) {
					
					try {
						
						FileWriter fw = new FileWriter(new File(filename));
						BufferedWriter out = new BufferedWriter(fw);
						
						out.flush();
						out.close();
						
						resetTextPane();
						
						LOG.INFO("LOG Successfully Cleared");
					} catch (Exception ex) {
						
						LOG.ERROR("Failed to clear LOG");
						
						JOptionPane.showMessageDialog(frame,
								"Unable to clear the Log.",
								"Clear Log", JOptionPane.ERROR_MESSAGE);
					}
				}
				
			} else {

				int value = chooser.showOpenDialog(frame);

				String selectedFile = "";
				if (value > 0) {
					return;
				} else {
					selectedFile = chooser.getSelectedFile().toString();
				}

				SimpleDateFormat date_format = new SimpleDateFormat(
						"dd MMM yyyy");
				String key = date_format.format(controller.getjCalendarPanel1()
						.getDate());

				if ("Browse1".equalsIgnoreCase(e.getActionCommand())) {

					controller.getjTextField1().setText(selectedFile);
					if (scheduleMap.containsKey(key)) {

						List<ScheduleTime> list = scheduleMap.get(key);

						if (list.size() > 0
								&& list.get(0) != null
								&& reminderList.containsKey(list.get(0)
										.getStartDate().toString())) {
							reminderList.get(
									list.get(0).getStartDate().toString())
									.cancel();
						}

						ScheduleTime time = new ScheduleTime();
						time.setMusicToBePlayed(controller.getjTextField1()
								.getText());

						Date date = (Date) controller.getjSpinner1().getValue();
						Date newDate = getScheduleTime(controller
								.getjCalendarPanel1().getDate(),
								date.getHours(), date.getMinutes(),
								date.getSeconds());
						time.setStartDate(newDate);

						list.remove(0);
						list.add(0, time);

						long val = time.getStartDate().getTime()
								- System.currentTimeMillis();

						if (val >= 0) {
							
							addTimeBySecondsDemo(time.getStartDate(), 1);
							reminderList.put(
									time.getStartDate().toString(),
									new PlayTimeScheduler(time.getStartDate()
											.toString(), val, time
											.getMusicToBePlayed()));
						}

					} else {

						List<ScheduleTime> list = new ArrayList<ScheduleTime>();
						ScheduleTime time = new ScheduleTime();
						time.setMusicToBePlayed(controller.getjTextField1()
								.getText());

						Date date = (Date) controller.getjSpinner1().getValue();
						Date newDate = getScheduleTime(controller
								.getjCalendarPanel1().getDate(),
								date.getHours(), date.getMinutes(),
								date.getSeconds());
						time.setStartDate(newDate);

						list.add(0, time);

						long val = time.getStartDate().getTime()
								- System.currentTimeMillis();

						if (val >= 0) {
							
							addTimeBySecondsDemo(time.getStartDate(), 1);
							reminderList.put(
									time.getStartDate().toString(),
									new PlayTimeScheduler(time.getStartDate()
											.toString(), val, time
											.getMusicToBePlayed()));
						}

						scheduleMap.put(key, list);
					}

				} else if ("Browse2".equalsIgnoreCase(e.getActionCommand())) {

					controller.getjTextField2().setText(selectedFile);

					if (scheduleMap.containsKey(key)) {

						List<ScheduleTime> list = scheduleMap.get(key);

						if (list.size() > 1
								&& list.get(1) != null
								&& reminderList.containsKey(list.get(1)
										.getStartDate().toString())) {
							reminderList.get(
									list.get(1).getStartDate().toString())
									.cancel();
						}

						ScheduleTime time = new ScheduleTime();
						time.setMusicToBePlayed(controller.getjTextField2()
								.getText());

						Date date = (Date) controller.getjSpinner2().getValue();
						Date newDate = getScheduleTime(controller
								.getjCalendarPanel1().getDate(),
								date.getHours(), date.getMinutes(),
								date.getSeconds());
						time.setStartDate(newDate);

						if (list.size() > 1) {
							list.remove(1);
						}

						list.add(1, time);

						long val = time.getStartDate().getTime()
								- System.currentTimeMillis();

						if (val >= 0) {
							
							addTimeBySecondsDemo(time.getStartDate(), 1);
							reminderList.put(
									time.getStartDate().toString(),
									new PlayTimeScheduler(time.getStartDate()
											.toString(), val, time
											.getMusicToBePlayed()));
						}

					} else {

						List<ScheduleTime> list = new ArrayList<ScheduleTime>();
						ScheduleTime time = new ScheduleTime();
						time.setMusicToBePlayed(controller.getjTextField2()
								.getText());

						Date date = (Date) controller.getjSpinner2().getValue();
						Date newDate = getScheduleTime(controller
								.getjCalendarPanel1().getDate(),
								date.getHours(), date.getMinutes(),
								date.getSeconds());
						time.setStartDate(newDate);

						for (int i = 0; i < 1; i++) {
							list.add(i, null);
						}

						list.add(1, time);

						long val = time.getStartDate().getTime()
								- System.currentTimeMillis();

						if (val >= 0) {
							
							addTimeBySecondsDemo(time.getStartDate(), 1);
							reminderList.put(
									time.getStartDate().toString(),
									new PlayTimeScheduler(time.getStartDate()
											.toString(), val, time
											.getMusicToBePlayed()));
						}

						scheduleMap.put(key, list);
					}
				} else if ("Browse3".equalsIgnoreCase(e.getActionCommand())) {

					controller.getjTextField3().setText(selectedFile);

					if (scheduleMap.containsKey(key)) {

						List<ScheduleTime> list = scheduleMap.get(key);

						if (list.size() > 2
								&& list.get(2) != null
								&& reminderList.containsKey(list.get(2)
										.getStartDate().toString())) {
							reminderList.get(
									list.get(2).getStartDate().toString())
									.cancel();
						}

						ScheduleTime time = new ScheduleTime();
						time.setMusicToBePlayed(controller.getjTextField3()
								.getText());
						Date date = (Date) controller.getjSpinner3().getValue();
						Date newDate = getScheduleTime(controller
								.getjCalendarPanel1().getDate(),
								date.getHours(), date.getMinutes(),
								date.getSeconds());
						time.setStartDate(newDate);

						if (list.size() > 2) {
							list.remove(2);
						}

						list.add(2, time);

						long val = time.getStartDate().getTime()
								- System.currentTimeMillis();

						if (val >= 0) {
							addTimeBySecondsDemo(time.getStartDate(), 1);
							reminderList.put(
									time.getStartDate().toString(),
									new PlayTimeScheduler(time.getStartDate()
											.toString(), val, time
											.getMusicToBePlayed()));
						}

					} else {

						List<ScheduleTime> list = new ArrayList<ScheduleTime>();
						ScheduleTime time = new ScheduleTime();
						time.setMusicToBePlayed(controller.getjTextField3()
								.getText());
						Date date = (Date) controller.getjSpinner3().getValue();
						Date newDate = getScheduleTime(controller
								.getjCalendarPanel1().getDate(),
								date.getHours(), date.getMinutes(),
								date.getSeconds());
						time.setStartDate(newDate);

						for (int i = 0; i < 2; i++) {
							list.add(i, null);
						}

						list.add(2, time);

						long val = time.getStartDate().getTime()
								- System.currentTimeMillis();

						if (val >= 0) {
							addTimeBySecondsDemo(time.getStartDate(), 1);
							reminderList.put(
									time.getStartDate().toString(),
									new PlayTimeScheduler(time.getStartDate()
											.toString(), val, time
											.getMusicToBePlayed()));
						}

						scheduleMap.put(key, list);
					}
				} else if ("Browse4".equalsIgnoreCase(e.getActionCommand())) {

					controller.getjTextField4().setText(selectedFile);

					if (scheduleMap.containsKey(key)) {

						List<ScheduleTime> list = scheduleMap.get(key);

						if (list.size() > 3
								&& list.get(3) != null
								&& reminderList.containsKey(list.get(3)
										.getStartDate().toString())) {
							reminderList.get(
									list.get(3).getStartDate().toString())
									.cancel();
						}

						ScheduleTime time = new ScheduleTime();
						time.setMusicToBePlayed(controller.getjTextField4()
								.getText());
						Date date = (Date) controller.getjSpinner4().getValue();
						Date newDate = getScheduleTime(controller
								.getjCalendarPanel1().getDate(),
								date.getHours(), date.getMinutes(),
								date.getSeconds());
						time.setStartDate(newDate);

						if (list.size() > 3) {
							list.remove(3);
						}

						list.add(3, time);

						long val = time.getStartDate().getTime()
								- System.currentTimeMillis();

						if (val >= 0) {
							addTimeBySecondsDemo(time.getStartDate(), 1);
							reminderList.put(
									time.getStartDate().toString(),
									new PlayTimeScheduler(time.getStartDate()
											.toString(), val, time
											.getMusicToBePlayed()));
						}

					} else {

						List<ScheduleTime> list = new ArrayList<ScheduleTime>();
						ScheduleTime time = new ScheduleTime();
						time.setMusicToBePlayed(controller.getjTextField4()
								.getText());
						Date date = (Date) controller.getjSpinner4().getValue();
						Date newDate = getScheduleTime(controller
								.getjCalendarPanel1().getDate(),
								date.getHours(), date.getMinutes(),
								date.getSeconds());
						time.setStartDate(newDate);

						for (int i = 0; i < 3; i++) {
							list.add(i, null);
						}

						list.add(3, time);

						long val = time.getStartDate().getTime()
								- System.currentTimeMillis();

						if (val >= 0) {
							addTimeBySecondsDemo(time.getStartDate(), 1);
							reminderList.put(
									time.getStartDate().toString(),
									new PlayTimeScheduler(time.getStartDate()
											.toString(), val, time
											.getMusicToBePlayed()));
						}

						scheduleMap.put(key, list);
					}
				} else if ("Browse5".equalsIgnoreCase(e.getActionCommand())) {

					controller.getjTextField5().setText(selectedFile);

					if (scheduleMap.containsKey(key)) {

						List<ScheduleTime> list = scheduleMap.get(key);

						if (list.size() > 4
								&& list.get(4) != null
								&& reminderList.containsKey(list.get(4)
										.getStartDate().toString())) {
							reminderList.get(
									list.get(4).getStartDate().toString())
									.cancel();
						}

						ScheduleTime time = new ScheduleTime();
						time.setMusicToBePlayed(controller.getjTextField5()
								.getText());
						Date date = (Date) controller.getjSpinner5().getValue();
						Date newDate = getScheduleTime(controller
								.getjCalendarPanel1().getDate(),
								date.getHours(), date.getMinutes(),
								date.getSeconds());
						time.setStartDate(newDate);

						if (list.size() > 4) {
							list.remove(4);
						}
						list.add(4, time);

						long val = time.getStartDate().getTime()
								- System.currentTimeMillis();

						if (val >= 0) {
							addTimeBySecondsDemo(time.getStartDate(), 1);
							reminderList.put(
									time.getStartDate().toString(),
									new PlayTimeScheduler(time.getStartDate()
											.toString(), val, time
											.getMusicToBePlayed()));
						}
					} else {

						List<ScheduleTime> list = new ArrayList<ScheduleTime>();
						ScheduleTime time = new ScheduleTime();
						time.setMusicToBePlayed(controller.getjTextField5()
								.getText());
						Date date = (Date) controller.getjSpinner5().getValue();
						Date newDate = getScheduleTime(controller
								.getjCalendarPanel1().getDate(),
								date.getHours(), date.getMinutes(),
								date.getSeconds());
						time.setStartDate(newDate);

						for (int i = 0; i < 4; i++) {
							list.add(i, null);
						}

						list.add(4, time);
						long val = time.getStartDate().getTime()
								- System.currentTimeMillis();

						if (val >= 0) {
							addTimeBySecondsDemo(time.getStartDate(), 1);
							reminderList.put(
									time.getStartDate().toString(),
									new PlayTimeScheduler(time.getStartDate()
											.toString(), val, time
											.getMusicToBePlayed()));
						}

						scheduleMap.put(key, list);
					}
				} else if ("Browse6".equalsIgnoreCase(e.getActionCommand())) {

					controller.getjTextField6().setText(selectedFile);

					if (scheduleMap.containsKey(key)) {

						List<ScheduleTime> list = scheduleMap.get(key);

						if (list.size() > 5
								&& list.get(5) != null
								&& reminderList.containsKey(list.get(5)
										.getStartDate().toString())) {
							reminderList.get(
									list.get(5).getStartDate().toString())
									.cancel();
						}

						ScheduleTime time = new ScheduleTime();
						time.setMusicToBePlayed(controller.getjTextField6()
								.getText());
						Date date = (Date) controller.getjSpinner6().getValue();
						Date newDate = getScheduleTime(controller
								.getjCalendarPanel1().getDate(),
								date.getHours(), date.getMinutes(),
								date.getSeconds());
						time.setStartDate(newDate);

						if (list.size() > 5) {
							list.remove(5);
						}
						list.add(5, time);

						long val = time.getStartDate().getTime()
								- System.currentTimeMillis();

						if (val >= 0) {
							addTimeBySecondsDemo(time.getStartDate(), 1);
							reminderList.put(
									time.getStartDate().toString(),
									new PlayTimeScheduler(time.getStartDate()
											.toString(), val, time
											.getMusicToBePlayed()));
						}
					} else {

						List<ScheduleTime> list = new ArrayList<ScheduleTime>();
						ScheduleTime time = new ScheduleTime();
						time.setMusicToBePlayed(controller.getjTextField6()
								.getText());
						Date date = (Date) controller.getjSpinner6().getValue();
						Date newDate = getScheduleTime(controller
								.getjCalendarPanel1().getDate(),
								date.getHours(), date.getMinutes(),
								date.getSeconds());
						time.setStartDate(newDate);

						for (int i = 0; i < 5; i++) {
							list.add(i, null);
						}

						list.add(5, time);
						long val = time.getStartDate().getTime()
								- System.currentTimeMillis();

						if (val >= 0) {
							addTimeBySecondsDemo(time.getStartDate(), 1);
							reminderList.put(
									time.getStartDate().toString(),
									new PlayTimeScheduler(time.getStartDate()
											.toString(), val, time
											.getMusicToBePlayed()));
						}

						scheduleMap.put(key, list);
					}
				} else if ("Browse7".equalsIgnoreCase(e.getActionCommand())) {

					controller.getjTextField7().setText(selectedFile);

					if (scheduleMap.containsKey(key)) {

						List<ScheduleTime> list = scheduleMap.get(key);

						if (list.size() > 6
								&& list.get(6) != null
								&& reminderList.containsKey(list.get(6)
										.getStartDate().toString())) {
							reminderList.get(
									list.get(6).getStartDate().toString())
									.cancel();
						}

						ScheduleTime time = new ScheduleTime();
						time.setMusicToBePlayed(controller.getjTextField7()
								.getText());
						Date date = (Date) controller.getjSpinner7().getValue();
						Date newDate = getScheduleTime(controller
								.getjCalendarPanel1().getDate(),
								date.getHours(), date.getMinutes(),
								date.getSeconds());
						time.setStartDate(newDate);

						if (list.size() > 6) {
							list.remove(6);
						}

						list.add(6, time);

						long val = time.getStartDate().getTime()
								- System.currentTimeMillis();

						if (val >= 0) {
							addTimeBySecondsDemo(time.getStartDate(), 1);
							reminderList.put(
									time.getStartDate().toString(),
									new PlayTimeScheduler(time.getStartDate()
											.toString(), val, time
											.getMusicToBePlayed()));
						}
					} else {

						List<ScheduleTime> list = new ArrayList<ScheduleTime>();
						ScheduleTime time = new ScheduleTime();
						time.setMusicToBePlayed(controller.getjTextField7()
								.getText());
						Date date = (Date) controller.getjSpinner7().getValue();
						Date newDate = getScheduleTime(controller
								.getjCalendarPanel1().getDate(),
								date.getHours(), date.getMinutes(),
								date.getSeconds());
						time.setStartDate(newDate);

						for (int i = 0; i < 6; i++) {
							list.add(i, null);
						}

						list.add(6, time);
						long val = time.getStartDate().getTime()
								- System.currentTimeMillis();

						if (val >= 0) {
							addTimeBySecondsDemo(time.getStartDate(), 1);
							reminderList.put(
									time.getStartDate().toString(),
									new PlayTimeScheduler(time.getStartDate()
											.toString(), val, time
											.getMusicToBePlayed()));
						}

						scheduleMap.put(key, list);
					}
				}

				Collections.sort(dateList);
				SerializationUtil.doSave(scheduleMap);
			}

		}
	}

	private static Date getScheduleTime(Date date, int hour, int min, int sec) {

		Calendar tomorrow = DateToCalendar(date);

		Calendar result = new GregorianCalendar(tomorrow.get(Calendar.YEAR),
				tomorrow.get(Calendar.MONTH), tomorrow.get(Calendar.DATE),
				hour, min, sec);
		return result.getTime();
	}

	public static Calendar DateToCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static void setDayInList() {

		Calendar c = Calendar.getInstance();
		c.setTime(controller.getjCalendarPanel1().getDate());
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		controller.getjList1().setSelectedIndex(dayOfWeek - 1);
	}

	public static void resetPane() {

		SimpleDateFormat date_format = new SimpleDateFormat("dd MMM yyyy");
		String key = date_format.format(controller.getjCalendarPanel1()
				.getDate());

		Date newDate = new Date();

		if (newDate == null) {
			newDate = new Date();
		}

		if (scheduleMap.containsKey(key)) {

			List<ScheduleTime> list = scheduleMap.get(key);

			if (list.get(0) != null) {

				controller.getjSpinner1().setValue(
						(Date) list.get(0).getStartDate());
				controller.getjTextField1().setText(
						list.get(0).getMusicToBePlayed());
			} else {

				controller.getjSpinner1().setValue(newDate);
				controller.getjTextField1().setText("");
			}

			if (list.size() > 1 && list.get(1) != null) {

				controller.getjSpinner2().setValue(
						(Date) list.get(1).getStartDate());
				controller.getjTextField2().setText(
						list.get(1).getMusicToBePlayed());
			} else {

				controller.getjSpinner2().setValue(newDate);
				controller.getjTextField2().setText("");
			}

			if (list.size() > 2 && list.get(2) != null) {

				controller.getjSpinner3().setValue(
						(Date) list.get(2).getStartDate());
				controller.getjTextField3().setText(
						list.get(2).getMusicToBePlayed());
			} else {

				controller.getjSpinner3().setValue(newDate);
				controller.getjTextField3().setText("");
			}

			if (list.size() > 3 && list.get(3) != null) {

				controller.getjSpinner4().setValue(
						(Date) list.get(3).getStartDate());
				controller.getjTextField4().setText(
						list.get(3).getMusicToBePlayed());
			} else {

				controller.getjSpinner4().setValue(newDate);
				controller.getjTextField4().setText("");
			}

			if (list.size() > 4 && list.get(4) != null) {

				controller.getjSpinner5().setValue(
						(Date) list.get(4).getStartDate());
				controller.getjTextField5().setText(
						list.get(4).getMusicToBePlayed());
			} else {

				controller.getjSpinner4().setValue(newDate);
				controller.getjTextField5().setText("");
			}

			if (list.size() > 5 && list.get(5) != null) {

				controller.getjSpinner6().setValue(
						(Date) list.get(5).getStartDate());
				controller.getjTextField6().setText(
						list.get(5).getMusicToBePlayed());
			} else {

				controller.getjSpinner6().setValue(newDate);
				controller.getjTextField6().setText("");
			}

			if (list.size() > 6 && list.get(6) != null) {

				controller.getjSpinner7().setValue(
						(Date) list.get(6).getStartDate());
				controller.getjTextField7().setText(
						list.get(6).getMusicToBePlayed());
			} else {

				controller.getjSpinner7().setValue(newDate);
				controller.getjTextField7().setText("");
			}

		} else {

			controller.getjSpinner1().setValue((Date) newDate);
			controller.getjSpinner2().setValue((Date) newDate);
			controller.getjSpinner3().setValue((Date) newDate);
			controller.getjSpinner4().setValue((Date) newDate);
			controller.getjSpinner5().setValue((Date) newDate);
			controller.getjSpinner6().setValue((Date) newDate);
			controller.getjSpinner7().setValue((Date) newDate);

			controller.getjTextField1().setText("");
			controller.getjTextField2().setText("");
			controller.getjTextField3().setText("");
			controller.getjTextField4().setText("");
			controller.getjTextField5().setText("");
			controller.getjTextField6().setText("");
			controller.getjTextField7().setText("");

		}

	}

	class ToggleListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if ("Lock".equalsIgnoreCase(e.getActionCommand())) {

				toggleComponents(false);
				controller.getjToggleButton1().setText("UnLock");
				controller.getjToggleButton1().setActionCommand("UnLock");
			} else {

				JPanel panel = new JPanel();
				JLabel label = new JLabel("Enter a password:");
				JPasswordField pass = new JPasswordField(10);
				panel.add(label);
				panel.add(pass);
				String[] options = new String[] { "OK", "Cancel" };

				int option = JOptionPane.showOptionDialog(frame, panel,
						"UnLock Settings", JOptionPane.NO_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
				if (option == 0) // pressing OK button
				{
					char[] password = pass.getPassword();

					if (ConnectionStatusTray.passkey
							.equals(new String(password))) {

						toggleComponents(true);
						controller.getjToggleButton1().setText("Lock");
						controller.getjToggleButton1().setActionCommand("Lock");
					} else {

						JOptionPane.showMessageDialog(frame,
								"Please Enter a valid password",
								"Password Incorrect",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		}

	}

	public void toggleComponents(boolean value) {

		menu.setEnabled(value);
		menu1.setEnabled(value);

		exit.setEnabled(value);
		about.setEnabled(value);
		load.setEnabled(value);
		startOnSystemStartup.setEnabled(value);

		controller.getjSpinner1().setEnabled(value);
		controller.getjSpinner2().setEnabled(value);
		controller.getjSpinner3().setEnabled(value);
		controller.getjSpinner4().setEnabled(value);
		controller.getjSpinner5().setEnabled(value);
		controller.getjSpinner6().setEnabled(false);
		controller.getjSpinner7().setEnabled(false);

		controller.getjTextField1().setEnabled(value);
		controller.getjTextField2().setEnabled(value);
		controller.getjTextField3().setEnabled(value);
		controller.getjTextField4().setEnabled(value);
		controller.getjTextField5().setEnabled(value);
		controller.getjTextField6().setEnabled(false);
		controller.getjTextField7().setEnabled(false);

		controller.getjButton1().setEnabled(value);
		controller.getjButton2().setEnabled(value);
		controller.getjButton3().setEnabled(value);
		controller.getjButton4().setEnabled(value);
		controller.getjButton5().setEnabled(value);
		controller.getjButton6().setEnabled(false);
		controller.getjButton7().setEnabled(false);

		controller.getjCheckBox1().setEnabled(value);
		controller.getjCheckBox2().setEnabled(value);
	}

	class ItemListner implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent event) {

			if (controller.getjCheckBox1().isSelected()) {

				controller.getjButton6().setEnabled(true);
				controller.getjTextField6().setEnabled(true);
				controller.getjSpinner6().setEnabled(true);

			} else {

				controller.getjButton6().setEnabled(false);
				controller.getjTextField6().setEnabled(false);
				controller.getjSpinner6().setEnabled(false);
			}

			if (controller.getjCheckBox2().isSelected()) {

				controller.getjButton7().setEnabled(true);
				controller.getjTextField7().setEnabled(true);
				controller.getjSpinner7().setEnabled(true);
			} else {

				controller.getjButton7().setEnabled(false);
				controller.getjTextField7().setEnabled(false);
				controller.getjSpinner7().setEnabled(false);
			}
		}

	}

	public static void cancelReminders() {

		LOG.INFO("Cancelling existing schedules started");
		
		for (String key : reminderList.keySet()) {
			PlayTimeScheduler sch = reminderList.get(key);
			LOG.INFOONCE("Cancel Schedule " + sch.getStartDate());
			sch.cancel();
		}
		
		LOG.INFO("Cancelling existing schedules done");
	}

	public static void resetSchedules() {
		SerializationUtil.doSave(scheduleMap);
		resetPane();
		PlayTimeScheduler.player.stop();
		System.gc();
	}

	private class FixedSizeNumberDocument extends PlainDocument {
		private JTextComponent owner;
		private int fixedSize;

		public FixedSizeNumberDocument(int fixedSize) {
			// this.owner = owner;
			this.fixedSize = fixedSize;
		}

		@Override
		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			if (getLength() + str.length() > fixedSize) {
				str = str.substring(0, fixedSize - getLength());
				// this.owner.getToolkit().beep();
			}

			try {
				Integer.parseInt(str);
			} catch (NumberFormatException e) {
				// inserted text is not a number
				// this.owner.getToolkit().beep();
				return;
			}

			super.insertString(offs, str, a);
		}
	}
	
	public static void resetTextPane() {
		
		String filename = "logs/playtimer.log";

		if (filename != null) {
			try {
				
				JTextPane pane = controller.getjTextPane1(); 
				
				pane.setText("");
				
				File filereader = new File(filename);
				pane.setEditable(false);
				pane.setPage(filereader.toURI().toURL());
				
				Document doc = controller.getjTextPane1().getDocument();
		    	doc.putProperty(Document.StreamDescriptionProperty, null);
		    	
				/*DefaultCaret caret = (DefaultCaret) controller.getjTextPane1().getCaret();
				caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);*/
		    	
			} catch (IOException e) {
				LOG.ERRORONCE("Attempted to read a bad file " + filename);
				System.err.println();
			}
		} else {
			LOG.ERRORONCE("File Not Found");
		}
	}
	
	public static void exit() {
		System.exit(0);
	}
	
	
	 public static void addTimeBySecondsDemo(Date date,int sec){

		/* System.out.println("Given date:"+date);
         Calendar calender = Calendar.getInstance();
         calender.setTimeInMillis(date.getTime());
         calender.add(Calendar.SECOND, sec);
         Date changeDate = calender.getTime();
         System.out.println("changeDate ..:"+changeDate);*/
         
         dateList.add(date);
    }
}
