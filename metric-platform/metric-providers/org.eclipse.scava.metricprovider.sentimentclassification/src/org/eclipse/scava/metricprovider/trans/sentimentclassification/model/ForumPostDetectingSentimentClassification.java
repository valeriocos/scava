package org.eclipse.scava.metricprovider.trans.sentimentclassification.model;

import com.googlecode.pongo.runtime.Pongo;
import com.googlecode.pongo.runtime.querying.StringQueryProducer;


public class ForumPostDetectingSentimentClassification extends Pongo {
	
	
	
	public ForumPostDetectingSentimentClassification() { 
		super();
		FORUMID.setOwningType("org.eclipse.scava.metricprovider.trans.sentimentclassification.model.ForumPostDetectingSentimentClassification");
		POSTID.setOwningType("org.eclipse.scava.metricprovider.trans.sentimentclassification.model.ForumPostDetectingSentimentClassification");
		CLASSIFICATIONRESULT.setOwningType("org.eclipse.scava.metricprovider.trans.sentimentclassification.model.ForumPostDetectingSentimentClassification");
		EMOTIONALDIMENSIONS.setOwningType("org.eclipse.scava.metricprovider.trans.sentimentclassification.model.ForumPostDetectingSentimentClassification");
	}
	
	public static StringQueryProducer FORUMID = new StringQueryProducer("forumId"); 
	public static StringQueryProducer POSTID = new StringQueryProducer("postId"); 
	public static StringQueryProducer CLASSIFICATIONRESULT = new StringQueryProducer("classificationResult"); 
	public static StringQueryProducer EMOTIONALDIMENSIONS = new StringQueryProducer("emotionalDimensions"); 
	
	
	public String getForumId() {
		return parseString(dbObject.get("forumId")+"", "");
	}
	
	public ForumPostDetectingSentimentClassification setForumId(String forumId) {
		dbObject.put("forumId", forumId);
		notifyChanged();
		return this;
	}
	public String getPostId() {
		return parseString(dbObject.get("postId")+"", "");
	}
	
	public ForumPostDetectingSentimentClassification setPostId(String postId) {
		dbObject.put("postId", postId);
		notifyChanged();
		return this;
	}
	public String getClassificationResult() {
		return parseString(dbObject.get("classificationResult")+"", "");
	}
	
	public ForumPostDetectingSentimentClassification setClassificationResult(String classificationResult) {
		dbObject.put("classificationResult", classificationResult);
		notifyChanged();
		return this;
	}
	public String getEmotionalDimensions() {
		return parseString(dbObject.get("emotionalDimensions")+"", "");
	}
	
	public ForumPostDetectingSentimentClassification setEmotionalDimensions(String emotionalDimensions) {
		dbObject.put("emotionalDimensions", emotionalDimensions);
		notifyChanged();
		return this;
	}
	
	
	
	
}