package org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model;

import com.googlecode.pongo.runtime.*;
import java.util.*;
import com.mongodb.*;

public class TopicDataCollection extends PongoCollection<TopicData> {
	
	public TopicDataCollection(DBCollection dbCollection) {
		super(dbCollection);
		createIndex("forumId");
	}
	
	public Iterable<TopicData> findById(String id) {
		return new IteratorIterable<TopicData>(new PongoCursorIterator<TopicData>(this, dbCollection.find(new BasicDBObject("_id", id))));
	}
	
	public Iterable<TopicData> findByForumId(String q) {
		return new IteratorIterable<TopicData>(new PongoCursorIterator<TopicData>(this, dbCollection.find(new BasicDBObject("forumId", q + ""))));
	}
	
	public TopicData findOneByForumId(String q) {
		TopicData topicData = (TopicData) PongoFactory.getInstance().createPongo(dbCollection.findOne(new BasicDBObject("forumId", q + "")));
		if (topicData != null) {
			topicData.setPongoCollection(this);
		}
		return topicData;
	}
	

	public long countByForumId(String q) {
		return dbCollection.count(new BasicDBObject("forumId", q + ""));
	}
	
	@Override
	public Iterator<TopicData> iterator() {
		return new PongoCursorIterator<TopicData>(this, dbCollection.find());
	}
	
	public void add(TopicData topicData) {
		super.add(topicData);
	}
	
	public void remove(TopicData topicData) {
		super.remove(topicData);
	}
	
}