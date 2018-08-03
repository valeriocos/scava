package org.eclipse.scava.metricprovider.trans.textpreprocessing.model;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class BugTrackerCommentsTextPreprocessing extends Pongo {
	
	protected List<String> plainText = null;
	
	
	public BugTrackerCommentsTextPreprocessing() { 
		super();
		dbObject.put("plainText", new BasicDBList());
		BUGTRACKERID.setOwningType("org.eclipse.scava.metricprovider.trans.textpreprocessing.model.BugTrackerCommentsTextPreprocessing");
		BUGID.setOwningType("org.eclipse.scava.metricprovider.trans.textpreprocessing.model.BugTrackerCommentsTextPreprocessing");
		COMMENTID.setOwningType("org.eclipse.scava.metricprovider.trans.textpreprocessing.model.BugTrackerCommentsTextPreprocessing");
		PLAINTEXT.setOwningType("org.eclipse.scava.metricprovider.trans.textpreprocessing.model.BugTrackerCommentsTextPreprocessing");
	}
	
	public static StringQueryProducer BUGTRACKERID = new StringQueryProducer("bugTrackerId"); 
	public static StringQueryProducer BUGID = new StringQueryProducer("bugId"); 
	public static StringQueryProducer COMMENTID = new StringQueryProducer("commentId"); 
	public static ArrayQueryProducer PLAINTEXT = new ArrayQueryProducer("plainText");
	
	
	public String getBugTrackerId() {
		return parseString(dbObject.get("bugTrackerId")+"", "");
	}
	
	public BugTrackerCommentsTextPreprocessing setBugTrackerId(String bugTrackerId) {
		dbObject.put("bugTrackerId", bugTrackerId);
		notifyChanged();
		return this;
	}
	public String getBugId() {
		return parseString(dbObject.get("bugId")+"", "");
	}
	
	public BugTrackerCommentsTextPreprocessing setBugId(String bugId) {
		dbObject.put("bugId", bugId);
		notifyChanged();
		return this;
	}
	public String getCommentId() {
		return parseString(dbObject.get("commentId")+"", "");
	}
	
	public BugTrackerCommentsTextPreprocessing setCommentId(String commentId) {
		dbObject.put("commentId", commentId);
		notifyChanged();
		return this;
	}
	
	public List<String> getPlainText() {
		if (plainText == null) {
			plainText = new PrimitiveList<String>(this, (BasicDBList) dbObject.get("plainText"));
		}
		return plainText;
	}
	
	//Method created by Adrián
	public BugTrackerCommentsTextPreprocessing setPlainText(List<String> plainText) {
		dbObject.put("plainText", plainText);
		this.plainText = plainText;
		notifyChanged();
		return this;
	}
	
}