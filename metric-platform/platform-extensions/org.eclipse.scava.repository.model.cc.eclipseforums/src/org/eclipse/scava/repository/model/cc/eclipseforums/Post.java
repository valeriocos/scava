package org.eclipse.scava.repository.model.cc.eclipseforums;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class Post extends Pongo {
	
	
	
	public Post() { 
		super();
		POST_ID.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Post");
		SUBJECT.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Post");
		FORUM_ID.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Post");
		TOPIC_ID.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Post");
		POSTER_ID.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Post");
		CREATED_DATE.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Post");
		URL.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Post");
		BODY.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Post");
		TOPIC_URL.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Post");
		TOPIC_POSTS_URL.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Post");
	}
	
	public static NumericalQueryProducer POST_ID = new NumericalQueryProducer("post_id");
	public static StringQueryProducer SUBJECT = new StringQueryProducer("subject"); 
	public static NumericalQueryProducer FORUM_ID = new NumericalQueryProducer("forum_id");
	public static NumericalQueryProducer TOPIC_ID = new NumericalQueryProducer("topic_id");
	public static NumericalQueryProducer POSTER_ID = new NumericalQueryProducer("poster_id");
	public static StringQueryProducer CREATED_DATE = new StringQueryProducer("created_date"); 
	public static StringQueryProducer URL = new StringQueryProducer("url"); 
	public static StringQueryProducer BODY = new StringQueryProducer("body"); 
	public static StringQueryProducer TOPIC_URL = new StringQueryProducer("topic_url"); 
	public static StringQueryProducer TOPIC_POSTS_URL = new StringQueryProducer("topic_posts_url"); 
	
	
	public int getPost_id() {
		return parseInteger(dbObject.get("post_id")+"", 0);
	}
	
	public Post setPost_id(int post_id) {
		dbObject.put("post_id", post_id);
		notifyChanged();
		return this;
	}
	public String getSubject() {
		return parseString(dbObject.get("subject")+"", "");
	}
	
	public Post setSubject(String subject) {
		dbObject.put("subject", subject);
		notifyChanged();
		return this;
	}
	public int getForum_id() {
		return parseInteger(dbObject.get("forum_id")+"", 0);
	}
	
	public Post setForum_id(int forum_id) {
		dbObject.put("forum_id", forum_id);
		notifyChanged();
		return this;
	}
	public int getTopic_id() {
		return parseInteger(dbObject.get("topic_id")+"", 0);
	}
	
	public Post setTopic_id(int topic_id) {
		dbObject.put("topic_id", topic_id);
		notifyChanged();
		return this;
	}
	public int getPoster_id() {
		return parseInteger(dbObject.get("poster_id")+"", 0);
	}
	
	public Post setPoster_id(int poster_id) {
		dbObject.put("poster_id", poster_id);
		notifyChanged();
		return this;
	}
	public String getCreated_date() {
		return parseString(dbObject.get("created_date")+"", "");
	}
	
	public Post setCreated_date(String created_date) {
		dbObject.put("created_date", created_date);
		notifyChanged();
		return this;
	}
	public String getUrl() {
		return parseString(dbObject.get("url")+"", "");
	}
	
	public Post setUrl(String url) {
		dbObject.put("url", url);
		notifyChanged();
		return this;
	}
	public String getBody() {
		return parseString(dbObject.get("body")+"", "");
	}
	
	public Post setBody(String body) {
		dbObject.put("body", body);
		notifyChanged();
		return this;
	}
	public String getTopic_url() {
		return parseString(dbObject.get("topic_url")+"", "");
	}
	
	public Post setTopic_url(String topic_url) {
		dbObject.put("topic_url", topic_url);
		notifyChanged();
		return this;
	}
	public String getTopic_posts_url() {
		return parseString(dbObject.get("topic_posts_url")+"", "");
	}
	
	public Post setTopic_posts_url(String topic_posts_url) {
		dbObject.put("topic_posts_url", topic_posts_url);
		notifyChanged();
		return this;
	}
	
	
	
	
}