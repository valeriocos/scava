package org.eclipse.scava.metricprovider.trans.index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.scava.metricprovider.trans.index.model.IndexTransMetric;
import org.eclipse.scava.metricprovider.trans.plaintextprocessing.PlainTextProcessingTransMetricProvider;
import org.eclipse.scava.platform.IMetricProvider;
import org.eclipse.scava.platform.ITransientMetricProvider;
import org.eclipse.scava.platform.MetricProviderContext;

import org.eclipse.scava.platform.delta.ProjectDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemProjectDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.PlatformBugTrackingSystemManager;
import org.eclipse.scava.platform.delta.communicationchannel.PlatformCommunicationChannelManager;
import org.eclipse.scava.repository.model.CommunicationChannel;
import org.eclipse.scava.repository.model.Project;
import org.eclipse.scava.repository.model.cc.nntp.NntpNewsGroup;
import org.eclipse.scava.repository.model.sourceforge.Discussion;

import com.mongodb.DB;

public class IndexTransMetricProvider implements ITransientMetricProvider<IndexTransMetric> {

	protected PlatformBugTrackingSystemManager platformBugTrackingSystemManager;
	protected PlatformCommunicationChannelManager communicationChannelManager;
	protected List<IMetricProvider> uses;
	protected MetricProviderContext context;

	@Override
	public String getIdentifier() {
		return IndexTransMetricProvider.class.getCanonicalName();
	}

	@Override
	public String getShortIdentifier() {
		return "indexer";
	}

	@Override
	public String getFriendlyName() {
		return "Indexer";
	}

	@Override
	public String getSummaryInformation() {

		return "This metric is responsible for creating and indexing documents into elasticsearch";
	}

	@Override
	public boolean appliesTo(Project project) {
		for (CommunicationChannel communicationChannel : project.getCommunicationChannels()) {
			if (communicationChannel instanceof NntpNewsGroup)
				return true;
			if (communicationChannel instanceof Discussion)
				return true;
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
		// Here I add all of the TransMetricProvider classes the indexer relies on
		list.add(PlainTextProcessingTransMetricProvider.class.getCanonicalName());
		// list.add( .class.getCanonicalName());

		return list;
	}

	@Override
	public void setMetricProviderContext(MetricProviderContext context) {
		this.platformBugTrackingSystemManager = context.getPlatformBugTrackingSystemManager();
		this.communicationChannelManager = context.getPlatformCommunicationChannelManager();
		this.context = context;

	}

	@Override
	public IndexTransMetric adapt(DB db) {
		return new IndexTransMetric(db);
	}

	@Override
	public void measure(Project project, ProjectDelta projectDelta, IndexTransMetric db) {
		System.err.println("Started " + getIdentifier());
		
		System.err.println("[INDEX TRANSMETRIC PROVIDER]  - HELLO DARKNESS MY OLD FIREND");

		BugTrackingSystemProjectDelta btspDelta = projectDelta.getBugTrackingSystemDelta();
		
		for (BugTrackingSystemDelta bugTrackingSystemProjectDelta : btspDelta.getBugTrackingSystemDeltas()) {

			String bugTrackerType = bugTrackingSystemProjectDelta.getBugTrackingSystem().getBugTrackerType();

			System.out.println(bugTrackerType);

			switch (bugTrackerType) {

			case "github":
				indexGitHub(project, projectDelta, db);
				break;
			case "matnis":
				indexMatnis();
				break;
			case "bitbucket":
				indexBitbucket();
				break;
			case "redmine":
				indexRedmine();
				break;
			case "gitlab: ":
				indexGitLab();
				break;
			case "bugzilla : ":
				indexBugzilla();
				break;
			case "jira : ":
				indexJira();
				break;
			}

		}



	}
		
	/**
	 * This method returns a unique Identifier based upon the SourceType, Project, Document Type and source ID;
	 * 
	 * @return String uid - a uniquely identifiable string.
	 */
	
	private String createUniqueDocumentId(ProjectDelta projectDelta) {
		
		String uid= "";
		
		return uid;
	}
	
	
	
	// METHODS RELATING TO THE SOURCES 
	private void indexJira() {

	}

	private void indexBugzilla() {

	}

	private void indexGitLab() {

	}

	private void indexRedmine() {

	}

	private void indexGitHub(Project project, ProjectDelta projectDelta, IndexTransMetric db) {

	}

	private void indexMatnis() {

	}

	private void indexBitbucket() {

	}

	private void indexNNTP() {

	}

	
	private void indexEclipseFourms() {

	}

}
