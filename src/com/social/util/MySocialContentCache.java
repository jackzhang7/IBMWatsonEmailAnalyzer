package com.social.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import com.social.UI.model.MySocialContent;
import com.social.UI.model.MySocialContentRepository;
import com.social.db.DatabaseMgr;


public class MySocialContentCache {

	 public static Hashtable <String, MySocialContent> hMySocialContent = null;
	 public static HashMap <String, MySocialContent> hMapMySocialContent = null;
	 //public static Hashtable <String, MySocialContent> hLastMySocialContent = null;
	 //Profile
	public static Hashtable <String, Double> hPersonality = null;
	public static Double avgPersonality = null;

	 public MySocialContentCache() {
		 if (hMySocialContent == null) {
			 hMySocialContent = new Hashtable<String, MySocialContent>();
			 initMySocialContentCacheFromDB();
		 }
	 }

	 public void initMySocialContentCacheFromDB() {
		 DatabaseMgr db=new DatabaseMgr();
		 hMapMySocialContent = db.loadAllMySocialContentFromDB();
		 if ( hMapMySocialContent != null ) {
			 for(MySocialContent social: hMapMySocialContent.values()){
				 insertToCache(social);
				 MySocialContentRepository.UpdateMySocialContents(social);
			 }
		 }
	 }


	 private static Date lastUpdateTime;

	 public void refreshMySocialContentCacheFromDB() {
		 DatabaseMgr db=new DatabaseMgr();
		 Calendar cld =Calendar.getInstance() ;
		 cld.add(Calendar.HOUR,-1);
		 lastUpdateTime = cld.getTime();
		hMapMySocialContent = db.loadAllMySocialContentFromDB(lastUpdateTime);
		if ( hMapMySocialContent != null ) {
			 for(MySocialContent social: hMapMySocialContent.values()){
				 if (insertToCache(social) == 1) {
					 MySocialContentRepository.UpdateMySocialContents(social);
					 MySocialContentRepository.UpdateMySocialContentsLatest(social);
					 //insertToLastUpdateCache(social);
				 }
			 }
		 }

	 }

	public static int insertToCache(MySocialContent MySocialContent) {
		String timeString = Util.getLocalTimeString(MySocialContent.getReceivedTime(), "America/New_York");
		String key1= MySocialContent.getKind()+"@"+timeString;
		String key = key1.trim();
		if(hMySocialContent.containsKey(key)){
			return 0;
	    }else{
	    	hMySocialContent.put(key, MySocialContent);
	    }
		return 1;
	}
	public Boolean isKeyInCache (String key) {
		return hMySocialContent.containsKey(key.trim());
	}
	public static MySocialContent getMySocialContentFromCache(String key) {
		MySocialContent retF = null;
		if(hMySocialContent.containsKey(key.trim())){
	    retF = hMySocialContent.get(key);
	      }
	  return retF;
	 }
	public void removeMySocialContentFromCache(MySocialContent MySocialContent) throws Exception {
		String key= MySocialContent.getKind()+"@"+MySocialContent.getReceivedTime();
	   if(hMySocialContent.containsKey(key.trim())){
		   hMySocialContent.remove(key.trim());
	   }
	}


	public static HashSet<String> numbers = null;

	
	public static void main(String args[]) {
		//updateAllMySocialContentStatus();
		//updatePrioritySeverity();

	}

}
