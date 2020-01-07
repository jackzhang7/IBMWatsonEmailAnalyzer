package com.social.watson;


import java.io.IOException;

import com.social.util.Log;
import com.social.util.PropertiesCache;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.SentimentOptions;

public class SentimentNLU {

	private static final String VERSION_DATE_VALUE = "2018-05-07";
	public static final String  NLU_password = PropertiesCache.getInstance().getProperty("NLU.password");
	public static final String NLU_username = PropertiesCache.getInstance().getProperty("NLU.username");
	public static final String NLU_URL = PropertiesCache.getInstance().getProperty("NLU.URL");
	/*
	 * { "url":
	 * "https://gateway.watsonplatform.net/natural-language-understanding/api",
	 * "username": "6ce9190c-4008-49a0-81e1-ed31acd005dd", "password":
	 * "zls4mfruRdPN" }
	 */
	
	private NaturalLanguageUnderstanding service;
	public Double getSentimentFromNLU(String text) {
		NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(VERSION_DATE_VALUE);
		service.setUsernameAndPassword(NLU_username, NLU_password);
		
		SentimentOptions sentiment = new SentimentOptions.Builder().document(true).build();
		Features features = new Features.Builder().sentiment(sentiment).build();
		AnalyzeOptions parameters = new AnalyzeOptions.Builder().text(text).features(features).build();
		AnalysisResults response = null;
		try {
			response = service.analyze(parameters).execute();
		} catch (Exception e)  {
			Log.print(e);
		}
		
		//System.out.println(response.getSentiment().getDocument().getScore());
		if ( response != null ) {
			System.out.println(response.getSentiment().getDocument());
			return (response.getSentiment().getDocument().getScore());
		} 
		return 0.0;
	}

	public static void main(String[] args) {

		SentimentNLU nlu = new SentimentNLU();
		String text = "You are average.";
		System.out.println(nlu.getSentimentFromNLU(text));

	}
}

