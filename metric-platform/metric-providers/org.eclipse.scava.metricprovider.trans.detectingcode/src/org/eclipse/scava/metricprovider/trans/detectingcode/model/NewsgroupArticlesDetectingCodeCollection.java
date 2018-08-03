package org.eclipse.scava.metricprovider.trans.detectingcode.model;

import com.googlecode.pongo.runtime.*;
import java.util.*;
import com.mongodb.*;

public class NewsgroupArticlesDetectingCodeCollection extends PongoCollection<NewsgroupArticlesDetectingCode> {
	
	public NewsgroupArticlesDetectingCodeCollection(DBCollection dbCollection) {
		super(dbCollection);
		createIndex("newsGroupName");
	}
	
	public Iterable<NewsgroupArticlesDetectingCode> findById(String id) {
		return new IteratorIterable<NewsgroupArticlesDetectingCode>(new PongoCursorIterator<NewsgroupArticlesDetectingCode>(this, dbCollection.find(new BasicDBObject("_id", id))));
	}
	
	public Iterable<NewsgroupArticlesDetectingCode> findByNewsGroupName(String q) {
		return new IteratorIterable<NewsgroupArticlesDetectingCode>(new PongoCursorIterator<NewsgroupArticlesDetectingCode>(this, dbCollection.find(new BasicDBObject("newsGroupName", q + ""))));
	}
	
	public NewsgroupArticlesDetectingCode findOneByNewsGroupName(String q) {
		NewsgroupArticlesDetectingCode newsgroupArticlesDetectingCode = (NewsgroupArticlesDetectingCode) PongoFactory.getInstance().createPongo(dbCollection.findOne(new BasicDBObject("newsGroupName", q + "")));
		if (newsgroupArticlesDetectingCode != null) {
			newsgroupArticlesDetectingCode.setPongoCollection(this);
		}
		return newsgroupArticlesDetectingCode;
	}
	

	public long countByNewsGroupName(String q) {
		return dbCollection.count(new BasicDBObject("newsGroupName", q + ""));
	}
	
	@Override
	public Iterator<NewsgroupArticlesDetectingCode> iterator() {
		return new PongoCursorIterator<NewsgroupArticlesDetectingCode>(this, dbCollection.find());
	}
	
	public void add(NewsgroupArticlesDetectingCode newsgroupArticlesDetectingCode) {
		super.add(newsgroupArticlesDetectingCode);
	}
	
	public void remove(NewsgroupArticlesDetectingCode newsgroupArticlesDetectingCode) {
		super.remove(newsgroupArticlesDetectingCode);
	}
	
}