package org.eclipse.scava.metricprovider.trans.textpreprocessing.model;

import com.googlecode.pongo.runtime.*;
import com.mongodb.*;
// protected region custom-imports on begin
// protected region custom-imports end

public class TextpreprocessingTransMetric extends PongoDB {
	
	public TextpreprocessingTransMetric() {}
	
	public TextpreprocessingTransMetric(DB db) {
		setDb(db);
	}
	
	protected BugTrackerCommentsTextPreprocessingCollection bugTrackerComments = null;
	protected NewsgroupArticlesTextPreprocessingCollection newsgroupArticles = null;
	
	// protected region custom-fields-and-methods on begin
	// protected region custom-fields-and-methods end
	
	
	public BugTrackerCommentsTextPreprocessingCollection getBugTrackerComments() {
		return bugTrackerComments;
	}
	
	public NewsgroupArticlesTextPreprocessingCollection getNewsgroupArticles() {
		return newsgroupArticles;
	}
	
	
	@Override
	public void setDb(DB db) {
		super.setDb(db);
		bugTrackerComments = new BugTrackerCommentsTextPreprocessingCollection(db.getCollection("TextpreprocessingTransMetric.bugTrackerComments"));
		pongoCollections.add(bugTrackerComments);
		newsgroupArticles = new NewsgroupArticlesTextPreprocessingCollection(db.getCollection("TextpreprocessingTransMetric.newsgroupArticles"));
		pongoCollections.add(newsgroupArticles);
	}
}