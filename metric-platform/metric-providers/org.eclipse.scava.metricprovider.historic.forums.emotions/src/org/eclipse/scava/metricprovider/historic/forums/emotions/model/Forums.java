package org.eclipse.scava.metricprovider.historic.forums.emotions.model;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class Forums extends Pongo {
	
	
	
	public Forums() { 
		super();
		FORUMID.setOwningType("org.eclipse.scava.metricprovider.historic.forums.emotions.model.Forums");
		TOPICID.setOwningType("org.eclipse.scava.metricprovider.historic.forums.emotions.model.Forums");
		NUMBEROFPOSTS.setOwningType("org.eclipse.scava.metricprovider.historic.forums.emotions.model.Forums");
		CUMULATIVENUMBEROFPOSTS.setOwningType("org.eclipse.scava.metricprovider.historic.forums.emotions.model.Forums");
	}
	
	public static StringQueryProducer FORUMID = new StringQueryProducer("forumId"); 
	public static StringQueryProducer TOPICID = new StringQueryProducer("topicId"); 
	public static NumericalQueryProducer NUMBEROFPOSTS = new NumericalQueryProducer("numberOfPosts");
	public static NumericalQueryProducer CUMULATIVENUMBEROFPOSTS = new NumericalQueryProducer("cumulativeNumberOfPosts");
	
	
	public String getForumId() {
		return parseString(dbObject.get("forumId")+"", "");
	}
	
	public Forums setForumId(String forumId) {
		dbObject.put("forumId", forumId);
		notifyChanged();
		return this;
	}
	public String getTopicId() {
		return parseString(dbObject.get("topicId")+"", "");
	}
	
	public Forums setTopicId(String topicId) {
		dbObject.put("topicId", topicId);
		notifyChanged();
		return this;
	}
	public int getNumberOfPosts() {
		return parseInteger(dbObject.get("numberOfPosts")+"", 0);
	}
	
	public Forums setNumberOfPosts(int numberOfPosts) {
		dbObject.put("numberOfPosts", numberOfPosts);
		notifyChanged();
		return this;
	}
	public int getCumulativeNumberOfPosts() {
		return parseInteger(dbObject.get("cumulativeNumberOfPosts")+"", 0);
	}
	
	public Forums setCumulativeNumberOfPosts(int cumulativeNumberOfPosts) {
		dbObject.put("cumulativeNumberOfPosts", cumulativeNumberOfPosts);
		notifyChanged();
		return this;
	}
	
	
	
	
}