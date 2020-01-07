package com.social.control;

import com.social.UI.model.MySocialContentRepository;
import com.social.util.Log;


public class SocialUpdatedLoader extends Thread {
	
	private void mysleep(long sec) {
		
		try {
			Thread.sleep(sec * 1000);
		} catch (InterruptedException e) {
			//do nothing
		}
	}

			
	public void run() {

		Log.print("MainLoop started ... [OK]");
		mysleep(10);
		
		while (true) {
			
			Log.print("Check Cases Update");
			MySocialContentRepository.UpdateSocialContents();
			
			mysleep(600);
			
		}
    }
}
