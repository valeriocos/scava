package org.eclipse.scava.platfrom.communicationchannel.eclipseforums.api;

import org.eclipse.scava.platform.Date;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelArticle;

public class EclipseForumsPost extends CommunicationChannelArticle {

	private static final long serialVersionUID = 1L;
	private String id;
	private String subject;
	private String topic_id;
	private String poster_id;
	private Date created_date;
	private String forum_id;
	private String body;
	private String html_url;
	private String topic_url;
	private String forum_url;
	private String user_url;

	public EclipseForumsPost() {

	}

	public String getId() {

		return id;
	}

	public String setId(String id) {

		return this.id = id;
	}

	public String getSubject() {

		return subject;
	}

	public void setSubject(String subject) {

		this.subject = subject;
	}

	public String getBody() {

		return body;
	}

	public void setBody(String body) {

		this.body = body;
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

	public String getPosterId() {

		return poster_id;
	}

	public void setPosterId(String poster_id) {

		this.poster_id = poster_id;
	}

	public String getPostId() {
		return id;
	}

	public void setPostId(String _id) {

		this.id = _id;
	}

	public Date getCreatedDate() {
		return created_date;
	}

	public void setCreatedDate(Date created_date) {
		this.created_date = created_date;
	}

	public String getForumId() {
		return forum_id;
	}

	public void setForumId(String forum_id) {
		this.forum_id = forum_id;
	}

	public String getTopicId() {
		return topic_id;
	}

	public void setTopicId(String topic_id) {
		this.topic_id = topic_id;
	}
}
