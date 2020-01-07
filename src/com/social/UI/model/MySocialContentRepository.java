package com.social.UI.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import javax.mail.MessagingException;

import com.social.db.DatabaseMgr;
import com.social.gmail.MailReceiverUtils;
import com.social.util.MySocialContentCache;
import com.social.util.SentenceSplitter;
import com.social.util.Util;


public class MySocialContentRepository {
	
    private static List<MySocialContent> MySocialContentData = null;
    private static List<MySocialContent> MySocialContentDataLatest = null;
    private static int maxMessagesize = 5000;
    public static MySocialContentCache socialCache;
    private static int WatsonApiCallDaily = 1000;
    private static int dayClock = 280;
	private static int firstFlag = 0;
	private static Date lastUpdateTime;
	//private static CcwinExtDataBase db;
	
	public static void resetCounter() {
		WatsonApiCallDaily = 1000;
		dayClock = 288;
	}

    public static List<MySocialContent> GetMySocialContents()
    {
        if (MySocialContentData == null)
        {
        	MySocialContentData = new LinkedList<MySocialContent>();
  
        	
        }
        return MySocialContentData;
    }
    
    public static void UpdateMySocialContents(MySocialContent MySocialContent)
    {
        if (MySocialContentData == null)
        {
        	MySocialContentData = new LinkedList<MySocialContent>();
        	MySocialContentData.add(MySocialContent);
        } else {
        	MySocialContentData.add(MySocialContent);
        }
    }
	
    public static List<MySocialContent> GetMySocialContentsLatest()
    {
        if (MySocialContentDataLatest == null)
        {
        	MySocialContentDataLatest = new LinkedList<MySocialContent>();
        	
        }
        return MySocialContentDataLatest;
    }
    
    public static void UpdateMySocialContentsLatest(MySocialContent MySocialContent)
    {
        if (MySocialContentDataLatest == null)
        {
        	MySocialContentDataLatest = new LinkedList<MySocialContent>();
        	MySocialContentDataLatest.add(MySocialContent);
        } else {
        	MySocialContentDataLatest.add(MySocialContent);
        }
    }   


	
	public static void UpdateSocialContents() {
		if (firstFlag == 0) {
			Calendar c =Calendar.getInstance() ;
			c.add(Calendar.HOUR,-1 );
			lastUpdateTime = c.getTime();		
			//SentenceSplitter sentenceSplitter = new SentenceSplitter();
			socialCache = new MySocialContentCache();
			firstFlag = 1;
		}
		
		Util util = new Util();
		//Log.print("ABS_PATH="+ util.getPath());
		//DatabaseMgr db = new DatabaseMgr();
		String gmailUser= Util.DATABASE_USER;
		String gmailPassword = Util.DATABASE_PASSWD;
		MailReceiverUtils mailUtils = new MailReceiverUtils(gmailUser, gmailPassword);  
		try {
			String returnStr = mailUtils.getMessageFromEmail();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	public static void insertMySocialContent2DB(MySocialContent MySocialContent) {
		DatabaseMgr db=new DatabaseMgr();
		db.insertSocialContentEntry(MySocialContent);
		
	}
	public static void updateMySocialContentFeedback2DB(MySocialContent MySocialContent) {
		DatabaseMgr db=new DatabaseMgr();
		//db.updateMySocialContentFeedback(MySocialContent);
		
	}
	private static void mySleep(long sec) {
		
		try {
			Thread.sleep(sec * 1000);
		} catch (Exception e) {
			
		}
	}
	public static void main(String args[]) throws Exception {
		int i=10;
		while (i>0) {
		mySleep( 5); 
		UpdateSocialContents();
		}
		  
	}
	
	
}
