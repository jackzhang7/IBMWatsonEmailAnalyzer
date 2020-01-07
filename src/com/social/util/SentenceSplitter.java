package com.social.util;

import java.io.IOException;
import java.io.InputStream;


import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceSplitter {

	public static SentenceDetector sentenceDetector = null;
	
	public SentenceSplitter() {
		
	    if (sentenceDetector != null) {
	    	return;
	    }
		InputStream modelIn = null;
	     
	    try {
	       modelIn = getClass().getResourceAsStream("en-sent.bin");
	       final SentenceModel sentenceModel = new SentenceModel(modelIn);
	       modelIn.close();
	       sentenceDetector = new SentenceDetectorME(sentenceModel);
	    }
	    catch (final IOException ioe) {
	           ioe.printStackTrace();
	        }
	    finally {
	           if (modelIn != null) {
	              try {
	                 modelIn.close();
	              } catch (final IOException e) {}
	           }
	        }

	}
	
	public static void main(String[] args) {
		
		SentenceSplitter sentence = new SentenceSplitter();
		SentenceDetector st = SentenceSplitter.sentenceDetector;
		String sentences[]=st.sentDetect("I?am Amal. I am engineer. I like travelling and driving");
		for(int i=0; i<sentences.length;i++)
		{
			System.out.println(sentences[i]);
		}

	}
	
}
