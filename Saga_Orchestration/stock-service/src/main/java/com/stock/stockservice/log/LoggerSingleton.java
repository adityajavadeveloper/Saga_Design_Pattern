package com.stock.stockservice.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerSingleton {

	private LoggerSingleton() {
	}

	private static Logger logger = LogManager.getLogger(LoggerSingleton.class);

	/*
	 * public static Logger getLogger() {
	 * 
	 * if(logger == null) { synchronized (LoggerSingleton.class) { logger =
	 * LogManager.getLogger(LoggerSingleton.class); } }
	 * 
	 * return logger; }
	 */

	public static void log(int logLevel, String className, String methodName, String message) {

		switch (logLevel) {
		case LogConst.DEBUG:
			logger.debug(className + " :: " + methodName + " :: " + message);
			break;
		case LogConst.ERROR:
			logger.error(className + " :: " + methodName + " :: " + message);
			break;
		case LogConst.FATAL:
			logger.fatal(className + " :: " + methodName + " :: " + message);
			break;
		case LogConst.INFO:
			logger.info(className + " :: " + methodName + " :: " + message);
			break;
		case LogConst.TRACE:
			logger.trace(className + " :: " + methodName + " :: " + message);
			break;
		case LogConst.WARN:
			logger.warn(className + " :: " + methodName + " :: " + message);
			break;
		default:
			logger.error(className + " :: " + methodName + " :: " + message);
		}

	}

	public static void error(String className, String methodName, String message, Throwable t) {
		logger.error(className + " :: " + methodName + " :: " + message + " :: " + t);
	}
}
