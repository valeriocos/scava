package org.eclipse.scava.platform.communicationchannel.eclipseforums.utils;

import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelForum;

public class EclipseForumsForum extends CommunicationChannelForum{
	
	private static final long serialVersionUID = 1L;
	
	int topic_count;
	int post_count;
	
	public int getTopic_count() {
		return topic_count;
	}
	public void setTopic_count(int topic_count) {
		this.topic_count = topic_count;
	}
	public int getPost_count() {
		return post_count;
	}
	public void setPost_count(int post_count) {
		this.post_count = post_count;
	}

}
