package com.social.watson;

import com.social.util.PropertiesCache;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;

public class ToneAnalyzerExample {

	  private static final String VERSION_DATE_VALUE = "2018-05-07";
	  public static final String TONE_password = PropertiesCache.getInstance().getProperty("TONE.password");
	  public static final String TONE_username = PropertiesCache.getInstance().getProperty("TONE.username");
	  public static final String TONE_URL = PropertiesCache.getInstance().getProperty("TONE.URL");
	  
  
  public static void main(String[] args) {
    ToneAnalyzer service = new ToneAnalyzer(VERSION_DATE_VALUE);
    service.setUsernameAndPassword(TONE_username, TONE_password);

    String text = "I know the times are difficult! Our sales have been "
        + "disappointing for the past three quarters for our data analytics "
        + "product suite. We have a competitive data analytics product "
        + "suite in the industry. But we need to do our job selling it! "
        + "We need to acknowledge and fix our sales challenges. "
        + "We can’t blame the economy for our lack of execution! " + "We are missing critical sales opportunities. "
        + "Our product is in no way inferior to the competitor products. "
        + "Our clients are hungry for analytical tools to improve their "
        + "business outcomes. Economy has nothing to do with it.";

    // Call the service and get the tone
    ToneOptions toneOptions = new ToneOptions.Builder().build();
    //    .text(text)
    //    .build();
   // ToneAnalysis tone = service.tone(toneOptions).execute();
   // System.out.println(tone);

  }
}