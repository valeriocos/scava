package org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model;

import com.googlecode.pongo.runtime.*;
import java.util.*;
import com.mongodb.*;

public class PostDataCollection extends PongoCollection<PostData> {
	
	public PostDataCollection(DBCollection dbCollection) {
		super(dbCollection);
		createIndex("forumId");
	}
	
	public Iterable<PostData> findById(String id) {
		return new IteratorIterable<PostData>(new PongoCursorIterator<PostData>(this, dbCollection.find(new BasicDBObject("_id", id))));
	}
	
	public Iterable<PostData> findByForumId(String q) {
		return new IteratorIterable<PostData>(new PongoCursorIterator<PostData>(this, dbCollection.find(new BasicDBObject("forumId", q + ""))));
	}
	
	public PostData findOneByForumId(String q) {
		PostData postData = (PostData) PongoFactory.getInstance().createPongo(dbCollection.findOne(new BasicDBObject("forumId", q + "")));
		if (postData != null) {
			postData.setPongoCollection(this);
		}
		return postData;
	}
	

	public long countByForumId(String q) {
		return dbCollection.count(new BasicDBObject("forumId", q + ""));
	}
	
	@Override
	public Iterator<PostData> iterator() {
		return new PongoCursorIterator<PostData>(this, dbCollection.find());
	}
	
	public void add(PostData postData) {
		super.add(postData);
	}
	
	public void remove(PostData postData) {
		super.remove(postData);
	}
	
}