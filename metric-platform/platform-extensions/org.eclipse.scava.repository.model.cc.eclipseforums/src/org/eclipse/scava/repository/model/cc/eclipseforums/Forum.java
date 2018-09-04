package org.eclipse.scava.repository.model.cc.eclipseforums;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class Forum extends Pongo {
	
	
	
	public Forum() { 
		super();
		FORUM_ID.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Forum");
		FORUM_NAME.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Forum");
		CATEGORY_ID.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Forum");
		DESCRIPTION.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Forum");
		CREATED_DATE.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Forum");
		TOPIC_COUNT.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Forum");
		POST_COUNT.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Forum");
		LAST_POST_ID.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Forum");
		CATEGORY_URL.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Forum");
	}
	
	public static NumericalQueryProducer FORUM_ID = new NumericalQueryProducer("forum_id");
	public static StringQueryProducer FORUM_NAME = new StringQueryProducer("forum_name"); 
	public static StringQueryProducer CATEGORY_ID = new StringQueryProducer("category_id"); 
	public static StringQueryProducer DESCRIPTION = new StringQueryProducer("description"); 
	public static StringQueryProducer CREATED_DATE = new StringQueryProducer("created_date"); 
	public static NumericalQueryProducer TOPIC_COUNT = new NumericalQueryProducer("topic_count");
	public static NumericalQueryProducer POST_COUNT = new NumericalQueryProducer("post_count");
	public static NumericalQueryProducer LAST_POST_ID = new NumericalQueryProducer("last_post_id");
	public static StringQueryProducer CATEGORY_URL = new StringQueryProducer("category_url"); 
	
	
	public int getForum_id() {
		return parseInteger(dbObject.get("forum_id")+"", 0);
	}
	
	public Forum setForum_id(int forum_id) {
		dbObject.put("forum_id", forum_id);
		notifyChanged();
		return this;
	}
	public String getForum_name() {
		return parseString(dbObject.get("forum_name")+"", "");
	}
	
	public Forum setForum_name(String forum_name) {
		dbObject.put("forum_name", forum_name);
		notifyChanged();
		return this;
	}
	public String getCategory_id() {
		return parseString(dbObject.get("category_id")+"", "");
	}
	
	public Forum setCategory_id(String category_id) {
		dbObject.put("category_id", category_id);
		notifyChanged();
		return this;
	}
	public String getDescription() {
		return parseString(dbObject.get("description")+"", "");
	}
	
	public Forum setDescription(String description) {
		dbObject.put("description", description);
		notifyChanged();
		return this;
	}
	public String getCreated_date() {
		return parseString(dbObject.get("created_date")+"", "");
	}
	
	public Forum setCreated_date(String created_date) {
		dbObject.put("created_date", created_date);
		notifyChanged();
		return this;
	}
	public int getTopic_count() {
		return parseInteger(dbObject.get("topic_count")+"", 0);
	}
	
	public Forum setTopic_count(int topic_count) {
		dbObject.put("topic_count", topic_count);
		notifyChanged();
		return this;
	}
	public int getPost_count() {
		return parseInteger(dbObject.get("post_count")+"", 0);
	}
	
	public Forum setPost_count(int post_count) {
		dbObject.put("post_count", post_count);
		notifyChanged();
		return this;
	}
	public int getLast_post_id() {
		return parseInteger(dbObject.get("last_post_id")+"", 0);
	}
	
	public Forum setLast_post_id(int last_post_id) {
		dbObject.put("last_post_id", last_post_id);
		notifyChanged();
		return this;
	}
	public String getCategory_url() {
		return parseString(dbObject.get("category_url")+"", "");
	}
	
	public Forum setCategory_url(String category_url) {
		dbObject.put("category_url", category_url);
		notifyChanged();
		return this;
	}
	
	
	
	
}