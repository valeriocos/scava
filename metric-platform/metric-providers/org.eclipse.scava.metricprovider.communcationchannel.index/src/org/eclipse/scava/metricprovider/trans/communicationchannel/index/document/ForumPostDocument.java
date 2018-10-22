package org.eclipse.scava.metricprovider.trans.communicationchannel.index.document;

import java.util.Date;

public class ForumPostDocument extends Document {
	
	private String subject;
	private String text;
	private String user;
	private String PostId;
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @return the postId
	 */
	public String getPostId() {
		return PostId;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * @param postId the postId to set
	 */
	public void setPostId(String postId) {
		PostId = postId;
	}
	
	//TODO add variables 

}
