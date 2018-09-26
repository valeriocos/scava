package org.eclipse.scava.platform.communicationchannel.eclipseforums.utils;

import org.eclipse.scava.platform.Date;
import org.eclipse.scava.platform.delta.communicationchannel.CommuincationChannelForumPost;


public class EclipseForumsPost extends CommuincationChannelForumPost {

	private static final long serialVersionUID = 1L;

	private String html_url;
	private String topic_url;
	private String forum_url;
	private String user_url;

	public EclipseForumsPost() {

	}

	
	public String getHtmlUrl() {

		return html_url;
	}

	public void setHtmlUrl(String html_url) {

		this.html_url = html_url;
	}

	public String getforumUrl() {

		return forum_url;
	}

	public void setForumUrl(String forum_url) {

		this.forum_url = forum_url;
	}

	public String getUserUrl() {

		return user_url;
	}

	public void setUserUrl(String user_url) {

		this.user_url = user_url;
	}

	public String getTopicUrl() {

		return topic_url;
	}

	public void setTopicUrl(String topic_url) {

		this.topic_url = topic_url;
	}


}
