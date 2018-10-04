package org.eclipse.scava.metricprovider.historic.index;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scava.metricprovider.historic.index.model.IndexHistoricMetric;
import org.eclipse.scava.metricprovider.trans.detectingcode.DetectingCodeTransMetricProvider;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.DetectingCodeTransMetric;
import org.eclipse.scava.metricprovider.trans.plaintextprocessing.PlainTextProcessingTransMetricProvider;
import org.eclipse.scava.metricprovider.trans.plaintextprocessing.model.BugTrackerCommentPlainTextProcessing;
import org.eclipse.scava.metricprovider.trans.plaintextprocessing.model.PlainTextProcessingTransMetric;
import org.eclipse.scava.metricprovider.trans.sentimentclassification.SentimentClassificationTransMetricProvider;
import org.eclipse.scava.metricprovider.trans.severityclassification.SeverityClassificationTransMetricProvider;
import org.eclipse.scava.platform.AbstractHistoricalMetricProvider;
import org.eclipse.scava.platform.IMetricProvider;
import org.eclipse.scava.platform.MetricProviderContext;
import org.eclipse.scava.platform.delta.bugtrackingsystem.PlatformBugTrackingSystemManager;
import org.eclipse.scava.platform.delta.communicationchannel.PlatformCommunicationChannelManager;
import org.eclipse.scava.repository.model.CommunicationChannel;
import org.eclipse.scava.repository.model.Project;
import org.eclipse.scava.repository.model.cc.nntp.NntpNewsGroup;
import org.eclipse.scava.repository.model.sourceforge.Discussion;

import com.googlecode.pongo.runtime.Pongo;

public class IndexHistoricMetricProvider extends AbstractHistoricalMetricProvider {

	public final static String IDENTIFIER = "org.eclipse.scava.metricprovider.historic.index";
	protected PlatformBugTrackingSystemManager platformBugTrackingSystemManager;
	protected PlatformCommunicationChannelManager communicationChannelManager;
	protected MetricProviderContext context;
	
	/**
	 * List of MPs that are used by this MP. These are MPs who have specified that 
	 * they 'provide' data for this MP.
	 */
	protected List<IMetricProvider> uses;
	
	@Override
	public Pongo measure(Project project) throws NullPointerException{
		System.err.println("Started " + getIdentifier());
		IndexHistoricMetric indexHistoricMetric = new IndexHistoricMetric();
		
			
		return indexHistoricMetric;
	}

	@Override
	public String getIdentifier() {
		
		return IDENTIFIER;
	}

	@Override
	public String getShortIdentifier() {
		
		return "indexer";
	}

	@Override
	public String getFriendlyName() {
		
		return "Populates the Elasticsearch Index";
	}

	@Override
	public String getSummaryInformation() {
		
		return "This metric prepares and Indexes documents which contain information realted to the deltas and NLP components)";
	}

	@Override
	public boolean appliesTo(Project project) {
			
		for (CommunicationChannel communicationChannel: project.getCommunicationChannels()) {
			if (communicationChannel instanceof NntpNewsGroup) return true;
			if (communicationChannel instanceof Discussion) return true;
			}
		
		return !project.getBugTrackingSystems().isEmpty();
	}

	@Override
	public void setUses(List<IMetricProvider> uses) {
		
		this.uses = uses;
		
	}

	@Override
	public List<String> getIdentifiersOfUses() {
		
		List<String> list = new ArrayList<String>();
		
		list.add(PlainTextProcessingTransMetricProvider.class.getCanonicalName());//plain text
		list.add(SeverityClassificationTransMetricProvider.class.getCanonicalName());//severity
		list.add(SentimentClassificationTransMetricProvider.class.getCanonicalName());//sentiment
		list.add(DetectingCodeTransMetricProvider.class.getCanonicalName());//code detect
		//ADD MORE HERE
		
	return list;
	}

	@Override
	public void setMetricProviderContext(MetricProviderContext context) {
		this.platformBugTrackingSystemManager = context.getPlatformBugTrackingSystemManager();
		this.communicationChannelManager = context.getPlatformCommunicationChannelManager();
		this.context = context;
	}
	
	
	
	private void clearDB(DetectingCodeTransMetric db) {
		db.getBugTrackerComments().getDbCollection().drop();
		db.getNewsgroupArticles().getDbCollection().drop();
		db.sync();
	}
	

	

}
