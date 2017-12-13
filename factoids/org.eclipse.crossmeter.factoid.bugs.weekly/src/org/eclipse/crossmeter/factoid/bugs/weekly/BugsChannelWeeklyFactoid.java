/*******************************************************************************
 * Copyright (c) 2014 CROSSMETER Partners.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yannis Korkontzelos - Implementation.
 *******************************************************************************/
package org.eclipse.crossmeter.factoid.bugs.weekly;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import org.eclipse.crossmeter.metricprovider.trans.bugs.dailyrequestsreplies.DailyRequestsRepliesTransMetricProvider;
import org.eclipse.crossmeter.metricprovider.trans.bugs.dailyrequestsreplies.model.BugsDailyRequestsRepliesTransMetric;
import org.eclipse.crossmeter.metricprovider.trans.bugs.dailyrequestsreplies.model.DayComments;
import org.eclipse.crossmeter.platform.AbstractFactoidMetricProvider;
import org.eclipse.crossmeter.platform.IMetricProvider;
import org.eclipse.crossmeter.platform.delta.ProjectDelta;
import org.eclipse.crossmeter.platform.factoids.Factoid;
import org.eclipse.crossmeter.platform.factoids.StarRating;
import org.eclipse.crossmeter.repository.model.Project;

public class BugsChannelWeeklyFactoid extends AbstractFactoidMetricProvider{

	protected List<IMetricProvider> uses;
	
	@Override
	public String getShortIdentifier() {
		return "BugChannelWeekly";
	}

	@Override
	public String getFriendlyName() {
		return "Bug Tracker Weekly";
		// This method will NOT be removed in a later version.
	}

	@Override
	public String getSummaryInformation() {
		return "summaryblah"; // This method will NOT be removed in a later version.
	}

	@Override
	public boolean appliesTo(Project project) {
	    return !project.getBugTrackingSystems().isEmpty();	   
	}

	@Override
	public List<String> getIdentifiersOfUses() {
		return Arrays.asList(DailyRequestsRepliesTransMetricProvider.class.getCanonicalName());
	}

	@Override
	public void setUses(List<IMetricProvider> uses) {
		this.uses = uses;
	}

	@Override
	public void measureImpl(Project project, ProjectDelta delta, Factoid factoid) {
//		factoid.setCategory(FactoidCategory.BUGS);
		factoid.setName(getFriendlyName());

		BugsDailyRequestsRepliesTransMetric dailyRequestsRepliesTransMetric = 
				((DailyRequestsRepliesTransMetricProvider)uses.get(0)).adapt(context.getProjectDB(project));

		float uniformPercentageOfComments = ( (float) 100 ) / 7;
		float maxPercentageOfComments = 0,
			  minPercentageOfComments = 101;
		String maxPercentageDay = "",
			   minPercentageDay = "";
		
		for (DayComments dayComments: dailyRequestsRepliesTransMetric.getDayComments()) {
			if ( dayComments.getPercentageOfComments() > maxPercentageOfComments ) {
				maxPercentageOfComments = dayComments.getPercentageOfComments();
				maxPercentageDay = dayComments.getName();
			}
			if ( dayComments.getPercentageOfComments() < minPercentageOfComments ) {
				minPercentageOfComments = dayComments.getPercentageOfComments(); 
				minPercentageDay = dayComments.getName();
			}
		}
		
		if ( maxPercentageOfComments < 2 * uniformPercentageOfComments ) {
			factoid.setStars(StarRating.FOUR);
		} else if ( maxPercentageOfComments < 3 * uniformPercentageOfComments ) {
			factoid.setStars(StarRating.THREE);
		} else if ( maxPercentageOfComments < 4 * uniformPercentageOfComments ) {
			factoid.setStars(StarRating.TWO);
		} else {
			factoid.setStars(StarRating.ONE);
		}

		StringBuffer stringBuffer = new StringBuffer();
		DecimalFormat decimalFormat = new DecimalFormat("#.##");

		stringBuffer.append("The busiest day of the week is ");
		stringBuffer.append(maxPercentageDay);
		stringBuffer.append(" (");
		stringBuffer.append(decimalFormat.format(maxPercentageOfComments));
		stringBuffer.append("% of reports and comments), while the least busy day is ");
		stringBuffer.append(minPercentageDay);
		stringBuffer.append(" (");
		stringBuffer.append(decimalFormat.format(minPercentageOfComments));
		stringBuffer.append("%) (");
		boolean first = true;
		for (DayComments dayComments: dailyRequestsRepliesTransMetric.getDayComments()) {
			if ( (!dayComments.getName().equals(maxPercentageDay)) &&
				 (!dayComments.getName().equals(minPercentageDay)) ) {
				if (!first)
					stringBuffer.append(", ");
				stringBuffer.append(dayComments.getName());
				stringBuffer.append(": ");
				stringBuffer.append(decimalFormat.format(dayComments.getPercentageOfComments()));
				stringBuffer.append("%");
				first = false;
			}
		}
		stringBuffer.append(").\n");
		
		factoid.setFactoid(stringBuffer.toString());

	}

}
