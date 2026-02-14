package com.mp3.player;

import org.apache.log4j.Logger;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class LOG {

	/* Get actual class name to be printed on */
	static Logger log = Logger.getLogger("PlayTimer");

	public static void main(String[] args) throws IOException, SQLException {

		log.debug("Hello this is an debug message");
		log.info("Hello this is an info message");
	}

	public static void INFO(String message) {
		log.info(message);
		Main.resetTextPane();
	}

	public static void DEBUG(String message) {
		log.debug(message);
		Main.resetTextPane();
	}

	public static void ERROR(String message) {
		log.error(message);
		Main.resetTextPane();
	}
	
	public static void ERRORONCE(String message) {
		log.error(message);
	}
	
	public static void INFOONCE(String message) {
		log.info(message);
	}
}