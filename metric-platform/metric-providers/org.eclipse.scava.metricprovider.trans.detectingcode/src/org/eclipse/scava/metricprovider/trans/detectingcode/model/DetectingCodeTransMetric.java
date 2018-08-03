package org.eclipse.scava.metricprovider.trans.detectingcode.model;

import com.googlecode.pongo.runtime.*;
import com.mongodb.*;
// protected region custom-imports on begin
// protected region custom-imports end

public class DetectingCodeTransMetric extends PongoDB {
	
	public DetectingCodeTransMetric() {}
	
	public DetectingCodeTransMetric(DB db) {
		setDb(db);
	}
	
	protected BugTrackerCommentsDetectingCodeCollection bugTrackerComments = null;
	protected NewsgroupArticlesDetectingCodeCollection newsgroupArticles = null;
	
	// protected region custom-fields-and-methods on begin
	// protected region custom-fields-and-methods end
	
	
	public BugTrackerCommentsDetectingCodeCollection getBugTrackerComments() {
		return bugTrackerComments;
	}
	
	public NewsgroupArticlesDetectingCodeCollection getNewsgroupArticles() {
		return newsgroupArticles;
	}
	
	
	@Override
	public void setDb(DB db) {
		super.setDb(db);
		bugTrackerComments = new BugTrackerCommentsDetectingCodeCollection(db.getCollection("DetectingCodeTransMetric.bugTrackerComments"));
		pongoCollections.add(bugTrackerComments);
		newsgroupArticles = new NewsgroupArticlesDetectingCodeCollection(db.getCollection("DetectingCodeTransMetric.newsgroupArticles"));
		pongoCollections.add(newsgroupArticles);
	}
}