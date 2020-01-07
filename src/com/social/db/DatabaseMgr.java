package com.social.db;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Date;
import java.util.Hashtable;
import com.social.UI.model.MySocialContent;
import com.social.util.Log;
import com.social.util.Util;

public class DatabaseMgr {

	private static Connection connection = null;
	private static Connection write_connection = null;
	private static Connection social_connection = null;

	public static final int CONNECTION_FOR_READ = 0;
	public static final int CONNECTION_FOR_WRITE = 1;
	public static final int CONNECTION_FOR_SPHINX = 2;
	public static final int CONNECTION_FOR_SURVEY = 3;
	
	public DatabaseMgr() {

		initDatabase();
	}
	
	public void initDatabase() {

		if (available() == true) {
			return;
		}

		String dbServerName = Util.DATABASE_HOST_NAME;
		String dbPort = Util.DATABASE_PORT;
		String dbName = Util.DATABASE_NAME;
		String dbUsername = Util.DATABASE_USER;
		String dbPassword = Util.DATABASE_PASSWD;

		String dbUrl = "jdbc:mysql://" + dbServerName + ":" + dbPort + "/" + dbName+ "?useTimezone=true&serverTimezone=EST5EDT";
		//Log.print(dbUrl);
		
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				 connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			//Log.print("INITIALIZING DATABASE");
		
	}
	
	public void closeConnection() {
		
		try {
			connection.close();
		} catch (SQLException e) {
			Log.print(e);
		}
	}
	
	public boolean available() {
		if (connection == null) {
			return false;
		} 

		try {
			if (connection.isValid(3)) {
				return true;
			} else {
				Log.print("DATABASE CONNECTION DOWN");
				return false;
			}
			
		} catch (SQLException e) {
			//.print(e);
			return false;			
		}
	}
	
	public boolean write_available() {
		if (write_connection == null) {
			return false;
		} 

		try {
			if (write_connection.isValid(3)) {
				return true;
			} else {
				Log.print("DATABASE CONNECTION FOR WRITE DOWN");
				return false;
			}
			
		} catch (SQLException e) {
			//Log.print(e);
			return false;			
		}
	}
	
	public boolean social_available() {
		if (connection == null) {
			return false;
		} 

		try {
			if (connection.isValid(3)) {
				return true;
			} else {
				Log.print("DATABASE CONNECTION FOR WRITE DOWN");
				return false;
			}
			
		} catch (SQLException e) {
			Log.print(e);
			return false;			
		}
	}
	
	public ResultSet queryDatabase(String query) {
		if (available() == false) {
			return null;
		}
		try {
			Statement st = social_connection.createStatement();
			ResultSet rs = st.executeQuery(query);

			return rs;
		} catch (SQLException e) {
			Log.print(e);
			return null;
		}
	}
	public ResultSet querySocialDatabase(String query) {
		if (social_available() == false) {
			return null;
		}
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);

			return rs;
		} catch (SQLException e) {
			Log.print(e);
			return null;
		}
	}
	
	public void closeResultSet(ResultSet rs) {

		if (rs == null) {
			return;
		}
		try {
			rs.close();
		} catch (SQLException e) {
			Log.print(e);
		}
	}

	public void closeStatement(Statement stmt) {

		try {
			stmt.close();
		} catch (SQLException e) {
			//Log.print(e);
		}
	}

	public void closePreparedStatement(PreparedStatement statement) {

		try {
			statement.close();
		} catch (SQLException e) {
			//.print(e);
		}
	}

	
	public Statement getWriteStatement() {
		
		try {
			return social_connection.createStatement();
		} catch (SQLException e) {
			Log.print(e);
			return null;
		}
	}
	
	public PreparedStatement getPreparedStatement(String query) {
		
		try {
			return social_connection.prepareStatement(query);
		} catch (SQLException e) {
			Log.print(e);
			return null;
		}
	}
	
	public PreparedStatement getPreparedStatement(String query, int flag) {
		
		try {
			return social_connection.prepareStatement(query, flag);
		} catch (SQLException e) {
			//Log.print(e);
			return null;
		}
	}
	
	public HashMap<String, MySocialContent> loadAllMySocialContentFromDB() {
		HashMap<String, MySocialContent> allMySocialContents = new HashMap<String, MySocialContent>();

		String query = "SELECT kind, Sentiment, Subject, FromWho, ReceivedTime, ToMe, Content from emails";
		ResultSet rs = querySocialDatabase(query);

		if (rs == null) {
			return null;
		}
		try {
			while (rs.next() == true) {
				String kind = rs.getString("kind");
				Double sentiment = rs.getDouble("Sentiment");
				String subject = rs.getString("Subject");
				String fromWho = rs.getString("FromWho");
				Timestamp receivedTime1 = rs.getTimestamp("ReceivedTime");
				String toMe = rs.getString("ToMe");
				String content = rs.getString("Content");

				//Timestamp updatedTime = rs.getTimestamp("updatedTime");
				Date date = new Date (receivedTime1.getTime());
				String timeString = Util.getLocalTimeString(date, "America/New_York");
				

				MySocialContent mySocialContent = new MySocialContent(kind, fromWho, toMe, date, content, sentiment, subject);
			//	Log.print("===================================");
			//	Log.print(kind+"@"+timeString);
				

				allMySocialContents.put(kind+"@"+timeString, mySocialContent);
			}
		} catch (SQLException e) {
			Log.print(e);
			closeResultSet(rs);
			return null;
		}

		closeResultSet(rs);
		return allMySocialContents;
	}
	
	public HashMap<String, MySocialContent> loadAllMySocialContentFromDB(Date lastUpdateTime) {
		HashMap<String, MySocialContent> allMySocialContents = new HashMap<String, MySocialContent>();
		String lastQueryDate = Util.getTimestampString(lastUpdateTime);
	
		String query = "SELECT kind, Sentiment, Subject, FromWho, ReceivedTime, ToMe, Content"
				        +"from emails where " + "(updatedTime > '"+lastQueryDate+"');";
		ResultSet rs = querySocialDatabase(query);

		if (rs == null) {
			return null;
		}
		try {
			while (rs.next() == true) {
				String kind = rs.getString("kind");
				Double sentiment = rs.getDouble("Sentiment");
				String subject = rs.getString("Subject");
				String fromWho = rs.getString("FromWho");
				Date receivedTime = rs.getDate("ReceivedTime");
				String toMe = rs.getString("ToMe");
				String content = rs.getString("Content");

				//Timestamp updatedTime = rs.getTimestamp("updatedTime");
				
				//Date date = new Date(updatedTime.getTime());

				MySocialContent mySocialContent = new MySocialContent(kind, fromWho, toMe, (java.sql.Time) receivedTime, content, sentiment, subject);

				allMySocialContents.put(kind+"@"+receivedTime, mySocialContent);
			}
		} catch (SQLException e) {
			Log.print(e);
			closeResultSet(rs);
			return null;
		}

		closeResultSet(rs);
		return allMySocialContents;
	}
	
	public void insertSocialContentEntry(MySocialContent sContent) {
		
		String query = "INSERT INTO emails (kind, FromWho, ReceivedTime, ToMe, Content, Subject, Sentiment)"
				+ " values (?, ?, ?, ?, ?, ?, ?)";

		PreparedStatement preparedStmt = null;
		try {
			Timestamp updateTime = null;
			preparedStmt = connection.prepareStatement(query);
			preparedStmt.setString(1, sContent.getKind());
			preparedStmt.setString(2, sContent.getFromWho());
			if (sContent.getReceivedTime() != null) {
				updateTime = new Timestamp(sContent.getReceivedTime().getTime());
			}
			preparedStmt.setTimestamp(3, updateTime);
			preparedStmt.setString(4, sContent.getToMe());
			preparedStmt.setString(5, sContent.getContent());
			preparedStmt.setString(6, sContent.getSubject());
			preparedStmt.setDouble(7, sContent.getSentiment());
			
			preparedStmt.execute();
			preparedStmt.close();
		} catch (SQLException e) {
			closeStatement(preparedStmt);
			//Log.print("Failed to update MySocialContents ");
			//Log.print(e);
			System.out.println(e);
			return;
		}
		
	}

	

}