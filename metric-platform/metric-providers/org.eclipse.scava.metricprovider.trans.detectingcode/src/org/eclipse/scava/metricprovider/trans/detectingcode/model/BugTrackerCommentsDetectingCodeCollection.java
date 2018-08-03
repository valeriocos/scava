package org.eclipse.scava.metricprovider.trans.detectingcode.model;

import com.googlecode.pongo.runtime.*;
import java.util.*;
import com.mongodb.*;

public class BugTrackerCommentsDetectingCodeCollection extends PongoCollection<BugTrackerCommentsDetectingCode> {
	
	public BugTrackerCommentsDetectingCodeCollection(DBCollection dbCollection) {
		super(dbCollection);
		createIndex("bugTrackerId");
	}
	
	public Iterable<BugTrackerCommentsDetectingCode> findById(String id) {
		return new IteratorIterable<BugTrackerCommentsDetectingCode>(new PongoCursorIterator<BugTrackerCommentsDetectingCode>(this, dbCollection.find(new BasicDBObject("_id", id))));
	}
	
	public Iterable<BugTrackerCommentsDetectingCode> findByBugTrackerId(String q) {
		return new IteratorIterable<BugTrackerCommentsDetectingCode>(new PongoCursorIterator<BugTrackerCommentsDetectingCode>(this, dbCollection.find(new BasicDBObject("bugTrackerId", q + ""))));
	}
	
	public BugTrackerCommentsDetectingCode findOneByBugTrackerId(String q) {
		BugTrackerCommentsDetectingCode bugTrackerCommentsDetectingCode = (BugTrackerCommentsDetectingCode) PongoFactory.getInstance().createPongo(dbCollection.findOne(new BasicDBObject("bugTrackerId", q + "")));
		if (bugTrackerCommentsDetectingCode != null) {
			bugTrackerCommentsDetectingCode.setPongoCollection(this);
		}
		return bugTrackerCommentsDetectingCode;
	}
	

	public long countByBugTrackerId(String q) {
		return dbCollection.count(new BasicDBObject("bugTrackerId", q + ""));
	}
	
	@Override
	public Iterator<BugTrackerCommentsDetectingCode> iterator() {
		return new PongoCursorIterator<BugTrackerCommentsDetectingCode>(this, dbCollection.find());
	}
	
	public void add(BugTrackerCommentsDetectingCode bugTrackerCommentsDetectingCode) {
		super.add(bugTrackerCommentsDetectingCode);
	}
	
	public void remove(BugTrackerCommentsDetectingCode bugTrackerCommentsDetectingCode) {
		super.remove(bugTrackerCommentsDetectingCode);
	}
	
}