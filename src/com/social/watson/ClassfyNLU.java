
package com.social.watson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import com.social.util.Log;
import com.social.util.Util;
import com.social.util.PropertiesCache;
import com.social.util.SentenceSplitter;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifyOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.SentimentOptions;

public class ClassfyNLU {

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
	
	private NaturalLanguageUnderstanding service;
	
	public Classification getClassyFromNLU(String text) {
		NaturalLanguageClassifier service = new NaturalLanguageClassifier();
		service.setUsernameAndPassword("{username}", "{password}");

		ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
		  .classifierId("10D41B-nlc-1")
		  .text("How hot will it be today?")
		  .build();
		Classification classification = service.classify("10D41B-nlc-1","How hot will it be today?");
		System.out.println(classification.getTopClass());
		
		return (classification);
	}
	public double getNaturalLanguageClassifier(String checkText) {	
		final int maxLine4Sentence = 20;
		final int maxLen4Sentence = 1000;
		int numLines;
		if (checkText == null) return 0.0;
		Double AvgScore = 0.0;
		Double MaxScore = 0.0;
		Double curScore = 0.0;
		int count = 0;
		numNlc99 = 0;
		avgNlcScore = 0.0;
		
		NaturalLanguageClassifier service = new NaturalLanguageClassifier();
		service.setUsernameAndPassword(CLASSIFIER_username, CLASSIFIER_password );
		
		//List<String> sentences = Util.tokenize(checkText);
		//if (SentenceSplitter.sentenceDetector == null) {
		//	SentenceSplitter sentDec = new SentenceSplitter();
		//}
		//String sentenceArray[]=(SentenceSplitter.sentenceDetector).sentDetect(checkText);
		String sentenceArray[] = {"Hello you are very ugly.", "I hate you so much."};
		List<String> sentences1 = Arrays.asList(sentenceArray);
		
		for (int i = 0; i < sentences1.size(); i++) {
			if (sentences1.get(i) == null) {
				continue;
			}
			//Log.print("\n"+sentences.get(i));
			
			{
				BufferedReader br = new BufferedReader(new StringReader(sentences1.get(i)));
				// find the number of lines in the message
			
				numLines=0;
		    	try {
					while(br.readLine() != null) { 
						numLines++;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	if (numLines >= maxLine4Sentence || sentences1.get(i).length() > maxLen4Sentence)
		    	{
		    		//Sorry, this is not one sentence !
		    		continue;
		    	}
			}
			
			//Classification classification = null;
			ClassifyOptions classifyOptions = new ClassifyOptions.Builder().classifierId(CLASSIFIER_id).text(sentences1.get(i)).build();
			Classification classification = service.classify(CLASSIFIER_id,CLASSIFIER_id);
			//System.out.println(classification);
			
			System.out.println(classification.getClasses().get(0).getConfidence());
			System.out.println(classification.getClasses());
			
			if (classification != null) {
				count ++;
				for (int i1 = 0; i1 < classification.getClasses().size(); i1++) {
					double confidence = classification.getClasses().get(i1).getConfidence();
					if ( classification.getClasses().get(i1).getName().equals("escalated")) {
						AvgScore = AvgScore - confidence;
						curScore = confidence;
						System.out.println("escalated:"+confidence);
					}
					else {
					AvgScore = AvgScore - 1 + confidence;
					curScore = 1 - confidence;
					}
				}
					
				if (curScore > NLC99Threshold ) {
					numNlc99 ++;
				}
				if (curScore > MaxScore) {
					MaxScore = curScore;
					MaxNlcMessage = sentences1.get(i);
				}
			}
			//Log.print("\nNLC curScore:"+curScore);
			//if (Util.TESTING_MODE.equalsIgnoreCase("YES")) 
			{
				System.out.println("\n"+sentences1.get(i)+": NLC Score:"+curScore);
				System.out.println("-------------------------------------------------------------");
			}
//			System.out.println(classification.getTopClass());
//			System.out.println(curScore);
//			
		}
		if ( count > 0 ) {
			avgNlcScore = AvgScore/count;
		} else {
			avgNlcScore = 0.0;
		}
		
		//System.out.println(avgNlcScore);
		
		return (MaxScore);
		//if (count > 0)
		//	return AvgScore/count;
		//return 0.0;
	}
	public static void main(String[] args) {

		ClassfyNLU nlu = new ClassfyNLU();
		String text = "Hello you are very ugly. I hate you so much.";
		System.out.println(nlu.getNaturalLanguageClassifier(text));

	}
}

