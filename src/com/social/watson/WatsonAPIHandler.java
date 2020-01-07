package com.social.watson;

import com.social.util.PropertiesCache;
import com.ibm.watson.developer_cloud.personality_insights.v3.PersonalityInsights;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.Profile;
import com.ibm.watson.developer_cloud.personality_insights.v3.model.ProfileOptions;

public class WatsonAPIHandler {
	
	public static final String CLASSIFIER_username = PropertiesCache.getInstance().getProperty("CLASSIFIER.username");
	public static final String CLASSIFIER_password = PropertiesCache.getInstance().getProperty("CLASSIFIER.password");
	public static final String CLASSIFIER_id = PropertiesCache.getInstance().getProperty("CLASSIFIER.id");
	public static final String ALCHEMY_key = PropertiesCache.getInstance().getProperty("ALCHEMY.key");
	public static String MaxNlcMessage;
	public static final String PERSONALITY_username1 = PropertiesCache.getInstance().getProperty("PERSONALITY.username1");
	public static final String PERSONALITY_password1 = PropertiesCache.getInstance().getProperty("PERSONALITY.password1");
	public static double avgNlcScore;
	public static int numNlc99;
	public static final double NLC99Threshold = 0.98;
	
	public String WatsonPersonality(ProfileOptions rawContent){
		PersonalityInsights service = new PersonalityInsights("ABC");
		service.setUsernameAndPassword(PERSONALITY_username1, PERSONALITY_password1);

		String returnText = null;
		try {
			  Profile profile = (Profile) service.getProfile(rawContent);
			  //System.out.println(profile);
			  returnText = profile.toString();

			} catch (Exception e) {
			  e.printStackTrace();
			}

		return returnText;
	}
	
	public  double WatsonSentimentNLU(String checkText) {
		SentimentNLU sentNLU = null;
		try {
			sentNLU = new SentimentNLU();
		} catch (Exception e) {
			  e.printStackTrace();
		}
		
		if (sentNLU != null) {
			return (sentNLU.getSentimentFromNLU(checkText));
		}
		return 0.0;
	  }
	
	public double WatsonNaturalLanguageClassifier(String checkText) {	
		ClassfyNLU nlu = new ClassfyNLU();
		
		Double MaxScore = nlu.getNaturalLanguageClassifier(checkText);
		
		MaxNlcMessage = ClassfyNLU.MaxNlcMessage;
		
		return (MaxScore);
		
	}

	

}


