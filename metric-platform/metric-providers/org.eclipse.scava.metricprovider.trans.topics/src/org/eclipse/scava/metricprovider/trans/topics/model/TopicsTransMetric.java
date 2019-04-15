/*******************************************************************************
 * Copyright (c) 2019 Edge Hill University
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
package org.eclipse.scava.metricprovider.trans.topics.model;

import com.googlecode.pongo.runtime.*;
import com.mongodb.*;
// protected region custom-imports on begin
// protected region custom-imports end

public class TopicsTransMetric extends PongoDB {
	
	public TopicsTransMetric() {}
	
	public TopicsTransMetric(DB db) {
		setDb(db);
	}
	
	protected BugTrackerCommentsDataCollection bugTrackerComments = null;
	protected BugTrackerTopicCollection bugTrackerTopics = null;
	protected NewsgroupArticlesDataCollection newsgroupArticles = null;
	protected NewsgroupTopicCollection newsgroupTopics = null;
	protected ForumPostDataCollection forumPosts = null;
	protected ForumPostTopicCollection forumTopics = null;
	
	// protected region custom-fields-and-methods on begin
	// protected region custom-fields-and-methods end
	
	
	public BugTrackerCommentsDataCollection getBugTrackerComments() {
		return bugTrackerComments;
	}
	
	public BugTrackerTopicCollection getBugTrackerTopics() {
		return bugTrackerTopics;
	}
	
	public NewsgroupArticlesDataCollection getNewsgroupArticles() {
		return newsgroupArticles;
	}
	
	public NewsgroupTopicCollection getNewsgroupTopics() {
		return newsgroupTopics;
	}
	
	public ForumPostDataCollection getForumPosts() {
		return forumPosts;
	}
	
	public ForumPostTopicCollection getForumTopics() {
		return forumTopics;
	}
	
	
	@Override
	public void setDb(DB db) {
		super.setDb(db);
		bugTrackerComments = new BugTrackerCommentsDataCollection(db.getCollection("TopicsTransMetric.bugTrackerComments"));
		pongoCollections.add(bugTrackerComments);
		bugTrackerTopics = new BugTrackerTopicCollection(db.getCollection("TopicsTransMetric.bugTrackerTopics"));
		pongoCollections.add(bugTrackerTopics);
		newsgroupArticles = new NewsgroupArticlesDataCollection(db.getCollection("TopicsTransMetric.newsgroupArticles"));
		pongoCollections.add(newsgroupArticles);
		newsgroupTopics = new NewsgroupTopicCollection(db.getCollection("TopicsTransMetric.newsgroupTopics"));
		pongoCollections.add(newsgroupTopics);
		forumPosts = new ForumPostDataCollection(db.getCollection("TopicsTransMetric.forumPosts"));
		pongoCollections.add(forumPosts);
		forumTopics = new ForumPostTopicCollection(db.getCollection("TopicsTransMetric.forumTopics"));
		pongoCollections.add(forumTopics);
	}
}