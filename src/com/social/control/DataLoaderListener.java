package com.social.control;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.social.util.Log;


public class DataLoaderListener implements ServletContextListener {

	private static SocialUpdatedLoader myUpdatedLoader = null;
	public static String runMode = "Web"; 
	
	public static String getRunMode() {
		return runMode;
	}

	public void contextInitialized(ServletContextEvent sce) {

		mySleep(1);
			//TomCat Server
		if (myUpdatedLoader == null) {
			myUpdatedLoader = new SocialUpdatedLoader();
			myUpdatedLoader.start();
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
		try {
			myUpdatedLoader.interrupted();
		} catch (Exception e)  {
			Log.print(e);
		}
	}
	
	private void mySleep(long sec) {
		
		try {
			Thread.sleep(sec * 1000);
		} catch (Exception e) {
			
		}
	}
	
}