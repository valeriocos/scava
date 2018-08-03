package org.eclipse.scava.metricprovider.trans.textpreprocessing.model;

import com.googlecode.pongo.runtime.*;
import java.util.*;
import com.mongodb.*;

public class BugTrackerCommentsTextPreprocessingCollection extends PongoCollection<BugTrackerCommentsTextPreprocessing> {
	
	public BugTrackerCommentsTextPreprocessingCollection(DBCollection dbCollection) {
		super(dbCollection);
		createIndex("bugTrackerId");
	}
	
	public Iterable<BugTrackerCommentsTextPreprocessing> findById(String id) {
		return new IteratorIterable<BugTrackerCommentsTextPreprocessing>(new PongoCursorIterator<BugTrackerCommentsTextPreprocessing>(this, dbCollection.find(new BasicDBObject("_id", id))));
	}
	
	public Iterable<BugTrackerCommentsTextPreprocessing> findByBugTrackerId(String q) {
		return new IteratorIterable<BugTrackerCommentsTextPreprocessing>(new PongoCursorIterator<BugTrackerCommentsTextPreprocessing>(this, dbCollection.find(new BasicDBObject("bugTrackerId", q + ""))));
	}
	
	public BugTrackerCommentsTextPreprocessing findOneByBugTrackerId(String q) {
		BugTrackerCommentsTextPreprocessing bugTrackerCommentsTextPreprocessing = (BugTrackerCommentsTextPreprocessing) PongoFactory.getInstance().createPongo(dbCollection.findOne(new BasicDBObject("bugTrackerId", q + "")));
		if (bugTrackerCommentsTextPreprocessing != null) {
			bugTrackerCommentsTextPreprocessing.setPongoCollection(this);
		}
		return bugTrackerCommentsTextPreprocessing;
	}
	

	public long countByBugTrackerId(String q) {
		return dbCollection.count(new BasicDBObject("bugTrackerId", q + ""));
	}
	
	@Override
	public Iterator<BugTrackerCommentsTextPreprocessing> iterator() {
		return new PongoCursorIterator<BugTrackerCommentsTextPreprocessing>(this, dbCollection.find());
	}
	
	public void add(BugTrackerCommentsTextPreprocessing bugTrackerCommentsTextPreprocessing) {
		super.add(bugTrackerCommentsTextPreprocessing);
	}
	
	public void remove(BugTrackerCommentsTextPreprocessing bugTrackerCommentsTextPreprocessing) {
		super.remove(bugTrackerCommentsTextPreprocessing);
	}
	
}