package org.eclipse.scava.index.indexer.example;

import java.util.Date;

public class ExampleDocument {

	String uid;
	String body;
	String creator;
	String emotion;
	String sentiment;
	String severity;
	
	Date created_at;
	Date updated_at;
	
	Boolean request;
	Boolean contains_code;

	public String getUid() {
		return uid;
	}


	public String getBody() {
		return body;
	}

	public String getCreator() {
		return creator;
	}

	public String getEmotion() {
		return emotion;
	}

	public String getSentiment() {
		return sentiment;
	}

	public String getSeverity() {
		return severity;
	}

	public Boolean getRequest() {
		return request;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public Boolean getContains_code() {
		return contains_code;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}


	public void setBody(String body) {
		this.body = body;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}

	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public void setRequest(Boolean request) {
		this.request = request;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public void setContains_code(Boolean contains_code) {
		this.contains_code = contains_code;
	}
		
}
