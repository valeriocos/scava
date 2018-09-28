/*******************************************************************************
 * Copyright (c) 2017 University of Manchester
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
//Adrián was here
package org.eclipse.scava.metricprovider.trans.bugs.emotions;

import java.util.Arrays;
import java.util.List;

import org.eclipse.scava.metricprovider.trans.bugs.emotions.model.BugTrackerData;
import org.eclipse.scava.metricprovider.trans.bugs.emotions.model.BugsEmotionsTransMetric;
import org.eclipse.scava.metricprovider.trans.bugs.emotions.model.EmotionDimension;
import org.eclipse.scava.metricprovider.trans.detectingcode.DetectingCodeTransMetricProvider;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.BugTrackerCommentDetectingCode;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.DetectingCodeTransMetric;
import org.eclipse.scava.platform.IMetricProvider;
import org.eclipse.scava.platform.ITransientMetricProvider;
import org.eclipse.scava.platform.MetricProviderContext;
import org.eclipse.scava.platform.delta.ProjectDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemComment;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemProjectDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.PlatformBugTrackingSystemManager;
import org.eclipse.scava.repository.model.BugTrackingSystem;
import org.eclipse.scava.repository.model.Project;
import org.eclipse.scava.sentimentclassifier.opennlptartarus.libsvm.ClassificationInstance;
import org.eclipse.scava.sentimentclassifier.opennlptartarus.libsvm.EmotionalDimensions;

import com.mongodb.DB;

public class EmotionsTransMetricProvider implements ITransientMetricProvider<BugsEmotionsTransMetric>{

	protected PlatformBugTrackingSystemManager platformBugTrackingSystemManager;
	
	protected List<IMetricProvider> uses;
	protected MetricProviderContext context;

	@Override
	public String getIdentifier() {
		return EmotionsTransMetricProvider.class.getCanonicalName();
	}

	@Override
	public boolean appliesTo(Project project) {
	    return !project.getBugTrackingSystems().isEmpty();	   
	}

	@Override
	public void setUses(List<IMetricProvider> uses) {
		this.uses = uses;
	}

	@Override
	public List<String> getIdentifiersOfUses() {
		return Arrays.asList(DetectingCodeTransMetricProvider.class.getCanonicalName());
	}

	@Override
	public void setMetricProviderContext(MetricProviderContext context) {
		this.context = context;
		this.platformBugTrackingSystemManager = context.getPlatformBugTrackingSystemManager();
	}

	@Override
	public BugsEmotionsTransMetric adapt(DB db) {
		return new BugsEmotionsTransMetric(db);
	}

	@Override
	public void measure(Project project, ProjectDelta projectDelta, BugsEmotionsTransMetric db) {
		
		DetectingCodeTransMetric detectingCodeMetric = ((DetectingCodeTransMetricProvider)uses.get(0)).adapt(context.getProjectDB(project));
		
		BugTrackingSystemProjectDelta delta = projectDelta.getBugTrackingSystemDelta();
		
		for (BugTrackingSystemDelta bugTrackingSystemDelta : delta.getBugTrackingSystemDeltas()) {
			
			BugTrackingSystem bugTracker = bugTrackingSystemDelta.getBugTrackingSystem();
			Iterable<BugTrackerData> bugTrackerDataIt = 
					db.getBugTrackerData().find(BugTrackerData.BUGTRACKERID.eq(bugTracker.getOSSMeterId()));
			BugTrackerData bugTrackerData = null;
			for (BugTrackerData bd:  bugTrackerDataIt) bugTrackerData = bd;
			if (bugTrackerData == null) {
				bugTrackerData = new BugTrackerData();
				bugTrackerData.setBugTrackerId(bugTracker.getOSSMeterId());
				bugTrackerData.setNumberOfComments(0);
				bugTrackerData.setCumulativeNumberOfComments(0);
				db.getBugTrackerData().add(bugTrackerData);
			}
			bugTrackerData.setNumberOfComments(bugTrackingSystemDelta.getComments().size());
			bugTrackerData.setCumulativeNumberOfComments(bugTrackerData.getCumulativeNumberOfComments() + 
					bugTrackingSystemDelta.getComments().size());
			
			db.sync();
			
			Iterable<EmotionDimension> emotionIt = db.getDimensions().
					find(EmotionDimension.BUGTRACKERID.eq(bugTracker.getOSSMeterId()));
			for (EmotionDimension emotion:  emotionIt) {
				emotion.setNumberOfComments(0);
			}
			
			for (BugTrackingSystemComment comment: bugTrackingSystemDelta.getComments()) {
				
				ClassificationInstance instance = new ClassificationInstance();
				instance.setBugTrackerId(bugTracker.getOSSMeterId());
				instance.setBugId(comment.getBugId());
				instance.setCommentId(comment.getCommentId());
				instance.setText(getNaturalLanguage(detectingCodeMetric, comment));
				
				String[] emotionalDimensions = EmotionalDimensions.getDimensions(instance).split(",");
				
				for (String dimension: emotionalDimensions) {
					dimension = dimension.trim();
					if (dimension.length()>0) {
						emotionIt = db.getDimensions().find(EmotionDimension.BUGTRACKERID.eq(bugTracker.getOSSMeterId()),
								EmotionDimension.EMOTIONLABEL.eq(dimension));
						EmotionDimension emotion = null;
						for (EmotionDimension em:  emotionIt) emotion = em;
						if (emotion == null) {
							emotion = new EmotionDimension();
							emotion.setBugTrackerId(bugTracker.getOSSMeterId());
							emotion.setEmotionLabel(dimension);
							emotion.setNumberOfComments(0);
							emotion.setCumulativeNumberOfComments(0);
							db.getDimensions().add(emotion);
						}
						emotion.setNumberOfComments(emotion.getNumberOfComments() + 1);
						emotion.setCumulativeNumberOfComments(emotion.getCumulativeNumberOfComments() + 1);
						db.sync();
					}
				}
			}

			db.sync();

			emotionIt = db.getDimensions().
					find(EmotionDimension.BUGTRACKERID.eq(bugTracker.getOSSMeterId()));
			for (EmotionDimension emotion: emotionIt) {
				if ( bugTrackerData.getNumberOfComments() > 0 ) {
					emotion.setPercentage( 
							((float)100*emotion.getNumberOfComments()) / bugTrackerData.getNumberOfComments());
				}
				else
					emotion.setPercentage( (float) 0 );
				if ( bugTrackerData.getCumulativeNumberOfComments() > 0 )
					emotion.setCumulativePercentage( 
						((float)100*emotion.getCumulativeNumberOfComments()) / bugTrackerData.getCumulativeNumberOfComments());
				else
					emotion.setCumulativePercentage( (float) 0 );
			}
			
			db.sync();
			
		}

	}

	@Override
	public String getShortIdentifier() {
		return "Bugemotions";
	}

	@Override
	public String getFriendlyName() {
		return "Emotional Dimensions in Bug Comments";
	}

	@Override
	public String getSummaryInformation() {
		return "Emotional Dimensions in Bug Comments";
	}
	
	private String getNaturalLanguage(DetectingCodeTransMetric db, BugTrackingSystemComment comment)
	{
		BugTrackerCommentDetectingCode bugtrackerCommentInDetectionCode = null;
		Iterable<BugTrackerCommentDetectingCode> bugtrackerCommentIt = db.getBugTrackerComments().
				find(BugTrackerCommentDetectingCode.BUGID.eq(comment.getBugId()),
						BugTrackerCommentDetectingCode.COMMENTID.eq(comment.getCommentId()));
		for (BugTrackerCommentDetectingCode btcdc:  bugtrackerCommentIt) {
			bugtrackerCommentInDetectionCode = btcdc;
		}
		if (bugtrackerCommentInDetectionCode.getNaturalLanguage() == null)
			return "";
		else
			return bugtrackerCommentInDetectionCode.getNaturalLanguage();
	}

}
