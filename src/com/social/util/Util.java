package com.social.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.BreakIterator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;

public class Util {

		public static final String DATABASE_HOST_NAME = PropertiesCache.getInstance().getProperty("DATABASE.HOSTNAME");
		public static final String DATABASE_PORT = PropertiesCache.getInstance().getProperty("DATABASE.PORT");
		public static final String DATABASE_NAME = PropertiesCache.getInstance().getProperty("DATABASE.NAME");
		public static final String DATABASE_USER = PropertiesCache.getInstance().getProperty("DATABASE.USER");
		public static final String DATABASE_PASSWD = PropertiesCache.getInstance().getProperty("DATABASE.PASSWD");
		public static final String GMAIL_USER = PropertiesCache.getInstance().getProperty("GMAIL.USER");
		public static final String GMAIL_PASSWD = PropertiesCache.getInstance().getProperty("GMAIL.PASSWD");
		//public static final
		
		public static final String NLU_username = PropertiesCache.getInstance().getProperty("NLU.username");
		public static final String NLU_password = PropertiesCache.getInstance().getProperty("NLU.password");
		public static final String NLU_URL = PropertiesCache.getInstance().getProperty("NLU.URL");
		
		public static final String TESTING_MODE = PropertiesCache.getInstance().getProperty("TESTING.MODE");
		public static final String TESTING_toAddr = PropertiesCache.getInstance().getProperty("TESTING.toAddr");
		public static final String TESTING_ccAddr = PropertiesCache.getInstance().getProperty("TESTING.ccAddr");
		public static final String TESTING_from = PropertiesCache.getInstance().getProperty("TESTING.from");
		
		
		

		
		public static String getTimestampString(Date date) {

			DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dfm.format(date);
			
		}
		public static Date getDateFromTimestampString(String date) {

			DateFormat dfm = new SimpleDateFormat("yyyy/MM/dd HH:mm z");
			try {
				return dfm.parse(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		public static Date getDateFromTimestampString(String date, String format) {

			DateFormat dfm = new SimpleDateFormat(format);
			try {
				return dfm.parse(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		

		
		

		
		

		public static Double round(Double input) {

			// precise to the first decimal place
			return (double) Math.round(Double.valueOf(input) * 10) / 10;
		}
		
		public static Double round(Double input, int nthDecimal) {

			int n = (int) Math.pow(10, nthDecimal);
			// precise to the first decimal place
			return (double) Math.round(Double.valueOf(input) * n) / n;
		}



		public static byte[] toBytes(long num, int width) {
			byte[] ret = new byte[width];
			for (int i = 0; i < width; i++) {
				ret[width - i - 1] = (byte) (num & 0xFF);
				num = (num >> 8);
			}
			return ret;
		}

		public static String getCalendarInHHT(Calendar calendar) {
			int hh, min, m; 

			hh = calendar.get(Calendar.HOUR_OF_DAY);

			min = calendar.get(Calendar.MINUTE);

			m = (min + 3) / 6;

			if (m == 10) {
				m = 0;
				hh++;
			}
			return ((hh < 10) ? "0" : "") + hh + m;
		}
		
		public static String arrayListToString(ArrayList<String> list) {
			
			String result = "";
			
			if (list == null) {
				return result;
			}
			
			for (int i = 0; i < list.size(); i++) {
				result += list.get(i);
				if (i != list.size() - 1) {
					result += " ";
				}
			}
			
			return result;
		}
		

		
		
		public static LinkedHashMap<String, Integer> sortByTimeRange (LinkedHashMap<String, Integer> map) {
			List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
			Collections.sort(entries,
					new Comparator<Map.Entry<String, Integer>>() {
						public int compare(
								Map.Entry<String, Integer> a,
								Map.Entry<String, Integer> b) {
							String aKey = a.getKey();
							String bKey = b.getKey();
							
							if (aKey.contains("/w")) { //by week
								Integer aKeyInt = Integer.valueOf(aKey.substring(0, aKey.indexOf("/w"))) * 52 + Integer.valueOf(aKey.substring(aKey.indexOf("/w") + 2));
								Integer bKeyInt = Integer.valueOf(bKey.substring(0, bKey.indexOf("/w"))) * 52 + Integer.valueOf(bKey.substring(bKey.indexOf("/w") + 2));
								
								return aKeyInt.compareTo(bKeyInt);
							} else if (aKey.contains("/Q")) { //by quarter
								Integer aKeyInt = Integer.valueOf(aKey.substring(0, aKey.indexOf("/Q"))) * 4 + Integer.valueOf(aKey.substring(aKey.indexOf("/Q") + 2));
								Integer bKeyInt = Integer.valueOf(bKey.substring(0, bKey.indexOf("/Q"))) * 4 + Integer.valueOf(bKey.substring(bKey.indexOf("/Q") + 2));
								
								return aKeyInt.compareTo(bKeyInt);
							} else if (aKey.contains("/") == false) { //by year
								Integer aKeyInt = Integer.valueOf(aKey);
								Integer bKeyInt = Integer.valueOf(bKey);
								
								return aKeyInt.compareTo(bKeyInt);
							} else { //by month
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMM");
									try {
										Date aKeyDate = sdf.parse(aKey);
										Date bKeyDate = sdf.parse(bKey);
										
										if(aKeyDate.before(bKeyDate)) {
											return -1;
										} else if (aKeyDate.after(bKeyDate)) {
											return 1;
										} else {
											return 0;
										}
									} catch (ParseException e) {
										Log.print(e);
										return 0;
									}
							}						
						}
					});
			map.clear();
			for (Map.Entry<String, Integer> entry : entries) {
				map.put(entry.getKey(), entry.getValue());
			}
			
			return map;
		}
		public static String getLocalTimeString(Date date, String tz) {

			//if (tz == null || tz.trim().equalsIgnoreCase("undefined")) {
			//	tz = DEFAULT_TIMEZONE;
			//}
			DateFormat dfm = new SimpleDateFormat("yyyy/MM/dd HH:mm z");
			dfm.setTimeZone(TimeZone.getTimeZone(tz));
			return dfm.format(date);
		}
		public static LinkedHashMap<String, String> tempSort (LinkedHashMap<String, String> map) {
			List<Map.Entry<String, String>> entries = new ArrayList<Map.Entry<String, String>>(map.entrySet());
			Collections.sort(entries,
					new Comparator<Map.Entry<String, String>>() {
						public int compare(
								Map.Entry<String, String> a,
								Map.Entry<String, String> b) {
							String aKey = a.getKey();
							String bKey = b.getKey();
							
							if (aKey.contains("/w")) { //by week
								Integer aKeyInt = Integer.valueOf(aKey.substring(0, aKey.indexOf("/w"))) * 52 + Integer.valueOf(aKey.substring(aKey.indexOf("/w") + 2));
								Integer bKeyInt = Integer.valueOf(bKey.substring(0, bKey.indexOf("/w"))) * 52 + Integer.valueOf(bKey.substring(bKey.indexOf("/w") + 2));
								
								return aKeyInt.compareTo(bKeyInt);
							} else if (aKey.contains("/Q")) { //by quarter
								Integer aKeyInt = Integer.valueOf(aKey.substring(0, aKey.indexOf("/Q"))) * 4 + Integer.valueOf(aKey.substring(aKey.indexOf("/Q") + 2));
								Integer bKeyInt = Integer.valueOf(bKey.substring(0, bKey.indexOf("/Q"))) * 4 + Integer.valueOf(bKey.substring(bKey.indexOf("/Q") + 2));
								
								return aKeyInt.compareTo(bKeyInt);
							} else if (aKey.contains("/") == false) { //by year
								Integer aKeyInt = Integer.valueOf(aKey);
								Integer bKeyInt = Integer.valueOf(bKey);
								
								return aKeyInt.compareTo(bKeyInt);
							} else { //by month
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMM");
									try {
										Date aKeyDate = sdf.parse(aKey);
										Date bKeyDate = sdf.parse(bKey);
										
										if(aKeyDate.before(bKeyDate)) {
											return -1;
										} else if (aKeyDate.after(bKeyDate)) {
											return 1;
										} else {
											return 0;
										}
									} catch (ParseException e) {
										Log.print(e);
										return 0;
									}
							}						
						}
					});
			map.clear();
			for (Map.Entry<String, String> entry : entries) {
				map.put(entry.getKey(), entry.getValue());
			}
			
			return map;
		}
		
		
		
		
		


		
		public static LinkedHashMap<String, Integer> sortHashMap (LinkedHashMap<String, Integer> map) {
			List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
			Collections.sort(entries,
					new Comparator<Map.Entry<String, Integer>>() {
						public int compare(
								Map.Entry<String, Integer> a,
								Map.Entry<String, Integer> b) {
							Integer aCount = a.getValue();
							Integer bCount = b.getValue();
							
							return 0 - aCount.compareTo(bCount);
						}
					});
			map.clear();
			for (Map.Entry<String, Integer> entry : entries) {
				map.put(entry.getKey(), entry.getValue());
			}
			
			return map;
		}
		public static long getDuration(Date startDate, Date endDate) {
			
			return (endDate.getTime() - startDate.getTime()) / 1000 / 60 / 60 / 24 + 1;
		}

		public static long getDurationInHour(Date startDate, Date endDate) {
			
			return (endDate.getTime() - startDate.getTime()) / 1000 / 60 / 60 ;
		}
		
		

		private static void addLine(Vector<String> vector, String line) {

			if (line.length() >= 2 && line.charAt(0) == ' '
					&& line.charAt(1) != ' ') {
				String newLine = line.substring(1);
				vector.addElement(newLine);
			} else {
				vector.addElement(line);
			}
		}

		private static String getNextWord(String text, int startIndex) {

			int index = 0;
			String tempText = text.substring(startIndex);

			if (tempText.charAt(index) == ' ') {
				// return all spaces
				do {
					index++;
				} while (index < tempText.length() && tempText.charAt(index) == ' ');
				return tempText.substring(0, index);
			} else if (tempText.charAt(index) == '\n') {
				return "\n";
			} else if (tempText.charAt(index) == '\r') {
				return "\r";
			} else {
				// return a word
				do {
					index++;
				} while (index < tempText.length() && tempText.charAt(index) != ' '
						&& tempText.charAt(index) != '\n'
						&& tempText.charAt(index) != '\r');
				return tempText.substring(0, index);
			}
		}
		
		public static boolean isNumeric(String s) {  
		    return s.matches("[-+]?\\d*\\.?\\d+");  
		}
		


		


		
		public static String getUTCOffSetFromTimeZone(String timeZone) {

			String toTimezone;

			int utcOffset = TimeZone.getTimeZone(timeZone).getRawOffset() / 1000 / 60 / 60;

			if (utcOffset < 0) {
				if (Math.abs(utcOffset) / 10 == 0)
					toTimezone = "-0" + Math.abs(utcOffset) + ":00";
				else {
					toTimezone = utcOffset + ":00";

				}
			} else {
				if (utcOffset / 10 == 0)
					toTimezone = "+0" + utcOffset + ":00";
				else {
					toTimezone = "+" + utcOffset + ":00";
				}
			}
			return toTimezone;
		}
		


		
		public static String getRegionBITarget(String regionName) {
			
			if (regionName == null) {
				return "0";
			}
			
			if (regionName.equalsIgnoreCase("WW")) {
				return "0.9";
			} else if (regionName.equalsIgnoreCase("NA")) {
				return "0.9";
			} else if (regionName.equalsIgnoreCase("EP")) {
				return "0.95";
			} else if (regionName.equalsIgnoreCase("Japan")) {
				return "0.9";
			} else if (regionName.equalsIgnoreCase("GCG")) {
				return "0.5";
			} else if (regionName.equalsIgnoreCase("MEA")) {
				return "0.8";
			} else if (regionName.equalsIgnoreCase("AP")) {
				return "0.5";
			} else {
				return "0";
			}
		}
		
		
		public static boolean isValidTimeZoneId(String timeZoneId) {
		    for (String tzId : TimeZone.getAvailableIDs()) {
	            if(tzId.equalsIgnoreCase(timeZoneId)) {
	                return true;
	            }
		    }
		    return false;
		}
		public static List<String> tokenize(String text){
		    List<String> sentences = new ArrayList<String>();
		    Locale currentLocale = new Locale("English", "American");
		    BreakIterator sentenceIterator = BreakIterator.getSentenceInstance(currentLocale);      
		    sentenceIterator.setText(text);
		    int boundary = sentenceIterator.first();
		    int lastBoundary = 0;
		    while (boundary != BreakIterator.DONE) {
		        boundary = sentenceIterator.next();         
		        if(boundary != BreakIterator.DONE){
		            sentences.add(text.substring(lastBoundary, boundary));
		        }
		        lastBoundary = boundary;            
		    }
		    return sentences;
		}
			
		public static String getDateDiff( Date d1, Date d2)
		{
			 long diff = d2.getTime() - d1.getTime();
		        long diffMinutes = diff / (60 * 1000) % 60;
		        int diffInDays = (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
		        long diffHours = diff / (60 * 60 * 1000 )% 24;

			String interval= diffInDays+"days "+ diffHours+"hours " +diffMinutes+"minutes";
			return interval;

		}
		//public String getPath() throws UnsupportedEncodingException {
		public String getPath() {	
			String path = this.getClass().getClassLoader().getResource("").getPath();
			String fullPath ="";
			try {
				fullPath = URLDecoder.decode(path, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//String pathArr[] = fullPath.split("/WEB-INF/classes/");
			//fullPath = pathArr[0];
			return fullPath;
		}
		public static String parseHtmlToText(String htmlStr) {
			if (htmlStr == null) return htmlStr;
			htmlStr = htmlStr.replaceAll("Body: ", "");
			htmlStr = htmlStr.replaceAll("</p><p>", "\n");
			htmlStr = htmlStr.replaceAll("</?p>", "");
			htmlStr = htmlStr.replaceAll("&gt;|&#62;", ">");
			htmlStr = htmlStr.replaceAll("&#39;", "\'");
			htmlStr = htmlStr.replaceAll("&lt;|&#60;", "<");
			htmlStr = htmlStr.replaceAll("&amp;|&#38;", "&");
			htmlStr = htmlStr.replaceAll("&quot;|&#34;", "\"");
			
			return htmlStr;
		}
		
}
