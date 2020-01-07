package com.social.util;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

public class Log {

	private static Logger logger = Logger.getLogger(Log.class);
	
	public static void print(String owner, String retainId, String message) {
		
		logger.info(owner + " (" + retainId + ") " + message);
	}
	
	public static void print(String message) {
		
		logger.info(message);
	}
	public static void logMessage(String message) {
		
		logger.info(message);
	}	
	public static void print(Level level, String message) {
		logger.log(level, message);
	}
	
	public static void print(Exception e) {
		
		logger.error(e.getMessage(), e);
	}
}
