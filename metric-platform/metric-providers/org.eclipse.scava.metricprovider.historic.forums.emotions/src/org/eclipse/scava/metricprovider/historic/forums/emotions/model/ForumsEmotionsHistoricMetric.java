package org.eclipse.scava.metricprovider.historic.forums.emotions.model;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class ForumsEmotionsHistoricMetric extends Pongo {
	
	protected List<Forums> forums = null;
	protected List<Emotion> emotions = null;
	
	
	public ForumsEmotionsHistoricMetric() { 
		super();
		dbObject.put("forums", new BasicDBList());
		dbObject.put("emotions", new BasicDBList());
	}
	
	
	
	
	
	public List<Forums> getForums() {
		if (forums == null) {
			forums = new PongoList<Forums>(this, "forums", true);
		}
		return forums;
	}
	public List<Emotion> getEmotions() {
		if (emotions == null) {
			emotions = new PongoList<Emotion>(this, "emotions", true);
		}
		return emotions;
	}
	
	
}