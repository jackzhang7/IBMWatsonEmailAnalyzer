package com.social.UI.model;

import java.time.LocalDate;
import java.util.Date;

public class MySocialContent {
	
	public Date receivedTime;
	public String content;
	public String subject;
	public String kind;
	String fromWho;
	public String toMe;
	public Double sentiment;
	
	public Double getSentiment() {
		return sentiment;
	}
	public void setSentiment(Double sentiment) {
		this.sentiment = sentiment;
	}
	public String getFromWho() {
		return fromWho;
	}
	public void setFromWho(String fromWho) {
		this.fromWho = fromWho;
	}
	
	public Date getReceivedTime() {
		return receivedTime;
	}
	public void setReceivedTime(Date date) {
		this.receivedTime = date;
	}
	
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	
	public String getToMe() {
		return toMe;
	}
	public void setToMe(String toMe) {
		this.toMe = toMe;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public MySocialContent(String kind, String fromWho, String toMe, Date receivedTime, String content, Double sentiment, String subject) {
		this.kind = kind;
		this.fromWho = fromWho;
		this.toMe = toMe;
		this.receivedTime = receivedTime;
		this.content = content;
		this.sentiment = sentiment;
		this.subject = subject;
	}
	public MySocialContent() {
		
	}
	

}
