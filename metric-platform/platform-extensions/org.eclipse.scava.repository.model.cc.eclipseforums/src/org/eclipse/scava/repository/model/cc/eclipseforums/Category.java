package org.eclipse.scava.repository.model.cc.eclipseforums;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class Category extends Pongo {
	
	
	
	public Category() { 
		super();
		CATEGORY_ID.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Category");
		CATEGORY_NAME.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Category");
		DESCRIPTION.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Category");
		URL.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Category");
		FORUM_URL.setOwningType("org.ossmeter.repository.model.cc.eclipseforums.Category");
	}
	
	public static NumericalQueryProducer CATEGORY_ID = new NumericalQueryProducer("category_id");
	public static StringQueryProducer CATEGORY_NAME = new StringQueryProducer("category_name"); 
	public static StringQueryProducer DESCRIPTION = new StringQueryProducer("description"); 
	public static StringQueryProducer URL = new StringQueryProducer("url"); 
	public static StringQueryProducer FORUM_URL = new StringQueryProducer("forum_url"); 
	
	
	public int getCategory_id() {
		return parseInteger(dbObject.get("category_id")+"", 0);
	}
	
	public Category setCategory_id(int category_id) {
		dbObject.put("category_id", category_id);
		notifyChanged();
		return this;
	}
	public String getCategory_name() {
		return parseString(dbObject.get("category_name")+"", "");
	}
	
	public Category setCategory_name(String category_name) {
		dbObject.put("category_name", category_name);
		notifyChanged();
		return this;
	}
	public String getDescription() {
		return parseString(dbObject.get("description")+"", "");
	}
	
	public Category setDescription(String description) {
		dbObject.put("description", description);
		notifyChanged();
		return this;
	}
	public String getUrl() {
		return parseString(dbObject.get("url")+"", "");
	}
	
	public Category setUrl(String url) {
		dbObject.put("url", url);
		notifyChanged();
		return this;
	}
	public String getForum_url() {
		return parseString(dbObject.get("forum_url")+"", "");
	}
	
	public Category setForum_url(String forum_url) {
		dbObject.put("forum_url", forum_url);
		notifyChanged();
		return this;
	}
	
	
	
	
}