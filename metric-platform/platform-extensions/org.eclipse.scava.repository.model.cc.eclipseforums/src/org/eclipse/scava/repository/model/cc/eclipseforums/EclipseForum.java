package org.eclipse.scava.repository.model.cc.eclipseforums;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class EclipseForum extends org.eclipse.scava.repository.model.CommunicationChannel {
	
	
	
	public EclipseForum() { 
		super();
		super.setSuperTypes("org.eclipse.scava.repository.model.cc.eclipseforums.CommunicationChannel");
		OPEN_ID.setOwningType("org.eclipse.scava.repository.model.cc.eclipseforums.EclipseForum");
		FORUM_ID.setOwningType("org.eclipse.scava.repository.model.cc.eclipseforums.EclipseForum");
		SUBJECT.setOwningType("org.eclipse.scava.repository.model.cc.eclipseforums.EclipseForum");
		DESCRIPTION.setOwningType("org.eclipse.scava.repository.model.cc.eclipseforums.EclipseForum");
	}
	
	public static StringQueryProducer OPEN_ID = new StringQueryProducer("open_id"); 
	public static StringQueryProducer FORUM_ID = new StringQueryProducer("forum_id"); 
	public static StringQueryProducer SUBJECT = new StringQueryProducer("subject"); 
	public static StringQueryProducer DESCRIPTION = new StringQueryProducer("description"); 
	
	
	public String getOpen_id() {
		return parseString(dbObject.get("open_id")+"", "");
	}
	
	public EclipseForum setOpen_id(String open_id) {
		dbObject.put("open_id", open_id);
		notifyChanged();
		return this;
	}
	public String getForum_id() {
		return parseString(dbObject.get("forum_id")+"", "");
	}
	
	public EclipseForum setForum_id(String forum_id) {
		dbObject.put("forum_id", forum_id);
		notifyChanged();
		return this;
	}
	public String getSubject() {
		return parseString(dbObject.get("subject")+"", "");
	}
	
	public EclipseForum setSubject(String subject) {
		dbObject.put("subject", subject);
		notifyChanged();
		return this;
	}
	public String getDescription() {
		return parseString(dbObject.get("description")+"", "");
	}
	
	public EclipseForum setDescription(String description) {
		dbObject.put("description", description);
		notifyChanged();
		return this;
	}
	
	
	
	
}