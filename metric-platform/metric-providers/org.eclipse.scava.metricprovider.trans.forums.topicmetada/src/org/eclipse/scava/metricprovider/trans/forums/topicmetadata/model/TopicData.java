package org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class TopicData extends Pongo {
	
	
	
	public TopicData() { 
		super();
		FORUMID.setOwningType("org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model.TopicData");
		TOPICID.setOwningType("org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model.TopicData");
		PRIORITY.setOwningType("org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model.TopicData");
		CREATIONDATE.setOwningType("org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model.TopicData");
		AVERAGESENTIMENT.setOwningType("org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model.TopicData");
		STARTSENTIMENT.setOwningType("org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model.TopicData");
		ENDSENTIMENT.setOwningType("org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model.TopicData");
	}
	
	public static StringQueryProducer FORUMID = new StringQueryProducer("forumId"); 
	public static StringQueryProducer TOPICID = new StringQueryProducer("topicId"); 
	public static StringQueryProducer PRIORITY = new StringQueryProducer("priority"); 
	public static StringQueryProducer CREATIONDATE = new StringQueryProducer("creationDate"); 
	public static NumericalQueryProducer AVERAGESENTIMENT = new NumericalQueryProducer("averageSentiment");
	public static StringQueryProducer STARTSENTIMENT = new StringQueryProducer("startSentiment"); 
	public static StringQueryProducer ENDSENTIMENT = new StringQueryProducer("endSentiment"); 
	
	
	public String getForumId() {
		return parseString(dbObject.get("forumId")+"", "");
	}
	
	public TopicData setForumId(String forumId) {
		dbObject.put("forumId", forumId);
		notifyChanged();
		return this;
	}
	public String getTopicId() {
		return parseString(dbObject.get("topicId")+"", "");
	}
	
	public TopicData setTopicId(String topicId) {
		dbObject.put("topicId", topicId);
		notifyChanged();
		return this;
	}
	public String getPriority() {
		return parseString(dbObject.get("priority")+"", "");
	}
	
	public TopicData setPriority(String priority) {
		dbObject.put("priority", priority);
		notifyChanged();
		return this;
	}
	public String getCreationDate() {
		return parseString(dbObject.get("creationDate")+"", "");
	}
	
	public TopicData setCreationDate(String creationDate) {
		dbObject.put("creationDate", creationDate);
		notifyChanged();
		return this;
	}
	public float getAverageSentiment() {
		return parseFloat(dbObject.get("averageSentiment")+"", 0.0f);
	}
	
	public TopicData setAverageSentiment(float averageSentiment) {
		dbObject.put("averageSentiment", averageSentiment);
		notifyChanged();
		return this;
	}
	public String getStartSentiment() {
		return parseString(dbObject.get("startSentiment")+"", "");
	}
	
	public TopicData setStartSentiment(String startSentiment) {
		dbObject.put("startSentiment", startSentiment);
		notifyChanged();
		return this;
	}
	public String getEndSentiment() {
		return parseString(dbObject.get("endSentiment")+"", "");
	}
	
	public TopicData setEndSentiment(String endSentiment) {
		dbObject.put("endSentiment", endSentiment);
		notifyChanged();
		return this;
	}
	
	
	
	
}