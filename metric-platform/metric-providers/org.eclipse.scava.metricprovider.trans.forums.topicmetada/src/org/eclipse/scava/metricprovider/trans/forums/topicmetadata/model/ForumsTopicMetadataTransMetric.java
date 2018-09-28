package org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model;

import com.googlecode.pongo.runtime.*;
import com.mongodb.*;
// protected region custom-imports on begin
// protected region custom-imports end

public class ForumsTopicMetadataTransMetric extends PongoDB {
	
	public ForumsTopicMetadataTransMetric() {}
	
	public ForumsTopicMetadataTransMetric(DB db) {
		setDb(db);
	}
	
	protected TopicDataCollection topicData = null;
	protected PostDataCollection posts = null;
	
	// protected region custom-fields-and-methods on begin
	// protected region custom-fields-and-methods end
	
	
	public TopicDataCollection getTopicData() {
		return topicData;
	}
	
	public PostDataCollection getPosts() {
		return posts;
	}
	
	
	@Override
	public void setDb(DB db) {
		super.setDb(db);
		topicData = new TopicDataCollection(db.getCollection("ForumsTopicMetadataTransMetric.topicData"));
		pongoCollections.add(topicData);
		posts = new PostDataCollection(db.getCollection("ForumsTopicMetadataTransMetric.posts"));
		pongoCollections.add(posts);
	}
}