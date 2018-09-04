package org.eclipse.scava.repository.model.cc.eclipseforums;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class Topic extends Pongo {
	
	
	
	public Topic() { 
		super();
		TOPIC_ID.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Topic");
		FORUM_ID.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Topic");
		LAST_POST_ID.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Topic");
		LAST_POST_DATE.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Topic");
		ROOT_POST_ID.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Topic");
		REPLIES.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Topic");
		VIEWS.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Topic");
		URL.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Topic");
		FORUM_URL.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Topic");
		POST_URL.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Topic");
	}
	
	public static NumericalQueryProducer TOPIC_ID = new NumericalQueryProducer("topic_id");
	public static NumericalQueryProducer FORUM_ID = new NumericalQueryProducer("forum_id");
	public static NumericalQueryProducer LAST_POST_ID = new NumericalQueryProducer("last_post_id");
	public static StringQueryProducer LAST_POST_DATE = new StringQueryProducer("last_post_date"); 
	public static NumericalQueryProducer ROOT_POST_ID = new NumericalQueryProducer("root_post_id");
	public static NumericalQueryProducer REPLIES = new NumericalQueryProducer("replies");
	public static NumericalQueryProducer VIEWS = new NumericalQueryProducer("views");
	public static StringQueryProducer URL = new StringQueryProducer("url"); 
	public static StringQueryProducer FORUM_URL = new StringQueryProducer("forum_url"); 
	public static StringQueryProducer POST_URL = new StringQueryProducer("post_url"); 
	
	
	public int getTopic_id() {
		return parseInteger(dbObject.get("topic_id")+"", 0);
	}
	
	public Topic setTopic_id(int topic_id) {
		dbObject.put("topic_id", topic_id);
		notifyChanged();
		return this;
	}
	public int getForum_id() {
		return parseInteger(dbObject.get("forum_id")+"", 0);
	}
	
	public Topic setForum_id(int forum_id) {
		dbObject.put("forum_id", forum_id);
		notifyChanged();
		return this;
	}
	public int getLast_post_id() {
		return parseInteger(dbObject.get("last_post_id")+"", 0);
	}
	
	public Topic setLast_post_id(int last_post_id) {
		dbObject.put("last_post_id", last_post_id);
		notifyChanged();
		return this;
	}
	public String getLast_post_date() {
		return parseString(dbObject.get("last_post_date")+"", "");
	}
	
	public Topic setLast_post_date(String last_post_date) {
		dbObject.put("last_post_date", last_post_date);
		notifyChanged();
		return this;
	}
	public int getRoot_post_id() {
		return parseInteger(dbObject.get("root_post_id")+"", 0);
	}
	
	public Topic setRoot_post_id(int root_post_id) {
		dbObject.put("root_post_id", root_post_id);
		notifyChanged();
		return this;
	}
	public int getReplies() {
		return parseInteger(dbObject.get("replies")+"", 0);
	}
	
	public Topic setReplies(int replies) {
		dbObject.put("replies", replies);
		notifyChanged();
		return this;
	}
	public int getViews() {
		return parseInteger(dbObject.get("views")+"", 0);
	}
	
	public Topic setViews(int views) {
		dbObject.put("views", views);
		notifyChanged();
		return this;
	}
	public String getUrl() {
		return parseString(dbObject.get("url")+"", "");
	}
	
	public Topic setUrl(String url) {
		dbObject.put("url", url);
		notifyChanged();
		return this;
	}
	public String getForum_url() {
		return parseString(dbObject.get("forum_url")+"", "");
	}
	
	public Topic setForum_url(String forum_url) {
		dbObject.put("forum_url", forum_url);
		notifyChanged();
		return this;
	}
	public String getPost_url() {
		return parseString(dbObject.get("post_url")+"", "");
	}
	
	public Topic setPost_url(String post_url) {
		dbObject.put("post_url", post_url);
		notifyChanged();
		return this;
	}
	
	
	
	
}