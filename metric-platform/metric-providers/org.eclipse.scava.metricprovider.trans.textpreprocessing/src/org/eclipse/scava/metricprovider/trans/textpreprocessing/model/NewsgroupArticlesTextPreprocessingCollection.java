package org.eclipse.scava.metricprovider.trans.textpreprocessing.model;

import com.googlecode.pongo.runtime.*;
import java.util.*;
import com.mongodb.*;

public class NewsgroupArticlesTextPreprocessingCollection extends PongoCollection<NewsgroupArticlesTextPreprocessing> {
	
	public NewsgroupArticlesTextPreprocessingCollection(DBCollection dbCollection) {
		super(dbCollection);
		createIndex("newsGroupName");
	}
	
	public Iterable<NewsgroupArticlesTextPreprocessing> findById(String id) {
		return new IteratorIterable<NewsgroupArticlesTextPreprocessing>(new PongoCursorIterator<NewsgroupArticlesTextPreprocessing>(this, dbCollection.find(new BasicDBObject("_id", id))));
	}
	
	public Iterable<NewsgroupArticlesTextPreprocessing> findByNewsGroupName(String q) {
		return new IteratorIterable<NewsgroupArticlesTextPreprocessing>(new PongoCursorIterator<NewsgroupArticlesTextPreprocessing>(this, dbCollection.find(new BasicDBObject("newsGroupName", q + ""))));
	}
	
	public NewsgroupArticlesTextPreprocessing findOneByNewsGroupName(String q) {
		NewsgroupArticlesTextPreprocessing newsgroupArticlesTextPreprocessing = (NewsgroupArticlesTextPreprocessing) PongoFactory.getInstance().createPongo(dbCollection.findOne(new BasicDBObject("newsGroupName", q + "")));
		if (newsgroupArticlesTextPreprocessing != null) {
			newsgroupArticlesTextPreprocessing.setPongoCollection(this);
		}
		return newsgroupArticlesTextPreprocessing;
	}
	

	public long countByNewsGroupName(String q) {
		return dbCollection.count(new BasicDBObject("newsGroupName", q + ""));
	}
	
	@Override
	public Iterator<NewsgroupArticlesTextPreprocessing> iterator() {
		return new PongoCursorIterator<NewsgroupArticlesTextPreprocessing>(this, dbCollection.find());
	}
	
	public void add(NewsgroupArticlesTextPreprocessing newsgroupArticlesTextPreprocessing) {
		super.add(newsgroupArticlesTextPreprocessing);
	}
	
	public void remove(NewsgroupArticlesTextPreprocessing newsgroupArticlesTextPreprocessing) {
		super.remove(newsgroupArticlesTextPreprocessing);
	}
	
}