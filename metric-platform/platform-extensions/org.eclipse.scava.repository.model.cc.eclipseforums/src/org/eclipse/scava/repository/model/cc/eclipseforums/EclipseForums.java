package org.eclipse.scava.repository.model.cc.eclipseforums;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;

// protected region custom-imports on begin
// protected region custom-imports end

public class EclipseForums extends org.eclipse.scava.repository.model.CommunicationChannel {
	
	
	// protected region custom-fields-and-methods on begin
	// protected region custom-fields-and-methods end
	
	public EclipseForums() { 
		super();
		super.setSuperTypes("org.ossmeter.repository.model.cc.eclipseforums.CommunicationChannel");
		USERNAME.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.EclipseForums");
		PASSWORD.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.EclipseForums");
		CATEGORY.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.EclipseForums");
		FORUM_ID.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.EclipseForums");
		TOPIC_ID.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.EclipseForums");
	}
	
	public static StringQueryProducer USERNAME = new StringQueryProducer("username"); 
	public static StringQueryProducer PASSWORD = new StringQueryProducer("password"); 
	public static StringQueryProducer CATEGORY = new StringQueryProducer("category"); 
	public static StringQueryProducer FORUM_ID = new StringQueryProducer("forum_id");
	public static StringQueryProducer TOPIC_ID = new StringQueryProducer("topic_id");
	
	
	public String getUsername() {
		return parseString(dbObject.get("username")+"", "");
	}
	
	public EclipseForums setUsername(String username) {
		dbObject.put("username", username);
		notifyChanged();
		return this;
	}
	public String getPassword() {
		return parseString(dbObject.get("password")+"", "");
	}
	
	public EclipseForums setPassword(String password) {
		dbObject.put("password", password);
		notifyChanged();
		return this;
	}
	public String getCategory() {
		return parseString(dbObject.get("category")+"", "");
	}
	
	public EclipseForums setCategory(String category) {
		dbObject.put("category", category);
		notifyChanged();
		return this;
	}
	public String getForum_id() {
		return parseString(dbObject.get("forum_id")+"", "");
	}
	
	public EclipseForums setForum_id(int forum_id) {
		dbObject.put("forum_id", forum_id);
		notifyChanged();
		return this;
	}
	public String getTopic_id() {
		return parseString(dbObject.get("topic_id")+"", "");
	}
	
	public EclipseForums setTopic_id(String topic_id) {
		dbObject.put("topic_id", topic_id);
		notifyChanged();
		return this;
	}
	
	
	
	
}