package org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class PostData extends Pongo {
	
	
	
	public PostData() { 
		super();
		FORUMID.setOwningType("org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model.PostData");
		TOPICID.setOwningType("org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model.PostData");
		POSTID.setOwningType("org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model.PostData");
		CREATIONDATE.setOwningType("org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model.PostData");
		CREATOR.setOwningType("org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model.PostData");
		CONTENTCLASS.setOwningType("org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model.PostData");
		REQUESTREPLYPREDICTION.setOwningType("org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model.PostData");
	}
	
	public static StringQueryProducer FORUMID = new StringQueryProducer("forumId"); 
	public static StringQueryProducer TOPICID = new StringQueryProducer("topicId"); 
	public static StringQueryProducer POSTID = new StringQueryProducer("postId"); 
	public static StringQueryProducer CREATIONDATE = new StringQueryProducer("creationDate"); 
	public static StringQueryProducer CREATOR = new StringQueryProducer("creator"); 
	public static StringQueryProducer CONTENTCLASS = new StringQueryProducer("contentClass"); 
	public static StringQueryProducer REQUESTREPLYPREDICTION = new StringQueryProducer("requestReplyPrediction"); 
	
	
	public String getForumId() {
		return parseString(dbObject.get("forumId")+"", "");
	}
	
	public PostData setForumId(String forumId) {
		dbObject.put("forumId", forumId);
		notifyChanged();
		return this;
	}
	public String getTopicId() {
		return parseString(dbObject.get("topicId")+"", "");
	}
	
	public PostData setTopicId(String topicId) {
		dbObject.put("topicId", topicId);
		notifyChanged();
		return this;
	}
	public String getPostId() {
		return parseString(dbObject.get("postId")+"", "");
	}
	
	public PostData setPostId(String postId) {
		dbObject.put("postId", postId);
		notifyChanged();
		return this;
	}
	public String getCreationDate() {
		return parseString(dbObject.get("creationDate")+"", "");
	}
	
	public PostData setCreationDate(String creationDate) {
		dbObject.put("creationDate", creationDate);
		notifyChanged();
		return this;
	}
	public String getCreator() {
		return parseString(dbObject.get("creator")+"", "");
	}
	
	public PostData setCreator(String creator) {
		dbObject.put("creator", creator);
		notifyChanged();
		return this;
	}
	public String getContentClass() {
		return parseString(dbObject.get("contentClass")+"", "");
	}
	
	public PostData setContentClass(String contentClass) {
		dbObject.put("contentClass", contentClass);
		notifyChanged();
		return this;
	}
	public String getRequestReplyPrediction() {
		return parseString(dbObject.get("requestReplyPrediction")+"", "");
	}
	
	public PostData setRequestReplyPrediction(String requestReplyPrediction) {
		dbObject.put("requestReplyPrediction", requestReplyPrediction);
		notifyChanged();
		return this;
	}
	
	
	
	
}