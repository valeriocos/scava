package org.eclipse.scava.metricprovider.trans.sentimentclassification.model;

import com.googlecode.pongo.runtime.*;
import java.util.*;
import com.mongodb.*;

public class ForumPostDetectingSentimentClassificationCollection extends PongoCollection<ForumPostDetectingSentimentClassification> {
	
	public ForumPostDetectingSentimentClassificationCollection(DBCollection dbCollection) {
		super(dbCollection);
	}
	
	public Iterable<ForumPostDetectingSentimentClassification> findById(String id) {
		return new IteratorIterable<ForumPostDetectingSentimentClassification>(new PongoCursorIterator<ForumPostDetectingSentimentClassification>(this, dbCollection.find(new BasicDBObject("_id", id))));
	}
	
	
	@Override
	public Iterator<ForumPostDetectingSentimentClassification> iterator() {
		return new PongoCursorIterator<ForumPostDetectingSentimentClassification>(this, dbCollection.find());
	}
	
	public void add(ForumPostDetectingSentimentClassification forumPostDetectingSentimentClassification) {
		super.add(forumPostDetectingSentimentClassification);
	}
	
	public void remove(ForumPostDetectingSentimentClassification forumPostDetectingSentimentClassification) {
		super.remove(forumPostDetectingSentimentClassification);
	}
	
}